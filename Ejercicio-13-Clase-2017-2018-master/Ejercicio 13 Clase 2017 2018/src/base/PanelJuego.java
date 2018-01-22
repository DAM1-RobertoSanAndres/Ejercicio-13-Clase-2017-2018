package base;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import pantallas.Pantalla;
import pantallas.PantallaInicial;

/**
 * 
 * @author jesusredondogarcia Clase PanelJuego. Controla los gráficos del
 *         Juego. Por ahora también controla la lógica del Juego. Extiende de
 *         JPanel. Todos los gráficos se gestionan mediante los gráficos de un
 *         JPanel. Implementa Runnable porque en el constructor se lanza un hilo
 *         que permite actualizar el Juego periódicamente. Implementa
 *         MouseListener para que pueda capturar las pulsaciones del ratón.
 */
public class PanelJuego extends JPanel implements Runnable, MouseListener, MouseMotionListener, ComponentListener {

	private static final long serialVersionUID = 1L;

	Pantalla pantallaActual;
	Font fueteGrande = new Font("Arial", Font.PLAIN, 30);

	/**
	 * Constructor de PanelJuego. - Inicializa el arrayList de cuadrados. - Asigna
	 * el mouse listener que implementa la propia clase. - Inicia un hilo para
	 * actualizar el juego periódicamente.
	 */
	public PanelJuego() {

		pantallaActual = new PantallaInicial(this);

		// MouseMotion
		this.addMouseMotionListener(this);
		// MouseListener:
		this.addMouseListener(this);
		// ComponentListener
		this.addComponentListener(this);
		// Lanzo el hilo.
		new Thread(this).start();
	}

	/**
	 * Sobreescritura del método paintComponent. Este método se llama
	 * automáticamente cuando se inicia el componente, se redimensiona o bien
	 * cuando se llama al método "repaint()". Nunca llamarlo directamente.
	 * 
	 * @param g
	 *            Es un Graphics que nos proveé JPanel para poner pintar el
	 *            componente a nuestro antojo.
	 */
	@Override
	public void paintComponent(Graphics g) {
		pantallaActual.renderizarPantalla(g);
	}

	@Override
	public void run() {
		while (true) {
			pantallaActual.ejecutarFrame();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		pantallaActual.pulsarRaton(e);
		//pantallaActual.moverRaton(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		pantallaActual.moverRaton(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		pantallaActual.moverRaton(e);
	}

	@Override
	public void componentResized(ComponentEvent e) {
		pantallaActual.redimensionarPantalla();
	}

	@Override
	public void componentMoved(ComponentEvent e) {

	}

	@Override
	public void componentShown(ComponentEvent e) {

	}

	@Override
	public void componentHidden(ComponentEvent e) {

	}

	public Pantalla getPantallaActual() {
		return pantallaActual;
	}

	public void setPantallaActual(Pantalla pantallaActual) {
		this.pantallaActual = pantallaActual;
	}

	public Font getFuenteGrande() {
		return fueteGrande;
	}

}