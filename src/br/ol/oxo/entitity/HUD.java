package br.ol.oxo.entitity;

import br.ol.oxo.OxoEntity;
import br.ol.oxo.OxoScene;
import br.ol.oxo.infra.Engine;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import renderer.core.Renderer;

/**
 * Title class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class HUD extends OxoEntity {
    
    private Font font = new Font("Arial", Font.PLAIN, 20);
    private boolean p1Visible, p2Visible;
    
    public HUD(String name, OxoScene scene) {
        super(name, scene);
    }

    @Override
    public void draw(Renderer renderer, Graphics2D g) {
        if (!visible) {
            return;
        }
        
        if (p1Visible) {
            int startX = 20;
            scene.drawText(g, font, "1P", startX, 50, Color.RED);
        }

        if (p2Visible) {
            int startX = Engine.SCREEN_WIDTH - scene.getTextWidth(g, font, "CPU") - 20;
            scene.drawText(g, font, "CPU", startX, 50, Color.BLUE);
        }
    }

    @Override
    public void updatePlaying1(Renderer renderer) {
        p1Visible = (int) (System.nanoTime() * 0.0000000025) % 2 == 0;
    }

    @Override
    public void updatePlaying2(Renderer renderer) {
        p2Visible = (int) (System.nanoTime() * 0.0000000025) % 2 == 0;
    }
    
    // broadcast messages

    @Override
    public void stateChanged() {
        visible = scene.state == OxoScene.State.PLAYING_1
                || scene.state == OxoScene.State.PLAYING_2
                || scene.state == OxoScene.State.END;
        p1Visible = p2Visible = visible;
    }
    
}
