package com.openisle.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * MessageParticipant
 *
 * 会话参与者实体，表示某用户加入了某个会话，并记录其最后阅读时间。
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "message_participants")
public class MessageParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 所属会话（必须），懒加载 */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    @JoinColumn(name = "conversation_id")
    private MessageConversation conversation;

    /** 参与者用户（必须），懒加载 */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    /** 最后读取会话的时间（可为空） */
    @Column
    private LocalDateTime lastReadAt;
}
