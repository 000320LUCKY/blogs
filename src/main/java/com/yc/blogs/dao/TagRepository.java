package com.yc.blogs.dao;


import com.yc.blogs.po.Tag;
import com.yc.blogs.po.Type;
import javafx.scene.control.TableColumn;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByName(String name);


    @Query("select t from Tag t")
    List<Tag> findTop (Pageable pageable);
}
