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
        Authz.requireRole(user, UserRole.ADMIN);
        model.addAttribute("noticeForm", new NoticeRequest()); return "notice/form";
    }
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, @CurrentUser LoginUser user, Model model) {
        model.addAttribute("notice", notices.detail(id, user)); return "notice/detail";
    }
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, @CurrentUser LoginUser user, Model model) {
        var notice = notices.editDetail(id, user);
        NoticeRequest form = new NoticeRequest(); form.setTitle(notice.title());
        form.setContent(notice.content()); form.setImportant(notice.important());
        model.addAttribute("noticeForm", form); model.addAttribute("noticeId", id); return "notice/form";
    }
    @PostMapping
    public String create(@Valid @ModelAttribute("noticeForm") NoticeRequest form, BindingResult errors,
                         @CurrentUser LoginUser user, Model model) {
        Authz.requireRole(user, UserRole.ADMIN);
        if (errors.hasErrors()) return "notice/form";
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
        notices.delete(id, user); flash.addFlashAttribute("successMessage", "공지가 삭제되었습니다.");
        return "redirect:/notices";
    }
}
