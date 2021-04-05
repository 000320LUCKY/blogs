package com.yc.blogs.service;

import com.yc.blogs.po.Comment;

import java.util.List;


public interface CommentService {

    List<Comment> listCommentByBlogId(Long id);

//    保存
    Comment saveComment(Comment comment);
}
