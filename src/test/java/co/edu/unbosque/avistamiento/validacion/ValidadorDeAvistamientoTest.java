package co.edu.unbosque.avistamiento.validacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.unbosque.avistamiento.model.Avistamiento;

class ValidadorDeAvistamientoTest {

    private static final LocalDate HOY = LocalDate.of(2026, 10, 14);

    private static final String ERROR_ESPECIE = "especie: debe tener entre 3 y 50 caracteres";
    private static final String ERROR_FECHA_FUTURA = "fecha: no puede ser posterior a hoy";
    private static final String ERROR_FECHA_FORMATO = "fecha: debe tener el formato AAAA-MM-DD";
    private static final String ERROR_FECHA_NULA = "fecha: es obligatoria con el formato AAAA-MM-DD";
    private static final String ERROR_LUGAR = "lugar: no puede estar vacío";
    private static final String ERROR_OBSERVADOR = "observador: no puede estar vacío";

    private ValidadorDeAvistamiento validador;

    @BeforeEach
    void preparar() {
        validador = new ValidadorDeAvistamiento(new RelojFijo(HOY));
    }

    private Avistamiento avistamientoValido() {
        Avistamiento a = new Avistamiento();
        a.setEspecie("Colibrí");
        a.setLugar("Jardín Botánico");
        a.setFecha("2026-10-01");
        a.setObservador("Mariana Ovallos");
        return a;
    }

    @Test
    void avistamientoConTodosLosCamposCorrectos_esValido() {
        // Arrange
        Avistamiento avistamiento = avistamientoValido();

        // Act
        String error = validador.validar(avistamiento);

        // Assert
        assertNull(error);
    }


    @Test
    void especieDe2Caracteres_esInvalida() {
        // Arrange
        Avistamiento avistamiento = avistamientoValido();
        avistamiento.setEspecie("ab");

        // Act
        String error = validador.validar(avistamiento);

        // Assert
        assertEquals(ERROR_ESPECIE, error);
    }

    @Test
    void especieDe3Caracteres_esValida() {
        // Arrange
        Avistamiento avistamiento = avistamientoValido();
        avistamiento.setEspecie("abc");

        // Act
        String error = validador.validar(avistamiento);

        // Assert
        assertNull(error);
    }

    @Test
    void especieDe50Caracteres_esValida() {
        // Arrange
        Avistamiento avistamiento = avistamientoValido();
        avistamiento.setEspecie("a".repeat(50));

        // Act
        String error = validador.validar(avistamiento);

        // Assert
        assertNull(error);
    }

    @Test
    void especieDe51Caracteres_esInvalida() {
        // Arrange
        Avistamiento avistamiento = avistamientoValido();
        avistamiento.setEspecie("a".repeat(51));

        // Act
        String error = validador.validar(avistamiento);

        // Assert
        assertEquals(ERROR_ESPECIE, error);
    }

    @Test
    void especieNula_esInvalida() {
        // Arrange
        Avistamiento avistamiento = avistamientoValido();
        avistamiento.setEspecie(null);

        // Act
        String error = validador.validar(avistamiento);

        // Assert
        assertEquals(ERROR_ESPECIE, error);
    }

    @Test
    void especieConSoloEspacios_esInvalida() {
        // Arrange
        Avistamiento avistamiento = avistamientoValido();
        avistamiento.setEspecie("   ");

        // Act
        String error = validador.validar(avistamiento);

        // Assert
        assertEquals(ERROR_ESPECIE, error);
    }


    @Test
    void fechaDeHoy_esValida() {
        // Arrange
        Avistamiento avistamiento = avistamientoValido();
        avistamiento.setFecha("2026-10-14");

        // Act
        String error = validador.validar(avistamiento);

        // Assert
        assertNull(error);
    }

    @Test
    void fechaDeManana_esInvalida() {
        // Arrange
        Avistamiento avistamiento = avistamientoValido();
        avistamiento.setFecha("2026-10-15");

        // Act
        String error = validador.validar(avistamiento);

        // Assert
        assertEquals(ERROR_FECHA_FUTURA, error);
    }

    @Test
    void fechaConFormatoDiaMesAnio_esInvalida() {
        // Arrange
        Avistamiento avistamiento = avistamientoValido();
        avistamiento.setFecha("14-10-2026");

        // Act
        String error = validador.validar(avistamiento);

        // Assert
        assertEquals(ERROR_FECHA_FORMATO, error);
    }

    @Test
    void fechaQueNoExisteEnElCalendario_esInvalida() {
        // Arrange
        Avistamiento avistamiento = avistamientoValido();
        avistamiento.setFecha("2026-02-30");

        // Act
        String error = validador.validar(avistamiento);

        // Assert
        assertEquals(ERROR_FECHA_FORMATO, error);
    }

    @Test
    void fechaNula_esInvalida() {
        // Arrange
        Avistamiento avistamiento = avistamientoValido();
        avistamiento.setFecha(null);

        // Act
        String error = validador.validar(avistamiento);

        // Assert
        assertEquals(ERROR_FECHA_NULA, error);
    }

    @Test
    void lugarVacio_esInvalido() {
        // Arrange
        Avistamiento avistamiento = avistamientoValido();
        avistamiento.setLugar("");

        // Act
        String error = validador.validar(avistamiento);

        // Assert
        assertEquals(ERROR_LUGAR, error);
    }

    @Test
    void observadorConSoloEspacios_esInvalido() {
        // Arrange
        Avistamiento avistamiento = avistamientoValido();
        avistamiento.setObservador("   ");

        // Act
        String error = validador.validar(avistamiento);

        // Assert
        assertEquals(ERROR_OBSERVADOR, error);
    }

        @Test
    void lugarNulo_esInvalido() {
        // Arrange
        Avistamiento avistamiento = avistamientoValido();
        avistamiento.setLugar(null);

        // Act
        String error = validador.validar(avistamiento);

        // Assert
        assertEquals(ERROR_LUGAR, error);
    }
}