package com.microservice.atencion.service;

import com.microservice.atencion.dto.AtencionDTO;
import com.microservice.atencion.model.Atencion;
import com.microservice.atencion.repository.AtencionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AtencionServiceTest {

    @InjectMocks
    private AtencionService atencionService;

    @Mock
    private AtencionRepository atencionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExisteAtencion() {
        int id = 1;
        when(atencionRepository.existsById(id)).thenReturn(true);

        boolean result = atencionService.existeAtencion(id);

        assertTrue(result);
        verify(atencionRepository, times(1)).existsById(id);
    }

    @Test
    void testFindAll() {
        List<Atencion> lista = new ArrayList<>();
        lista.add(new Atencion());
        when(atencionRepository.findAll()).thenReturn(lista);

        List<Atencion> result = atencionService.findAll();

        assertEquals(1, result.size());
        verify(atencionRepository, times(1)).findAll();
    }

    @Test
    void testGetPatientById() {
        int id = 1;
        Atencion atencion = new Atencion();
        when(atencionRepository.findById(id)).thenReturn(Optional.of(atencion));

        Optional<Atencion> result = atencionService.getPatientById(id);

        assertTrue(result.isPresent());
        verify(atencionRepository, times(1)).findById(id);
    }

    @Test
    void testSaveWithDTO() {
        AtencionDTO dto = new AtencionDTO(
            LocalDate.now(), LocalTime.now(), 100.0, 1, "Comentario");

        Atencion atencionGuardada = new Atencion();
        atencionGuardada.setId_atencion(1);
        when(atencionRepository.save(any(Atencion.class))).thenReturn(atencionGuardada);

        Atencion result = atencionService.save(dto);

        assertNotNull(result);
        assertEquals(1, result.getId_atencion());
        verify(atencionRepository, times(1)).save(any(Atencion.class));
    }

    @Test
    void testDelete() {
        int id = 1;
        doNothing().when(atencionRepository).deleteById(id);

        atencionService.delete(id);

        verify(atencionRepository, times(1)).deleteById(id);
    }
}
