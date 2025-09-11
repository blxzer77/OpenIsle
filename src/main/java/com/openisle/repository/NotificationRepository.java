package com.openisle.repository;

import com.openisle.model.Notification;
import com.openisle.model.User;
import com.openisle.model.Post;
import com.openisle.model.Comment;
import com.openisle.model.NotificationType;
import com.openisle.model.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

/**
 * NotificationRepository
 *
 * 通知仓库接口，提供针对用户通知的查询、统计与清理能力。
 *
 * 能力概览：
 * - 列表与分页：按用户、按是否已读、按排除的通知类型进行排序分页查询（按创建时间倒序）。
 * - 计数：未读/已读数量统计，支持排除部分通知类型后再统计。
 * - 清理：基于类型 + 触发用户 + 关联帖子/评论/反应类型的定向删除，用于撤销/更新通知。
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    /** 按用户查询通知（倒序） */
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    /** 按用户与已读状态查询通知（倒序） */
    List<Notification> findByUserAndReadOrderByCreatedAtDesc(User user, boolean read);
    /** 按用户分页查询通知（倒序） */
    Page<Notification> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    /** 按用户与已读状态分页查询通知（倒序） */
    Page<Notification> findByUserAndReadOrderByCreatedAtDesc(User user, boolean read, Pageable pageable);
    /**
     * 按用户分页查询通知（倒序），并排除指定类型集合。
     * 可用于屏蔽部分噪声型通知后展示关键通知。
     */
    Page<Notification> findByUserAndTypeNotInOrderByCreatedAtDesc(User user, Collection<NotificationType> types, Pageable pageable);
    /**
     * 按用户与已读状态分页查询通知（倒序），并排除指定类型集合。
     */
    Page<Notification> findByUserAndReadAndTypeNotInOrderByCreatedAtDesc(User user, boolean read, Collection<NotificationType> types, Pageable pageable);
    /** 统计用户的（未）读通知数量 */
    long countByUserAndRead(User user, boolean read);
    /** 统计用户的（未）读通知数量，排除指定类型集合 */
    long countByUserAndReadAndTypeNotIn(User user, boolean read, Collection<NotificationType> types);

    /**
     * 删除给定类型且由某触发用户产生的所有通知。
     * 场景：撤销关注、撤销系统批量通知等。
     */
    void deleteByTypeAndFromUser(NotificationType type, User fromUser);

    /**
     * 查询给定类型且由某触发用户产生的通知。
     */
    List<Notification> findByTypeAndFromUser(NotificationType type, User fromUser);

    /**
     * 删除指定类型 + 触发用户 + 帖子维度的通知。
     * 场景：用户取消了对某帖子的操作（如取消订阅）。
     */
    void deleteByTypeAndFromUserAndPost(NotificationType type, User fromUser, Post post);

    /**
     * 删除指定类型 + 触发用户 + 帖子 + 反应类型 的通知。
     * 场景：撤销针对某帖子的点赞/表情通知。
     */
    void deleteByTypeAndFromUserAndPostAndReactionType(NotificationType type, User fromUser, Post post, ReactionType reactionType);

    /**
     * 删除指定类型 + 触发用户 + 评论 + 反应类型 的通知。
     * 场景：撤销针对某评论的点赞/表情通知。
     */
    void deleteByTypeAndFromUserAndCommentAndReactionType(NotificationType type, User fromUser, Comment comment, ReactionType reactionType);
}
