
package com.example;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class PeticionIA {
    public String[] getResponse() throws Exception {
        String urlStr = "http://localhost:11434/api/generate";
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        String jsonInputString = "{\"model\": \"llama3\", \"prompt\": \"Proporcióname 2 palabras cotidianas en español que tengan más de 5 letras y que no terminen en 'mente', 'ente' o 'me' (por ejemplo, 'describirme' o 'inicialmente'). Solo dame las palabras, sin ninguna otra información adicional de tu parte, y preséntalas en esta estructura de respuesta: Palabra1, Palabra2. No incluyas ninguna otra palabra en tu respuesta.\", \"stream\": false}";

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        // Cerrar conexiones
        in.close();
        conn.disconnect();

        // Convertir la respuesta a un objeto JSON
        JSONObject jsonResponse = new JSONObject(content.toString());

        // Obtener el valor de la celda "response"
        String response = jsonResponse.getString("response");

        // Dividir la respuesta por la coma y guardar las palabras en un arreglo
        String[] words = response.split(", ");

        return words;
    }

    public static void main(String[] args) {
        try {
            PeticionIA peticion = new PeticionIA();
            String[] response = peticion.getResponse();
            for (String word : response) {
                System.out.println(word);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}