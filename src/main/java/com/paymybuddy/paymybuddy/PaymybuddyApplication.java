package com.paymybuddy.paymybuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée de l'application Spring Boot PayMyBuddy.
 */
@SpringBootApplication
public class PaymybuddyApplication {

	/**
	 * Démarre le contexte Spring et expose l'application web.
	 *
	 * @param args arguments de démarrage.
	 */
	public static void main(String[] args) {
		SpringApplication.run(PaymybuddyApplication.class, args);
	}

}
