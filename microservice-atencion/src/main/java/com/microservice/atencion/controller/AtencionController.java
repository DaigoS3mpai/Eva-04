package com.microservice.atencion.controller;

import com.microservice.atencion.dto.AtencionDTO;
import com.microservice.atencion.model.Atencion;
import com.microservice.atencion.service.AtencionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/atencion")
@Tag(name = "Atenciones", description = "API para gestión de atenciones")
public class AtencionController {

    private final AtencionService atencionService;

    public AtencionController(AtencionService atencionService) {
        this.atencionService = atencionService;
    }

    // Listar todas las atenciones
    @Operation(summary = "Listar atenciones")
    @GetMapping
    public ResponseEntity<List<Atencion>> getAll() {
        List<Atencion> list = atencionService.findAll();
        return ResponseEntity.ok(list);
    }

    // Obtener atencion por ID con HATEOAS
    @Operation(summary = "Obtener atencion por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Optional<Atencion> atencion = atencionService.getPatientById(id);

        if (atencion.isPresent()) {
            EntityModel<Atencion> resource = EntityModel.of(atencion.get());
            resource.add(linkTo(methodOn(AtencionController.class).getById(id)).withSelfRel());
            resource.add(linkTo(methodOn(AtencionController.class).getAll()).withRel("todas"));
            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear nueva atencion
    @Operation(summary = "Crear atencion")
    @PostMapping
    public ResponseEntity<Atencion> create(@RequestBody AtencionDTO dto) {
        Atencion saved = atencionService.save(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Eliminar atencion
    @Operation(summary = "Eliminar atencion")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (!atencionService.existeAtencion(id)) {
            return ResponseEntity.notFound().build();
        }
        atencionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
