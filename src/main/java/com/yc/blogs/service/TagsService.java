package com.yc.blogs.service;


import com.yc.blogs.po.Tag;
import com.yc.blogs.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagsService {

    Tag savaTag(Tag tag);

    Tag getTag(Long id);

    Page<Tag> listTag(Pageable pageable);

    Tag updateTag(Long id, Tag tag);

    void deleteTag (Long id);

    Tag getTagByName(String name);

    List <Tag> listTag();

    List<Tag> listTag(String ids);

    List<Tag> listTagTop(Integer size);


}
