package com.openisle.repository;

import com.openisle.model.MessageConversation;
import com.openisle.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MessageConversationRepository
 *
 * 会话仓库接口：
 * - 提供按会话ID、参与者用户、用户ID等维度的查询能力；
 * - 通过必要的 JOIN FETCH 降低 N+1 查询风险；
 * - 支持频道会话统计与筛选。
 */
@Repository
public interface MessageConversationRepository extends JpaRepository<MessageConversation, Long> {

    /**
     * 根据会话ID查询，同时联带加载参与者及其用户信息，避免后续懒加载产生 N+1 查询。
     * 按需用于会话详情页或需要展示参与者列表的场景。
     *
     * @param id 会话ID
     * @return 携带参与者与用户的会话
     */
    @Query("SELECT c FROM MessageConversation c LEFT JOIN FETCH c.participants p LEFT JOIN FETCH p.user WHERE c.id = :id")
    java.util.Optional<MessageConversation> findByIdWithParticipantsAndUsers(@Param("id") Long id);

    /**
     * 查询两个指定用户之间的双人私聊会话（非频道），按创建时间倒序返回。
     *
     * @param user1 用户1
     * @param user2 用户2
     * @return 两人之间的会话列表（可能存在多个历史会话）
     */
    @Query("SELECT c FROM MessageConversation c " +
           "WHERE c.channel = false AND size(c.participants) = 2 " +
           "AND EXISTS (SELECT 1 FROM c.participants p1 WHERE p1.user = :user1) " +
           "AND EXISTS (SELECT 1 FROM c.participants p2 WHERE p2.user = :user2) " +
           "ORDER BY c.createdAt DESC")
    List<MessageConversation> findConversationsByUsers(@Param("user1") User user1, @Param("user2") User user2);
    
    /**
     * 查询某用户参与的会话列表，并联带加载最后一条消息及其发送者、参与者与用户，
     * 结果按“最后消息时间（若无则按会话创建时间）”倒序排列。
     *
     * @param userId 用户ID
     * @return 该用户参与的会话列表（含必要联表数据）
     */
    @Query("SELECT DISTINCT c FROM MessageConversation c " +
           "JOIN c.participants p " +
           "LEFT JOIN FETCH c.lastMessage lm " +
           "LEFT JOIN FETCH lm.sender " +
           "LEFT JOIN FETCH c.participants cp " +
           "LEFT JOIN FETCH cp.user " +
           "WHERE p.user.id = :userId " +
           "ORDER BY COALESCE(lm.createdAt, c.createdAt) DESC")
    List<MessageConversation> findConversationsByUserIdOrderByLastMessageDesc(@Param("userId") Long userId);

    /** 查询所有频道型会话 */
    List<MessageConversation> findByChannelTrue();

    /** 统计频道型会话数量 */
    long countByChannelTrue();
}
