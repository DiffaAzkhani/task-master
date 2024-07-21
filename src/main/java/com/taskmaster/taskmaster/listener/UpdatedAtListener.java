package com.taskmaster.taskmaster.listener;

import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.event.UpdatedAtAware;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class UpdatedAtListener {

    @PrePersist
    @PreUpdate
    public void setUpdatedAt(Object entity){
        LocalDateTime localDateTime = TimeUtil.getFormattedLocalDateTimeNow();
        if (entity instanceof UpdatedAtAware){
            UpdatedAtAware updatedAtAware = (UpdatedAtAware) entity;
            updatedAtAware.setUpdatedAt(localDateTime);
        }
    }

}
