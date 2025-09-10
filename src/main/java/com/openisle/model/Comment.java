package com.openisle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 评论实体。
 *
 * 设计说明：
 * - 逻辑删除：通过 `@SQLDelete` 与 `@Where`，删除操作改为设置 `deleted_at`，默认查询仅返回未删除记录。
 * - 内容存储：正文使用 TEXT，满足大多数评论长度需求。
 * - 关联关系：
 *   - `author` 指向用户；
 *   - `post` 指向所属帖子；
 *   - `parent` 支持楼中楼/子评论结构。
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comments")
@SQLDelete(sql = "UPDATE comments SET deleted_at = CURRENT_TIMESTAMP(6) WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主键，自增 ID

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 评论内容

    @CreationTimestamp
    @Column(nullable = false, updatable = false,
            columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)")
    private LocalDateTime createdAt; // 创建时间

    @ManyToOne(fetch = FetchType.LAZY,  optional = false)
    @JoinColumn(name = "author_id")
    private User author; // 评论作者

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    private Post post; // 所属帖子

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent; // 父评论（可为空）

    @Column
    private LocalDateTime pinnedAt; // 置顶时间

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // 逻辑删除时间
}
