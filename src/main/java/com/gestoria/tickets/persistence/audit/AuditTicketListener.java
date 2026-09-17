package com.gestoria.tickets.persistence.audit;

import com.gestoria.tickets.persistence.entity.TicketEntity;
import jakarta.persistence.*;
import org.springframework.util.SerializationUtils;

public class AuditTicketListener {
    private TicketEntity currentValue;

    @PostLoad
    public void postLoad(TicketEntity entity){
        System.out.println("POST LOAD");
        this.currentValue = SerializationUtils.clone(entity);
    }

    @PostPersist
    @PostUpdate
    public void onPostPersist(TicketEntity entity){
        System.out.println("POST PERSISTS OR UPDATE");
        System.out.println("OLD VALUE: " + this.currentValue);
        System.out.println("NEW VALUE: " + entity.toString());
    }

    @PreRemove
    public void onPreDelete(TicketEntity entity){
        System.out.println("DELETING VALUE: " + entity.toString());
    }
}
