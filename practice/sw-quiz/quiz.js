/* 문제 원본은 그대로 연결하고, 학습 기록과 화면 상태만 브라우저에 보관한다. */
(function () {
  "use strict";
  const el = id => document.getElementById(id);
  const params = new URLSearchParams(window.location.search);
  const workbook = params.get("bank") === "workbook";
  const bank = workbook ? window.SCM_WORKBOOK_QUIZ_BANK : window.SCM_QUIZ_BANK;
  const core = window.SCM_STUDY_STATE;
  const notice = message => { el("notice").textContent = message; el("notice").hidden = false; };
  if (!core || !bank || !Array.isArray(bank.questions) || !bank.questions.length) {
    notice("문제 자료를 불러오지 못했습니다. 문제집 폴더의 HTML과 JavaScript 파일을 함께 열어 주세요.");
    el("learning-view").hidden = true;
    return;
  }
  // 문제집별 버전을 저장 이름에 포함해 SW 기술 문제와 SCM 워크북의 풀이 기록을 구분합니다.
  const key = "scm.sw-quiz." + bank.version;
  const byId = new Map(bank.questions.map(q => [q.id, q]));
  const categories = new Map(bank.categories.map(c => [c.id, c]));
  const shortName = category => category.name.replace(/ \((?:PRD|TRD)[^)]*\)/g, "");
  const format = value => value.toLocaleString("ko-KR");
  const scopeNames = {all: "문제 풀기", unanswered: "미풀이 학습", wrong: "오답 노트", bookmarked: "북마크"};
  let state, subset = [], cursor = 0, mapPage = 0, completed = false, storageFailed = false;
  const mapSize = 20;
  try { state = core.restore(JSON.parse(localStorage.getItem(key) || "null"), bank); }
  catch (_) {
    state = core.restore(null, bank);
    notice("저장된 기록을 읽을 수 없습니다. 기록 파일이 있다면 가져오기로 복원할 수 있습니다.");
  }

  document.title = "SCM · " + (workbook ? "워크북 " : "SW 기술 ") + format(bank.questions.length) + "제";
  el("bank-count").textContent = format(bank.questions.length) + " QUESTIONS";
  el("quiz-description").textContent = workbook
    ? "40개 모듈의 기존 워크북 403문항. 개념부터 설계·면접까지, 한 문제씩 연결해 보세요."
    : "Java·Spring·JPA·SQL 등 20개 과목. 프로젝트에 쓰는 기술을 한 문제씩 익혀 보세요.";
  el("bank-select").value = workbook ? "workbook" : "sw";
  [window.SCM_QUIZ_BANK, window.SCM_WORKBOOK_QUIZ_BANK].forEach((value, index) => {
    if (value) el("bank-select").options[index].textContent = (index ? "SCM 워크북 · " : "SW 기술 · ") + format(value.questions.length) + "제";
    else el("bank-select").options[index].disabled = true;
  });
  bank.categories.forEach(category => {
    const option = document.createElement("option");
    option.value = category.id;
    option.textContent = shortName(category) + " · " + bank.questions.filter(q => q.category === category.id).length + "문제";
    el("category").append(option);
  });
  el("category").options[0].textContent = "전체 과목 · " + format(bank.questions.length) + "문제";

  // 문서에서 지정한 문항·모듈을 우선한다. 링크를 한 번 적용한 뒤에는 저장한 학습 조건으로 복원한다.
  const requestedId = Number(params.get("question"));
  const requestedModule = params.get("module");
  if (workbook && requestedModule && /^\d{1,2}$/.test(requestedModule)) {
    const category = "module-" + requestedModule.padStart(2, "0");
    if (categories.has(category)) {
      state.filters = {category, status: "all", search: ""}; state.view = "learn";
      // 모듈 링크로 들어와도 해당 과목의 마지막 문항을 복원하며, 처음 방문한 과목은 첫 문항부터 보여 줍니다.
      state.currentId = state.positions[category] || null;
    }
  }
  // 문항 번호를 지정한 링크는 저장된 위치보다 우선하므로, 모듈 선택을 처리한 다음 적용합니다.
  if (byId.has(requestedId)) {
    state.currentId = requestedId; state.view = "learn";
    state.filters = {category: byId.get(requestedId).category, status: "all", search: ""};
  }
  // 오답 노트로 직접 들어가는 링크도 지원한다.
  if (params.get("status") === "wrong") { state.filters.status = "wrong"; state.view = "learn"; }
  if (params.has("question") || params.has("module") || params.has("status")) {
    try {
      const url = new URL(window.location.href);
      ["question", "module", "status"].forEach(name => url.searchParams.delete(name));
      window.history.replaceState(null, "", url.href);
    } catch (_) { /* file:// 환경에 따라 주소 변경이 제한돼도 학습은 계속한다. */ }
  }

  // state에는 답안·선택 중인 보기·과목별 위치가 함께 있어, 새로고침 뒤에도 같은 상태로 복원할 수 있습니다.
  function save() {
    try {
      localStorage.setItem(key, JSON.stringify(state));
      el("save-status").textContent = "이 브라우저에 자동 저장됨";
      storageFailed = false;
    } catch (_) {
      el("save-status").textContent = "저장 불가 · 기록 내보내기 이용";
      if (!storageFailed) notice("이 브라우저에서 자동 저장을 사용할 수 없습니다. 학습 후 ‘기록 내보내기’로 보관해 주세요.");
      storageFailed = true;
    }
  }
  function syncControls() {
    el("category").value = state.filters.category;
    el("status").value = state.filters.status;
    el("search").value = state.filters.search;
    el("shuffle").setAttribute("aria-pressed", String(state.randomized));
  }
  function prepareReview() {
    if (state.filters.status !== "wrong") return;
    subset.forEach(id => {
      // 기존 오답과 해설은 감추되, 제출 전 선택한 보기는 보존한다.
      if (!state.retries.includes(id)) { state.retries.push(id); delete state.drafts[id]; }
    });
  }
  function applyFilters(preserve = false) {
    completed = false;
    subset = core.select(state, bank);
    // preserve가 true이고 저장된 문항이 현재 조건에도 맞을 때만 이어서 보여 주며, 나머지는 첫 문항부터 시작합니다.
    const existing = preserve ? subset.indexOf(state.currentId) : -1;
    // currentId는 문제집의 문항 번호이고, cursor는 조건에 맞게 추린 subset 안에서의 순서입니다.
    cursor = existing >= 0 ? existing : 0;
    mapPage = Math.floor(cursor / mapSize);
    prepareReview();
    render();
  }
  function readFilters() {
    const categoryChanged = state.filters.category !== el("category").value;
    state.filters = {category: el("category").value, status: el("status").value, search: el("search").value};
    // 과목을 바꿀 때는 그 과목의 위치로 돌아가고, 검색어나 풀이 범위를 바꿀 때는 결과를 처음부터 보여 줍니다.
    if (categoryChanged) state.currentId = state.positions[state.filters.category] || null;
    state.view = "learn";
    applyFilters(categoryChanged);
  }

  function renderStats() {
    const summary = core.summarize(state, bank);
    el("answered").replaceChildren(document.createTextNode(format(summary.answered) + " "));
    const small = document.createElement("small"); small.textContent = "/ " + format(summary.total); el("answered").append(small);
    el("correct").textContent = format(summary.correct);
    el("incorrect").textContent = format(summary.wrong);
    el("accuracy").textContent = summary.accuracy === null ? "—" : summary.accuracy + "%";
    el("wrong-badge").textContent = format(summary.wrong);
    el("bookmark-badge").textContent = format(state.bookmarks.length);
    el("overall-percent").textContent = summary.percent + "%";
    el("overall-bar").style.width = summary.percent + "%";
    el("overall-progress").setAttribute("aria-valuenow", String(summary.percent));
    el("overall-detail").textContent = format(summary.answered) + " / " + format(summary.total) + "문제 완료";
    el("journey-title").textContent = summary.answered === summary.total ? "한 바퀴 완주했어요. 이제 더 깊이!"
      : summary.answered ? "쌓인 만큼, 실력이 되고 있어요." : "첫 문제부터 시작해 볼까요?";
    const current = byId.get(state.currentId);
    el("journey-description").textContent = current
      ? shortName(categories.get(current.category)) + " · " + (current.code || "Q" + current.id) + "에서 이어갈 수 있어요."
      : "정답을 확인할 때마다 학습 진도가 쌓입니다.";
    el("continue-learning").textContent = current ? "이어서 학습하기 →" : "학습 시작하기 →";

    const local = core.summarize(state, bank, state.filters.category);
    el("session-category").textContent = categories.has(state.filters.category) ? shortName(categories.get(state.filters.category)) : "전체 과목";
    el("session-percent").replaceChildren(document.createTextNode(String(local.percent)));
    const percent = document.createElement("span"); percent.textContent = "%"; el("session-percent").append(percent);
    el("session-detail").textContent = format(local.answered) + " / " + format(local.total) + "문제";
    el("progress-bar").style.width = local.percent + "%";
    el("session-correct").textContent = format(local.correct);
    el("session-wrong").textContent = format(local.wrong);
    el("session-remaining").textContent = format(local.remaining);
  }

  function renderSubjects() {
    el("subject-count").textContent = bank.categories.length + "개 과목";
    el("subject-grid").replaceChildren();
    bank.categories.forEach((category, index) => {
      const summary = core.summarize(state, bank, category.id);
      const card = document.createElement("article"); card.className = "subject-card";
      const number = document.createElement("span"); number.className = "subject-index"; number.textContent = String(index + 1).padStart(2, "0");
      const heading = document.createElement("h3"); heading.textContent = shortName(category);
      const numbers = document.createElement("div"); numbers.className = "subject-numbers";
      const detail = document.createElement("span"); detail.textContent = summary.answered + " / " + summary.total + "문제 완료";
      const rate = document.createElement("strong"); rate.textContent = summary.percent + "%"; numbers.append(detail, rate);
      const track = document.createElement("div"); track.className = "progress-track";
      const bar = document.createElement("div"); bar.style.width = summary.percent + "%"; track.append(bar);
      const result = document.createElement("p"); result.className = "subject-result";
      result.textContent = "정답 " + summary.correct + " · 오답 " + summary.wrong + " · 미풀이 " + summary.remaining;
      const start = document.createElement("button"); start.type = "button";
      // 답을 제출하지 않았어도 positions에 위치가 있으면 이미 본 과목이므로 이어풀기로 안내합니다.
      start.textContent = summary.answered || state.positions[category.id] ? "이어서 학습하기 →" : "학습 시작하기 →";
      start.setAttribute("aria-label", shortName(category) + " " + start.textContent);
      start.addEventListener("click", () => {
        state.filters = {category: category.id, status: "all", search: ""}; state.view = "learn";
        // 저장한 위치가 있으면 그 문항부터, 없으면 첫 미풀이부터 시작하고, 모두 풀었다면 첫 문항을 보여 줍니다.
        const questions = bank.questions.filter(q => q.category === category.id);
        state.currentId = state.positions[category.id]
          || (questions.find(q => !core.hasAnswer(state, q.id)) || questions[0])?.id || null;
        syncControls(); applyFilters(true); scrollToLearning();
      });
      card.append(number, heading, numbers, track, result, start); el("subject-grid").append(card);
    });
  }

  function renderNavigation() {
    const subjects = state.view === "subjects";
    const mode = subjects ? "과목별 학습" : scopeNames[state.filters.status];
    el("breadcrumb").textContent = mode;
    el("subjects-view").hidden = !subjects;
    el("learning-view").hidden = subjects;
    const active = subjects ? "view-subjects" : state.filters.status === "wrong" ? "view-wrong"
      : state.filters.status === "bookmarked" ? "view-bookmarks" : "view-learn";
    ["view-learn", "view-subjects", "view-wrong", "view-bookmarks"].forEach(id => {
      if (id === active) el(id).setAttribute("aria-current", "page"); else el(id).removeAttribute("aria-current");
    });
    el("mode-heading").textContent = scopeNames[state.filters.status];
    el("mode-eyebrow").textContent = state.filters.status === "wrong" ? "MISTAKES MAKE PROGRESS" : "LET’S PRACTICE";
    el("scope-description").textContent = state.filters.status === "wrong" ? "정답을 가리고 다시 풀어요. 맞히면 오답 노트에서 졸업!"
      : "나의 속도로 한 문제씩 풀어 보세요.";
    el("result-count").textContent = format(core.select(state, bank).length) + "문제";
  }

  function renderMap() {
    el("question-map").replaceChildren();
    const pages = Math.ceil(subset.length / mapSize);
    mapPage = Math.max(0, Math.min(mapPage, pages - 1));
    const start = mapPage * mapSize;
    subset.slice(start, start + mapSize).forEach((id, offset) => {
      const q = byId.get(id), button = document.createElement("button"); button.type = "button";
      button.textContent = q.code ? q.code.slice(1) : String(id);
      const answered = core.hasAnswer(state, id), good = answered && state.answers[id] === q.answer;
      if (answered) button.className = good ? "good" : "bad";
      button.setAttribute("aria-label", (q.code || "Q" + id) + " · " + (answered ? good ? "정답" : "오답" : "미풀이"));
      button.setAttribute("aria-current", String(!completed && id === state.currentId));
      button.addEventListener("click", () => { completed = false; cursor = start + offset; render(); el("question-title").focus({preventScroll: true}); });
      el("question-map").append(button);
    });
    el("map-range").textContent = subset.length ? (start + 1) + "–" + Math.min(start + mapSize, subset.length) : "0";
    el("map-page").textContent = pages ? (mapPage + 1) + " / " + pages : "0 / 0";
    el("map-previous").disabled = mapPage === 0;
    el("map-next").disabled = mapPage >= pages - 1;
  }

  function renderQuestion() {
    const empty = !subset.length || completed;
    el("quiz").hidden = empty; el("empty").hidden = !empty;
    if (empty) {
      const reviewDone = state.filters.status === "wrong" && !core.select(state, bank).length && !state.filters.search;
      el("empty-heading").textContent = reviewDone ? "이 범위의 오답을 모두 복습했어요!" : completed ? "선택한 문제를 끝까지 살펴봤어요." : "선택한 조건에 맞는 문제가 없어요.";
      el("empty-description").textContent = reviewDone ? "맞힌 문제는 오답 노트에서 빠집니다. 다른 과목에도 도전해 보세요."
        : completed ? "과목별 진도를 확인하거나 아직 풀지 않은 문제를 이어서 풀어 보세요."
        : "과목이나 풀이 범위를 바꾸거나 검색어를 지워 보세요.";
      return;
    }
    const q = byId.get(subset[cursor]); state.currentId = q.id;
    // 보기만 한 문항도 위치를 저장해, 답을 제출하지 않고 다른 과목에 다녀와도 같은 문항으로 돌아오게 합니다.
    state.positions[q.category] = q.id;
    const answered = core.hasAnswer(state, q.id), revealed = answered && !state.retries.includes(q.id);
    const choice = revealed ? state.answers[q.id] : core.validChoice(state.drafts[q.id]) ? state.drafts[q.id] : null;
    el("category-name").textContent = shortName(categories.get(q.category));
    el("position").textContent = (cursor + 1) + " / " + format(subset.length);
    el("question-number").textContent = "QUESTION " + (q.code || String(q.id).padStart(3, "0"));
    el("question-title").textContent = q.prompt;
    el("bookmark").setAttribute("aria-pressed", String(state.bookmarks.includes(q.id)));
    el("bookmark").textContent = state.bookmarks.includes(q.id) ? "★ 북마크됨" : "☆ 북마크";
    el("options").replaceChildren();
    q.options.forEach((text, index) => {
      const label = document.createElement("label"); label.className = "option";
      const input = document.createElement("input"); input.type = "radio"; input.name = "answer";
      input.value = index; input.checked = choice === index; input.disabled = revealed;
      const number = document.createElement("span"); number.className = "number"; number.textContent = index + 1;
      number.setAttribute("aria-hidden", "true");
      const value = document.createElement("span"); value.className = "option-text"; value.textContent = text;
      label.append(input, number, value);
      input.addEventListener("change", () => {
        state.drafts[q.id] = index;
        el("answer-hint").textContent = (index + 1) + "번 선택 · 정답을 확인해 보세요.";
        el("feedback").textContent = ""; save();
      });
      if (revealed && (index === q.answer || index === state.answers[q.id])) {
        const verdict = document.createElement("span"); verdict.className = "verdict";
        const good = index === q.answer; label.classList.add(good ? "good" : "bad");
        verdict.textContent = good ? "✓ 정답" : "내 선택"; label.append(verdict);
      }
      el("options").append(label);
    });
    el("answer-hint").textContent = revealed ? "해설을 읽고 개념을 연결해 보세요." : choice === null ? "보기 하나를 선택하세요." : (choice + 1) + "번 선택 · 정답을 확인해 보세요.";
    el("grade").hidden = revealed; el("retry").hidden = !revealed;
    el("feedback").className = "feedback" + (revealed ? state.answers[q.id] === q.answer ? " good-text" : " bad-text" : "");
    el("feedback").textContent = revealed ? state.answers[q.id] === q.answer ? "✓ 정답이에요. 잘 이해했어요!" : "다시 배울 기회예요. 정답은 " + (q.answer + 1) + "번입니다." : "";
    el("explanation").hidden = !revealed;
    el("explanation-text").textContent = revealed ? q.explanation : "";
    el("reference").href = q.reference || categories.get(q.category).reference;
    el("previous").disabled = cursor === 0;
    el("next").textContent = cursor === subset.length - 1 ? "학습 마치기 ✓" : "다음 문제 →";
  }
  function render() {
    renderQuestion(); renderStats(); renderNavigation(); renderMap();
    if (state.view === "subjects") renderSubjects();
    save();
  }
  function scrollToLearning() {
    el(state.view === "subjects" ? "subjects-heading" : "mode-heading").scrollIntoView({block: "start"});
  }
  function openScope(scope) {
    state.view = "learn"; state.filters.status = scope; state.filters.search = "";
    if (scope === "wrong" || scope === "bookmarked") state.filters.category = "all";
    syncControls(); applyFilters(); scrollToLearning();
  }

  el("bank-select").addEventListener("change", () => {
    save(); const url = new URL(window.location.href); url.search = "";
    url.searchParams.set("bank", el("bank-select").value); window.location.href = url.href;
  });
  ["category", "status"].forEach(id => el(id).addEventListener("change", readFilters));
  el("search").addEventListener("input", readFilters);
  el("view-subjects").addEventListener("click", () => { state.view = "subjects"; render(); scrollToLearning(); });
  el("view-learn").addEventListener("click", () => openScope("all"));
  el("view-wrong").addEventListener("click", () => openScope("wrong"));
  el("view-bookmarks").addEventListener("click", () => openScope("bookmarked"));
  el("continue-learning").addEventListener("click", () => {
    state.view = "learn";
    // 끝난 복습 범위라면 현재 과목의 전체 문제로 돌아간다.
    if (!core.select(state, bank).length) { state.filters.status = "all"; state.filters.search = ""; }
    syncControls(); applyFilters(true); scrollToLearning();
  });
  el("shuffle").addEventListener("click", () => {
    state.randomized = !state.randomized;
    if (state.randomized) {
      state.randomOrder = bank.questions.map(q => q.id);
      for (let i = state.randomOrder.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [state.randomOrder[i], state.randomOrder[j]] = [state.randomOrder[j], state.randomOrder[i]];
      }
    }
    syncControls(); applyFilters();
  });
  el("grade").addEventListener("click", () => {
    if (!subset.length || completed) return;
    if (!core.validChoice(state.drafts[state.currentId])) {
      el("feedback").textContent = "보기 하나를 고른 뒤 정답을 확인하세요."; return;
    }
    state = core.grade(state, bank, state.currentId, state.drafts[state.currentId]);
    // 채점한 해설을 읽을 수 있도록, 다음 문항으로 이동하기 전에는 현재 문항을 유지한다.
    render();
  });
  el("retry").addEventListener("click", () => {
    if (!state.retries.includes(state.currentId)) state.retries.push(state.currentId);
    delete state.drafts[state.currentId]; render();
  });
  el("bookmark").addEventListener("click", () => {
    state.bookmarks = state.bookmarks.includes(state.currentId) ? state.bookmarks.filter(id => id !== state.currentId) : [...state.bookmarks, state.currentId];
    render();
  });
  function move(delta) {
    if (!subset.length || completed) return;
    if (delta > 0 && cursor === subset.length - 1) {
      // 미제출 선택은 보존하고 마지막 문제에서도 학습 종료 화면으로 이동한다.
      completed = true; render(); el("empty-heading").scrollIntoView({block: "nearest"}); return;
    }
    cursor = Math.max(0, Math.min(subset.length - 1, cursor + delta));
    mapPage = Math.floor(cursor / mapSize); render();
    el("question-title").focus({preventScroll: true}); el("quiz").scrollIntoView({block: "nearest"});
  }
  el("previous").addEventListener("click", () => move(-1));
  el("next").addEventListener("click", () => move(1));
  el("map-previous").addEventListener("click", () => { mapPage--; renderMap(); });
  el("map-next").addEventListener("click", () => { mapPage++; renderMap(); });
  el("show-all").addEventListener("click", () => {
    state.filters = {category: "all", status: "all", search: ""}; state.view = "learn";
    syncControls(); applyFilters();
  });

  el("export").addEventListener("click", () => {
    // 다른 브라우저에서도 같은 문제집의 기록인지 확인할 수 있도록 종류·버전과 학습 상태를 함께 내보냅니다.
    const backup = {format: "scm-study-backup", bankVersion: bank.version, exportedAt: new Date().toISOString(), state};
    const url = URL.createObjectURL(new Blob([JSON.stringify(backup, null, 2)], {type: "application/json"}));
    const link = document.createElement("a"); link.href = url;
    link.download = "scm-study-" + (workbook ? "workbook-" : "sw-") + new Date().toISOString().slice(0, 10) + ".json";
    link.click(); setTimeout(() => URL.revokeObjectURL(url), 1000);
    notice("학습 기록 파일을 내보냈습니다. 다른 브라우저에서 같은 문제집을 열고 가져올 수 있습니다.");
  });
  el("import").addEventListener("click", () => el("import-file").click());
  el("import-file").addEventListener("change", async event => {
    const file = event.target.files[0]; if (!file) return;
    try {
      if (file.size > 5 * 1024 * 1024) throw new Error("학습 기록 파일은 5MB 이하만 가져올 수 있습니다.");
      const backup = JSON.parse(await file.text());
      // 다른 문제집의 답안을 잘못 덮어쓰지 않도록 파일 형식과 현재 문제집 버전을 먼저 확인합니다.
      if (!backup || typeof backup !== "object" || backup.format !== "scm-study-backup" || backup.bankVersion !== bank.version || !backup.state || typeof backup.state !== "object" || Array.isArray(backup.state)) {
        throw new Error("현재 문제집에서 내보낸 기록 파일을 선택해 주세요. 문제집 종류와 버전이 같아야 합니다.");
      }
      if (!window.confirm("가져온 기록으로 현재 문제집의 학습 기록을 바꿀까요? 현재 기록은 먼저 내보내기로 보관할 수 있습니다.")) return;
      state = core.restore(backup.state, bank); syncControls(); applyFilters(true);
      notice("학습 기록을 가져왔습니다. 저장한 위치에서 이어서 풀어 보세요.");
    } catch (error) { notice(error instanceof SyntaxError ? "올바른 JSON 학습 기록 파일이 아닙니다." : error.message); }
    finally { event.target.value = ""; }
  });
  el("reset").addEventListener("click", () => {
    if (!window.confirm("현재 문제집의 답안·오답·북마크·진도를 모두 초기화할까요? 다른 문제집의 기록은 유지됩니다.")) return;
    state = core.restore(null, bank); syncControls(); applyFilters(); notice("현재 문제집의 학습 기록을 초기화했습니다.");
  });
  document.addEventListener("keydown", event => {
    if (event.ctrlKey || event.metaKey || event.altKey || event.target.matches("input:not([type=radio]), select, textarea")
      || event.target.isContentEditable || state.view !== "learn" || completed) return;
    if (!/^[1-4]$/.test(event.key) || !subset.length) return;
    const input = el("options").querySelectorAll("input")[Number(event.key) - 1];
    if (input && !input.disabled) { input.checked = true; input.dispatchEvent(new Event("change")); event.preventDefault(); }
  });
  syncControls(); applyFilters(true);
})();
