package com.yc.blogs.service;

import com.yc.blogs.po.Blog;
import com.yc.blogs.vo.BlogQuery;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogsService {

    Blog getBlog(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable, String query);

    Page<Blog> listBlog(Pageable pageable, Long tagId);

    Blog getAndConvert(Long id);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog) throws NotFoundException;

    Page<Blog> listBlog(Pageable pageable);

    void deleteBlog(Long id);

    List<Blog> listBlogTop(Integer size);

    Map<String, List<Blog>> archiveBlog();

    Long countBlog();

}
