package co.edu.unbosque.avistamiento.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import co.edu.unbosque.avistamiento.model.Avistamiento;
import co.edu.unbosque.avistamiento.repository.AvistamientoRepository;

class AvistamientoServiceTest {

    @Test
    void buscarPorId_cuandoExiste_devuelveElAvistamiento() {
        // Arrange
        AvistamientoRepository repositorio = mock(AvistamientoRepository.class);
        AvistamientoService servicio = new AvistamientoService(repositorio);

        Avistamiento colibri = new Avistamiento();
        colibri.setId(1L);
        colibri.setEspecie("Colibrí");

        when(repositorio.findById(1L)).thenReturn(Optional.of(colibri));

        // Act
        Optional<Avistamiento> resultado = servicio.buscarPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Colibrí", resultado.get().getEspecie());
    }

    @Test
    void buscarPorId_cuandoNoExiste_devuelveVacio() {
        // Arrange
        AvistamientoRepository repositorio = mock(AvistamientoRepository.class);
        AvistamientoService servicio = new AvistamientoService(repositorio);

        when(repositorio.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Avistamiento> resultado = servicio.buscarPorId(1L);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    void eliminar_cuandoExiste_borraYDevuelveTrue() {
        // Arrange
        AvistamientoRepository repositorio = mock(AvistamientoRepository.class);
        AvistamientoService servicio = new AvistamientoService(repositorio);

        when(repositorio.existsById(1L)).thenReturn(true);

        // Act
        boolean resultado = servicio.eliminar(1L);

        // Assert
        assertTrue(resultado);
        verify(repositorio, times(1)).deleteById(1L);
    }

    @Test
    void eliminar_cuandoNoExiste_noBorraYDevuelveFalse() {
        // Arrange
        AvistamientoRepository repositorio = mock(AvistamientoRepository.class);
        AvistamientoService servicio = new AvistamientoService(repositorio);

        when(repositorio.existsById(1L)).thenReturn(false);

        // Act
        boolean resultado = servicio.eliminar(1L);

        // Assert
        assertFalse(resultado);
        verify(repositorio, never()).deleteById(anyLong());
    }

    @Test
    void agregarAvistamiento_cuandoGuarda_devuelveElAvistamiento() {
        // Arrange
        AvistamientoRepository repositorio = mock(AvistamientoRepository.class);
        AvistamientoService servicio = new AvistamientoService(repositorio);

        Avistamiento colibri = new Avistamiento();
        colibri.setEspecie("Colibri");

        when(repositorio.save(colibri)).thenReturn(colibri);

        // Act
        Avistamiento resultado = servicio.agregarAvistamiento(colibri);

        // Assert
        assertEquals("Colibri", resultado.getEspecie());
        verify(repositorio, times(1)).save(resultado);
    }

    @Test
    void actualizarAvistamiento_CuandoNoExiste_devuelveVacio() {
        // Arrange
        AvistamientoRepository repositorio = mock(AvistamientoRepository.class);
        AvistamientoService servicio = new AvistamientoService(repositorio);

        Avistamiento colibri = new Avistamiento();
        colibri.setEspecie("Colibri");
        colibri.setFecha("14-08-2026");
        colibri.setLugar("Bogotá");
        colibri.setObservador("Ovallos Mariana");

        when(repositorio.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Avistamiento> resultado = servicio.actualizar(99L, colibri);

        // Assert
        assertTrue(resultado.isEmpty());
        verify(repositorio, never()).save(any());
    }
}