package com.microservice.agendacita.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.agendacita.dto.AgendarDTO;
import com.microservice.agendacita.model.Agendar;
import com.microservice.agendacita.model.EstadoCita;
import com.microservice.agendacita.service.AgendarService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AgendarController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AgendarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgendarService agendarService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCrearCita() throws Exception {
        AgendarDTO dto = new AgendarDTO();
        dto.setPacienteId(1L);
        dto.setFechaHora(LocalDateTime.now().plusDays(1)); // ✅ Fecha futura siempre

        Agendar citaGuardada = new Agendar();
        citaGuardada.setId(10L);
        citaGuardada.setPacienteId(1L);
        citaGuardada.setFechaHora(dto.getFechaHora());

        when(agendarService.agendar(Mockito.any(Agendar.class))).thenReturn(citaGuardada);

        mockMvc.perform(post("/api/agendar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.pacienteId").value(1));
    }

    @Test
    void testObtenerCita() throws Exception {
        Agendar cita = new Agendar();
        cita.setId(5L);
        cita.setPacienteId(2L);
        cita.setFechaHora(LocalDateTime.now().plusDays(2)); // ✅ Fecha futura siempre

        when(agendarService.obtenerPorId(5L)).thenReturn(cita);

        mockMvc.perform(get("/api/agendar/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.pacienteId").value(2));
    }

    @Test
    void testListarPorPaciente() throws Exception {
        Agendar c1 = new Agendar();
        c1.setId(1L);
        c1.setPacienteId(9L);
        c1.setFechaHora(LocalDateTime.now().plusDays(3)); // ✅ Fecha futura siempre

        when(agendarService.listarPorPaciente(9L)).thenReturn(List.of(c1));

        mockMvc.perform(get("/api/agendar/paciente/9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pacienteId").value(9));
    }

    @Test
    void testListarPorEstado() throws Exception {
        Agendar c1 = new Agendar();
        c1.setId(2L);
        c1.setEstado(EstadoCita.AGENDADA);

        when(agendarService.listarPorEstado(EstadoCita.AGENDADA)).thenReturn(List.of(c1));

        mockMvc.perform(get("/api/agendar/estado")
                        .param("estado", "AGENDADA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("AGENDADA"));
    }

    @Test
    void testListarPorRangoFechas() throws Exception {
        LocalDateTime desde = LocalDateTime.now().plusDays(1);
        LocalDateTime hasta = LocalDateTime.now().plusDays(30);

        Agendar cita = new Agendar();
        cita.setId(3L);
        cita.setFechaHora(LocalDateTime.now().plusDays(10)); // ✅ Fecha dentro del rango

        when(agendarService.listarPorRangoDeFecha(desde, hasta)).thenReturn(List.of(cita));

        mockMvc.perform(get("/api/agendar/rango")
                        .param("desde", desde.toString())
                        .param("hasta", hasta.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3));
    }

    @Test
    void testCancelarCita() throws Exception {
        Agendar cancelada = new Agendar();
        cancelada.setId(4L);
        cancelada.setEstado(EstadoCita.CANCELADA);

        when(agendarService.cancelar(4L)).thenReturn(cancelada);

        mockMvc.perform(put("/api/agendar/cancelar/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CANCELADA"));
    }
}
