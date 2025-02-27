package com.example.elice.focus.controller;

import com.example.elice.focus.domain.Focus;
import com.example.elice.focus.domain.dto.FocusCreateDto;
import com.example.elice.focus.domain.dto.FocusResponseDto;
import com.example.elice.focus.service.FocusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Focus API", description = "집중 API")
@RestController
@RequestMapping("/api/focus")
public class FocusController {

    private final FocusService focusService;

    public FocusController(FocusService focusService) {
        this.focusService = focusService;
    }

    // 새로운 Focus 생성
    @Operation(summary = "Focus 생성", description = "새로운 Focus를 생성합니다.")
    @PostMapping("/tasks")
    public ResponseEntity<FocusResponseDto> createTask(@RequestBody FocusCreateDto focusCreateDto) {
        Focus focus = convertToEntity(focusCreateDto);
        Focus createdFocus = focusService.createFocusTask(focus);
        return new ResponseEntity<>(convertToResponseDto(createdFocus), HttpStatus.CREATED);
    }

    // 모든 Focus 조회
    @Operation(summary = "Focus 조회", description = "모든 Focus를 조회합니다.")
    @GetMapping("/tasks")
    public ResponseEntity<List<FocusResponseDto>> getAllTasks() {
        List<Focus> tasks = focusService.getAllFocusTasks();
        List<FocusResponseDto> responseDtos = tasks.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    // 특정 Focus 조회
    @Operation(summary = "특정 Focus 조회", description = "특정 Focus를 조회합니다.")
    @GetMapping("/tasks/{id}")
    public ResponseEntity<FocusResponseDto> getTaskById(@PathVariable Long id) {
        return focusService.getFocusTaskById(id)
                .map(this::convertToResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private Focus convertToEntity(FocusCreateDto dto) {
        Focus focus = new Focus();
        focus.setTasks(dto.getTasks());
        focus.setTargetTime(dto.getTargetTime());
        focus.setRecordTime(dto.getRecordTime());
        focus.setPlaces(dto.getPlaces());
        return focus;
    }

    private FocusResponseDto convertToResponseDto(Focus focus) {
        FocusResponseDto dto = new FocusResponseDto();
        dto.setId(focus.getId());
        dto.setTasks(focus.getTasks());
        dto.setTargetTime(focus.getTargetTime());
        dto.setRecordTime(focus.getRecordTime());
        dto.setPlaces(focus.getPlaces());
        dto.setCreatedDate(focus.getCreatedDate() != null ? focus.getCreatedDate().toString() : null);
        return dto;
    }
}
