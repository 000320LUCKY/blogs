package com.yc.blogs.web.admin;

import com.yc.blogs.po.Blog;
import com.yc.blogs.po.User;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogsController {

    private static final String INPUt = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogsService blogsService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagsService tagsService;

    @GetMapping("/blogs")
    public String blogs(Model model,
                        @PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogsService.listBlog(pageable, blog));
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(Model model,
                         @PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog) {
        model.addAttribute("page", blogsService.listBlog(pageable, blog));
        return "admin/blogs :: blogList";
    }


    @GetMapping("/blogs/input")
    public String input(Model model) {
//        初始化blog后能在model里拿到数据
        model.addAttribute("blog", new Blog());
        setTypeAndTag(model);
        return INPUt;
    }

//    设置分类与标签
    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagsService.listTag());
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        Blog blog = blogsService.getBlog(id);
        blog.init();
        model.addAttribute("blog", blogsService.getBlog(id));
        return INPUt;
    }

    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagsService.listTag(blog.getTagIds()));

//        修改，新增方法一
//        Blog b1;
//        if (blog.getId() == null) {
//            b1 = blogsService.saveBlog(blog);
//        }else {
//            try {
//                b1 = blogsService.updateBlog(blog.getId(), blog);
//            } catch (NotFoundException e) {
//                e.printStackTrace();
//            }
//        }


        //        修改，新增方法二
        Blog b = blogsService.saveBlog(blog);
        if (b == null) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_LIST;

    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogsService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功！");
        return REDIRECT_LIST;
    }

}
