package com.openisle.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Notification
 *
 * 系统通知实体，表示用户在帖子/评论/互动等场景下收到的通知。
 * 支持多种通知类型（见 {@link NotificationType}），可关联帖子、评论及触发方用户。
 *
 * 设计要点：
 * - {@code type}: 通知类别（如点赞、评论、关注等）。
 * - {@code user}: 通知接收者（被通知的用户）。
 * - {@code post}/{@code comment}: 关联内容，二者可空（根据通知类型按需关联）。
 * - {@code fromUser}: 触发该通知的用户（如点赞者、评论者）。
 * - {@code reactionType}: 若由反应触发，记录反应类型（通常为 LIKE）。
 * - {@code content}: 附加信息，展示在通知文案中。
 * - {@code approved}: 可选审批标记，表示该通知是否通过某种审核流程。
 * - {@code read}: 是否已读，默认为 {@code false}。
 * - {@code createdAt}: 创建时间，自动生成。
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name ="notifications")
public class Notification {
    /** 主键ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 通知类型（必须） */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private NotificationType type;

    /** 通知接收者（必须） */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name ="user_id")
    private User user;

    /** 关联的帖子（可选，依类型而定） */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    /** 关联的评论（可选，依类型而定） */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    /** 触发通知的用户（如点赞者/评论者，通常可选） */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    /** 若因反应产生的通知，记录反应类型（如 LIKE） */
    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type")
    private ReactionType reactionType;

    /** 可选的通知内容文案（最长 1000 字符） */
    @Column(length = 1000)
    private String content;

    /** 可选审批标记：null=未知/未设置，true=通过，false=拒绝 */
    @Column
    private Boolean approved;

    /** 是否已读（必须），默认 false */
    @Column(name ="is_read", nullable = false)
    private boolean read =false;

    /** 创建时间（自动生成，不可更新） */
    @CreationTimestamp
    @Column(nullable = false, updatable = false,
            columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)")
    private LocalDateTime createdAt;
}
