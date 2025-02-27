package com.example.elice.focus.domain.dto;

import lombok.Data;

@Data
public class FocusCreateDto {
    private String tasks;
    private String targetTime;
    private String recordTime;
    private String places;
}