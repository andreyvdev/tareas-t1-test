package pe.edu.cibertec.tareas_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.tareas_api.model.Proyecto;
import pe.edu.cibertec.tareas_api.model.Tarea;
import pe.edu.cibertec.tareas_api.repository.ProyectoRepository;
import pe.edu.cibertec.tareas_api.repository.TareaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    private static final List<String> ESTADOS_VALIDOS = Arrays.asList("PENDIENTE", "EN_PROGRESO", "COMPLETADA");
    private static final List<String> PRIORIDADES_VALIDAS = Arrays.asList("BAJA", "MEDIA", "ALTA");

    public List<Tarea> listarTodos() {
        return tareaRepository.findAll();
    }

    public List<Tarea> listarActivos() {
        return tareaRepository.findByActivoTrue();
    }

    public Optional<Tarea> buscarPorId(Long id) {
        return tareaRepository.findById(id);
    }

    public List<Tarea> listarPorProyecto(Long proyectoId) {
        return tareaRepository.findByProyectoId(proyectoId);
    }

    public List<Tarea> listarPorEstado(String estado) {
        return tareaRepository.findByEstado(estado);
    }

    public List<Tarea> listarPorPrioridad(String prioridad) {
        return tareaRepository.findByPrioridad(prioridad);
    }

    public Tarea crear(Tarea tarea) {
        if (!ESTADOS_VALIDOS.contains(tarea.getEstado())) {
            throw new RuntimeException("Estado inválido. Debe ser: PENDIENTE, EN_PROGRESO o COMPLETADA");
        }

        if (!PRIORIDADES_VALIDAS.contains(tarea.getPrioridad())) {
            throw new RuntimeException("Prioridad inválida. Debe ser: BAJA, MEDIA o ALTA");
        }

        Proyecto proyecto = proyectoRepository.findById(tarea.getProyecto().getId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        if (!proyecto.getActivo()) {
            throw new RuntimeException("El proyecto debe estar activo");
        }

        tarea.setProyecto(proyecto);
        return tareaRepository.save(tarea);
    }

    public Tarea actualizar(Long id, Tarea tarea) {
        Tarea tareaExistente = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        if (!ESTADOS_VALIDOS.contains(tarea.getEstado())) {
            throw new RuntimeException("Estado inválido. Debe ser: PENDIENTE, EN_PROGRESO o COMPLETADA");
        }

        if (!PRIORIDADES_VALIDAS.contains(tarea.getPrioridad())) {
            throw new RuntimeException("Prioridad inválida. Debe ser: BAJA, MEDIA o ALTA");
        }

        Proyecto proyecto = proyectoRepository.findById(tarea.getProyecto().getId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        if (!proyecto.getActivo()) {
            throw new RuntimeException("El proyecto debe estar activo");
        }

        tareaExistente.setTitulo(tarea.getTitulo());
        tareaExistente.setDescripcion(tarea.getDescripcion());
        tareaExistente.setEstado(tarea.getEstado());
        tareaExistente.setPrioridad(tarea.getPrioridad());
        tareaExistente.setProyecto(proyecto);
        if (tarea.getActivo() != null) {
            tareaExistente.setActivo(tarea.getActivo());
        }

        return tareaRepository.save(tareaExistente);
    }

    public void eliminar(Long id) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        tarea.setActivo(false);
        tareaRepository.save(tarea);
    }
}
