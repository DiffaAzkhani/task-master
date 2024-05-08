package com.taskmaster.taskmaster.event;

import java.time.LocalDateTime;

public interface UpdatedAtAware {
    void setUpdatedAt(LocalDateTime updatedAt);
}
