package com.example.scm.controller.web;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.dto.category.CategoryDetailView;
import com.example.scm.dto.category.CategoryForm;
import com.example.scm.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryWebController {

    private final CategoryService categoryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.list());
        return "category/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        model.addAttribute("mode", "create");
        return "category/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("categoryForm") CategoryForm categoryForm,
                         BindingResult bindingResult,
                         @CurrentUser LoginUser loginUser,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "create");
            return "category/form";
        }
        try {
            Long id = categoryService.create(categoryForm, loginUser);
            redirectAttributes.addFlashAttribute("successMessage", "카테고리가 등록되었습니다.");
            return "redirect:/categories/" + id;
        } catch (BusinessException e) {
            model.addAttribute("mode", "create");
            model.addAttribute("errorMessage", e.getMessage());
            return "category/form";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        CategoryDetailView detail = categoryService.getDetail(id);
        model.addAttribute("category", detail);
        model.addAttribute("items", detail.getItems());
        return "category/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("categoryForm", categoryService.getForm(id));
        model.addAttribute("mode", "edit");
        model.addAttribute("categoryId", id);
        return "category/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("categoryForm") CategoryForm categoryForm,
                         BindingResult bindingResult,
                         @CurrentUser LoginUser loginUser,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("categoryId", id);
            return "category/form";
        }
        try {
            categoryService.update(id, categoryForm, loginUser);
            redirectAttributes.addFlashAttribute("successMessage", "카테고리가 수정되었습니다.");
            return "redirect:/categories/" + id;
        } catch (BusinessException e) {
            model.addAttribute("mode", "edit");
            model.addAttribute("categoryId", id);
            model.addAttribute("errorMessage", e.getMessage());
            return "category/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @CurrentUser LoginUser loginUser,
                         RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id, loginUser);
            redirectAttributes.addFlashAttribute("successMessage", "카테고리가 삭제되었습니다.");
            return "redirect:/categories";
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/categories/" + id;
        }
    }
}
