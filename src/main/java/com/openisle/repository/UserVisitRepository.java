package com.openisle.repository;

import com.openisle.model.User;
import com.openisle.model.UserVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

/**
 * 用户访问记录数据访问层接口
 * 提供对 UserVisit 实体的数据库操作功能
 * 
 * @author OpenIsle Team
 */
public interface UserVisitRepository extends JpaRepository<UserVisit, Long> {
    
    /**
     * 根据用户和访问日期查找访问记录
     * 由于 UserVisit 实体有唯一约束 (user_id, visit_date)，
     * 所以同一个用户在同一天最多只有一条记录
     * 
     * @param user 用户对象
     * @param date 访问日期
     * @return 如果找到则返回 UserVisit 对象，否则返回 Optional.empty()
     */
    Optional<UserVisit> findByUserAndVisitDate(User user, LocalDate date);
    
    /**
     * 统计指定用户的总访问次数
     * 计算该用户在系统中记录的所有访问天数
     * 
     * @param user 用户对象
     * @return 该用户的总访问天数
     */
    long countByUser(User user);
    
    /**
     * 统计指定日期的总访问用户数
     * 计算在某个特定日期有多少不同的用户访问了系统
     * 
     * @param date 访问日期
     * @return 该日期的访问用户总数
     */
    long countByVisitDate(LocalDate date);

    /**
     * 统计指定日期范围内的每日访问统计
     * 返回一个包含日期和对应访问用户数的列表
     * 用于生成访问趋势图表或报表
     * 
     * @param start 开始日期（包含）
     * @param end 结束日期（包含）
     * @return 包含 [日期, 访问用户数] 的 Object 数组列表
     */
    @Query("SELECT uv.visitDate AS d, COUNT(uv) AS c FROM UserVisit uv WHERE uv.visitDate BETWEEN :start AND :end GROUP BY uv.visitDate ORDER BY uv.visitDate")
    List<Object[]> countRange(@Param("start")  LocalDate start, @Param("end") LocalDate end);
}
