package com.openisle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

/**
 * User
 *
 * 平台用户实体，记录账号、权限、资料与通知偏好等信息。
 *
 * 设计要点：
 * - {@code username}/{@code email} 唯一约束；密码存储为哈希。
 * - {@code verified}/{@code approved} 用于账号验证与人工审核。
 * - {@code experience}/{@code point} 表示成长体系与积分体系。
 * - {@code role} 角色权限；{@code displayMedal} 主页展示勋章。
 * - {@code disabledNotificationTypes}/{@code disabledEmailNotificationTypes}
 *   控制站内/邮件通知的关闭项，默认关闭部分被动型通知。
 * - {@code createdAt} 由数据库自动生成，精度微秒。
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主键，自增 ID

    @Column(nullable = false, unique = true)
    private String username; // 用户名，唯一

    @Column(nullable = false, unique = true)
    private String email; // 邮箱，唯一

    @Column(nullable = false)
    private String password; // 哈希后的密码

    @Column(nullable = false)
    private boolean verified = false; // 是否已验证邮箱/身份

    private String verificationCode; // 验证码（注册验证/安全校验）

    private String passwordResetCode; // 重置密码验证码

    private String avatar; // 头像 URL

    @Column(nullable = false)
    private int experience = 0; // 经验值

    @Column(nullable = false)
    private int point = 0; // 积分

    @Column(length = 1000)
    private String introduction; // 个人简介

    @Column(length = 1000)
    private String registerReason; // 注册理由（供审核）

    @Column(nullable = false)
    private boolean approved = true; // 是否通过人工审核

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER; // 角色（管理员/普通用户）

    @Enumerated(EnumType.STRING)
    private MedalType displayMedal; // 展示在个人主页的勋章类型

    @ElementCollection(targetClass = NotificationType.class)
    @CollectionTable(name = "user_disabled_notification_types", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private Set<NotificationType> disabledNotificationTypes = EnumSet.of(
            NotificationType.POST_VIEWED,
            NotificationType.USER_ACTIVITY
    );

    @ElementCollection(targetClass = NotificationType.class)
    @CollectionTable(name = "user_disabled_email_notification_types", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private Set<NotificationType> disabledEmailNotificationTypes = EnumSet.noneOf(NotificationType.class); // 关闭的邮件通知类型集合

    @CreationTimestamp
    @Column(nullable = false, updatable = false,
            columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)")
    private LocalDateTime createdAt; // 注册时间
}
