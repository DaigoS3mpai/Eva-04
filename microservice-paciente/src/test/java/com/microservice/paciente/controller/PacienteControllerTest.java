package com.microservice.paciente.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.paciente.model.Paciente;
import com.microservice.paciente.service.PacienteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PacienteController.class)
class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService pacienteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListarTodos() throws Exception {
        Paciente p1 = new Paciente();
        Paciente p2 = new Paciente();
        when(pacienteService.findAll()).thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(get("/api/v1/pacientes/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetByIdFound() throws Exception {
        Paciente p = new Paciente();
        p.setId_paciente(1);
        p.setNombres("Carlos");

        when(pacienteService.getPatientById(1)).thenReturn(Optional.of(p));

        mockMvc.perform(get("/api/v1/pacientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombres").value("Carlos"));
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        when(pacienteService.getPatientById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/pacientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No se encontró el paciente con ID: 99"));
    }

@Test
void testSavePaciente() throws Exception {
    Paciente p = new Paciente();
    p.setRut("12345678-9"); // Cumple con el patrón
    p.setNombres("Laura");
    p.setApellidos("Gonzalez");
    p.setCorreo("laura@example.com");

    LocalDate localDate = LocalDate.of(2000, 1, 1);
    Date fecha = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    p.setFechaNacimiento(fecha);

    when(pacienteService.save(any(Paciente.class))).thenReturn(p);

    mockMvc.perform(post("/api/v1/pacientes") // corrige el endpoint sin "path"
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(p)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nombres").value("Laura"));
}


@Test
void testDeletePaciente() throws Exception {
    // Mock para que getPatientById(10) devuelva un paciente
    when(pacienteService.getPatientById(10)).thenReturn(Optional.of(new Paciente()));

    doNothing().when(pacienteService).delete(10);

    mockMvc.perform(delete("/api/v1/pacientes/10"))
            .andExpect(status().isNoContent());
}
}
