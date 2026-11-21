/* 학습 기록을 복원·검색·채점하며, 같은 함수를 브라우저 화면과 Node.js 테스트에서 사용합니다. */
(function (root) {
  "use strict";
  const scopes = ["all", "unanswered", "wrong", "bookmarked"];
  // 첫 번째 보기의 값인 0도 제출한 답입니다. 값의 참·거짓이 아니라 기록의 존재 여부를 확인합니다.
  const hasAnswer = (state, id) => Object.prototype.hasOwnProperty.call(state.answers, id);
  // 화면의 1~4번 보기를 코드에서는 0~3으로 저장하므로, 이 범위의 정수만 답안으로 받습니다.
  const validChoice = value => Number.isInteger(value) && value >= 0 && value < 4;

  // 저장 파일에는 오래되거나 잘못된 값이 있을 수 있어, 현재 문제집에 맞는 기록만 복원합니다.
  function restore(saved, bank) {
    const source = saved && typeof saved === "object" && !Array.isArray(saved) ? saved : {};
    // byId로 문항 번호에 해당하는 문제를 찾아, 저장된 기록이 현재 문제집에 속하는지 확인합니다.
    const byId = new Map(bank.questions.map(q => [q.id, q]));
    // 제출한 답안인 answers와 선택만 한 보기인 drafts에 같은 검사를 적용합니다.
    const record = input => {
      const result = {};
      if (input && typeof input === "object" && !Array.isArray(input)) {
        Object.entries(input).forEach(([id, choice]) => {
          // 저장할 때 문항 번호는 문자열이 됩니다. '01'처럼 같은 번호를 중복 표현한 기록은 제외합니다.
          if (byId.has(Number(id)) && String(Number(id)) === id && validChoice(choice)) result[id] = choice;
        });
      }
      return result;
    };
    // 북마크·재풀이·무작위 순서에서 사라진 문항과 중복 번호를 제거합니다.
    const ids = input => Array.isArray(input) ? [...new Set(input.filter(id => byId.has(id)))] : [];
    // positions는 {java: 3, oop: 27}처럼 과목마다 마지막으로 본 문항 번호를 보관합니다.
    const positions = {};
    if (source.positions && typeof source.positions === "object" && !Array.isArray(source.positions)) {
      Object.entries(source.positions).forEach(([category, id]) => {
        // 실제 문항의 소속 과목까지 일치해야, 다른 과목의 문제로 잘못 이어지는 일을 막을 수 있습니다.
        if (byId.get(id)?.category === category) positions[category] = id;
      });
    }
    // 가장 최근 문항인 currentId를 positions에도 반영해, 과목별 위치가 없던 예전 기록도 이어서 풀게 합니다.
    const current = byId.get(source.currentId);
    if (current) positions[current.category] = current.id;
    const filters = source.filters || {};
    const order = ids(source.randomOrder);
    return {
      schema: 2,
      // 제출한 답안과 선택만 한 보기를 따로 보관해, 채점 전 선택은 풀이 진도에 넣지 않습니다.
      answers: record(source.answers), bookmarks: ids(source.bookmarks),
      drafts: record(source.drafts), retries: ids(source.retries),
      currentId: byId.has(source.currentId) ? source.currentId : null,
      positions,
      filters: {
        category: bank.categories.some(c => c.id === filters.category) ? filters.category : "all",
        status: scopes.includes(filters.status) ? filters.status : "all",
        search: typeof filters.search === "string" ? filters.search.slice(0, 200) : ""
      },
      randomized: source.randomized === true && order.length > 0,
      // 문항이 추가되어도 저장된 무작위 순서 뒤에 새 문항을 연결한다.
      randomOrder: order.concat(bank.questions.map(q => q.id).filter(id => !order.includes(id))),
      view: source.view === "subjects" ? "subjects" : "learn",
      updatedAt: typeof source.updatedAt === "string" && Number.isFinite(Date.parse(source.updatedAt)) ? source.updatedAt : null
    };
  }

  // 정답과 해설은 검색 대상에서 제외해, 검색 결과로 답이 드러나지 않게 합니다.
  function select(state, bank) {
    const byId = new Map(bank.questions.map(q => [q.id, q]));
    const search = state.filters.search.trim().toLocaleLowerCase();
    const order = state.randomized ? state.randomOrder : bank.questions.map(q => q.id);
    return order.filter(id => {
      const q = byId.get(id), answered = hasAnswer(state, id);
      return q && (state.filters.category === "all" || q.category === state.filters.category)
        && (!search || [q.code, q.prompt, ...q.options].join(" ").toLocaleLowerCase().includes(search))
        && (state.filters.status === "all"
          || state.filters.status === "unanswered" && !answered
          || state.filters.status === "wrong" && answered && state.answers[id] !== q.answer
          || state.filters.status === "bookmarked" && state.bookmarks.includes(id));
    });
  }

  // 같은 문제를 여러 번 풀어도 마지막 제출 답안 하나만 집계해 진도가 중복으로 늘지 않게 합니다.
  function summarize(state, bank, category = "all") {
    const questions = bank.questions.filter(q => category === "all" || q.category === category);
    const answered = questions.filter(q => hasAnswer(state, q.id));
    const correct = answered.filter(q => state.answers[q.id] === q.answer).length;
    return {total: questions.length, answered: answered.length, correct,
      wrong: answered.length - correct, remaining: questions.length - answered.length,
      percent: questions.length ? Math.round(answered.length / questions.length * 100) : 0,
      accuracy: answered.length ? Math.round(correct / answered.length * 100) : null};
  }

  // 채점이 끝난 문항은 임시 선택과 재풀이 표시를 정리하고, 제출한 답안에 저장합니다.
  function grade(state, bank, id, choice) {
    if (!bank.questions.some(q => q.id === id) || !validChoice(choice)) throw new Error("유효하지 않은 답안입니다.");
    const drafts = {...state.drafts}; delete drafts[id];
    // 기존 state를 복사해 이번 채점에 필요한 값만 바꾸고, 다른 과목의 위치와 북마크는 유지합니다.
    return {...state, answers: {...state.answers, [id]: choice}, drafts,
      retries: state.retries.filter(value => value !== id), updatedAt: new Date().toISOString()};
  }

  const api = {restore, select, summarize, grade, hasAnswer, validChoice};
  if (typeof module === "object" && module.exports) module.exports = api;
  else root.SCM_STUDY_STATE = api;
})(typeof window === "object" ? window : globalThis);
