package com.openisle.repository;

import com.openisle.model.MessageParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MessageParticipantRepository
 *
 * 会话参与者仓库接口：用于按会话/用户维度查询参与者记录。
 */
@Repository
public interface MessageParticipantRepository extends JpaRepository<MessageParticipant, Integer> {
    /** 根据会话ID与用户ID查询参与者记录（用于权限校验/读取状态等） */
    Optional<MessageParticipant> findByConversationIdAndUserId(Long conversationId, Long userId);
    /** 查询某用户参与的所有会话参与记录 */
    List<MessageParticipant> findByUserId(Long userId);
}
