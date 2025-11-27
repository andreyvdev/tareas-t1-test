package pe.edu.cibertec.tareas_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.cibertec.tareas_api.model.Usuario;
import pe.edu.cibertec.tareas_api.repository.UsuarioRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository _repo;

    @InjectMocks
    private UsuarioService _service;

    private Usuario usuario;

    @BeforeEach
    void SetUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Andres Yamunaque Villanueva");
        usuario.setEmail("andresdev.california@nvidia.com");
        usuario.setRol("developer");
        usuario.setActivo(true);
    }

    /*
    a) listarTodos() - Verificar que el método retorna una lista de usuarios. Mockee el repositorio para
    que retorne una lista con un usuario y verifique que el resultado no sea nulo, tenga tamaño 1, y
    que el repositorio se llamó exactamente 1 vez
     */
    @Test
    @DisplayName("Verificar que el método retorna una lista de usuarios.")
    void listarTodos() {
        List<Usuario> usuarios = Arrays.asList(usuario);
        when (_repo.findAll()).thenReturn(usuarios);

        List<Usuario> res = _service.listarTodos();

        assertNotNull(res);
        assertEquals(1, res.size());

        verify(_repo, times(1)).findAll();
    }

    /*
    b) buscarPorId_Exitoso() -
    Buscar un usuario por ID cuando existe. Mockee el repositorio para
    retornar un Optional con el usuario y verifique que esté presente
     */
    @Test
    @DisplayName("Buscar un usuario por ID cuando existe.")
    void buscarPorId_Existoso()
    {
        when(_repo.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> res = _service.buscarPorId(1L);

        assertTrue(res.isPresent());
        verify(_repo, times(1)).findById(1L);
    }

    /*
    c) crear_Exitoso() - Crear un usuario correctamente. Mockee save para retornar el usuario y
verifique que el resultado no sea nulo
     */
    @Test
    @DisplayName("Crear un usuario correctamente.")
    void crear_Existoso() {
        when (_repo.save(any(Usuario.class))).thenReturn(usuario);

        Usuario res = _service.crear(usuario);

        assertNotNull(res);
        verify (_repo, times(1)).save(usuario);
    }
}
