package com.openisle.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.openisle.model.User;
import java.time.LocalDateTime;
import java.util.Optional;


/**
 * UserRepository
 *
 * 用户仓库接口，提供用户维度的查询与统计能力。
 *
 * 能力概览：
 * - 基础查找：按用户名、邮箱查询；用户名模糊搜索；按角色筛选。
 * - 统计指标：经验值阈值统计、注册时间阈值统计；注册量日聚合。
 */
public interface UserRepository extends JpaRepository<User, Long> {
    // 根据用户名/邮箱查询
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    // 用户名模糊搜索（忽略大小写）
    java.util.List<User> findByUsernameContainingIgnoreCase(String keyword);
    // 按角色筛选
    java.util.List<User> findByRole(com.openisle.model.Role role);
    // 统计经验值大于等于指定值的用户数量
    long countByExperienceGreaterThanEqual(int experience);
    // 统计在指定时间前注册的用户数量
    long countByCreatedAtBefore(LocalDateTime createdAt);

    // 时间序列统计：按天聚合注册数量
    @Query("SELECT FUNCTION('date', u.createdAt) AS d, COUNT(u) AS c FROM User u " +
            "WHERE u.createdAt >= :start AND u.createdAt < :end GROUP BY d ORDER BY d")
    java.util.List<Object[]> countDailyRange(@Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end);
}
