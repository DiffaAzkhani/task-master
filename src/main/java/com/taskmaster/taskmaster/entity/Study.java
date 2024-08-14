package com.taskmaster.taskmaster.entity;

import com.taskmaster.taskmaster.enums.StudyCategory;
import com.taskmaster.taskmaster.enums.StudyLevel;
import com.taskmaster.taskmaster.enums.StudyType;
import com.taskmaster.taskmaster.event.CreatedAtAware;
import com.taskmaster.taskmaster.event.UpdatedAtAware;
import com.taskmaster.taskmaster.listener.CreatedAtListener;
import com.taskmaster.taskmaster.listener.UpdatedAtListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "study")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners({
    CreatedAtListener.class,
    UpdatedAtListener.class
})
@ToString(exclude = {"orderItems","coupons", "users"})
@EqualsAndHashCode(exclude = {"orderItems","coupons", "users"})
public class Study implements CreatedAtAware, UpdatedAtAware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 13)
    private String code;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer discount;

    private String description;

    @Column(unique = true, nullable = false, length = 200)
    private String link;

    @Enumerated(EnumType.STRING)
    private StudyCategory category;

    @Enumerated(EnumType.STRING)
    private StudyType type;

    @Enumerated(EnumType.STRING)
    private StudyLevel level;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "studies")
    private Set<User> users = new HashSet<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "study_coupon",
        joinColumns = @JoinColumn(name = "study_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    )
    private Set<Coupon> coupons;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAnswer> userAnswers = new ArrayList<>();

}
