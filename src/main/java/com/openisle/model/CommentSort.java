package com.openisle.model;

/**
 * CommentSort
 *
 * 评论排序方式枚举，用于控制评论列表的展示顺序。
 */
public enum CommentSort {
    NEWEST,           // 最新发布优先
    OLDEST,           // 最早发布优先
    MOST_INTERACTIONS // 互动（回复/反应）最多优先
}
