package base;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * @author jesusredondogarcia
 * Clase Sprite. Representa un elemento pintable y colisionable del juego.
 */
public class Sprite {
	
	private BufferedImage buffer;
	private Color color = Color.BLACK;
	//Variables de dimensión
	private int ancho;
	private int alto;
	//Variables de colocación
	private int posX;
	private int posY;
	//Variables para la velocidad
	private int velocidadX;
	private int velocidadY;
	private boolean powerUp = false;
	
	/**
	 * Constructor simple para un Sprite sin imagen y sin velocidad.
	 * @param ancho Ancho que ocupa el Sprite (en pixels)
	 * @param alto Altura que ocupa el Sprite (en pixels)
	 * @param posX posición horizontal del sprite en el mundo.
	 * @param posY posición vertical del Sprite en el mundo. El origen se sitúa en la parte superior.
	 * @param ruta es la ruta de la imagen a cargar
	 */
	public Sprite(int ancho, int alto, int posX, int posY, String ruta) {
		this.ancho = ancho;
		this.alto = alto;
		this.posX = posX;
		this.posY = posY;
		actualizarBuffer(ruta);
	}
	
	/**
	 * Constructor para un Sprite sin imagen.
	 * @param ancho Ancho que ocupa el Sprite (en pixels)
	 * @param alto Altura que ocupa el Sprite (en pixels)
	 * @param posX posición horizontal del sprite en el mundo.
	 * @param posY posición vertical del Sprite en el mundo. El origen se sitúa en la parte superior.
	 * @param velocidadX velocidad horizontal del Sprite.
	 * @param velocidadY velocidad vertical del Sprite. 
	 * @param ruta es la ruta de la imagen a cargar
	 */
	public Sprite(int ancho, int alto, int posX, int posY, int velocidadX, int velocidadY, String ruta) {
		this.ancho = ancho;
		this.alto = alto;
		this.posX = posX;
		this.posY = posY;
		this.velocidadX = velocidadX;
		this.velocidadY = velocidadY;
		actualizarBuffer(ruta);
	}
	
	/**
	 * Método para actualizar el buffer que guarda cada Sprite.
	 * Por ahora sólo guarda un bufferedImage que está completamente relleno de un color.
	 * @param ruta es la ruta de la imagen a cargar
	 */
	public void actualizarBuffer(String ruta){
		
		//Creo un nuevo buffer del tamaño adecuado
		buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buffer.getGraphics();
		
		//Intento pintarlo con una imagen
		Image imagenAuxiliar = null;
		try {
			imagenAuxiliar = ImageIO.read(new File(ruta));
			imagenAuxiliar = imagenAuxiliar.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
			g.drawImage(imagenAuxiliar, 0, 0, null);
			return;
		} catch (Exception e) {
			// Si no pinto un cuadrado del color por defecto
			g.setColor(color);
			g.fillRect(0, 0, ancho, alto);
			g.dispose();
		}		
	}
public void actualizarBuffer(String ruta, int anchoNuevo, int altoNuevo){
		
		//Creo un nuevo buffer del tamaño adecuado
		buffer = new BufferedImage(anchoNuevo, altoNuevo, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buffer.getGraphics();
		
		//Intento pintarlo con una imagen
		Image imagenAuxiliar = null;
		try {
			imagenAuxiliar = ImageIO.read(new File(ruta));
			imagenAuxiliar = imagenAuxiliar.getScaledInstance(anchoNuevo, altoNuevo, Image.SCALE_SMOOTH);
			g.drawImage(imagenAuxiliar, 0, 0, null);
			return;
		} catch (Exception e) {
			// Si no pinto un cuadrado del color por defecto
			g.setColor(color);
			g.fillRect(0, 0, ancho, alto);
			g.dispose();
		}		
	}
	/**
	 * Método que me permite comprobar si dos Sprites colisionan
	 * @param otroSprite
	 * @return
	 */
	public boolean colisionan(Sprite otroSprite){
		//Checkeamos si comparten algún espacio a lo ancho:
		boolean colisionAncho = false;
		if(posX<otroSprite.getPosX()){
			colisionAncho = ancho+posX > otroSprite.getPosX();
		}else{
			colisionAncho = otroSprite.getAncho()+otroSprite.getPosX() > posX;
		}
		
		//Checkeamos si comparten algún espacio a lo alto:
		boolean colisionAlto = false;
		if(posY<otroSprite.getPosY()){
			colisionAlto = alto > otroSprite.getPosY()-posY;
		}else{
			colisionAlto = otroSprite.getAlto() > posY-otroSprite.getPosY();
		}
		
		return colisionAncho&&colisionAlto;
	}	
	
	/**
	 * Método para mover el Sprite por el mundo.
	 * @param anchoMundo ancho del mundo sobre el que se mueve el Sprite
	 * @param altoMundo alto del mundo sobre el que se mueve el Sprite
	 */
	public void moverSprite(int anchoMundo, int altoMundo){
		if(posX >= anchoMundo - ancho) { //por la derecha
			velocidadX = -1  * Math.abs(velocidadX);
		} 
		if(posX <= 0){//por la izquierda
			velocidadX = Math.abs(velocidadX);;
		}
		if(posY >= altoMundo -alto){//por abajo
			velocidadY = -1  * Math.abs(velocidadY);
		}
		if(posY <= 0){ //Por arriba
			velocidadY = Math.abs(velocidadY);;
		}
		moverSprite();
	}
	/**
	 * Actualiza las posiciones teniendo en cuenta la velocidad.
	 */
	public void moverSprite(){
		posX = posX + velocidadX;
		posY = posY + velocidadY;
	}
	
	/**
	 * Método que pinta el Sprite en el mundo teniendo en cuenta las características propias del Sprite.
	 * @param g Es el Graphics del mundo que se utilizará para pintar el Sprite.
	 */
	public void pintarSpriteEnMundo(Graphics g){
		g.drawImage(buffer, posX, posY, null);
	}
	public void pintarSpriteEnMundo(Graphics g, int posX, int posY){
		g.drawImage(buffer, posX, posY, null);
	}
		
	//Métodos para obtener:
	public int getAncho(){
		return ancho;
	}
	
	public int getAlto(){
		return alto;
	}
	
	public int getPosX(){
		return posX;
	}
	
	public int getPosY(){
		return posY;
	}
	
	public BufferedImage getBuffer(){
		return buffer;
	}
	
	public int getVelocidadX(){
		return velocidadX;
	}
	
	public int getVelocidadY(){
		return velocidadY;
	}
	
	//métodos para cambiar:
	public void setAncho(int ancho){
		this.ancho = ancho;
	}
	
	public void setAlto(int alto){
		this.alto = alto;
	}
	
	public void setPosX(int posX){
		this.posX = posX;
	}
	
	public void setPosY(int posY){
		this.posY = posY;
	}
	
	public void setBuffer(BufferedImage buffer){
		this.buffer = buffer;
	}
	
	public void setVelocidadX(int velocidadX){
		this.velocidadX = velocidadX;
	}
	
	public void setVelocidadY(int velocidadY){
		this.velocidadY = velocidadY;
	}
	public void setColor(Color colorNuevo){
		this.color=colorNuevo;
		actualizarBuffer(null);
	}

	public boolean isPowerUp() {
		return powerUp;
	}

	public void setPowerUp(boolean powerUp) {
		this.powerUp = powerUp;
	}

	
	
}
