package com.taskmaster.taskmaster.event;

import java.time.LocalDateTime;

public interface CreatedAtAware {
    void setCreatedAt(LocalDateTime createdAt);
}
