package br.ol.oxo.entitity;

import br.ol.oxo.OxoEntity;
import br.ol.oxo.OxoScene;
import br.ol.oxo.OxoScene.State;
import br.ol.oxo.infra.Engine;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import renderer.core.Renderer;

/**
 * End class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class End extends OxoEntity {
    
    private Font font = new Font("Arial", Font.PLAIN, 50);
    
    private String youWinMessage = "YOU WIN";
    private String youLoseMessage = "YOU LOSE";
    private String drawMessage = "DRAW";

    private boolean youWinMessageVisible = false;
    private boolean youLoseMessageVisible = false;
    private boolean drawMessageVisible = false;
    
    public End(String name, OxoScene scene) {
        super(name, scene);
    }

    @Override
    public void init() throws Exception {
    }

    @Override
    public void updateEnd(Renderer renderer) {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    youWinMessageVisible = scene.model.checkWin1();
                    youLoseMessageVisible = scene.model.checkWin2();
                    drawMessageVisible = scene.model.isDraw();
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    while (System.currentTimeMillis() - waitTime < 5000) {
                        break yield;
                    }
                    scene.broadcastMessage("fadeOut");
                    instructionPointer = 2;
                case 2:
                    boolean fadeEffectFinished = scene.getProperty("fadeEffectFinished", Boolean.class);
                    if (!fadeEffectFinished) {
                        break yield;
                    }
                    instructionPointer = 3;
                case 3:
                    scene.setState(State.TITLE);
                    break yield;
            }
        }
    }

    @Override
    public void draw(Renderer renderer, Graphics2D g) {
        if (!visible) {
            return;
        }
        
        if (youWinMessageVisible) {
            int startX = (Engine.SCREEN_WIDTH - scene.getTextWidth(g, font, youWinMessage)) / 2;
            scene.drawText(g, font, youWinMessage, startX, 170, Color.BLUE);
        }
        else if (youLoseMessageVisible) {
            int startX = (Engine.SCREEN_WIDTH - scene.getTextWidth(g, font, youLoseMessage)) / 2;
            scene.drawText(g, font, youLoseMessage, startX, 170, Color.RED);
        }
        if (drawMessageVisible) {
            int startX = (Engine.SCREEN_WIDTH - scene.getTextWidth(g, font, drawMessage)) / 2;
            scene.drawText(g, font, drawMessage, startX, 170, Color.DARK_GRAY);
        }
        
    }
    
    // broadcast messages

    @Override
    public void stateChanged() {
        visible = scene.state == OxoScene.State.END;
        if (scene.state == OxoScene.State.END) {
            instructionPointer = 0;
        }
    }
    
}
