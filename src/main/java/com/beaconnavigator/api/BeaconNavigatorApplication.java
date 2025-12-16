package com.beaconnavigator.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.*;
import java.net.URI;

@SpringBootApplication
public class BeaconNavigatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeaconNavigatorApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void abrirNavegadorAposInicio() {
		String url = "http://localhost:8080/swagger-ui/index.html"; // URL padrão do Swagger
		System.out.println("🚀 Aplicação iniciada! Tentando abrir o Swagger em: " + url);

		try {
			// Verifica se o ambiente suporta operação de Desktop (Windows/Mac/Linux com
			// interface)
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Desktop.Action.BROWSE)) {
					desktop.browse(new URI(url));
				}
			} else {
				// Fallback para ambientes onde Desktop não é suportado (ex: alguns Linux)
				Runtime runtime = Runtime.getRuntime();
				String os = System.getProperty("os.name").toLowerCase();
				if (os.contains("win")) {
					runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
				} else if (os.contains("mac")) {
					runtime.exec("open " + url);
				} else if (os.contains("nix") || os.contains("nux")) {
					runtime.exec("xdg-open " + url);
				}
			}
		} catch (Exception e) {
			// Se falhar (ex: rodando em servidor headless), apenas ignora e segue a vida
			System.err.println("⚠️ Não foi possível abrir o navegador automaticamente: " + e.getMessage());
		}
	}
}