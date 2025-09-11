package com.openisle.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

/**
 * 反应/表情实体。
 * 
 * 设计说明：
 * - 支持用户对帖子、评论进行表情反应（如点赞、踩、推荐等）。
 * - 通过联合唯一约束确保同一用户对同一内容只能有一种类型的反应。
 * - 使用懒加载优化性能，减少不必要的关联查询。
 * 
 * 注意：Message 功能未实现，Message 类未创建。
 * 如需调用相关消息功能，请先补齐对应实体与业务逻辑，或暂时避免依赖。
 *
 * 已创建 Message 类并实现功能
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "reactions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "post_id", "type"}),
                @UniqueConstraint(columnNames = {"user_id", "comment_id", "type"})
//                ,
//                @UniqueConstraint(columnNames = {"user_id", "message_id", "type"})
        })
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主键，自增 ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionType type; // 反应类型（如点赞、踩、推荐等）

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user; // 发起反应的用户，必填，懒加载

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // 被反应的帖子（可为空）

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment; // 被反应的评论（可为空）

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    /** 针对站内消息(Message)的反应关联（可为空）。*/
    private Message message; // 被反应的消息（暂未实现）

    @CreationTimestamp
    @Column(nullable = false, updatable = false,
            columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)")
    private java.time.LocalDateTime createdAt; // 反应时间，由数据库自动生成
}
