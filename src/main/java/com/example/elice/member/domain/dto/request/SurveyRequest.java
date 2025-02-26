package com.example.elice.member.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyRequest {

    @NotBlank(message = "나이를 입력해 주세요!")
    private Integer age;

    @NotBlank(message = "직업을 입력해 주세요!")
    private String job;

    @NotBlank(message = "사용 목적을 입력해 주세요!")
    private String useObject;
}
