package br.ol.oxo.entitity;

import br.ol.oxo.OxoEntity;
import br.ol.oxo.OxoScene;
import renderer.core.Renderer;
import renderer.parser.wavefront.WavefrontParser;

/**
 * Sharp class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Sharp extends OxoEntity {

    public Sharp(String name, OxoScene scene) {
        super(name, scene);
    }

    @Override
    public void init() throws Exception {
        mesh = WavefrontParser.load("/res/sharp.obj", 100);
        transform.translate(0, 0, -500);
        transform.rotateX(Math.toRadians(90));
    }
    
    @Override
    public void update(Renderer renderer) {
    }

    // broadcast messages

    @Override
    public void stateChanged() {
        visible = scene.state != OxoScene.State.SELECT_START_PLAYER;
    }
    
}
