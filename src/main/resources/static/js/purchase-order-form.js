/* ============================================================
   purchase-order-form.js
   학습 모듈 35-7: purchaseorder/form.html의 lines[i].* 구조를 이해한 뒤 읽는다.
   발주 작성 폼의 라인 동적 추가/삭제 + 인덱스 재정렬.
   - 서버 폼 객체 PurchaseOrderCreateRequest 의 lines[i].itemId/quantity/unitPrice 인덱스 바인딩에 맞춘다.
   - 행 삭제/추가 후 모든 라인의 name/id 인덱스를 0..n 으로 재정렬한다(서버 List 바인딩 누락 방지).
   - 외부 CDN 미사용(오프라인).
   ============================================================ */
// 이 코드는 발주·수주 작성 화면에서 물건 줄을 추가하거나 지웁니다.
// 브라우저에서 하는 일은 입력 화면 정리입니다. 가격 계산과 입력 검사는 서버에서도 수행합니다.
(function () {
    "use strict";

    // document는 현재 웹 화면입니다. getElementById는 이름표(id)로 화면 요소를 찾습니다.
    var tbody = document.getElementById("po-line-body");
    var addBtn = document.getElementById("po-add-line");
    var template = document.getElementById("po-line-template");
    if (!tbody || !addBtn || !template) {
        // 이 세 가지가 없는 다른 화면에서는 줄을 다룰 필요가 없으므로 아무 일도 하지 않고 끝냅니다.
        return;
    }

    // 라인 한 행의 입력 필드 name/id 를 주어진 인덱스로 재작성한다.
    function applyIndex(row, index) {
        // 한 줄에 있는 물건·수량·가격 입력칸을 찾습니다. index는 컴퓨터가 0부터 세는 줄 번호입니다.
        var fields = row.querySelectorAll("[data-name]");
        fields.forEach(function (el) {
            var base = el.getAttribute("data-name"); // 품목 번호·수량·단가 중 어느 입력칸인지 나타냅니다.
            // 첫 줄은 lines[0].quantity처럼 이름을 붙여 서버가 어떤 줄의 값인지 구별하게 합니다.
            el.setAttribute("name", "lines[" + index + "]." + base);
            el.setAttribute("id", "lines" + index + "_" + base);
        });
        var label = row.querySelector(".po-line-no");
        if (label) {
            label.textContent = index + 1;
        }
    }

    // 전체 행을 0..n 으로 재정렬.
    // 예: 0, 1, 2번 중 1번을 지우면 남은 0, 2번을 0, 1번으로 다시 매깁니다.
    function reindex() {
        var rows = tbody.querySelectorAll("tr.po-line");
        rows.forEach(function (row, i) {
            applyIndex(row, i);
        });
        toggleRemoveButtons(rows.length);
    }

    // 라인이 1개뿐이면 삭제 버튼 비활성(최소 1건 유지).
    function toggleRemoveButtons(count) {
        var btns = tbody.querySelectorAll(".po-remove-line");
        btns.forEach(function (btn) {
            btn.disabled = (count <= 1);
        });
    }

    // 라인 추가: 템플릿 복제 → 인덱스 부여.
    // addEventListener는 버튼을 누르는 순간 실행할 일을 등록합니다.
    addBtn.addEventListener("click", function () {
        var index = tbody.querySelectorAll("tr.po-line").length;
        // template은 빈 물건 줄의 본보기입니다. cloneNode(true)로 안쪽 입력칸까지 복사합니다.
        var node = template.content
            ? template.content.firstElementChild.cloneNode(true)
            : template.firstElementChild.cloneNode(true);
        applyIndex(node, index);
        tbody.appendChild(node);
        toggleRemoveButtons(tbody.querySelectorAll("tr.po-line").length);
    });

    // 라인 삭제(이벤트 위임).
    // 나중에 추가된 삭제 버튼도 작동하도록 표가 클릭을 받고 어떤 버튼인지 확인합니다.
    tbody.addEventListener("click", function (e) {
        // e.target은 실제 누른 곳입니다. closest는 그곳에서 가장 가까운 삭제 버튼을 찾습니다.
        var btn = e.target.closest(".po-remove-line");
        if (!btn) {
            return;
        }
        var rows = tbody.querySelectorAll("tr.po-line");
        if (rows.length <= 1) {
            return; // 최소 1건 유지
        }
        btn.closest("tr.po-line").remove();
        reindex();
    });

    // 품목 선택 시 표준단가를 단가 입력의 placeholder 로 안내(미입력 시 서버가 표준단가 적용).
    tbody.addEventListener("change", function (e) {
        var sel = e.target;
        if (!sel.matches("[data-name='itemId']")) {
            return;
        }
        var row = sel.closest("tr.po-line");
        var priceInput = row.querySelector("[data-name='unitPrice']");
        var opt = sel.options[sel.selectedIndex];
        var stdPrice = opt ? opt.getAttribute("data-price") : null;
        if (priceInput && stdPrice) {
            // placeholder는 빈 입력칸에 보이는 안내입니다. 가격을 실제 입력값으로 저장하는 것은 아닙니다.
            priceInput.placeholder = "표준단가 " + Number(stdPrice).toLocaleString();
        } else if (priceInput) {
            priceInput.placeholder = "미입력 시 표준단가 적용";
        }
    });

    // 초기 상태 보정(서버가 빈 라인 1건 렌더 → 삭제 버튼 비활성).
    reindex();
})();
