package com.yc.blogs.web;

import com.yc.blogs.po.Comment;
import com.yc.blogs.po.User;
import com.yc.blogs.service.BlogsService;
import com.yc.blogs.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogsService blogsService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        System.out.println(blogId+"评论"+commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session) {
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogsService.getBlog(blogId));
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatat());
            comment.setAdminComment(true);
        } else {
            comment.setAvatar(avatar);
        }
//        comment.setAvatar(avatar);
        commentService.saveComment(comment);
        System.out.println(blogId+"重定向"+comment);
        return "redirect:/comments/" + blogId;
    }
}
