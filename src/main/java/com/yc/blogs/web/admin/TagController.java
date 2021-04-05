package com.yc.blogs.web.admin;

import com.yc.blogs.po.Tag;
import com.yc.blogs.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagsService tagsService;

    /**
     *
     * @param pageable
     * @param model 前端页面展示模型
     * @return
     */
    @GetMapping("tags")
    public String tags(@PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC)
                       Pageable pageable, Model model) {
        model.addAttribute("page", tagsService.listTag(pageable));
        return "admin/tags";
    }

//    新增跳转
    @GetMapping("/tags/input")
    public String input() {
        return "admin/tags-input";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagsService.getTag(id));
        return "admin/tags-input";
    }

    @PostMapping("/tags")
    public String post(Tag tag, RedirectAttributes attributes) {
        Tag tag1 = tagsService.getTagByName(tag.getName());
        System.out.println("不能重复添加"+tag1);
        if (tag1 != null ) {
            attributes.addFlashAttribute("message", "已存在,不能重复添加");
            System.out.println("不能重复添加"+tag1);
            return "redirect:/admin/tags/input";
        }
        Tag t = tagsService.savaTag(tag);
        if (t == null ) {
            attributes.addFlashAttribute("message", "操作失败！");
        }else {
            attributes.addFlashAttribute("message", "操作成功！");
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/{id}")
    public String editPost(@Validated Tag tag, @PathVariable Long id, RedirectAttributes attributes) {
        Tag tag1 = tagsService.getTagByName(tag.getName());
        if (tag1 != null ) {
            attributes.addFlashAttribute("message", "已存在,不能重复添加");
            System.out.println("不能重复添加"+tag1);
            return "redirect:/admin/tags/input";
        }
        Tag t = tagsService.updateTag(id, tag);
        if (t == null ) {
            attributes.addFlashAttribute("message", "操作失败！");
        }else {
            attributes.addFlashAttribute("message", "操作成功！");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        tagsService.deleteTag(id);
        attributes.addFlashAttribute("message", "删除成功！");
        return "redirect:/admin/tags";
    }
}
