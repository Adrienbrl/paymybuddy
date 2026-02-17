# PayMyBuddy

Application web Java (Spring Boot) de gestion de paiements entre utilisateurs.

## Stack technique
- Java 21
- Spring Boot (Web, Thymeleaf, Data JPA, Validation, Security)
- MySQL
- Flyway (migrations SQL)

## Modèle Physique de Données (MPD)

Le schéma relationnel repose sur 3 tables principales :
- `user` : utilisateurs de l'application,
- `user_connection` : relations entre utilisateurs,
- `transfer` : transactions entre un expéditeur et un destinataire.