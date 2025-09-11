package com.openisle.model;

/**
 * PostStatus
 *
 * 帖子状态枚举，覆盖发布审核等常见生命周期状态。
 */
public enum PostStatus {
    PUBLISHED, // 已发布
    PENDING,   // 待审核
    REJECTED   // 已拒绝
}
