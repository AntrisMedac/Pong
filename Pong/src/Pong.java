import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class Pong extends JPanel implements ActionListener, KeyListener{
    private class Casilla {
        int x;
        int y;

        Casilla(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int pantallaAncho;
    int pantallaAlto;
    int tamañoCasilla = 25;

    private class Barra{
        int x;
        int y;
        int ancho = tamañoCasilla;
        int altura = tamañoCasilla;

        Barra(int x, int y, int ancho, int altura){
            this.x = x;
            this.y = y;
            this.ancho = ancho;
            this.altura = altura;
        }
    }

    Barra barraIzq;
    Barra barraDer;
    Casilla bola;

    int velocidadIzqY;
    int velocidadDerY;
    int velocidadBolaY;
    int velocidadBolaX;

    int scoreRojo;
    int scoreAzul;
    boolean puntuar;

    Timer gameLoop;

    boolean empezar;
    boolean continuar;

    Random random;
    int salidaBola;

    Pong(int pantallaAlto, int pantallaAncho){
        this.pantallaAncho = pantallaAncho;
        this.pantallaAlto = pantallaAlto;
        setPreferredSize(new Dimension(this.pantallaAncho, this.pantallaAlto));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        bola = new Casilla(18, 10);
        barraIzq = new Barra(2, 7,1,8);
        barraDer = new Barra(35, 7,1,8);

        velocidadIzqY = 0;
        velocidadDerY = 0;

        scoreRojo = 0;
        scoreAzul = 0;
        puntuar = false;

        empezar = false;
        continuar = false;

        random = new Random();

        gameLoop = new Timer(1000/30, this); //how long it takes to start timer, milliseconds gone between frames 
        gameLoop.start();
    }

    public void rejugar(){
        // Reiniciar posiciones de la bola y las barras
        bola = new Casilla(pantallaAncho / 2 / tamañoCasilla, pantallaAlto / 2 / tamañoCasilla); // Bola al centro
        barraIzq = new Barra(2, pantallaAlto / 2 / tamañoCasilla - 4, 1, 8); // Reposicionar barra izquierda
        barraDer = new Barra(pantallaAncho / tamañoCasilla - 3, pantallaAlto / 2 / tamañoCasilla - 4, 1, 8); // Reposicionar barra derecha
    
        // Reiniciar velocidades de la bola
        velocidadBolaX = random.nextBoolean() ? 1 : -1;
        velocidadBolaY = random.nextBoolean() ? 1 : -1;
    
        // Reiniciar estado de puntuación
        puntuar = false;
    
        // Reiniciar el temporizador
        gameLoop.start();
    
        // Volver a habilitar el foco para recibir eventos de teclado
        requestFocusInWindow();
    }
    

    public void draw(Graphics g) {
        //Bola
        g.setColor(Color.WHITE);
        g.drawOval(bola.x*tamañoCasilla, bola.y*tamañoCasilla, tamañoCasilla, tamañoCasilla);

        //Barra izq
        g.setColor(Color.red);
        g.fill3DRect(barraIzq.x*tamañoCasilla, barraIzq.y*tamañoCasilla, tamañoCasilla, 8*tamañoCasilla, true);

        //Barra der
        g.setColor(Color.blue);
        g.fill3DRect(barraDer.x*tamañoCasilla, barraDer.y*tamañoCasilla, tamañoCasilla, 8*tamañoCasilla, true);

        if (!empezar) {
            g.setFont(new Font("Arial", Font.PLAIN, 22));
            g.setColor(Color.WHITE);
            g.drawString("Pulsa espacio para comenzar", tamañoCasilla * 13, tamañoCasilla * 18);
        }
        else{
            velocidadBolaX = random.nextBoolean() ? 1 : -1;
            velocidadBolaY = random.nextBoolean() ? 1 : -1;
            empezar = false;
            repaint();
        }

        //Mensaje para seguir jugando
        if (puntuar) {
            // g.setFont(new Font("Arial", Font.PLAIN, 22));
            // g.setColor(Color.WHITE);
            // g.drawString("Para continuar pulsa espacio", tamañoCasilla * 13, tamañoCasilla * 18);
            //Score
            g.setFont(new Font("Arial", Font.PLAIN, 32));
            g.setColor(Color.WHITE);
            g.drawString(scoreRojo + " : " + scoreAzul, tamañoCasilla * 17, tamañoCasilla * 2);
        }
        //Score
        else {
            g.setFont(new Font("Arial", Font.PLAIN, 32));
            g.setColor(Color.WHITE);
            g.drawString(scoreRojo + " : " + scoreAzul, tamañoCasilla * 17, tamañoCasilla * 2);
        }
    }

    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

    public void mover(){
        colisiones(barraIzq, barraDer, bola);
        barraIzq.y += velocidadIzqY;
        barraDer.y += velocidadDerY;
        bola.x += velocidadBolaX;
        bola.y += velocidadBolaY;
    }

    public void colisiones(Barra barraIzq, Barra barraDer, Casilla bola) {
        // Colisión con la barra izquierda
        if (bola.x == barraIzq.x + 1 && bola.y >= barraIzq.y && bola.y < barraIzq.y + barraIzq.altura) {
            velocidadBolaX = 1; // Rebotar hacia la derecha
        }
        // Colisión con la barra derecha
        if (bola.x == barraDer.x - 1 && bola.y >= barraDer.y && bola.y < barraDer.y + barraDer.altura) {
            velocidadBolaX = -1; // Rebotar hacia la izquierda
        }
        if (bola.y == 0) {
            velocidadBolaY += 1;
        }
        if (bola.y >= pantallaAlto/tamañoCasilla -1) {
            velocidadBolaY += -1;
        }
        if (bola.x == 0) {
            scoreAzul ++;
            puntuar = true;
            gameLoop.stop();
        }
        if (bola.x >= pantallaAncho/tamañoCasilla -1) {
            scoreRojo ++;
            puntuar = true;
            gameLoop.stop();
        }
        if (barraIzq.y == -1) {
            velocidadIzqY = 1;
        }
        if (barraIzq.y >= pantallaAlto/tamañoCasilla -7) {
            velocidadIzqY = -1;
        }
        if (barraDer.y == -1) {
            velocidadDerY = 1;
        }
        if (barraDer.y >= pantallaAlto/tamañoCasilla -7) {
            velocidadDerY = -1;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W){
            velocidadIzqY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_S){
            velocidadIzqY = 1;
        }
    
        if (e.getKeyCode() == KeyEvent.VK_UP){
            velocidadDerY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN){
            velocidadDerY = 1;
        }
    
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !empezar) {
            empezar = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE && puntuar) {
            rejugar();
        }
    }
    

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W){
            velocidadIzqY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_S){
            velocidadIzqY = 0;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP){
            velocidadDerY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN){
            velocidadDerY = 0;
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE && puntuar) {
            continuar = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mover();
        repaint();
        if (puntuar) {
            // Esperar a que se presione la tecla de espacio para reiniciar
            requestFocusInWindow();
        }
    }
    

    //No se usa
    @Override
    public void keyTyped(KeyEvent e) {
    }
}