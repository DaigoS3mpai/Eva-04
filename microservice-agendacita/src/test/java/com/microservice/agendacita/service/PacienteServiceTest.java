package com.microservice.agendacita.service;

import com.microservice.agendacita.model.Agendar;
import com.microservice.agendacita.model.EstadoCita;
import com.microservice.agendacita.repository.AgendarRe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class PacienteServiceTest {
    private AgendarRe agendarRepo;
    private AgendarService agendarService;

    @BeforeEach
    void setUp() {
        agendarRepo = mock(AgendarRe.class);
        agendarService = new AgendarService(agendarRepo);
    }

    @Test
void testGuardarCita() {
    Agendar cita = new Agendar(1L, 123L, LocalDateTime.now().plusDays(1), EstadoCita.PENDIENTE);
    when(agendarRepo.save(any(Agendar.class))).thenReturn(cita);

    Agendar resultado = agendarService.agendar(cita);

    assertNotNull(resultado);
    assertEquals(123L, resultado.getPacienteId());
    verify(agendarRepo, times(1)).save(any(Agendar.class));
}

    @Test
void testListarCitas() {
    Agendar cita1 = new Agendar(1L, 100L, LocalDateTime.now().plusDays(1), EstadoCita.COMPLETADA);
    Agendar cita2 = new Agendar(2L, 101L, LocalDateTime.now().plusDays(2), EstadoCita.PENDIENTE);

    when(agendarRepo.findAll()).thenReturn(Arrays.asList(cita1, cita2));

    List<Agendar> resultado = agendarService.obtenerCitas();

    assertEquals(2, resultado.size());
    verify(agendarRepo, times(1)).findAll();
}

    @Test
    void testBuscarCitaPorId_existente() {
        Agendar cita = new Agendar(1L, 101L, LocalDateTime.now(), EstadoCita.PENDIENTE);
        when(agendarRepo.findById(1L)).thenReturn(Optional.of(cita));

        Optional<Agendar> resultado = Optional.ofNullable(agendarService.obtenerPorId(1L));

        assertTrue(resultado.isPresent());
        assertEquals(101L, resultado.get().getPacienteId());
    }
}

