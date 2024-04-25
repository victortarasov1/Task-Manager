package tarasov.victor.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Schema to hold error response info")
public record ErrorResponseDto(String errorMessage, LocalDateTime errorTime) {
}