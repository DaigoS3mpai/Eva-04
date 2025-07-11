package com.microservice.paciente.controller;

import com.microservice.paciente.model.Paciente;
import com.microservice.paciente.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/pacientes")
@Tag(name = "Pacientes", description = "API para gestión de pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    // Listar todos los pacientes
    @Operation(summary = "Listar pacientes")
    @GetMapping("/listar")
    public List<Paciente> getAllPatients(){
        return pacienteService.findAll();
    }

    // Obtener paciente por ID
    @Operation(summary = "Obtener paciente por ID")
    @GetMapping("/{id_paciente}")
    public ResponseEntity<?> getPatientById(@PathVariable("id_paciente") Integer id){
        Optional<Paciente> paciente = pacienteService.getPatientById(id);

        if(paciente.isPresent()){
            return ResponseEntity.ok(paciente.get());
        } else{
            Map<String,String> errorBody = new HashMap<>();
            errorBody.put("message","No se encontró el paciente con ID: " + id);
            errorBody.put("status","404");
            errorBody.put("timestamp",LocalDateTime.now().toString());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
        }
    }

    // Crear nuevo paciente
    @Operation(summary = "Crear paciente")
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Paciente paciente){
        try{
            Paciente pacienteGuardado = pacienteService.save(paciente);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(pacienteGuardado.getId_paciente())
                    .toUri();

            return ResponseEntity.created(location).body(pacienteGuardado);
        } catch(DataIntegrityViolationException e){
            Map<String,String> error = new HashMap<>();
            error.put("message","El email ya está registrado");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

    // Actualizar paciente existente
    @Operation(summary = "Actualizar paciente")
    @PutMapping("/{id_paciente}")
    public ResponseEntity<?> update(@PathVariable("id_paciente") int id, @Valid @RequestBody Paciente paciente){
        Optional<Paciente> pacienteExistente = pacienteService.getPatientById(id);

        if (!pacienteExistente.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Paciente pac = pacienteExistente.get();
        pac.setRut(paciente.getRut());
        pac.setNombres(paciente.getNombres());
        pac.setApellidos(paciente.getApellidos());
        pac.setFechaNacimiento(paciente.getFechaNacimiento());
        pac.setCorreo(paciente.getCorreo());

        Paciente actualizado = pacienteService.save(pac);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar paciente por ID
    @Operation(summary = "Eliminar paciente")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id){
        Optional<Paciente> pacienteExistente = pacienteService.getPatientById(id);
        if (!pacienteExistente.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        pacienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
