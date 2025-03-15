package com.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class App {

    // 🔹 1. Leer el archivo JSON desde un .txt
    public static String leerArchivo(String rutaArchivo) {
        try {
            return new String(Files.readAllBytes(Paths.get(rutaArchivo)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 🔹 2. Obtener transacciones de un usuario específico
    public static List<JSONObject> obtenerTransacciones(String jsonData, String usuario) {
        List<JSONObject> transaccionesUsuario = new ArrayList<>();
        try {
            JSONObject data = new JSONObject(jsonData);
            if (data.has(usuario)) {
                JSONArray transacciones = data.getJSONArray(usuario);
                for (int i = 0; i < transacciones.length(); i++) {
                    transaccionesUsuario.add(transacciones.getJSONObject(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return transaccionesUsuario;
    }

    // 🔹 3. Generar extracto bancario en un archivo .txt
    public static void generarExtracto(String usuario, List<JSONObject> transacciones) {
        String nombreArchivo = "extracto_" + usuario.replace("@", "_").replace(".", "_") + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            writer.write("📄 Extracto bancario de " + usuario + "\n\n");
            for (JSONObject transaccion : transacciones) {
                writer.write("📅 Fecha: " + transaccion.getString("timestamp") + "\n");
                writer.write("💳 Tipo: " + transaccion.getString("type") + "\n");
                writer.write("💰 Monto: " + transaccion.getString("balance") + "\n");
                writer.write("--------------------------\n");
            }
            System.out.println("✅ Extracto generado: " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el correo del usuario: ");
        String usuario = scanner.nextLine().trim();
        scanner.close();

        String jsonData = leerArchivo("transactions.txt");
        if (jsonData == null) {
            System.out.println("❌ Error al leer el archivo.");
            return;
        }

        List<JSONObject> transacciones = obtenerTransacciones(jsonData, usuario);
        if (transacciones.isEmpty()) {
            System.out.println("⚠️ No se encontraron transacciones para el usuario: " + usuario);
        } else {
            generarExtracto(usuario, transacciones);
        }
    }
}
