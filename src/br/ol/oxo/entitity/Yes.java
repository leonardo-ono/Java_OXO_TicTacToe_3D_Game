package br.ol.oxo.entitity;

import br.ol.oxo.OxoEntity;
import br.ol.oxo.OxoScene;
import renderer.parser.wavefront.WavefrontParser;

/**
 * Yes class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Yes extends OxoEntity {

    public Yes(String name, OxoScene scene) {
        super(name, scene);
    }

    @Override
    public void init() throws Exception {
        mesh = WavefrontParser.load("/res/yes.obj", 100);
        transform.translate(0, 0, -500);
        transform.rotateX(Math.toRadians(90));
    }
    
    // broadcast messages

    @Override
    public void stateChanged() {
        visible = scene.state == OxoScene.State.SELECT_START_PLAYER;
    }
    
}
