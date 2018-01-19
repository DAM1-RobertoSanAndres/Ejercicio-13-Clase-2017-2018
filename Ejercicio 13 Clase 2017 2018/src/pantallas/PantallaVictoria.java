package pantallas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import base.PanelJuego;

public class PantallaVictoria implements Pantalla {

	Image fondo = null;
	Image fondoEscalado = null;
	PanelJuego panelJuego;
	Color colorLetra = Color.YELLOW;
	double tiempoDeJuego;
	private DecimalFormat formatoDecimal= new DecimalFormat("#.##");;
	private Font fuenteTiempo= new Font("MiLetra", Font.PLAIN, 25);
	private Font fueteVictoria = new Font("Arial", Font.ITALIC/Font.BOLD, 40);

	public PantallaVictoria(PanelJuego panelJuego, double tiempoDeJuego) {
		this.panelJuego = panelJuego;
		this.tiempoDeJuego = tiempoDeJuego;
		inicializarPantalla();
		redimensionarPantalla();
	}

	@Override
	public void inicializarPantalla() {
		try {
			fondo = ImageIO.read(new File("Imagenes/fondoVictoria.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void renderizarPantalla(Graphics g) {
		rellenarFondo(g);		
		g.setColor(colorLetra);
		g.setFont(fueteVictoria);
		g.drawString("VICTORIA", panelJuego.getWidth() / 2+70, panelJuego.getHeight() / 2+100);
		g.setColor(Color.WHITE);
		g.setFont(fuenteTiempo);
		g.drawString(formatoDecimal.format(tiempoDeJuego / 1000000000), panelJuego.getWidth() / 2+ 140, panelJuego.getHeight() / 2+150);
	}

	@Override
	public void ejecutarFrame() {
		panelJuego.repaint();
	}

	@Override
	public void moverRaton(MouseEvent e) {

	}

	@Override
	public void pulsarRaton(MouseEvent e) {
		panelJuego.setPantallaActual(new PantallaInicial(panelJuego));
	}

	@Override
	public void redimensionarPantalla() {
		fondoEscalado = fondo.getScaledInstance(panelJuego.getWidth(), panelJuego.getHeight(), Image.SCALE_SMOOTH);
	}

	private void rellenarFondo(Graphics g) {
		g.drawImage(fondoEscalado, 0, 0, null);
	}
}
