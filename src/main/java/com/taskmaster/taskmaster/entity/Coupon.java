package com.taskmaster.taskmaster.entity;

import com.taskmaster.taskmaster.event.CreatedAtAware;
import com.taskmaster.taskmaster.event.UpdatedAtAware;
import com.taskmaster.taskmaster.listener.CreatedAtListener;
import com.taskmaster.taskmaster.listener.UpdatedAtListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "coupon")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners({
    CreatedAtListener.class,
    UpdatedAtListener.class
})
public class Coupon implements CreatedAtAware, UpdatedAtAware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String code;

    @NotNull
    private BigDecimal discount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

}
