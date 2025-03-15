package com.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testLeerArchivo() {
        String contenidoEsperado = "{ \"juan.jose@urosario.edu.co\": [ { \"balance\": \"50\", \"type\": \"Deposit\", \"timestamp\": \"2025-02-11 14:17:21.921536\" } ] }";

        // Crear archivo de prueba
        String rutaArchivo = "test_transactions.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            writer.write(contenidoEsperado);
        } catch (Exception e) {
            fail("No se pudo crear el archivo de prueba.");
        }

        // Leer el archivo
        String contenidoLeido = App.leerArchivo(rutaArchivo);

        // Verificar que el contenido leído es igual al esperado
        assertNotNull(contenidoLeido, "El contenido no debe ser nulo.");
        assertEquals(contenidoEsperado, contenidoLeido, "El contenido del archivo no coincide.");

        // Eliminar archivo de prueba
        new File(rutaArchivo).delete();
    }

    @Test
    public void testObtenerTransacciones() {
        String jsonData = "{ \"juan.jose@urosario.edu.co\": [" +
                          "{\"balance\": \"50\", \"type\": \"Deposit\", \"timestamp\": \"2025-02-11 14:17:21.921536\"}," +
                          "{\"balance\": \"-20\", \"type\": \"Withdrawal\", \"timestamp\": \"2025-02-15 10:30:15.123456\"}" +
                          "]}";

        List<JSONObject> transacciones = App.obtenerTransacciones(jsonData, "juan.jose@urosario.edu.co");

        // Verificar que se han encontrado 2 transacciones para "juan.jose@urosario.edu.co"
        assertEquals(2, transacciones.size(), "Debe haber 2 transacciones para el usuario.");

        // Verificar detalles de la primera transacción
        JSONObject primeraTransaccion = transacciones.get(0);
        assertEquals("50", primeraTransaccion.getString("balance"), "El balance debe ser '50'.");
        assertEquals("Deposit", primeraTransaccion.getString("type"), "El tipo debe ser 'Deposit'.");
        assertEquals("2025-02-11 14:17:21.921536", primeraTransaccion.getString("timestamp"), "La fecha no coincide.");
    }

    @Test
    public void testGenerarExtracto() {
        String usuario = "juan.jose@urosario.edu.co";
        List<JSONObject> transacciones = List.of(
            new JSONObject().put("balance", "50").put("type", "Deposit").put("timestamp", "2025-02-11 14:17:21.921536"),
            new JSONObject().put("balance", "-20").put("type", "Withdrawal").put("timestamp", "2025-02-15 10:30:15.123456")
        );

        String nombreArchivo = "extracto_juan_jose_urosario_edu_co.txt";

        // Generar el extracto
        App.generarExtracto(usuario, transacciones);

        // Verificar que el archivo fue creado
        File archivo = new File(nombreArchivo);
        assertTrue(archivo.exists(), "El archivo de extracto no se generó correctamente.");

        // Leer contenido del archivo
        try {
            String contenido = new String(Files.readAllBytes(Paths.get(nombreArchivo)));
            assertNotNull(contenido, "El contenido del archivo no debe ser nulo.");
            assertTrue(contenido.contains("juan.jose@urosario.edu.co"), "El archivo debe contener el usuario.");
            assertTrue(contenido.contains("50"), "El archivo debe contener la transacción de 50.");
            assertTrue(contenido.contains("-20"), "El archivo debe contener la transacción de -20.");
        } catch (IOException e) {
            fail("Error al leer el archivo de extracto.");
        }

        // Eliminar archivo de prueba
        archivo.delete();
    }
}
