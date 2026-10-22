package co.edu.unbosque.avistamiento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import co.edu.unbosque.avistamiento.model.Avistamiento;
import co.edu.unbosque.avistamiento.repository.AvistamientoRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AvistamientoApiIntegracionTest {

    @LocalServerPort
    private int puerto;

    @Autowired
    private AvistamientoRepository repositorio;

    private RestTestClient cliente;

    private static final String JSON_VALIDO = """
            {"especie":"Colibrí","lugar":"Jardín Botánico","fecha":"2026-10-01","observador":"Mariana Ovallos"}
            """;

    @BeforeEach
    void preparar() {
        repositorio.deleteAll();
        cliente = RestTestClient.bindToServer().baseUrl("http://localhost:" + puerto).build();
    }

    private Avistamiento guardar(String especie, String lugar) {
        Avistamiento a = new Avistamiento();
        a.setEspecie(especie);
        a.setLugar(lugar);
        a.setFecha("2026-10-01");
        a.setObservador("Mariana Ovallos");
        return repositorio.save(a);
    }

    @Test
    void post_conDatosValidos_responde201YLuegoSePuedeConsultarPorGet() {
        // Act
        cliente.post().uri("/avistamientos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(JSON_VALIDO)
                .exchange()
                // Assert
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").exists()
                .jsonPath("$.especie").isEqualTo("Colibrí")
                .jsonPath("$.lugar").isEqualTo("Jardín Botánico");

        assertEquals(1, repositorio.count());
        Long idNuevo = repositorio.findAll().get(0).getId();

        cliente.get().uri("/avistamientos/{id}", idNuevo)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.especie").isEqualTo("Colibrí");
    }

    @Test
    void post_conEspecieDe2Caracteres_responde400ConMensajeYNoGuarda() {
        // Arrange
        String json = """
                {"especie":"ab","lugar":"Jardín Botánico","fecha":"2026-10-01","observador":"Mariana Ovallos"}
                """;

        // Act
        cliente.post().uri("/avistamientos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .exchange()
                // Assert
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("especie: debe tener entre 3 y 50 caracteres");

        assertEquals(0, repositorio.count());
    }

    @Test
    void getTodos_conDosAvistamientos_responde200ConLosDos() {
        // Arrange
        guardar("Colibrí", "Jardín Botánico");
        guardar("Cóndor", "Chingaza");

        // Act
        cliente.get().uri("/avistamientos")
                .exchange()
                // Assert
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].especie").isEqualTo("Colibrí")
                .jsonPath("$[1].especie").isEqualTo("Cóndor");
    }

    @Test
    void getPorId_existente_responde200ConSusDatos() {
        // Arrange
        Avistamiento guardado = guardar("Colibrí", "Monserrate");

        // Act
        cliente.get().uri("/avistamientos/{id}", guardado.getId())
                .exchange()
                // Assert
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(guardado.getId())
                .jsonPath("$.especie").isEqualTo("Colibrí")
                .jsonPath("$.lugar").isEqualTo("Monserrate");
    }

    @Test
    void getPorId_inexistente_responde404() {
        // Act
        cliente.get().uri("/avistamientos/{id}", 999)
                .exchange()
                // Assert
                .expectStatus().isNotFound();
    }

    @Test
    void put_existenteConDatosValidos_responde200YCambiaEnLaBase() {
        // Arrange
        Avistamiento guardado = guardar("Colibrí", "Jardín Botánico");
        String json = """
                {"especie":"Colibrí verde","lugar":"Monserrate","fecha":"2026-10-02","observador":"Mariana Ovallos"}
                """;

        // Act
        cliente.put().uri("/avistamientos/{id}", guardado.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .exchange()
                // Assert
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.especie").isEqualTo("Colibrí verde")
                .jsonPath("$.lugar").isEqualTo("Monserrate");

        Avistamiento enLaBase = repositorio.findById(guardado.getId()).orElseThrow();
        assertEquals("Colibrí verde", enLaBase.getEspecie());
        assertEquals("Monserrate", enLaBase.getLugar());
        assertEquals("2026-10-02", enLaBase.getFecha());
    }

    @Test
    void put_conFechaFutura_responde400YNoCambiaLaBase() {
        // Arrange
        Avistamiento guardado = guardar("Colibrí", "Jardín Botánico");
        String json = """
                {"especie":"Colibrí","lugar":"Jardín Botánico","fecha":"2999-01-01","observador":"Mariana Ovallos"}
                """;

        // Act
        cliente.put().uri("/avistamientos/{id}", guardado.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .exchange()
                // Assert
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("fecha: no puede ser posterior a hoy");

        assertEquals("2026-10-01", repositorio.findById(guardado.getId()).orElseThrow().getFecha());
    }

    @Test
    void put_inexistente_responde404() {
        // Act
        cliente.put().uri("/avistamientos/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .body(JSON_VALIDO)
                .exchange()
                // Assert
                .expectStatus().isNotFound();

        assertEquals(0, repositorio.count());
    }

    @Test
    void delete_existente_responde204YLuegoGetResponde404() {
        // Arrange
        Avistamiento guardado = guardar("Colibrí", "Jardín Botánico");

        // Act
        cliente.delete().uri("/avistamientos/{id}", guardado.getId())
                .exchange()
                // Assert
                .expectStatus().isNoContent();

        assertFalse(repositorio.existsById(guardado.getId()));

        cliente.get().uri("/avistamientos/{id}", guardado.getId())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void delete_inexistente_responde404() {
        // Act
        cliente.delete().uri("/avistamientos/{id}", 999)
                .exchange()
                // Assert
                .expectStatus().isNotFound();
    }
}