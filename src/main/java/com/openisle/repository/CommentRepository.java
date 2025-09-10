package com.openisle.repository;

import com.openisle.model.Comment;
import com.openisle.model.Post;
import com.openisle.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论仓库接口。
 *
 * 提供针对评论的常用查询、统计能力，包括：
 * - 帖子根评论列表、子评论列表；
 * - 作者评论列表（支持分页、按时间倒序）；
 * - 内容关键字搜索；
 * - 参与互动用户去重、最新评论时间、按时间段的日聚合统计等。
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 查询某帖子的根评论（parent 为空），按创建时间正序
    List<Comment> findByPostAndParentIsNullOrderByCreatedAtAsc(Post post);
    // 查询某条评论的子评论，按创建时间正序
    List<Comment> findByParentOrderByCreatedAtAsc(Comment parent);
    // 查询某作者的评论，按创建时间倒序（支持分页）
    List<Comment> findByAuthorOrderByCreatedAtDesc(User author, Pageable pageable);
    // 评论内容模糊匹配（忽略大小写）
    List<Comment> findByContentContainingIgnoreCase(String keyword);

    // 去重查询：参与指定帖子的评论作者集合
    @Query("SELECT DISTINCT c.author FROM Comment c WHERE c.post = :post")
    List<User> findDistinctAuthorsByPost(@Param("post") Post post);

    // 指定帖子下的最新评论时间
    @Query("SELECT MAX(c.createdAt) FROM Comment c WHERE c.post = :post")
    LocalDateTime findLastCommentTime(@Param("post") Post post);

    // 统计某用户在指定时间点之后的评论数量（按用户名）
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.author.username = :username AND c.createdAt >= :start")
    long countByAuthorAfter(@Param("username") String username,
                            @Param("start") java.time.LocalDateTime start);

    // 查询某用户（按 userId）最近一条评论时间
    @Query("SELECT MAX(c.createdAt) FROM Comment c WHERE c.author.id = :userId")
    LocalDateTime findLastCommentTimeOfUserByUserId(@Param("userId") Long userId);

    // 统计某帖子下的评论总数（按 postId）
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    long countByPostId(@Param("postId") Long postId);

    // 统计某用户的评论总数（按作者 id）
    long countByAuthor_Id(Long userId);

    // 时间序列统计：按天聚合评论数量（闭区间左开右闭）
    @Query("SELECT FUNCTION('date', c.createdAt) AS d, COUNT(c) AS c FROM Comment c " +
            "WHERE c.createdAt >= :start AND c.createdAt < :end GROUP BY d ORDER BY d")
    List<Object[]> countDailyRange(@Param("start") java.time.LocalDateTime start,
                                   @Param("end") java.time.LocalDateTime end);
}
