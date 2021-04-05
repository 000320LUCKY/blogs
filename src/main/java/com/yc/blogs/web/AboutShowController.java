package com.yc.blogs.web;

import com.yc.blogs.service.BlogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutShowController {

    @Autowired
    private BlogsService blogsService;

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("footer/newblog")
    public String newBlogs(Model model) {
        model.addAttribute("newblogs", blogsService.listBlogTop(3));
        return "_fragment :: newblogList";
    }
}
