package br.ol.oxo.entitity;

import br.ol.oxo.OxoEntity;
import br.ol.oxo.OxoScene;
import br.ol.oxo.OxoScene.State;
import br.ol.oxo.infra.Engine;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import renderer.core.Mouse;
import renderer.core.Renderer;

/**
 * DoYouWantToStart class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class SelectStartPlayer extends OxoEntity {
    
    private Font font = new Font("Arial", Font.PLAIN, 14);
    private String questionMessage = "Do you want to start ?";
    
    public SelectStartPlayer(String name, OxoScene scene) {
        super(name, scene);
    }

    @Override
    public void draw(Renderer renderer, Graphics2D g) {
        if (!visible) {
            return;
        }
        int startX = (Engine.SCREEN_WIDTH - scene.getTextWidth(g, font, questionMessage)) / 2;
        scene.drawText(g, font, questionMessage, startX, 100, Color.DARK_GRAY);
    }

    // broadcast messages

    @Override
    public void stateChanged() {
        visible = scene.state == OxoScene.State.SELECT_START_PLAYER;
    }
    
}
