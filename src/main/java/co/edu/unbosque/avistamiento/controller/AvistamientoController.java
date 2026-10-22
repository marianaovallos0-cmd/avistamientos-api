package co.edu.unbosque.avistamiento.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.avistamiento.model.Avistamiento;
import co.edu.unbosque.avistamiento.service.AvistamientoService;
import co.edu.unbosque.avistamiento.validacion.ValidadorDeAvistamiento;

@RestController
@RequestMapping("/avistamientos")
public class AvistamientoController {

    private final AvistamientoService service;
    private final ValidadorDeAvistamiento validador;

    public AvistamientoController(AvistamientoService service, ValidadorDeAvistamiento validador) {
        this.service = service;
        this.validador = validador;
    }

    @GetMapping
    public ResponseEntity<List<Avistamiento>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerAvistamiento());
    }

    @GetMapping("/resumen")
    public ResponseEntity<List<Object[]>> resumen() {
        return ResponseEntity.ok(service.resumenPorEspecie());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avistamiento> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Avistamiento avistamiento) {
        String error = validador.validar(avistamiento);
        if (error != null) {
            return ResponseEntity.badRequest().body(Map.of("error", error));
        }
        Avistamiento creado = service.agregarAvistamiento(avistamiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Avistamiento datosNuevos) {
        String error = validador.validar(datosNuevos);
        if (error != null) {
            return ResponseEntity.badRequest().body(Map.of("error", error));
        }
        return service.actualizar(id, datosNuevos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}