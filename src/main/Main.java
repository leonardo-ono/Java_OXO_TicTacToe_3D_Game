package main;

import br.ol.oxo.infra.Engine;
import br.ol.oxo.OxoScene;
import br.ol.oxo.infra.Scene;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Main class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Main {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Scene scene = new OxoScene();
                Engine engine = new Engine(scene);

                JFrame view = new JFrame();

                // hide mouse cursor
                view.setCursor(view.getToolkit().createCustomCursor(
                    new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB)
                    , new Point(0, 0), "null"));
                
                view.setTitle("OXO");
                view.setSize(800, 600);
                view.setLocationRelativeTo(null);
                view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                view.getContentPane().add(engine);
                view.setVisible(true);

                engine.requestFocus();
                try {        
                    engine.init();
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(-1);
                }
            }
        });
    }
    
}
