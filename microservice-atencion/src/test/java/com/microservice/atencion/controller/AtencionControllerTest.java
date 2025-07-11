package com.microservice.atencion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microservice.atencion.dto.AtencionDTO;
import com.microservice.atencion.model.Atencion;
import com.microservice.atencion.service.AtencionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Usa WebMvcTest para cargar solo el contexto web del controlador
@WebMvcTest(AtencionController.class)
public class AtencionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AtencionService atencionService;

    private ObjectMapper objectMapper;

    private Atencion atencion1;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // <-- importante para LocalDate/LocalTime

        atencion1 = new Atencion();
        atencion1.setId_atencion(1);
        atencion1.setFecha_atencion(LocalDate.of(2023,7,1));
        atencion1.setHora_atencion(LocalTime.of(10,0));
        atencion1.setCosto(1500.0);
        atencion1.setId_paciente(123);
        atencion1.setComentario("Atencion test");
    }

    @Test
    public void testGetAll() throws Exception {
        List<Atencion> list = Arrays.asList(atencion1);
        when(atencionService.findAll()).thenReturn(list);

        mockMvc.perform(get("/atencion"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id_atencion").value(atencion1.getId_atencion()));

        verify(atencionService, times(1)).findAll();
    }

    @Test
    public void testGetByIdFound() throws Exception {
        when(atencionService.getPatientById(1)).thenReturn(Optional.of(atencion1));

        mockMvc.perform(get("/atencion/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id_atencion").value(atencion1.getId_atencion()));

        verify(atencionService, times(1)).getPatientById(1);
    }

    @Test
    public void testGetByIdNotFound() throws Exception {
        when(atencionService.getPatientById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/atencion/1"))
            .andExpect(status().isNotFound());

        verify(atencionService, times(1)).getPatientById(1);
    }

    @Test
    public void testCreate() throws Exception {
        AtencionDTO dto = new AtencionDTO(
            atencion1.getFecha_atencion(),
            atencion1.getHora_atencion(),
            atencion1.getCosto(),
            atencion1.getId_paciente(),
            atencion1.getComentario()
        );

        when(atencionService.save(any(AtencionDTO.class))).thenReturn(atencion1);

        mockMvc.perform(post("/atencion")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id_atencion").value(atencion1.getId_atencion()));

        verify(atencionService, times(1)).save(any(AtencionDTO.class));
    }

    @Test
    public void testDeleteFound() throws Exception {
        when(atencionService.existeAtencion(1)).thenReturn(true);
        doNothing().when(atencionService).delete(1);

        mockMvc.perform(delete("/atencion/1"))
            .andExpect(status().isNoContent());

        verify(atencionService, times(1)).existeAtencion(1);
        verify(atencionService, times(1)).delete(1);
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        when(atencionService.existeAtencion(1)).thenReturn(false);

        mockMvc.perform(delete("/atencion/1"))
            .andExpect(status().isNotFound());

        verify(atencionService, times(1)).existeAtencion(1);
        verify(atencionService, times(0)).delete(anyInt());
    }
}
