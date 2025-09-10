package com.openisle.repository;

import com.openisle.model.Post;
import com.openisle.model.User;
import com.openisle.model.Category;
import com.openisle.model.Tag;
import com.openisle.model.PostStatus;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 帖子仓库接口。
 *
 * 说明：
 * - 继承 {@link JpaRepository}，提供基础的 CRUD 与分页能力。
 * - 结合方法名约定与 JPQL 查询，覆盖常见列表/统计需求。
 */
public interface PostRepository extends JpaRepository<Post, Long> {
    // 按状态筛选帖子
    List<Post> findByStatus(PostStatus status);
    List<Post> findByStatus(PostStatus status, Pageable pageable);
    // 按状态并按创建时间倒序
    List<Post> findByStatusOrderByCreatedAtDesc(PostStatus status);
    List<Post> findByStatusOrderByCreatedAtDesc(PostStatus status, Pageable pageable);
    // 按状态并按浏览量倒序
    List<Post> findByStatusOrderByViewsDesc(PostStatus status);
    List<Post> findByStatusOrderByViewsDesc(PostStatus status, Pageable pageable);
    // 某作者的帖子（按创建时间倒序）
    List<Post> findByAuthorAndStatusOrderByCreatedAtDesc(User author, PostStatus status);
    // 分类集合筛选（含分页/排序变体）
    List<Post> findByCategoryInAndStatus(List<Category> categories, PostStatus status);
    List<Post> findByCategoryInAndStatus(List<Category> categories, PostStatus status, Pageable pageable);
    List<Post> findByCategoryInAndStatusOrderByCreatedAtDesc(List<Category> categories, PostStatus status);
    List<Post> findByCategoryInAndStatusOrderByCreatedAtDesc(List<Category> categories, PostStatus status, Pageable pageable);
    // 标签集合筛选（去重，含分页/排序变体）
    List<Post> findDistinctByTagsInAndStatus(List<Tag> tags, PostStatus status);
    List<Post> findDistinctByTagsInAndStatus(List<Tag> tags, PostStatus status, Pageable pageable);
    List<Post> findDistinctByTagsInAndStatusOrderByCreatedAtDesc(List<Tag> tags, PostStatus status);
    List<Post> findDistinctByTagsInAndStatusOrderByCreatedAtDesc(List<Tag> tags, PostStatus status, Pageable pageable);
    List<Post> findDistinctByCategoryInAndTagsInAndStatus(List<Category> categories, List<Tag> tags, PostStatus status);
    List<Post> findDistinctByCategoryInAndTagsInAndStatus(List<Category> categories, List<Tag> tags, PostStatus status, Pageable pageable);
    List<Post> findDistinctByCategoryInAndTagsInAndStatusOrderByCreatedAtDesc(List<Category> categories, List<Tag> tags, PostStatus status);
    List<Post> findDistinctByCategoryInAndTagsInAndStatusOrderByCreatedAtDesc(List<Category> categories, List<Tag> tags, PostStatus status, Pageable pageable);

    // Queries requiring all provided tags to be present
    // 查询包含“全部提供标签”的帖子（利用 HAVING 统计去重后的标签匹配数）
    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t IN :tags AND p.status = :status GROUP BY p.id HAVING COUNT(DISTINCT t.id) = :tagCount")
    List<Post> findByAllTags(@Param("tags") List<Tag> tags, @Param("status") PostStatus status, @Param("tagCount") long tagCount);

    @Query(value = "SELECT p FROM Post p JOIN p.tags t WHERE t IN :tags AND p.status = :status GROUP BY p.id HAVING COUNT(DISTINCT t.id) = :tagCount")
    List<Post> findByAllTags(@Param("tags") List<Tag> tags, @Param("status") PostStatus status,  @Param("tagCount") long tagCount, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t IN :tags AND p.status = :status GROUP BY p.id HAVING COUNT(DISTINCT t.id) = :tagCount ORDER BY p.createdAt DESC")
    List<Post> findByAllTagsOrderByCreatedAtDesc(@Param("tags") List<Tag> tags, @Param("status") PostStatus status, @Param("tagCount") long tagCount);

    @Query(value = "SELECT p FROM Post p JOIN p.tags t WHERE t IN :tags AND p.status = :status GROUP BY p.id HAVING COUNT(DISTINCT t.id) = :tagCount ORDER BY p.createdAt DESC")
    List<Post> findByAllTagsOrderByCreatedAtDesc(@Param("tags") List<Tag> tags, @Param("status") PostStatus status, @Param("tagCount") long tagCount, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t IN :tags AND p.status = :status GROUP BY p.id HAVING COUNT(DISTINCT t.id) = :tagCount ORDER BY p.views DESC")
    List<Post> findByAllTagsOrderByViewsDesc(@Param("tags") List<Tag> tags, @Param("status") PostStatus status, @Param("tagCount") long tagCount);

    @Query(value = "SELECT p FROM Post p JOIN p.tags t WHERE t IN :tags AND p.status = :status GROUP BY p.id HAVING COUNT(DISTINCT t.id) = :tagCount ORDER BY p.views DESC")
    List<Post> findByAllTagsOrderByViewsDesc(@Param("tags") List<Tag> tags, @Param("status") PostStatus status, @Param("tagCount") long tagCount, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE p.category IN :categories AND t IN :tags AND p.status = :status GROUP BY p.id HAVING COUNT(DISTINCT t.id) = :tagCount")
    List<Post> findByCategoriesAndAllTags(@Param("categories") List<Category> categories, @Param("tags") List<Tag> tags, @Param("status") PostStatus status, @Param("tagCount") long tagCount);

    @Query(value = "SELECT p FROM Post p JOIN p.tags t WHERE p.category IN :categories AND t IN :tags AND p.status = :status GROUP BY p.id HAVING COUNT(DISTINCT t.id) = :tagCount")
    List<Post> findByCategoriesAndAllTags(@Param("categories") List<Category> categories, @Param("tags") List<Tag> tags, @Param("status") PostStatus status, @Param("tagCount") long tagCount, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE p.category IN :categories AND t IN :tags AND p.status = :status GROUP BY p.id HAVING COUNT(DISTINCT t.id) = :tagCount ORDER BY p.views DESC")
    List<Post> findByCategoriesAndAllTagsOrderByViewsDesc(@Param("categories") List<Category> categories, @Param("tags") List<Tag> tags, @Param("status") PostStatus status, @Param("tagCount") long tagCount);

    @Query(value = "SELECT p FROM Post p JOIN p.tags t WHERE p.category IN :categories AND t IN :tags AND p.status = :status GROUP BY p.id HAVING COUNT(DISTINCT t.id) = :tagCount ORDER BY p.views DESC")
    List<Post> findByCategoriesAndAllTagsOrderByViewsDesc(@Param("categories") List<Category> categories, @Param("tags") List<Tag> tags, @Param("status") PostStatus status, @Param("tagCount") long tagCount, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE p.category IN :categories AND t IN :tags AND p.status = :status GROUP BY p.id HAVING COUNT(DISTINCT t.id) = :tagCount ORDER BY p.createdAt DESC")
    List<Post> findByCategoriesAndAllTagsOrderByCreatedAtDesc(@Param("categories") List<Category> categories, @Param("tags") List<Tag> tags, @Param("status") PostStatus status, @Param("tagCount") long tagCount);

    @Query(value = "SELECT p FROM Post p JOIN p.tags t WHERE p.category IN :categories AND t IN :tags AND p.status = :status GROUP BY p.id HAVING COUNT(DISTINCT t.id) = :tagCount ORDER BY p.createdAt DESC")
    List<Post> findByCategoriesAndAllTagsOrderByCreatedAtDesc(@Param("categories") List<Category> categories, @Param("tags") List<Tag> tags, @Param("status") PostStatus status, @Param("tagCount") long tagCount, Pageable pageable);

    // 分类/标签组合的热门聚合（按浏览量倒序）
    List<Post> findByCategoryInAndStatusOrderByViewsDesc(List<Category> categories, PostStatus status);
    List<Post> findByCategoryInAndStatusOrderByViewsDesc(List<Category> categories, PostStatus status, Pageable pageable);
    List<Post> findDistinctByTagsInAndStatusOrderByViewsDesc(List<Tag> tags, PostStatus status);
    List<Post> findDistinctByTagsInAndStatusOrderByViewsDesc(List<Tag> tags, PostStatus status, Pageable pageable);
    List<Post> findDistinctByCategoryInAndTagsInAndStatusOrderByViewsDesc(List<Category> categories, List<Tag> tags, PostStatus status);
    List<Post> findDistinctByCategoryInAndTagsInAndStatusOrderByViewsDesc(List<Category> categories, List<Tag> tags, PostStatus status, Pageable pageable);
    // 关键字搜索（忽略大小写）
    List<Post> findByContentContainingIgnoreCaseAndStatus(String keyword, PostStatus status);
    List<Post> findByTitleContainingIgnoreCaseAndStatus(String keyword, PostStatus status);
    List<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseAndStatus(String titleKeyword, String contentKeyword, PostStatus status);

    // 用户近况：最后一条发布时间
    @Query("SELECT MAX(p.createdAt) FROM Post p WHERE p.author.username = :username AND p.status = com.openisle.model.PostStatus.PUBLISHED")
    LocalDateTime findLastPostTime(@Param("username") String username);

    // 用户累计浏览量（仅统计已发布）
    @Query("SELECT SUM(p.views) FROM Post p WHERE p.author.username = :username AND p.status = com.openisle.model.PostStatus.PUBLISHED")
    Long sumViews(@Param("username") String username);

    // 统计用户在指定时间后的发帖数量
    @Query("SELECT COUNT(p) FROM Post p WHERE p.author.username = :username AND p.createdAt >= :start")
    long countByAuthorAfter(@Param("username") String username, @Param("start") java.time.LocalDateTime start);

    // 统计维度：分类/标签/作者
    long countByCategory_Id(Long categoryId);

    @Query("SELECT c.id, COUNT(p) FROM Post p JOIN p.category c where c.id IN :categoryIds GROUP BY c.id")
    List<Object[]> countPostsByCategoryIds(@Param("categoryIds") List<Long> categoryIds);

    long countDistinctByTags_Id(Long tagId);

    long countByAuthor_IdAndRssExcludedFalse(Long userId);

    @Query("SELECT t.id, COUNT(DISTINCT p) FROM Post p JOIN p.tags t WHERE t.id IN :tagIds GROUP BY t.id")
    List<Object[]> countPostsByTagIds(@Param("tagIds") List<Long> tagIds);

    long countByAuthor_Id(Long userId);

    // 时间序列统计：按天聚合发帖数量
    @Query("SELECT FUNCTION('date', p.createdAt) AS d, COUNT(p) AS c FROM Post p " +
            "WHERE p.createdAt >= :start AND p.createdAt < :end GROUP BY d ORDER BY d")
    java.util.List<Object[]> countDailyRange(@Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end);

    // 仅返回未排除 RSS 的最新帖子
    List<Post> findByStatusAndRssExcludedFalseOrderByCreatedAtDesc(PostStatus status, Pageable pageable);
}
