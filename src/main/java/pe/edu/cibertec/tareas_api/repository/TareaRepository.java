package pe.edu.cibertec.tareas_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.tareas_api.model.Tarea;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByProyectoId(Long proyectoId);

    List<Tarea> findByEstado(String estado);

    List<Tarea> findByPrioridad(String prioridad);

    List<Tarea> findByActivoTrue();
}
