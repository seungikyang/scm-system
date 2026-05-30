/* ============================================================
   purchase-order-form.js
   발주 작성 폼의 라인 동적 추가/삭제 + 인덱스 재정렬.
   - 서버 폼 객체 PurchaseOrderCreateRequest 의 lines[i].itemId/quantity/unitPrice 인덱스 바인딩에 맞춘다.
   - 행 삭제/추가 후 모든 라인의 name/id 인덱스를 0..n 으로 재정렬한다(서버 List 바인딩 누락 방지).
   - 외부 CDN 미사용(오프라인).
   ============================================================ */
(function () {
    "use strict";

    var tbody = document.getElementById("po-line-body");
    var addBtn = document.getElementById("po-add-line");
    var template = document.getElementById("po-line-template");
    if (!tbody || !addBtn || !template) {
        return;
    }

    // 라인 한 행의 입력 필드 name/id 를 주어진 인덱스로 재작성한다.
    function applyIndex(row, index) {
        var fields = row.querySelectorAll("[data-name]");
        fields.forEach(function (el) {
            var base = el.getAttribute("data-name"); // itemId | quantity | unitPrice
            el.setAttribute("name", "lines[" + index + "]." + base);
            el.setAttribute("id", "lines" + index + "_" + base);
        });
        var label = row.querySelector(".po-line-no");
        if (label) {
            label.textContent = index + 1;
        }
    }

    // 전체 행을 0..n 으로 재정렬.
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
    addBtn.addEventListener("click", function () {
        var index = tbody.querySelectorAll("tr.po-line").length;
        var node = template.content
            ? template.content.firstElementChild.cloneNode(true)
            : template.firstElementChild.cloneNode(true);
        applyIndex(node, index);
        tbody.appendChild(node);
        toggleRemoveButtons(tbody.querySelectorAll("tr.po-line").length);
    });

    // 라인 삭제(이벤트 위임).
    tbody.addEventListener("click", function (e) {
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
            priceInput.placeholder = "표준단가 " + Number(stdPrice).toLocaleString();
        } else if (priceInput) {
            priceInput.placeholder = "미입력 시 표준단가 적용";
        }
    });

    // 초기 상태 보정(서버가 빈 라인 1건 렌더 → 삭제 버튼 비활성).
    reindex();
})();
