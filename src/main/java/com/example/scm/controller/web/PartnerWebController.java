package com.example.scm.controller.web;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.domain.enums.PartnerStatus;
import com.example.scm.domain.enums.PartnerType;
import com.example.scm.dto.partner.PartnerForm;
import com.example.scm.dto.partner.PartnerListView;
import com.example.scm.dto.partner.PartnerSearchForm;
import com.example.scm.service.PartnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/partners")
@RequiredArgsConstructor
public class PartnerWebController {

    private final PartnerService partnerService;

    @GetMapping
    public String list(@ModelAttribute("searchForm") PartnerSearchForm searchForm,
                       @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
                       Pageable pageable,
                       Model model) {
        Page<PartnerListView> partners = partnerService.search(searchForm, pageable);
        model.addAttribute("partners", partners);
        model.addAttribute("partnerTypes", PartnerType.values());
        model.addAttribute("statuses", PartnerStatus.values());
        return "partner/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("partnerForm", new PartnerForm());
        model.addAttribute("partnerTypes", PartnerType.values());
        model.addAttribute("mode", "create");
        return "partner/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("partnerForm") PartnerForm partnerForm,
                         BindingResult bindingResult,
                         @CurrentUser LoginUser loginUser,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("partnerTypes", PartnerType.values());
            model.addAttribute("mode", "create");
            return "partner/form";
        }
        try {
            Long id = partnerService.create(partnerForm, loginUser);
            redirectAttributes.addFlashAttribute("successMessage", "거래처가 등록되었습니다.");
            return "redirect:/partners/" + id;
        } catch (BusinessException e) {
            model.addAttribute("partnerTypes", PartnerType.values());
            model.addAttribute("mode", "create");
            model.addAttribute("errorMessage", e.getMessage());
            return "partner/form";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("partner", partnerService.getDetail(id));
        return "partner/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("partnerForm", partnerService.getForm(id));
        model.addAttribute("partnerTypes", PartnerType.values());
        model.addAttribute("mode", "edit");
        model.addAttribute("partnerId", id);
        return "partner/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("partnerForm") PartnerForm partnerForm,
                         BindingResult bindingResult,
                         @CurrentUser LoginUser loginUser,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("partnerTypes", PartnerType.values());
            model.addAttribute("mode", "edit");
            model.addAttribute("partnerId", id);
            return "partner/form";
        }
        try {
            partnerService.update(id, partnerForm, loginUser);
            redirectAttributes.addFlashAttribute("successMessage", "거래처가 수정되었습니다.");
            return "redirect:/partners/" + id;
        } catch (BusinessException e) {
            model.addAttribute("partnerTypes", PartnerType.values());
            model.addAttribute("mode", "edit");
            model.addAttribute("partnerId", id);
            model.addAttribute("errorMessage", e.getMessage());
            return "partner/form";
        }
    }

    @PostMapping("/{id}/deactivate")
    public String deactivate(@PathVariable Long id,
                             @CurrentUser LoginUser loginUser,
                             RedirectAttributes redirectAttributes) {
        partnerService.deactivate(id, loginUser);
        redirectAttributes.addFlashAttribute("successMessage", "거래처가 비활성 처리되었습니다.");
        return "redirect:/partners/" + id;
    }
}
