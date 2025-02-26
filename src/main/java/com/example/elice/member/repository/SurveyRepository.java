package com.example.elice.member.repository;

import com.example.elice.member.domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository  extends JpaRepository<Survey, Long> {
}
