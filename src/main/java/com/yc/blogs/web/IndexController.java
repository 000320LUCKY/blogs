package com.yc.blogs.web;

import com.yc.blogs.NotFoundExceptions;
import com.yc.blogs.service.BlogsService;
import com.yc.blogs.service.TagsService;
import com.yc.blogs.service.TypeService;
import com.yc.blogs.vo.BlogQuery;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class IndexController {

    @Autowired
    private BlogsService blogsService;
    @Autowired
    private TypeService typeService;

    @Autowired
    private TagsService tagsService;

    @GetMapping("/")
    public String index(Model model,
                        @PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("page", blogsService.listBlog(pageable));
        model.addAttribute("types", typeService.listTypeTop(6));
        model.addAttribute("tags", tagsService.listTagTop(10));
        model.addAttribute("recommendBlogs", blogsService.listBlogTop(8));
        System.out.println("-------index-------");
        return "index";
    }

    @PostMapping("/search")
    public String search(Model model,
                         @PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, @RequestParam String query) {
//        按分页方式查询
        model.addAttribute("page",blogsService.listBlog(pageable, "%"+query+"%"));
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
//        System.out.println("-------index-------"+blogsService.getAndConvert(id));
        model.addAttribute("blog", blogsService.getAndConvert(id));
        System.out.println("-------index-------");
        return "blog";
    }
}
