package com.microservice.paciente.service;

import com.microservice.paciente.model.Paciente;
import com.microservice.paciente.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.MockitoAnnotations;

class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Paciente p1 = new Paciente();
        Paciente p2 = new Paciente();
        when(pacienteRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Paciente> pacientes = pacienteService.findAll();

        assertEquals(2, pacientes.size());
        verify(pacienteRepository, times(1)).findAll();
    }

    @Test
    void testGetPatientById() {
        Paciente paciente = new Paciente();
        paciente.setId_paciente(1);
        when(pacienteRepository.findById(1)).thenReturn(Optional.of(paciente));

        Optional<Paciente> result = pacienteService.getPatientById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId_paciente());
    }

    @Test
    void testGetPatientById2() {
        Paciente paciente = new Paciente();
        paciente.setId_paciente(2);
        when(pacienteRepository.findById(2)).thenReturn(Optional.of(paciente));

        Paciente result = pacienteService.getPatientById2(2);

        assertEquals(2, result.getId_paciente());
    }

    @Test
    void testSave() {
        Paciente paciente = new Paciente();
        paciente.setNombres("Juan");
        when(pacienteRepository.save(paciente)).thenReturn(paciente);

        Paciente saved = pacienteService.save(paciente);

        assertNotNull(saved);
        assertEquals("Juan", saved.getNombres());
        verify(pacienteRepository, times(1)).save(paciente);
    }

    @Test
    void testDelete() {
        pacienteService.delete(3);
        verify(pacienteRepository, times(1)).deleteById(3);
    }
}
