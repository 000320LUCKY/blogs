package com.yc.blogs.dao;

import com.yc.blogs.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type, Long> {

    Type findByName(String name);

//    自定义查询
    @Query("select t from Type t")
    List<Type> findTop(Pageable pageable);
}
