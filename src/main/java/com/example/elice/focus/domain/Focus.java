package com.example.elice.focus.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Focus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private String tasks;

    private String targetTime;

    private String recordTime;

    private String places;

    private LocalDateTime createdDate;

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
    }
}
