📌 ChâTop - Backend
Ce dépôt contient le code du backend de ChâTop, une application de mise en relation entre propriétaires et locataires pour la location immobilière. L'API est développée en Spring Boot et sécurisée avec JWT. Elle expose plusieurs endpoints documentés avec Swagger.

🚀 Installation et Lancement du Projet
1️⃣ Prérequis
Avant de commencer, assure-toi d'avoir installé :

Java 21 ou une version compatible
Maven (pour la gestion des dépendances)
Postman (pour tester l’API, facultatif)
MySQL(pour la base de données)
2️⃣ Cloner le projet
Exécute la commande suivante dans ton terminal :

git clone https://github.com/JelaliSameh/chatTop.git
cd chatTop
3️⃣ Configurer la base de données
 MySQL 
Si tu veux utiliser MySQL, suis ces étapes :

🔹 Installation MySQL
Si MySQL n'est pas encore installé, télécharge-le et installe-le :

Télécharger MySQL
🔹 Configuration MySQL
Ouvre MySQL et crée une nouvelle base de données :
CREATE DATABASE data;
Configure application.properties ou application.yml pour se connecter à MySQL :
Dans src/main/resources/application.properties :

properties

spring.datasource.url=jdbc:mysql://localhost:3306/chatop
spring.datasource.username=root  # Remplace par ton utilisateur
spring.datasource.password=root  # Remplace par ton mot de passe

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
Ou si tu utilises PostgreSQL, remplace la configuration :

properties
Copier
Modifier
spring.datasource.url=jdbc:postgresql://localhost:5432/chatop
spring.datasource.username=postgres
spring.datasource.password=admin
4️⃣ Lancer le projet
Exécute la commande suivante depuis la racine du projet :

mvn spring-boot:run

📝 Documentation API
L’API est documentée avec Swagger. Une fois le backend lancé, tu peux consulter la documentation en accédant à :

📌 Swagger UI :
http://localhost:3001/swagger-ui/index.html

📌 OpenAPI JSON :
http://localhost:3001/v3/api-docs

Tu peux utiliser Postman ou cURL pour tester les endpoints de l'API.

🛠️ Technologies Utilisées
Spring Boot (Backend)
Spring Security & JWT (Authentification)
Spring Data JPA (Accès aux données)
Swagger (Documentation API)
MySQL(Base de données)
🎯 Fonctionnalités Principales
✅ Gestion des utilisateurs (inscription, connexion avec JWT)
✅ Gestion des locations (CRUD des annonces immobilières)
✅ Messagerie entre utilisateurs
✅ Sécurisation des endpoints avec JWT

✨ Auteur
👩‍💻 Badri Sameh - Développeuse Full Stack

🚀 Prête à améliorer le projet ! N’hésite pas à me contacter pour toute question. 😃







