package pe.edu.cibertec.tareas_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.tareas_api.model.Proyecto;
import pe.edu.cibertec.tareas_api.model.Usuario;
import pe.edu.cibertec.tareas_api.repository.ProyectoRepository;
import pe.edu.cibertec.tareas_api.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProyectoService {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Proyecto> listarTodos() {
        return proyectoRepository.findAll();
    }

    public List<Proyecto> listarActivos() {
        return proyectoRepository.findByActivoTrue();
    }

    public Optional<Proyecto> buscarPorId(Long id) {
        return proyectoRepository.findById(id);
    }

    public List<Proyecto> listarPorUsuario(Long usuarioId) {
        return proyectoRepository.findByUsuarioId(usuarioId);
    }

    public Proyecto crear(Proyecto proyecto) {
        if (proyecto.getFechaFin().isBefore(proyecto.getFechaInicio())) {
            throw new RuntimeException("La fecha de fin debe ser mayor a la fecha de inicio");
        }

        Usuario usuario = usuarioRepository.findById(proyecto.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            throw new RuntimeException("El usuario debe estar activo");
        }

        proyecto.setUsuario(usuario);
        return proyectoRepository.save(proyecto);
    }

    public Proyecto actualizar(Long id, Proyecto proyecto) {
        Proyecto proyectoExistente = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        if (proyecto.getFechaFin().isBefore(proyecto.getFechaInicio())) {
            throw new RuntimeException("La fecha de fin debe ser mayor a la fecha de inicio");
        }

        Usuario usuario = usuarioRepository.findById(proyecto.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            throw new RuntimeException("El usuario debe estar activo");
        }

        proyectoExistente.setNombre(proyecto.getNombre());
        proyectoExistente.setDescripcion(proyecto.getDescripcion());
        proyectoExistente.setFechaInicio(proyecto.getFechaInicio());
        proyectoExistente.setFechaFin(proyecto.getFechaFin());
        proyectoExistente.setUsuario(usuario);
        if (proyecto.getActivo() != null) {
            proyectoExistente.setActivo(proyecto.getActivo());
        }

        return proyectoRepository.save(proyectoExistente);
    }

    public void eliminar(Long id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        proyecto.setActivo(false);
        proyectoRepository.save(proyecto);
    }
}
