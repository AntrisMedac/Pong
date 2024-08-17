import java.util.Formatter;
import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int pantallaAncho = 925;
        int pantallaAlto = 550;

        JFrame pantalla = new JFrame("Pong");

        pantalla.setVisible(true);
        pantalla.setSize(pantallaAlto, pantallaAncho);
        pantalla.setLocationRelativeTo(null);
        pantalla.setResizable(false);
        pantalla.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Pong Pong = new Pong(pantallaAlto, pantallaAncho);
        pantalla.add(Pong);
        pantalla.pack();
        Pong.requestFocus();
    }
}
