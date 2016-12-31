package br.ol.oxo.entitity;

import br.ol.oxo.OxoEntity;
import br.ol.oxo.OxoScene;
import br.ol.oxo.OxoScene.State;
import renderer.core.Renderer;
import renderer.core.Time;
import renderer.parser.wavefront.WavefrontParser;

/**
 * OLPresents class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class OLPresents extends OxoEntity {
    
    private Camera camera;
    
    private double y = -100;
    private double targetY = 500;
    
    
    private double v;
    private double dy;
    private double dyTotal;
    
    public OLPresents(String name, OxoScene scene) {
        super(name, scene);
    }

    @Override
    public void init() throws Exception {
        mesh = WavefrontParser.load("/res/ol_presents.obj", 150);
        transform.translate(0, -100, 0);
        camera = (Camera) scene.getCamera();
    }

    @Override
    public void updateOlPresents(Renderer renderer) {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    camera.angleX = Math.toRadians(90);
                    scene.broadcastMessage("fadeIn");
                    instructionPointer = 1;
                case 1:
                    boolean fadeEffectFinished = scene.getProperty("fadeEffectFinished", Boolean.class);
                    if (!fadeEffectFinished) {
                        break yield;
                    }
                    waitTime = System.currentTimeMillis();;
                    instructionPointer = 2;
                case 2:
                    while (System.currentTimeMillis() - waitTime < 2000) {
                        break yield;
                    }
                    instructionPointer = 3;
                case 3:
                    dy = targetY - y;
                    y += dy * 0.05;
                    transform.setIdentity();
                    transform.translate(0, y, 0);
                    if (Math.abs(dy) < 1) {
                        waitTime = System.currentTimeMillis();;
                        instructionPointer = 4;
                    }
                    break yield;
                case 4:
                    while (System.currentTimeMillis() - waitTime < 3000) {
                        break yield;
                    }
                    instructionPointer = 5;
                case 5:
                    camera.setTargetAngle(Math.toRadians(0), 0, 0.05);
                    if (camera.isRotationToTargetAngleFinished()) {
                        instructionPointer = 6;
                    }
                    break yield;
                case 6:
                    camera.mouseEnabled = true; 
                    camera.vp = 0.05;
                    scene.setState(State.TITLE);
                    break yield;
            }
        }
    }
    
    // broadcast messages

    @Override
    public void stateChanged() {
        visible = scene.state == State.OL_PRESENTS;
    }
    
}
