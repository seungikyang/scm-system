const {test} = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const vm = require('node:vm');
const core = require('../../../practice/sw-quiz/study-state.js');
const root = path.resolve(__dirname, '../../..');
const context = {window: {}};
for (const name of ['questions.js', 'workbook-questions.js']) {
  vm.runInNewContext(fs.readFileSync(path.join(root, 'practice/sw-quiz', name), 'utf8'), context);
}
// JSON으로 읽어 서로 다른 VM의 Array 프로토타입을 정규화한다.
const sw = JSON.parse(JSON.stringify(context.window.SCM_QUIZ_BANK));
const workbook = JSON.parse(JSON.stringify(context.window.SCM_WORKBOOK_QUIZ_BANK));

for (const [name, bank] of [['SW 3000제', sw], ['워크북 403제', workbook]]) {
  test(name + ': 기존 저장 답안과 북마크가 유지된다', () => {
    const q = bank.questions[0];
    const state = core.restore({answers: {[q.id]: q.answer}, bookmarks: [q.id], currentId: q.id}, bank);
    assert.equal(state.answers[q.id], q.answer);
    assert.deepEqual(state.bookmarks, [q.id]);
    assert.equal(state.currentId, q.id);
    assert.equal(state.positions[q.category], q.id);
    assert.equal(core.summarize(state, bank).answered, 1);
    assert.equal(core.summarize(state, bank).correct, 1);
  });
  test(name + ': 오답을 다시 맞히면 진도는 중복 증가하지 않고 복습 대상에서 제외된다', () => {
    const q = bank.questions[0];
    let state = core.grade(core.restore(null, bank), bank, q.id, (q.answer + 1) % 4);
    state.filters.status = 'wrong';
    assert.deepEqual(core.select(state, bank), [q.id]);
    state.retries.push(q.id); state.drafts[q.id] = q.answer;
    const pending = core.restore(JSON.parse(JSON.stringify(state)), bank);
    assert.equal(core.summarize(pending, bank).wrong, 1);
    assert.equal(pending.drafts[q.id], q.answer);
    assert.deepEqual(pending.retries, [q.id]);
    state = core.grade(pending, bank, q.id, q.answer);
    assert.deepEqual(core.select(state, bank), []);
    const summary = core.summarize(state, bank);
    assert.equal(summary.answered, 1); assert.equal(summary.correct, 1); assert.equal(summary.wrong, 0);
    assert.equal(state.drafts[q.id], undefined); assert.deepEqual(state.retries, []);
  });
  test(name + ': 선택한 보기·과목·검색·무작위 순서·마지막 문항이 새로고침 후 복원된다', () => {
    const q = bank.questions[1];
    const state = core.restore(null, bank);
    state.drafts[q.id] = 0; state.currentId = q.id; state.positions[q.category] = q.id;
    state.filters = {category: q.category, status: 'unanswered', search: q.prompt.slice(0, 5)};
    state.randomized = true; state.randomOrder.reverse(); state.view = 'subjects';
    const restored = core.restore(JSON.parse(JSON.stringify(state)), bank);
    assert.deepEqual(restored, state);
    assert.ok(core.select(restored, bank).includes(q.id));
    assert.equal(core.summarize(restored, bank).answered, 0);
  });
  test(name + ': 과목 진도는 제출한 답만 집계하고 필터들이 함께 적용된다', () => {
    const first = bank.questions[0], other = bank.questions.find(q => q.category !== first.category);
    let state = core.grade(core.restore(null, bank), bank, first.id, first.answer);
    state = core.grade(state, bank, other.id, (other.answer + 1) % 4);
    state.filters = {category: first.category, status: 'wrong', search: ''};
    assert.deepEqual(core.select(state, bank), []);
    const local = core.summarize(state, bank, first.category);
    assert.equal(local.answered, 1); assert.equal(local.correct, 1); assert.equal(local.wrong, 0);
    state.filters.status = 'unanswered';
    assert.ok(!core.select(state, bank).includes(first.id));
    state.bookmarks = [first.id, other.id]; state.filters.status = 'bookmarked';
    assert.deepEqual(core.select(state, bank), [first.id]);
  });
  test(name + ': 과목을 바꿔도 각 과목의 마지막 문항과 미제출 보기를 복원한다', () => {
    const first = bank.questions[2], other = bank.questions.find(q => q.category !== first.category);
    const state = core.restore(null, bank);
    state.positions = {[first.category]: first.id, [other.category]: other.id};
    state.currentId = other.id;
    state.drafts[first.id] = 0;
    const restored = core.restore(JSON.parse(JSON.stringify(state)), bank);
    assert.deepEqual(restored.positions, state.positions);
    assert.equal(restored.drafts[first.id], 0);
    assert.equal(core.summarize(restored, bank).answered, 0);
  });
}

test('과목별 마지막 문항은 실제 소속 과목과 문항 번호가 맞는 기록만 복원한다', () => {
  const first = workbook.questions[0], other = workbook.questions.find(q => q.category !== first.category);
  const saved = {positions: {[first.category]: other.id, [other.category]: other.id, unknown: first.id}};
  assert.deepEqual(core.restore(saved, workbook).positions, {[other.category]: other.id});
  for (const value of [null, [], '잘못된 기록', 123, {[first.category]: '1'}, {[first.category]: 999999}]) {
    assert.deepEqual(core.restore({positions: value}, workbook).positions, {});
  }
});

test('손상된 기록의 존재하지 않는 번호와 잘못된 답안을 제거한다', () => {
  const state = core.restore({answers: {'1': 0, '2': -1, '3': 4, '4': '1', '5': false, '999999': 1, '01': 2},
    drafts: {'1': 0, '999999': 1}, bookmarks: [1, 1, '2', 999999], retries: [1, 1, 999999],
    currentId: 999999, filters: {category: 'unknown', status: 'unknown', search: 14}, updatedAt: 'invalid'}, sw);
  assert.deepEqual(state.answers, {'1': 0}); assert.deepEqual(state.drafts, {'1': 0});
  assert.deepEqual(state.bookmarks, [1]); assert.deepEqual(state.retries, [1]);
  assert.equal(state.currentId, null);
  assert.deepEqual(state.filters, {category: 'all', status: 'all', search: ''});
  assert.equal(state.updatedAt, null);
  for (const saved of [null, 0, false, [], 'bad']) assert.equal(core.summarize(core.restore(saved, sw), sw).answered, 0);
});

test('문항 번호와 보기 내용으로 검색하고 정답·해설은 검색에 사용하지 않는다', () => {
  const state = core.restore(null, workbook), q = workbook.questions[0];
  state.filters.search = q.code;
  assert.deepEqual(core.select(state, workbook), [q.id]);
  state.filters.search = q.options[0];
  assert.ok(core.select(state, workbook).includes(q.id));
});

test('새 문항이 추가되어도 저장한 무작위 순서를 유지하며 누락 없이 이어 붙인다', () => {
  const state = core.restore({randomized: true, randomOrder: [3, 2, 3, 1, 999999]}, sw);
  assert.deepEqual(state.randomOrder.slice(0, 4), [3, 2, 1, 4]);
  assert.equal(state.randomOrder.length, 3000);
  assert.equal(new Set(state.randomOrder).size, 3000);
});

test('범위를 벗어난 답안은 제출 기록을 변경하지 않는다', () => {
  const state = core.restore(null, sw);
  for (const choice of [-1, 4, '1', null, NaN]) assert.throws(() => core.grade(state, sw, 1, choice));
  assert.throws(() => core.grade(state, sw, 999999, 0));
  assert.deepEqual(state.answers, {});
});
