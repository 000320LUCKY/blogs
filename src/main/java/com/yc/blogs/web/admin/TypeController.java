package com.yc.blogs.web.admin;

import com.yc.blogs.po.Type;
import com.yc.blogs.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    /**
     *
     * @param pageable
     * @param model 前端页面展示模型
     * @return
     */
    @GetMapping("/types")
    public String types(@PageableDefault(size = 3, sort = {"id"},direction = Sort.Direction.DESC)
                                    Pageable pageable, Model model) {
        model.addAttribute("page", typeService.listType(pageable));
        return "admin/types";
    }

//    新增跳转
    @GetMapping("/types/input")
    public String input() {
        return "admin/type-input";
    }

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("type", typeService.getType(id));
        return "admin/type-input";
    }

    @PostMapping("/types")
    public String post(Type type, RedirectAttributes attributes) {
        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null ) {
            attributes.addFlashAttribute("message", "已存在,不能重复添加");
            System.out.println("不能重复添加"+type1);
            return "redirect:/admin/types/input";
        }
        Type t = typeService.saveType(type);
        if (t == null ) {
            attributes.addFlashAttribute("message", "操作失败！");
        }else {
            attributes.addFlashAttribute("message", "操作成功！");
        }
        return "redirect:/admin/types";
    }


    @PostMapping("/types/{id}")
    public String editPost(@Validated  Type type, @PathVariable Long id, RedirectAttributes attributes) {
        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null ) {
            attributes.addFlashAttribute("message", "已存在,不能重复添加");
            System.out.println("不能重复添加"+type1);
            return "redirect:/admin/types/input";
        }
        Type t = typeService.updateType(id, type);
        if (t == null) {
            attributes.addFlashAttribute("message", "更新失败！");
        }else {
            attributes.addFlashAttribute("message", "更新成功！");
        }
        return "redirect:/admin/types";
    }


    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        typeService.deleteType(id);
        attributes.addFlashAttribute("message","删除成功！");
        return "redirect:/admin/types";
    }

}
