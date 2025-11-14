-- V2 : Données de référence / jeu de test PayMyBuddy

-- 1) Rendre la colonne assez large pour tout type de hash
ALTER TABLE `user`
  MODIFY `password` VARCHAR(100) NOT NULL;

-- 2) Création de quelques utilisateurs (les mots de passe sont hashés BCrypt)
INSERT INTO `user` (`username`, `email`, `password`)
VALUES
('luffy', 'luffy@paymybuddy.com', '$2a$10$yHsOxeKwv8iWZVz8DN0F9eB8h7TfWvN4x7LNkxUixYF8RL4Mni6KO'),
('zoro', 'zoro@paymybuddy.com', '$2a$10$7Yh1bG0Gh4RViB32MNHhtObqlcEtnwX0nuz53mFPyGp1xkYPReAaC'),
('nami', 'nami@paymybuddy.com', '$2a$10$r7ZqlkP5xDndxMK6RZFxEetVrA8oN1UeQdksEMHfy7OfZoW2eOBxe');

-- 3) Création de connexions (amis)
INSERT INTO `user_connection` (`user_id`, `connection_id`)
VALUES
(1, 2),
(1, 3),
(2, 1),
(3, 1);

-- 4) Création de transferts fictifs
INSERT INTO `transfer` (`sender_id`, `receiver_id`, `description`, `amount`)
VALUES
(1, 2, 'Déjeuner entre amis', 15.00),
(2, 1, 'Nouveau sabre', 22.50),
(3, 1, 'Café du matin', 3.20);
