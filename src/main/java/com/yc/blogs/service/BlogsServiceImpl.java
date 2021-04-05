package com.yc.blogs.service;

import com.yc.blogs.dao.BlogsRepository;
import com.yc.blogs.po.Blog;
import com.yc.blogs.po.Type;
import com.yc.blogs.util.JpaUtil;
import com.yc.blogs.util.MarkDownUtils;
import com.yc.blogs.util.MyBeanUtils;
import com.yc.blogs.vo.BlogQuery;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogsServiceImpl implements BlogsService {

    @Autowired
    private BlogsRepository blogsRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogsRepository.findById(id).get();
    }


    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogsRepository.findAll(new Specification<Blog>() {
            /**
             *
             * @param root 表的字段、属性值（映射的）
             * @param cq 容器（查询的条件）
             * @param cb 方法（具体条件的表达式）
             *           predicates 条件集合
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%"+blog.getTitle()+"%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                //list转化成数组
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, String query) {

        return blogsRepository.findByQuery(query, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, Long tagId) {
        return blogsRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                关联查询
                Join join = root.join("tags");
                return cb.equal(join.get("id"), tagId);
            }
        },pageable);
    }

    //    转化成HTML
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogsRepository.findById(id).get();
        if (blog == null) {
            try {
                throw new NotFoundException("博客不存在！");
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        Blog b = new Blog();
//        将blog cope给b
        BeanUtils.copyProperties(blog, b);
        String content = b.getContent();
        b.setContent(MarkDownUtils.markdownToHtmlExtensitons(content));

        blogsRepository.updateViews(id);
        return b;
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        Blog save = null;
        if (blog.getId() == null) {
            blog.setViews(0);
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            save =blogsRepository.save(blog);
        } else {
            Optional<Blog> blog1 = blogsRepository.findById(blog.getId());
//            排除Filed里的字段更新
            String[] Filed = {"views", "createTime"};
            JpaUtil.copyNotNullPropertiesExclude(blog, blog1, Filed);
            blog.setUpdateTime(new Date());
            save = blogsRepository.save(blog1.get());
        }
        return save;
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogsRepository.findById(id).get();
        if (b == null) {
            try {
                throw new NotFoundException("该博客不存在");
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
//        只cope  blog里面有值的 到b里面(将为空的过滤)
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogsRepository.save(b);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {

        return blogsRepository.findAll(pageable);
    }


    @Override
    public void deleteBlog(Long id) {
        blogsRepository.deleteById(id);
    }

    @Override
    public List<Blog> listBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogsRepository.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogsRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogsRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogsRepository.count();
    }
}
