-- V4 : Nouveau jeu de données de référence pour PayMyBuddy

-- 1) Vider les tables
DELETE FROM `transfer`;
DELETE FROM `user_connection`;
DELETE FROM `user`;

-- 2) Réinitialiser les auto-incréments
ALTER TABLE `user` AUTO_INCREMENT = 1;
ALTER TABLE `transfer` AUTO_INCREMENT = 1;

-- 3) Création de 6 utilisateurs
INSERT INTO `user` (`username`, `email`, `password`) VALUES
('anatole',   'anatole@paymybuddy.com',   '$2a$10$yHsOxeKwv8iWZVz8DN0F9eB8h7TfwvN4x7LNkxUixYF8RL4Mni6K0'),
('berthe',     'berthe@paymybuddy.com',     '$2a$10$7Yh1bG0Gh4RViB32MNHht0bqlcEtnwX0nuz53mFPyGp1xkYPReAaC'),
('célestine', 'célestine@paymybuddy.com', '$2a$10$r7ZqlkP5xDndxMK6RZFxEetVrA8oN1UeQdksEMHfy70fZoW2e0Bxe'),
('désiré',   'désiré@paymybuddy.com',   '$2a$10$yHsOxeKwv8iWZVz8DN0F9eB8h7TfwvN4x7LNkxUixYF8RL4Mni6K0'),
('eugène',    'eugène@paymybuddy.com',    '$2a$10$7Yh1bG0Gh4RViB32MNHht0bqlcEtnwX0nuz53mFPyGp1xkYPReAaC'),
('françois',    'françois@paymybuddy.com',    '$2a$10$r7ZqlkP5xDndxMK6RZFxEetVrA8oN1UeQdksEMHfy70fZoW2e0Bxe');

-- 4) Connexions (amis) :
INSERT INTO `user_connection` (`user_id`, `connection_id`) VALUES
(1, 2), -- anatole <-> berthe
(2, 1),
(1, 3), -- anatole <-> célestine
(3, 1),
(2, 4), -- berthe <-> désiré
(4, 2),
(3, 5), -- célestine <-> eugène
(5, 3),
(4, 6), -- désiré <-> françois
(6, 4),
(5, 6), -- eugène <-> françois
(6, 5);

-- 5) Transferts crédibles de la vie quotidienne
INSERT INTO `transfer` (`sender_id`, `receiver_id`, `description`, `amount`) VALUES
(1, 2, 'Courses du week-end',       42.30),  -- anatole rembourse berthe
(2, 1, 'Billets de cinéma',         18.00),  -- berthe rembourse anatole
(3, 1, 'Uber retour soirée',        12.50),  -- célestine rembourse anatole
(4, 2, 'Restaurant',                27.90),  -- désiré rembourse berthe
(5, 3, 'Cadeau commun anniversaire',15.00),  -- eugène rembourse célestine
(6, 4, 'Essence',                   30.00),  -- françois rembourse désiré
(2, 5, 'Abonnement Netflix partagé', 5.99),  -- berthe rembourse eugène
(3, 6, 'Places de concert',         45.00),  -- célestine rembourse françois
(5, 1, 'Bowling',                   10.00),  -- eugène rembourse anatole
(6, 2, 'Pizzas entre amis',         19.50);  -- françois rembourse berthe