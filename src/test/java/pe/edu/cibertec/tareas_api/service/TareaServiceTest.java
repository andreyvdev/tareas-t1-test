package pe.edu.cibertec.tareas_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.cibertec.tareas_api.model.Proyecto;
import pe.edu.cibertec.tareas_api.model.Tarea;
import pe.edu.cibertec.tareas_api.model.Usuario;
import pe.edu.cibertec.tareas_api.repository.TareaRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TareaServiceTest {

    @Mock
    private TareaRepository _repo;

    @InjectMocks
    private TareaService _service;

    private Tarea tarea;
    private Proyecto proyecto;

    @BeforeEach
    void SetUp() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Andres Yamunaque Villanueva");
        usuario.setEmail("andresdev.california@nvidia.com");
        usuario.setRol("developer");
        usuario.setActivo(true);

        proyecto = new Proyecto();
        proyecto.setId(1L);
        proyecto.setNombre("Implementacion de seguridad Spring Security");
        proyecto.setDescripcion("Implementacion de seguridad Spring Security al proyecto");
        proyecto.setFechaInicio(LocalDate.of(2025, 11, 26));
        proyecto.setFechaFin(LocalDate.of(2025, 12, 30));
        proyecto.setUsuario(usuario);
        proyecto.setActivo(true);

        tarea = new Tarea();
        tarea.setId(1L);
        tarea.setTitulo("Creacion de sistema de gestion de roles de usuario");
        tarea.setDescripcion("Crear la gestion de usuarios por roles");
        tarea.setEstado("EN_PROGRESO");
        tarea.setPrioridad("ALTA");
        tarea.setProyecto(proyecto);
        tarea.setActivo(true);
    }

    /*
    a) listarTodos() - Verificar que el método retorna una lista de tareas. Mockee el repositorio y
verifique el resultado.
     */
    @Test
    @DisplayName("Verificar que el método retorna una lista de tareas.")
    void listarTodos() {
        List<Tarea> tareas = Arrays.asList(tarea);
        when (_repo.findAll()).thenReturn(tareas);

        List<Tarea> res = _repo.findAll();

        assertNotNull(res);
        assertEquals(1, res.size());
        verify(_repo, times(1)).findAll();
    }

    /*
    b) crear_Exitoso() - Crear una tarea correctamente. Mockee proyectoRepository.findById para
retornar el proyecto y tareaRepository.save
     */
    @Test
    @DisplayName("Crear una tarea correctamente.")
    void crear_Exitoso() {
        when (_repo.findById(1L)).thenReturn(Optional.empty());
        when (_repo.save(any(Tarea.class))).thenReturn(tarea);

        Tarea res = _repo.save(tarea);

        assertNotNull(res);
        assertTrue(_repo.findById(1l).isEmpty());

        verify (_repo, times(1)).findById(1L);
        verify (_repo, times(1)).save(tarea);
    }

    /*
    c) crear_EstadoInvalido() - Verificar que se lanza excepción cuando el estado no es válido.
Configure la tarea con estado "ESTADO_INVALIDO" y verifique que se lance RuntimeException
     */
    @Test
    @DisplayName("Verificar que se lanza excepción cuando el estado no es válido.")
    void crear_EstadoInvalido() {

        tarea.setEstado("ESTADO_INVALIDO");
        Exception ex = assertThrows(RuntimeException.class,
                () -> _service.crear(tarea));

        assertEquals("Estado inválido. Debe ser: PENDIENTE, EN_PROGRESO o COMPLETADA", ex.getMessage());
        verify (_repo, never()).save(any());
    }
}
