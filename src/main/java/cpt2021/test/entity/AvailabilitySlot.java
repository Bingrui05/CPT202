package cpt2021.test.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*; // 注意：Spring Boot 3 使用的是 jakarta 包
import java.time.LocalDateTime;

/**
 * 可约时间段实体类
 * 已完美适配 cpt2021.test 团队规范
 */
@Entity
@Table(name = "availability_slots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilitySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long specialistId; // 关联的专家ID

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String status; // AVAILABLE, BOOKED, CANCELLED
}