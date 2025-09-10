package com.openisle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import java.util.HashSet;
import java.util.Set;

import com.openisle.model.Tag;

/**
 * 帖子实体（主贴/主题）。
 *
 * 设计说明：
 * - 使用 JPA 注解将对象映射到数据表 `posts`。
 * - `@Inheritance(strategy = JOINED)` 预留了帖子类型的继承扩展能力（如投票贴、抽奖贴等），
 *   在子类表中仅保存子类特有字段，主表保存共有字段。
 * - 创建时间 `createdAt` 由数据库自动填充，精度为微秒，便于基于时间排序与统计。
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "posts")
@Inheritance(strategy = InheritanceType.JOINED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主键，自增 ID

    @Column(nullable = false)
    private String title; // 标题，非空

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content; // 正文内容，使用 LONGTEXT 以支持较长文本

    @CreationTimestamp
    @Column(nullable = false, updatable = false,
            columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)")
    private LocalDateTime createdAt; // 创建时间，由数据库自动生成

    @ManyToOne(optional = false ,fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author; // 作者（用户），必填，懒加载

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category; // 所属分类，必填，懒加载

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>(); // 标签集合，多对多，懒加载

    @Column(nullable = false)
    private long views = 0; // 浏览次数，默认 0

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status = PostStatus.PUBLISHED; // 帖子状态（已发布/待审核/已拒绝）

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostType type = PostType.NORMAL; // 帖子类型（普通/抽奖/投票）

    @Column(nullable = false)
    private boolean closed =false; // 是否关闭评论/互动

    @Column
    private LocalDateTime pinnedAt; // 置顶时间（为空表示未置顶）

    @Column(nullable = true)
    private Boolean rssExcluded = true; // 是否排除在 RSS 源之外
}
