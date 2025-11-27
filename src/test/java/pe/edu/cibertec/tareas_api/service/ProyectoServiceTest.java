package pe.edu.cibertec.tareas_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.cibertec.tareas_api.model.Proyecto;
import pe.edu.cibertec.tareas_api.model.Usuario;
import pe.edu.cibertec.tareas_api.repository.ProyectoRepository;
import pe.edu.cibertec.tareas_api.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProyectoServiceTest {

    @Mock
    private ProyectoRepository _repo;

    @InjectMocks
    private ProyectoService _service;

    private Proyecto proyecto;
    private Usuario usuario;

    @BeforeEach
    void SetUp() {
        usuario = new Usuario();
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
    }

    /*
    a) listarTodos() - Verificar que el método retorna una lista de proyectos. Mockee el repositorio y
verifique el resultado.
     */
    @Test
    @DisplayName("Verificar que el método retorna una lista de proyectos.")
    void listarTodos() {
        List<Proyecto> proyectos = Arrays.asList(proyecto);
        when (_repo.findAll()).thenReturn(proyectos);

        List<Proyecto> res = _service.listarTodos();

        assertNotNull(res);
        assertEquals(1, res.size());
        verify(_repo, times(1)).findAll();
    }

    /*
b) buscarPorId_Exitoso() - Buscar un proyecto por ID. Mockee el repositorio para retornar un
Optional con el proyecto y verifique que el nombre sea correcto.
     */
    @Test
    @DisplayName("Buscar un proyecto por ID.")
    void buscarPorId_Exitoso() {
        when (_repo.findById(1L)).thenReturn(Optional.of(proyecto));

        Optional<Proyecto> res = _service.buscarPorId(1L);

        assertTrue(res.isPresent());
        assertEquals("Implementacion de seguridad Spring Security", res.get().getNombre());

        verify(_repo, times(1)).findById(1L);
    }

    /*
c) crear_Exitoso() - Crear un proyecto correctamente. Mockee usuarioRepository.findById para
retornar el usuario y proyectoRepository.save para retornar el proyecto.
     */
    @Test
    @DisplayName("Crear un proyecto correctamente.")
    void crear_Existoso() {
        when (_repo.findById(1L)).thenReturn(Optional.empty());
        when (_repo.save(any(Proyecto.class))).thenReturn(proyecto);

        Proyecto res = _repo.save(proyecto);

        assertNotNull(res);
        assertTrue(_repo.findById(1l).isEmpty());

        verify (_repo, times(1)).findById(1L);
        verify (_repo, times(1)).save(proyecto);
    }

    /*
d) crear_FechaInvalida() - Verificar que se lanza excepción cuando la fecha fin es anterior a la
fecha inicio. Configure el proyecto con fechaFin anterior a fechaInicio y verifique que se lance
RuntimeException.
     */
    @Test
    @DisplayName("Verificar que se lanza excepción cuando la fecha fin es anterior a la fecha inicio.")
    void crear_FechaInvalida() {

        proyecto.setFechaFin(LocalDate.of(2024, 11, 10));
        Exception ex = assertThrows(RuntimeException.class,
                () -> _service.crear(proyecto));

        assertEquals("La fecha de fin debe ser mayor a la fecha de inicio", ex.getMessage());
        verify (_repo, never()).save(any());
    }
}
