package com.example;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class App {
    public static void main(String[] args) throws Exception {
        String urlStr = "http://localhost:11434/api/generate";
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        String jsonInputString = "{\"model\": \"llama3\", \"prompt\": \"Proporcióname 2 palabras cotidianas en español que tengan más de 5 letras y que no terminen en 'mente', 'ente' o 'me' (por ejemplo, 'describirme', 'Inicialmente'). Solo dame las palabras, sin ninguna otra información adicional.\", \"stream\": false}";

        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);           
        }

        long startTime = System.currentTimeMillis();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        long endTime = System.currentTimeMillis();

        // Cerrar conexiones
        in.close();
        conn.disconnect();

        // Convertir la respuesta a un objeto JSON
        JSONObject jsonResponse = new JSONObject(content.toString());

        // Obtener el valor de la celda "response"
        String response = jsonResponse.getString("response");

        System.out.println("Respuesta: " + response);
        System.out.println("Tiempo de respuesta: " + (endTime - startTime) + " milisegundos");
    }
}