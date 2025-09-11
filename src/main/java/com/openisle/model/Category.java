package com.openisle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Category
 *
 * 帖子分类实体，用于组织与聚合内容，便于导航与筛选。
 *
 * 字段说明：
 * - {@code name}: 分类名称（唯一）。
 * - {@code icon}/{@code smallIcon}: 图标与小图标（小图标可选）。
 * - {@code description}: 分类描述文案。
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主键，自增 ID

    @Column(nullable = false, unique = true)
    private String name; // 分类名称，唯一

    @Column(nullable = false)
    private String icon; // 图标，必填

    @Column
    private String smallIcon; // 小图标（可选）

    @Column(name = "description", nullable = false)
    private String description; // 分类描述
}
