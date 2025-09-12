package com.openisle.repository;

import com.openisle.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;

/**
 * MessageRepository
 *
 * 消息仓库接口：提供按会话维度的消息查询、分页与未读统计等能力。
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    /** 按会话查询消息并按创建时间正序返回（用于会话完整回放） */
    List<Message> findByConversationIdOrderByCreatedAtAsc(Long conversationId);

    /** 按会话分页查询消息 */
    Page<Message> findByConversationId(Long conversationId, Pageable pageable);

    /** 统计某会话在指定时间之后产生的消息数量 */
    long countByConversationIdAndCreatedAtAfter(Long conversationId, LocalDateTime createdAt);

    // 只计算不是指定用户发送的消息，即别人发给当前用户的信息
    /** 统计指定时间后、排除某发送者ID的消息数量（用于未读对端消息数） */
    long countByConversationIdAndCreatedAtAfterAndSenderIdNot(Long conversationId, LocalDateTime createdAt, Long senderId);
}
