package com.microservice.agendacita.controller;

import com.microservice.agendacita.model.Agendar;
import com.microservice.agendacita.model.EstadoCita;
import com.microservice.agendacita.service.AgendarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.microservice.agendacita.dto.AgendarDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/agendar")
@RequiredArgsConstructor
@Tag(name = "Agendamiento", description = "API para gestión de agendamiento de citas")
public class AgendarController {

    private final AgendarService agendarService;

    // Crear nueva cita
    @Operation(summary = "Crear nueva cita")
    @PostMapping
    public ResponseEntity<Agendar> crearCita(@RequestBody @Valid AgendarDTO dto) {
        Agendar nuevaCita = new Agendar();
        nuevaCita.setPacienteId(dto.getPacienteId());
        nuevaCita.setFechaHora(dto.getFechaHora());

        return ResponseEntity.ok(agendarService.agendar(nuevaCita));
    }

    // Obtener cita por ID con HATEOAS
    @Operation(summary = "Obtener cita por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCita(@PathVariable Long id) {
        Agendar cita = agendarService.obtenerPorId(id);

        // HATEOAS: crea recurso con links
        EntityModel<Agendar> resource = EntityModel.of(cita);
        resource.add(linkTo(methodOn(AgendarController.class).obtenerCita(id)).withSelfRel());
        resource.add(linkTo(methodOn(AgendarController.class).listarPorPaciente(cita.getPacienteId())).withRel("citas-del-paciente"));

        return ResponseEntity.ok(resource);
    }

    // Listar citas por paciente
    @Operation(summary = "Listar citas por paciente")
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Agendar>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(agendarService.listarPorPaciente(pacienteId));
    }

    // Cancelar cita por ID
    @Operation(summary = "Cancelar cita")
    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Agendar> cancelarCita(@PathVariable Long id) {
        Agendar citaCancelada = agendarService.cancelar(id);
        return ResponseEntity.ok(citaCancelada);
    }

    // Buscar citas por rango de fechas
    @Operation(summary = "Listar citas por rango de fechas")
    @GetMapping("/rango")
    public ResponseEntity<List<Agendar>> listarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return ResponseEntity.ok(agendarService.listarPorRangoDeFecha(desde, hasta));
    }

    // Buscar citas por estado
    @Operation(summary = "Listar citas por estado")
    @GetMapping("/estado")
    public ResponseEntity<List<Agendar>> listarPorEstado(@RequestParam EstadoCita estado) {
        return ResponseEntity.ok(agendarService.listarPorEstado(estado));
    }
}
