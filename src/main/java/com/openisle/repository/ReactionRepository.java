package com.openisle.repository;

import com.openisle.model.Message;

import com.openisle.model.Comment;
import com.openisle.model.Post;
import com.openisle.model.User;
import com.openisle.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * ReactionRepository
 *
 * 负责对 {@link Reaction} 实体的持久化访问与统计查询。
 *
 * 说明：
 * - 本仓库同时支持对帖子(Post)与评论(Comment)上的点赞/表态数据进行查询与统计。
 * - 约定：当前业务仅统计/筛选 {@code ReactionType.LIKE}（点赞）时使用到的部分查询，
 *   若未来扩展其他类型（例如 DISLIKE），需相应调整 JPQL 条件。
 */
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    /**
     * 根据用户、帖子及反应类型查询唯一反应。
     *
     * @param user  触发反应的用户
     * @param post  所属帖子
     * @param type  反应类型（通常为 LIKE）
     * @return 匹配到的反应（若存在）
     */
    Optional<Reaction> findByUserAndPostAndType(User user, Post post, com.openisle.model.ReactionType type);

    /**
     * 根据用户、评论及反应类型查询唯一反应。
     *
     * @param user     触发反应的用户
     * @param comment  所属评论
     * @param type     反应类型（通常为 LIKE）
     * @return 匹配到的反应（若存在）
     */
    Optional<Reaction> findByUserAndCommentAndType(User user, Comment comment, com.openisle.model.ReactionType type);

    /**
     * 查询某帖子下的所有反应。
     *
     * 提示：如数据量较大可结合分页或仅统计需要的聚合结果。
     *
     * @param post 帖子
     * @return 反应列表
     */
    List<Reaction> findByPost(Post post);

    /**
     * 查询某评论下的所有反应。
     *
     * @param comment 评论
     * @return 反应列表
     */
    List<Reaction> findByComment(Comment comment);

    /**
     * 查询指定作者(@username)的帖子中，按收到的点赞数倒序的帖子ID列表。
     * 仅统计点赞 {@code ReactionType.LIKE}。
     *
     * @param username 帖子作者用户名
     * @param pageable 限制返回数量及排序（JPQL已含排序，通常只需限制 size）
     * @return 帖子ID列表（按收到的点赞数降序）
     */
    @Query("SELECT r.post.id FROM Reaction r WHERE r.post IS NOT NULL AND r.post.author.username = :username AND r.type = com.openisle.model.ReactionType.LIKE GROUP BY r.post.id ORDER BY COUNT(r.id) DESC")
    List<Long> findTopPostIds(@Param("username")  String username, Pageable pageable);

    /**
     * 查询指定作者(@username)的评论中，按收到的点赞数倒序的评论ID列表。
     * 仅统计点赞 {@code ReactionType.LIKE}。
     *
     * @param username 评论作者用户名
     * @param pageable 限制返回数量及排序（JPQL已含排序，通常只需限制 size）
     * @return 评论ID列表（按收到的点赞数降序）
     */
    @Query("SELECT r.comment.id FROM Reaction r WHERE r.comment IS NOT NULL AND r.comment.author.username = :username AND r.type = com.openisle.model.ReactionType.LIKE GROUP BY r.comment.id ORDER BY COUNT(r.id) DESC")
    List<Long> findTopCommentIds(@Param("username")  String username, Pageable pageable);

    /**
     * 统计指定用户(@username)向外发送的点赞总数。
     *
     * @param username 用户名（点赞发起者）
     * @return 发送的点赞数
     */
    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.user.username = :username AND r.type = com.openisle.model.ReactionType.LIKE")
    long countLikesSent(@Param("username") String username);

    /**
     * 统计指定用户(@username)在给定时间点之后创建的所有反应数量。
     * 注意：方法名与语义更贴近“用户在时间段内的反应总数”，与“Comment”无关；
     * 若仅统计评论相关，需在 JPQL 中追加过滤条件。
     *
     * @param username 用户名（反应发起者）
     * @param start    起始时间（含）
     * @return 起始时间后的反应数量
     */
    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.user.username = :username AND r.createdAt >= :start")
    long countByUserAndComment(@Param("username") String username,
                               @Param("start") LocalDateTime start);

    /**
     * 统计指定用户(@username)在自己帖子或评论上收到的点赞总数（去重计数 Reaction.id）。
     *
     * @param username 内容作者用户名
     * @return 收到的点赞数
     */
    @Query("""
            SELECT COUNT(DISTINCT r.id)
            FROM Reaction r
            LEFT JOIN r.post    p
            LEFT JOIN p.author  pa
            LEFT JOIN r.comment c
            LEFT JOIN c.author  ca
            WHERE r.type = com.openisle.model.ReactionType.LIKE
              AND (
                   (r.post    IS NOT NULL AND pa.username = :username)
                OR (r.comment IS NOT NULL AND ca.username = :username)
              )
            """)
    long countLikesReceived(@Param("username") String username);

    /**
     * 统计指定用户(@username)在自己帖子或评论上收到的所有反应数量（不限类型）。
     *
     * @param username 内容作者用户名
     * @return 收到的反应数
     */
    @Query("""
            SELECT COUNT(r) FROM Reaction r
            LEFT JOIN r.post p
            LEFT JOIN r.comment c
            WHERE (p IS NOT NULL AND p.author.username = :username) OR
                  (c IS NOT NULL AND c.author.username = :username)
            """)
    long countReceived(@Param("username") String username);

    // 待实现
    // 如未来支持消息(Message)上的反应，可开启如下方法并引入实体：
    // 已实现！
    /**
     * 根据用户、消息与反应类型查询唯一反应（Message 维度）。
     */
    Optional<Reaction> findByUserAndMessageAndType(User user, Message message, com.openisle.model.ReactionType type);
    /**
     * 查询某条消息下的所有反应（Message 维度）。
     */
    List<Reaction> findByMessage(Message message);
}


