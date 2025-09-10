package com.openisle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;
import java.util.HashSet;


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
