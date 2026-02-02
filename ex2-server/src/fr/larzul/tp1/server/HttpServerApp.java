package fr.larzul.tp1.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class HttpServerApp {

    private static final int PORT = 8080; // port à utiliser pour se connecter
    private static final Path WWW_ROOT = Path.of("ex2-server", "www"); // définit dossier racine du serveur web

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur HTTP en écoute sur http://localhost:" + PORT);

            while (true) {
                try (Socket client = serverSocket.accept()) {
                    handleClient(client);
                } catch (Exception e) {
                    System.out.println("Erreur client: " + e.getMessage());
                }
            }
        }
    }

    private static void handleClient(Socket client) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
        OutputStream out = client.getOutputStream();

        // 1) on lit la première ligne: "GET /index.html HTTP/1.1"
        String requestLine = in.readLine();
        if (requestLine == null || requestLine.isBlank()) return;

        System.out.println("Requête: " + requestLine);

        // 2) on ignore le reste des headers jusqu'à la ligne vide (on s'en fiche du reste)
        String line;
        while ((line = in.readLine()) != null && !line.isBlank()) {}

        // 3) on parse : méthode + chemin
        String[] parts = requestLine.split(" ");
        String method = parts[0];
        String path = parts.length > 1 ? parts[1] : "/";

        if (!method.equals("GET")) {
            sendSimpleResponse(out, 405, "Method Not Allowed", "<h1>405 Method Not Allowed</h1>");
            return;
        }

        if (path.equals("/")) path = "/index.html";

        Path filePath = WWW_ROOT.resolve(path.substring(1)); // enlève le "/" au début

        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            byte[] body = Files.readAllBytes(filePath);
            sendResponse(out, 200, "OK", "text/html; charset=utf-8", body);
        } else {
            Path notFound = WWW_ROOT.resolve("404.html");
            byte[] body = Files.exists(notFound)
                    ? Files.readAllBytes(notFound)
                    : "<h1>Fichier introuvable</h1>".getBytes(StandardCharsets.UTF_8);

            sendResponse(out, 401, "Not Found", "text/html; charset=utf-8", body);
        }
    }

    private static void sendSimpleResponse(OutputStream out, int code, String msg, String html) throws IOException {
        byte[] body = html.getBytes(StandardCharsets.UTF_8);
        sendResponse(out, code, msg, "text/html; charset=utf-8", body);
    }

    private static void sendResponse(OutputStream out, int code, String msg, String contentType, byte[] body) throws IOException {
        String headers =
                "HTTP/1.0 " + code + " " + msg + "\r\n" +
                        "Content-Type: " + contentType + "\r\n" +
                        "Content-Length: " + body.length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n";

        out.write(headers.getBytes(StandardCharsets.UTF_8));
        out.write(body);
        out.flush();
    }
}