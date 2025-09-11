package com.openisle.repository;

import com.openisle.model.Tag;
import com.openisle.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * TagRepository
 *
 * 标签仓库接口，提供标签的检索、筛选与作者维度的查询能力。
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
    // 名称模糊搜索（忽略大小写）
    List<Tag> findByNameContainingIgnoreCase(String keyword);
    // 按审核状态筛选
    List<Tag> findByApproved(boolean approved);
    List<Tag> findByApprovedTrue();
    // 名称模糊 + 已审核
    List<Tag> findByNameContainingIgnoreCaseAndApprovedTrue(String keyword);

    // 某创建者的标签（按创建时间倒序，可分页）
    List<Tag> findByCreatorOrderByCreatedAtDesc(User creator, Pageable pageable);
    List<Tag> findByCreator(User creator);
}
