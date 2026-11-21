package com.example.scm.controller.web;
import com.example.scm.common.auth.*;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.dto.notice.NoticeRequest;
import com.example.scm.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 * 브라우저의 공지 화면 요청을 받아 담당 Service에 일을 맡깁니다.
 * Model은 화면에 넣을 자료 꾸러미이고 반환하는 문자열은 사용할 HTML 화면 파일의 이름입니다.
 */
@Controller @RequestMapping("/notices") @RequiredArgsConstructor
public class NoticeWebController {
    private final NoticeService notices;
    @GetMapping
    public String list(@RequestParam(required = false) String keyword, @PageableDefault(size = 20) Pageable pageable,
                       @CurrentUser LoginUser user, Model model) {
        model.addAttribute("notices", notices.list(keyword, pageable, user));
        model.addAttribute("keyword", keyword); return "notice/list";
    }
    @GetMapping("/new")
    public String newForm(@CurrentUser LoginUser user, Model model) {
        // 저장할 때 Service가 다시 검사하지만, 권한 없는 사람이 빈 양식을 채운 뒤에야 거절당하지 않도록 화면을 열 때도 먼저 막습니다.
        Authz.requireRole(user, UserRole.ADMIN);
        model.addAttribute("noticeForm", new NoticeRequest()); return "notice/form";
    }
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, @CurrentUser LoginUser user, Model model) {
        model.addAttribute("notice", notices.detail(id, user)); return "notice/detail";
    }
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, @CurrentUser LoginUser user, Model model) {
        // detail 대신 editDetail을 부릅니다. 관리자가 수정 화면을 열 때마다 조회수가 오르면 실제 읽은 횟수가 부풀려지기 때문입니다.
        var notice = notices.editDetail(id, user);
        // 응답 자료를 그대로 쓰지 않고 입력 양식(NoticeRequest)에 옮겨 담습니다. 저장 버튼을 누르면 같은 양식으로 돌아오게 하기 위해서입니다.
        NoticeRequest form = new NoticeRequest(); form.setTitle(notice.title());
        form.setContent(notice.content()); form.setImportant(notice.important());
        model.addAttribute("noticeForm", form); model.addAttribute("noticeId", id); return "notice/form";
    }
    @PostMapping
    public String create(@Valid @ModelAttribute("noticeForm") NoticeRequest form, BindingResult errors,
                         @CurrentUser LoginUser user, Model model) {
        // 입력 오류를 보여 주기 전에 권한부터 확인합니다. 권한 없는 사람에게 "제목을 입력하세요" 같은 안내를 주지 않기 위해서입니다.
        Authz.requireRole(user, UserRole.ADMIN);
        if (errors.hasErrors()) return "notice/form";
        // 저장 후 redirect로 상세 화면에 보냅니다. 새로고침해도 같은 공지가 두 번 등록되지 않게 하기 위해서입니다.
        try { return "redirect:/notices/" + notices.create(form, user).id(); }
        catch (BusinessException e) { model.addAttribute("errorMessage", e.getMessage()); return "notice/form"; }
    }
    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("noticeForm") NoticeRequest form,
                         BindingResult errors, @CurrentUser LoginUser user, Model model) {
        Authz.requireRole(user, UserRole.ADMIN); model.addAttribute("noticeId", id);
        // 오류가 나도 noticeId를 남겨 같은 글을 수정하게 합니다. 번호가 없으면 새 글 등록 화면이 됩니다.
        if (errors.hasErrors()) return "notice/form";
        try { notices.update(id, form, user); return "redirect:/notices/" + id; }
        catch (BusinessException e) { model.addAttribute("errorMessage", e.getMessage()); return "notice/form"; }
    }
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, @CurrentUser LoginUser user, RedirectAttributes flash) {
        // 삭제는 보여 줄 양식이 없으므로 화면 단계의 권한 검사를 따로 두지 않고, NoticeService.delete의 관리자 검사에 맡깁니다.
        // 삭제는 GET이 아닌 POST로만 받습니다. 링크 미리보기나 검색 로봇이 주소만 열어도 글이 지워지는 일을 막기 위해서입니다.
        notices.delete(id, user); flash.addFlashAttribute("successMessage", "공지가 삭제되었습니다.");
        return "redirect:/notices";
    }
}
