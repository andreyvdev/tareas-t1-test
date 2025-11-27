package pe.edu.cibertec.tareas_api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.tareas_api.model.Proyecto;
import pe.edu.cibertec.tareas_api.service.ProyectoService;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

    @GetMapping
    public ResponseEntity<List<Proyecto>> listarTodos() {
        return ResponseEntity.ok(proyectoService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Proyecto>> listarActivos() {
        return ResponseEntity.ok(proyectoService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> buscarPorId(@PathVariable Long id) {
        return proyectoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Proyecto>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(proyectoService.listarPorUsuario(usuarioId));
    }

    @PostMapping
    public ResponseEntity<Proyecto> crear(@Valid @RequestBody Proyecto proyecto) {
        try {
            Proyecto nuevoProyecto = proyectoService.crear(proyecto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProyecto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> actualizar(@PathVariable Long id, @Valid @RequestBody Proyecto proyecto) {
        try {
            Proyecto proyectoActualizado = proyectoService.actualizar(id, proyecto);
            return ResponseEntity.ok(proyectoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            proyectoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
