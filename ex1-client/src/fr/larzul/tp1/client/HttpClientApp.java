package fr.larzul.tp1.client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpClientApp {

    public static void main(String[] args) throws IOException {
        // modifier les valeurs pour tester
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 8080;
        String path = args.length > 2 ? args[2] : "/index.html";

        try (Socket socket = new Socket(host, port)) {
            // 1) on envoie une requête HTTP GET
            OutputStream out = socket.getOutputStream();
            String request =
                    "GET " + path + " HTTP/1.0\r\n" +
                            "Host: " + host + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n";
            out.write(request.getBytes(StandardCharsets.UTF_8));
            out.flush();

            // 2) on lit la réponse
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            // on lit la status line: "HTTP/1.0 200 OK"
            String statusLine = in.readLine();
            if (statusLine == null) {
                System.out.println("Réponse vide.");
                return;
            }

            System.out.println("Status: " + statusLine);

            // on extrait le code (200, 401, 404, ...)
            int statusCode = parseStatusCode(statusLine);

            // ont lit les headers jusqu'à la ligne vide
            String line;
            while ((line = in.readLine()) != null && !line.isBlank()) {
            }

            // 3) on lit le body (HTML) et on l'affiche
            StringBuilder body = new StringBuilder();
            while ((line = in.readLine()) != null) {
                body.append(line).append("\n");
            }

            System.out.println(body);

            // 4) Si on a un code d'erreur, on affiche un message
            if (statusCode == 401) {
                System.out.println("Le serveur indique que le fichier n'existe pas (401).");
            } else if (statusCode >= 400) {
                System.out.println("Erreur HTTP : " + statusCode);
            }
        }
    }

    private static int parseStatusCode(String statusLine) {
        String[] parts = statusLine.split(" ");
        if (parts.length >= 2) {
            try {
                return Integer.parseInt(parts[1]);
            } catch (NumberFormatException ignored) {}
        }
        return -1;
    }
}