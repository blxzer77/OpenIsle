package com.openisle.model;

/**
 * NotificationType
 *
 * 站内通知类型枚举。每个枚举值代表一类事件，便于消息聚合、筛选与订阅控制。
 */
public enum NotificationType {
    POST_VIEWED,              // 有人查看了你的帖子
    COMMENT_REPLY,            // 有人回复了你的帖子或评论
    REACTION,                 // 有人对你的帖子或评论进行了表情/反应
    POST_REVIEW_REQUEST,      // 有新帖子等待审核
    POST_REVIEWED,            // 你的帖子审核通过或被拒绝
    POST_DELETED,             // 管理员删除了你的帖子
    POST_UPDATED,             // 你订阅的帖子有新评论或更新
    POST_SUBSCRIBED,          // 有人订阅了你的帖子
    POST_UNSUBSCRIBED,        // 有人取消订阅你的帖子
    FOLLOWED_POST,            // 你关注的人发布了新帖子
    USER_FOLLOWED,            // 有人关注了你
    USER_UNFOLLOWED,          // 有人取消关注你
    USER_ACTIVITY,            // 你关注的用户发布了动态（贴子/评论）
    REGISTER_REQUEST,         // 有用户提交注册审核请求
    ACTIVITY_REDEEM,          // 有用户领取了活动奖励
    POINT_REDEEM,             // 有用户兑换了积分商品
    LOTTERY_WIN,              // 你赢得了一条抽奖帖子
    LOTTERY_DRAW,             // 你的抽奖帖子已开奖
    POLL_VOTE,                // 有人参与了你的投票
    POLL_RESULT_OWNER,        // 你的投票已结束（所有者视角）
    POLL_RESULT_PARTICIPANT,  // 你参与的投票已结束（参与者视角）
    POST_FEATURED,            // 你的帖子被精选/推荐
    MENTION                   // 你在帖子或评论中被 @ 提及
}
