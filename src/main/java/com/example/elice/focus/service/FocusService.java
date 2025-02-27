package com.example.elice.focus.service;

import com.example.elice.focus.domain.Focus;
import com.example.elice.focus.repository.FocusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FocusService {

    private final FocusRepository focusRepository;

    public FocusService(FocusRepository focusRepository) {
        this.focusRepository = focusRepository;
    }

    // 새로운 FocusTask 생성 (사용자가 목표 시간을 설정)
    @Transactional
    public Focus createFocusTask(Focus focus) {
        return focusRepository.save(focus);
    }

    // 모든 FocusTask 조회
    public List<Focus> getAllFocusTasks() {
        return focusRepository.findAll();
    }

    // ID로 FocusTask 조회
    public Optional<Focus> getFocusTaskById(Long id) {
        return focusRepository.findById(id);
    }
}