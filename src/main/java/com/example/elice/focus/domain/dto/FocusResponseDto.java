package com.example.elice.focus.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FocusResponseDto {

    private Long id;
    private String tasks;
    private String targetTime;
    private String recordTime;
    private String places;
    private String createdDate;
}