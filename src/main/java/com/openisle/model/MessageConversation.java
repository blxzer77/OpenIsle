package com.openisle.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.List;


/**
 * MessageConversation
 *
 * 消息会话实体，既可表示私聊会话，也可表示公共频道（channel）。
 *
 * 字段说明：
 * - {@code channel}: 是否为频道；频道具备名称/描述/头像等元信息。
 * - {@code name}/{@code description}/{@code avatar}: 频道元信息（仅频道使用）。
 * - {@code lastMessage}: 会话里最近一条消息（便于会话列表展示）。
 * - {@code participants}: 参与者集合（含最后已读时间等）。
 * - {@code messages}: 消息集合；使用 @JsonBackReference 避免序列化循环。
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "message_conversations")
public class MessageConversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 是否为公共频道（否则为普通会话），默认 false */
    @Column(nullable = false)
    private boolean channel = false;

    /** 频道名称（频道时可选） */
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description; // 频道描述（频道时可选）

    private String avatar; // 频道头像（频道时可选）

    /** 创建时间（自动生成，不可更新） */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 最近一条消息（用于会话列表展示） */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_message_id")
    private Message lastMessage;

    /** 参与者集合（会话端维护），级联保存与孤儿删除 */
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MessageParticipant> participants = new HashSet<>();

    /** 消息集合，反向引用避免序列化环 */
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<Message> messages = new HashSet<>();
}
