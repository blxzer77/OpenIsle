package com.openisle.repository;

import com.openisle.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 分类仓库接口。
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // 名称模糊搜索（忽略大小写）
    List<Category> findByNameContainingIgnoreCase(String keyword);
}
