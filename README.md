# TP1 – HTTP Client / Server (Java)

Projet réalisé dans le cadre du cours **Cloud Computing** à l’**UQAC**.  
L’objectif de ce TP est d’implémenter un **serveur HTTP minimal** et un **client HTTP simple** en Java, en utilisant des **sockets TCP**, sans bibliothèque HTTP externe.

---

## 📁 Structure du projet

- **ex2-server**  
  Serveur HTTP minimal en Java.  
  Il écoute sur un port donné, traite des requêtes HTTP `GET` et renvoie des pages HTML (depuis le dossier `www/`).

- **ex1-client**  
  Client HTTP simple en Java.  
  Il se connecte à un serveur HTTP, envoie une requête `GET` et affiche le contenu HTML reçu sur la sortie standard.

---

## 🚀 Lancement du serveur (Exercice 2)

1. Ouvrir le projet dans **IntelliJ** ou **Eclipse**.
2. Lancer la classe : `HttpServerApp`
3. Le serveur écoute par défaut sur le port **8080**.

### Accès via navigateur

- Page existante :
    - `http://localhost:8080/` (sert `index.html`)
- Page inexistante :
    - `http://localhost:8080/x.html` (renvoie une page d’erreur)

### Comportement attendu

- Le serveur renvoie `index.html` si le fichier existe dans `ex2-server/www/`
- Sinon, il renvoie une page d’erreur HTML avec un code **401** (conformément à l’énoncé)

---

## 🌐 Utilisation du client HTTP (Exercice 1)

1. Lancer la classe : `HttpClientApp`

### Arguments possibles

Le programme accepte (optionnellement) :
- `<host> <port> <path>`

Si aucun argument n’est fourni, le client utilise des valeurs par défaut (ex. `localhost`, `8080`, `/index.html` ou `/x.html` selon ta config).

### Exemples (serveur local)

- Page existante :
    - `localhost 8080 /index.html`
- Page inexistante :
    - `localhost 8080 /x.html`

Le client :
- se connecte en TCP au serveur
- envoie une requête HTTP `GET`
- affiche la *status line* (code HTTP)
- affiche le contenu HTML reçu sur la sortie standard

### Exemples (serveur Web existant)

- `example.com 80 /`
- `example.com 80 /x.html`

> Remarque : certains sites (ex. Google/Amazon) redirigent vers HTTPS et peuvent répondre par des codes 301/302, ce qui est normal.

---

## 🧪 Tests réalisés

- Serveur local : `localhost:8080`
- Serveur HTTP existant : `example.com:80`
- Cas fichier existant : `200 OK`
- Cas fichier inexistant : `401` (serveur local) / `404` ou autre (serveur public)

---

## 🛠️ Technologies utilisées

- Java
- TCP Sockets (`Socket`, `ServerSocket`)
- HTTP simulé manuellement (requêtes/réponses construites à la main)

---

## 👤 Auteur

Julien Larzul