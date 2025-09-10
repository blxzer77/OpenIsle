package com.openisle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主键，自增 ID

    @Column(nullable = false, unique = true)
    private String name; // 标签名称，唯一

    @Column
    private String icon; // 图标（可选）

    @Column
    private String smallIcon; // 小图标（可选）

    @Column(name = "description", nullable = false)
    private String description; // 标签描述

    @Column(nullable = false)
    private boolean approved = true; // 是否已审核通过

    @CreationTimestamp
    @Column(nullable = false, updatable = false,
            columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)")
    private LocalDateTime createdAt; // 创建时间

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id")
    private User creator; // 创建者

}
