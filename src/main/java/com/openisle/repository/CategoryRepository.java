package com.openisle.repository;

import com.openisle.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * CategoryRepository
 *
 * 分类仓库接口，提供基于名称的常用查询能力。
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // 名称模糊搜索（忽略大小写）
    List<Category> findByNameContainingIgnoreCase(String keyword);
}
