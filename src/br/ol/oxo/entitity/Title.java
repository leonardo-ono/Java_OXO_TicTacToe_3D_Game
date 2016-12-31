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
 * Title class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Title extends OxoEntity {
    
    private Font font = new Font("Arial", Font.PLAIN, 14);
    private String clickWithMouseToStart = "Click with mouse to start vs CPU";
    private boolean clickWithMouseToStartVisible = false;
    private String programmedByOL = "Programmed by O.L. (c) 2016";
    private MouseCursor mouseCursor;
    
    public Title(String name, OxoScene scene) {
        super(name, scene);
    }

    @Override
    public void init() throws Exception {
        mouseCursor = scene.getProperty("getMouseCursor", MouseCursor.class);
    }

    @Override
    public void updateTitle(Renderer renderer) {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    scene.broadcastMessage("fadeIn");
                    instructionPointer = 1;
                case 1:
                    boolean fadeEffectFinished = scene.getProperty("fadeEffectFinished", Boolean.class);
                    if (!fadeEffectFinished) {
                        break yield;
                    }
                    instructionPointer = 2;
                case 2:
                    mouseCursor.mode = MouseCursor.Mode.MOUSE;
                    mouseCursor.locked = false;
                    mouseCursor.visible = true;
                    mouseCursor.blink();
                    instructionPointer = 3;
                case 3:
                    clickWithMouseToStartVisible = (int) (System.nanoTime() * 0.0000000025) % 2 == 0;
                    if (Mouse.pressed && !Mouse.pressedConsumed) {
                        Mouse.pressedConsumed = true;
                        scene.model.start();
                        scene.setState(State.SELECT_START_PLAYER);
                    }
                    break yield;
            }
        }
    }

    @Override
    public void draw(Renderer renderer, Graphics2D g) {
        if (!visible) {
            return;
        }
        if (clickWithMouseToStartVisible) {
            int startX = (Engine.SCREEN_WIDTH - scene.getTextWidth(g, font, clickWithMouseToStart)) / 2;
            scene.drawText(g, font, clickWithMouseToStart, startX, 250, Color.DARK_GRAY);
        }
        int startX = (Engine.SCREEN_WIDTH - scene.getTextWidth(g, font, programmedByOL)) / 2;
        scene.drawText(g, font, programmedByOL, startX, 300, Color.DARK_GRAY);
    }
    
    // broadcast messages

    @Override
    public void stateChanged() {
        visible = scene.state == OxoScene.State.TITLE;
        if (scene.state == OxoScene.State.TITLE) {
            instructionPointer = 0;
        }
    }
    
}
