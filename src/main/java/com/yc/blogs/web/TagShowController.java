package com.yc.blogs.web;

import com.yc.blogs.po.Tag;
import com.yc.blogs.service.BlogsService;
import com.yc.blogs.service.TagsService;
import com.yc.blogs.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.metrics.StartupStep;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagsService tagsService;

    @Autowired
    private BlogsService blogsService;

    @GetMapping("/tags/{id}")
    public String tags(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id, Model model) {
//        所有分类
        List<Tag> tags = tagsService.listTagTop(10000);
        if (id == -1) {
            id = tags.get(0).getId();
        }
        System.out.println("-------tags-------");
        model.addAttribute("tags", tags);
        model.addAttribute("page", blogsService.listBlog(pageable, id));
        model.addAttribute("activeTagId", id);
        return "tags";
    }
}
