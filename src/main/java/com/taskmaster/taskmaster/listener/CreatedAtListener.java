package com.taskmaster.taskmaster.listener;

import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.event.CreatedAtAware;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;

public class CreatedAtListener {

    @PrePersist
    public void setCreatedAt(Object entity){
        LocalDateTime localDateTime = TimeUtil.getFormattedLocalDateTimeNow();
        if (entity instanceof CreatedAtAware){
            CreatedAtAware createdAtAware = (CreatedAtAware) entity;
            createdAtAware.setCreatedAt(localDateTime);
        }
    }

}
