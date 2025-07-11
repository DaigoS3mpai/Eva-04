package com.microservice.agendacita.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgendarDTO {

    @NotNull(message = "El ID del paciente es obligatorio")
    private Long pacienteId;

    @NotNull(message = "La fecha y hora son obligatorias")
    @Future(message = "La fecha debe ser en el futuro")
    private LocalDateTime fechaHora;
}