package pe.edu.cibertec.tareas_api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.tareas_api.model.Tarea;
import pe.edu.cibertec.tareas_api.service.TareaService;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @GetMapping
    public ResponseEntity<List<Tarea>> listarTodos() {
        return ResponseEntity.ok(tareaService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Tarea>> listarActivos() {
        return ResponseEntity.ok(tareaService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarea> buscarPorId(@PathVariable Long id) {
        return tareaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<Tarea>> listarPorProyecto(@PathVariable Long proyectoId) {
        return ResponseEntity.ok(tareaService.listarPorProyecto(proyectoId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Tarea>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(tareaService.listarPorEstado(estado));
    }

    @GetMapping("/prioridad/{prioridad}")
    public ResponseEntity<List<Tarea>> listarPorPrioridad(@PathVariable String prioridad) {
        return ResponseEntity.ok(tareaService.listarPorPrioridad(prioridad));
    }

    @PostMapping
    public ResponseEntity<Tarea> crear(@Valid @RequestBody Tarea tarea) {
        try {
            Tarea nuevaTarea = tareaService.crear(tarea);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaTarea);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarea> actualizar(@PathVariable Long id, @Valid @RequestBody Tarea tarea) {
        try {
            Tarea tareaActualizada = tareaService.actualizar(id, tarea);
            return ResponseEntity.ok(tareaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            tareaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
