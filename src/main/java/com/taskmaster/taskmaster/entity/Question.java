package com.taskmaster.taskmaster.entity;

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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "question")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners({
    CreatedAtListener.class,
    UpdatedAtListener.class
})
@ToString(exclude = {"answers","userAnswers"})
@EqualsAndHashCode(exclude = {"answers","userAnswers"})
public class Question implements CreatedAtAware, UpdatedAtAware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 200)
    private String questionText;

    @Column(length = 200)
    private String imageUrl;

    @Column(nullable = false)
    private String explanation;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserAnswer> userAnswers = new ArrayList<>();

}
