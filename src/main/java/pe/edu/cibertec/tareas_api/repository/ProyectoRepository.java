package pe.edu.cibertec.tareas_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.tareas_api.model.Proyecto;

import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    List<Proyecto> findByUsuarioId(Long usuarioId);

    List<Proyecto> findByActivoTrue();
}
