package com.example.scm.controller.web;

import com.example.scm.common.auth.CurrentUser;
import com.example.scm.common.auth.LoginUser;
import com.example.scm.common.exception.BusinessException;
import com.example.scm.domain.enums.ItemStatus;
import com.example.scm.dto.item.ItemForm;
import com.example.scm.dto.item.ItemListView;
import com.example.scm.dto.item.ItemSearchForm;
import com.example.scm.service.CategoryService;
import com.example.scm.service.ItemService;
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
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemWebController {

    private final ItemService itemService;
    private final CategoryService categoryService;

    @GetMapping
    public String list(@ModelAttribute("searchForm") ItemSearchForm searchForm,
                       @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
                       Pageable pageable,
                       Model model) {
        Page<ItemListView> items = itemService.search(searchForm, pageable);
        model.addAttribute("items", items);
        model.addAttribute("categories", categoryService.list());
        model.addAttribute("statuses", ItemStatus.values());
        return "item/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("itemForm", new ItemForm());
        model.addAttribute("categories", categoryService.list());
        model.addAttribute("mode", "create");
        return "item/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("itemForm") ItemForm itemForm,
                         BindingResult bindingResult,
                         @CurrentUser LoginUser loginUser,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.list());
            model.addAttribute("mode", "create");
            return "item/form";
        }
        try {
            Long id = itemService.create(itemForm, loginUser);
            redirectAttributes.addFlashAttribute("successMessage", "품목이 등록되었습니다.");
            return "redirect:/items/" + id;
        } catch (BusinessException e) {
            model.addAttribute("categories", categoryService.list());
            model.addAttribute("mode", "create");
            model.addAttribute("errorMessage", e.getMessage());
            return "item/form";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("item", itemService.getDetail(id));
        return "item/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("itemForm", itemService.getForm(id));
        model.addAttribute("categories", categoryService.list());
        model.addAttribute("mode", "edit");
        model.addAttribute("itemId", id);
        return "item/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("itemForm") ItemForm itemForm,
                         BindingResult bindingResult,
                         @CurrentUser LoginUser loginUser,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.list());
            model.addAttribute("mode", "edit");
            model.addAttribute("itemId", id);
            return "item/form";
        }
        try {
            itemService.update(id, itemForm, loginUser);
            redirectAttributes.addFlashAttribute("successMessage", "품목이 수정되었습니다.");
            return "redirect:/items/" + id;
        } catch (BusinessException e) {
            model.addAttribute("categories", categoryService.list());
            model.addAttribute("mode", "edit");
            model.addAttribute("itemId", id);
            model.addAttribute("errorMessage", e.getMessage());
            return "item/form";
        }
    }

    @PostMapping("/{id}/discontinue")
    public String discontinue(@PathVariable Long id,
                              @CurrentUser LoginUser loginUser,
                              RedirectAttributes redirectAttributes) {
        itemService.discontinue(id, loginUser);
        redirectAttributes.addFlashAttribute("successMessage", "품목이 단종 처리되었습니다.");
        return "redirect:/items/" + id;
    }
}
