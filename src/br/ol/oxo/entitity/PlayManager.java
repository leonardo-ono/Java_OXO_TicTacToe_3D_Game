package br.ol.oxo.entitity;

import br.ol.oxo.OxoEntity;
import br.ol.oxo.OxoScene;
import renderer.core.Renderer;

/**
 * PlayManager class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class PlayManager extends OxoEntity {
    
    private MouseCursor mouseCursor;
    
    public PlayManager(String name, OxoScene scene) {
        super(name, scene);
    }

    @Override
    public void init() throws Exception {
        mouseCursor = scene.getProperty("getMouseCursor", MouseCursor.class);
    }

    @Override
    public void updatePlaying1(Renderer renderer) {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    mouseCursor.mode = MouseCursor.Mode.MOUSE;
                    mouseCursor.visible = true;
                    mouseCursor.blink();
                    instructionPointer = 1;
                    break yield;
                case 1:
                    break yield;
            }
        }
    }
    
    @Override
    public void updatePlaying2(Renderer renderer) {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    mouseCursor.mode = MouseCursor.Mode.MANUAL;
                    mouseCursor.x = 180;
                    mouseCursor.y = 120;
                    mouseCursor.internalX = 180;
                    mouseCursor.internalY = 120;
                    mouseCursor.setManualTarget(mouseCursor.internalX, mouseCursor.internalY);
                    mouseCursor.manualV = 0.1;
                    mouseCursor.locked = false;
                    mouseCursor.visible = true;
                    mouseCursor.blink();
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                    break yield;
                case 1:
                    while (System.currentTimeMillis() - waitTime < 2000) {
                        break yield;
                    }
                    int aiMove = scene.model.processNextAIMove();
                    int tx = 70, ty = 70;
                    switch (aiMove) {
                        case 1: mouseCursor.setManualTarget(-tx, -ty); break;
                        case 2: mouseCursor.setManualTarget(0, -ty); break;
                        case 4: mouseCursor.setManualTarget(tx, -ty); break;
                        case 8: mouseCursor.setManualTarget(-tx, 0); break;
                        case 16: mouseCursor.setManualTarget(0, 0); break;
                        case 32: mouseCursor.setManualTarget(tx, 0); break;
                        case 64: mouseCursor.setManualTarget(-tx, ty); break;
                        case 128: mouseCursor.setManualTarget(0, ty); break;
                        case 256: mouseCursor.setManualTarget(tx, ty); break;
                    }
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 4;
                case 4:
                    if (mouseCursor.isManualTargetPositionReached()) {
                        instructionPointer = 5;
                    }
                    break yield;
                case 5:
                    scene.model.commitAIMove();
                    mouseCursor.visible = false;
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 6;
                case 6:
                    while (System.currentTimeMillis() - waitTime < 1000) {
                        break yield;
                    }
                    if (scene.model.checkWin2() || scene.model.isDraw()) {
                        scene.setState(OxoScene.State.END);
                        break yield;
                    }
                    scene.setState(OxoScene.State.PLAYING_1);
                    break yield;
            }
        }
    }
    
    // broadcast messages

    @Override
    public void stateChanged() {
        if (scene.state == OxoScene.State.PLAYING_1 
                || scene.state == OxoScene.State.PLAYING_2) {
            
            instructionPointer = 0;
        }
    }
    
}
