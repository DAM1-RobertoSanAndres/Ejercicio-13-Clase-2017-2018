package pantallas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import base.PanelJuego;
import base.Sprite;

public class PantallaJuego implements Pantalla {

	private static final int ANCHO_ASTEROIDE = 40;
	private static final int ANCHO_NAVE = 45;
	private static final int ALTO_NAVE = 40;
	private static final int ANCHO_DISPARO = 16;
	private static final int ALTO_DISPARO = 40;
	static Graphics gg;

	// Array que almacena todos los cuadrados que se moverán por pantalla.
	ArrayList<Sprite> asteroides;
	Image fondo = null;
	Image fondoEscalado = null;
	Sprite nave;
	Sprite disparo = null;
	// Variables para el contador de tiempo
	double tiempoInicial;
	double tiempoDeJuego;
	Font fuenteTiempo;
	private DecimalFormat formatoDecimal;

	// Variable para el panel:
	PanelJuego panelJuego;

	public PantallaJuego(PanelJuego panelJuego) {
		this.panelJuego = panelJuego;
		inicializarPantalla();
		redimensionarPantalla();
	}

	@Override
	public void inicializarPantalla() {
		int asteroideConPower;
		
		asteroides = new ArrayList<Sprite>();
		// Inicializo el array con los 6 asteroides random:
		Random rd = new Random();
		for (int i = 0; i < 3; i++) {
			Sprite creador;
			int posX = 10;
			int poxY = 10;
			int velocidadX = rd.nextInt(10) + 1;
			int velocidadY = rd.nextInt(10) + 1;
			creador = new Sprite(ANCHO_ASTEROIDE, ANCHO_ASTEROIDE, posX, poxY, velocidadX, velocidadY,
					"Imagenes/asteroide.png");
			asteroides.add(creador);
				
		}
			asteroideConPower = rd.nextInt(asteroides.size())+1;
		
		asteroides.get(asteroideConPower).setPowerUp(true);
		
		// Cargo la imagen de fondo de pantalla
		try {
			fondo = ImageIO.read(new File("Imagenes/galaxia.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Inicializo la nave:
		// Inicialmente la pinto fuera.
		nave = new Sprite(ANCHO_NAVE, ALTO_NAVE, -100, -100, "Imagenes/nave.png");

		// El tiempo de juego es cero
		tiempoDeJuego = 0;

		// La letra para pintar el tiempo
		fuenteTiempo = new Font("MiLetra", Font.BOLD, 20);

		// Formato decimal
		formatoDecimal = new DecimalFormat("#.##");
		tiempoInicial = System.nanoTime();
	}

	@Override
	public void renderizarPantalla(Graphics g) {
		gg=g;
		
		rellenarFondo(g);
		// Pintamos los cuadrados:
		for (Sprite cuadrado : asteroides) {
			cuadrado.pintarSpriteEnMundo(g);
		}
		if (disparo != null) {
			disparo.pintarSpriteEnMundo(g);
		}
		
		// Pintamos la nave
		nave.pintarSpriteEnMundo(g);

		// Pintamos el contador de tiempo:
		g.setColor(Color.WHITE);
		g.setFont(fuenteTiempo);
		g.drawString(formatoDecimal.format(tiempoDeJuego / 1000000000), 25, 25);
	}

	@Override
	public void ejecutarFrame() {
		panelJuego.repaint();
		try {
			Thread.sleep(25);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		colisiones();
		moverSprites();
		actualizarTiempo();
		if (asteroides.isEmpty()) {
			panelJuego.setPantallaActual(new PantallaVictoria(panelJuego, tiempoDeJuego));
		}
	}

	@Override
	public void moverRaton(MouseEvent e) {
		nave.setPosX(e.getX() - ANCHO_NAVE / 2);
		nave.setPosY(e.getY() - ALTO_NAVE / 2);
	}

	@Override
	public void pulsarRaton(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (disparo == null) { // No tengo disparo, lo creo:
				disparo = new Sprite(ANCHO_DISPARO, ALTO_DISPARO, e.getX() - ANCHO_DISPARO / 2,
						e.getY() - ALTO_DISPARO / 2, 0, -10, null);
				disparo.setColor(Color.GREEN);
			}
		}
	}

	@Override
	public void redimensionarPantalla() {
		fondoEscalado = fondo.getScaledInstance(panelJuego.getWidth(), panelJuego.getHeight(), Image.SCALE_SMOOTH);
	}

	/**
	 * Método que se utiliza para rellenar el fondo del JPanel.
	 * 
	 * @param g
	 *            Es el gráficos sobre el que vamos a pintar el fondo.
	 */
	private void rellenarFondo(Graphics g) {
		g.drawImage(fondoEscalado, 0, 0, null);
	}

	/**
	 * Método para mover todos los Sprites del juego.
	 */
	private void moverSprites() {
		for (int i = 0; i < asteroides.size(); i++) {
			Sprite aux = asteroides.get(i);
			aux.moverSprite(panelJuego.getWidth(), panelJuego.getHeight());
		}
		if (disparo != null) {
			disparo.moverSprite();
			// Oye, que esto se va de la pantalla!
			if (disparo.getPosY() + disparo.getAlto() < 0) {
				disparo = null;
			}
		}
	}

	/**
	 * Método que actualiza el tiempo que ha transcurrido de juego
	 */
	public void actualizarTiempo() {
		float tiempoActual = System.nanoTime(); // <--Aquí se mide el nuevo tiempo. En esta precisa instrucción.
		tiempoDeJuego = tiempoActual - tiempoInicial;
	}

	/**
	 * Comprueba si hay colisiones en nuestro juego y actúa en consecuencia:
	 */
	public void colisiones() {
		// El disparo con los asteroides:
		Random rd = new Random();
		for (int i = 0; i < asteroides.size() && disparo != null; i++) {
			if (asteroides.get(i).colisionan(disparo)) {
				disparo = null;
				if(asteroides.get(i).getAncho() == ANCHO_ASTEROIDE) {
					for (int j = 0; j < 2; j++) {
						int velocidadX = rd.nextInt(10) + 1;
						int velocidadY = rd.nextInt(10) + 1;
						Sprite mitad =  new Sprite(asteroides.get(i).getAlto()/2, asteroides.get(i).getAncho()/2, asteroides.get(i).getPosY(), asteroides.get(i).getPosX(),velocidadX, velocidadX,
								"Imagenes/asteroide.png");
						asteroides.add(mitad);
						mitad.pintarSpriteEnMundo(gg, mitad.getPosX(), mitad.getPosY());
					}
					if(asteroides.get(i).isPowerUp()) {
						for (int j = 0; j < asteroides.size(); j++) {
							asteroides.get(j).setVelocidadX(asteroides.get(j).getVelocidadX()/2);
							asteroides.get(j).setAceleracionY(asteroides.get(j).getVelocidadY()/2);
						}
					}
					}
				
				asteroides.remove(i);
			}
		}
	}
}
