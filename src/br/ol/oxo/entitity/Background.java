package br.ol.oxo.entitity;

import br.ol.oxo.OxoEntity;
import br.ol.oxo.OxoScene;
import renderer.core.Renderer;
import renderer.core.Time;
import renderer.parser.wavefront.WavefrontParser;

/**
 * Background class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Background extends OxoEntity {

    public Background(String name, OxoScene scene) {
        super(name, scene);
    }

    @Override
    public void init() throws Exception {
        mesh = WavefrontParser.load("/res/background.obj", 1000);
    }
    
    @Override
    public void update(Renderer renderer) {
        transform.rotateX(Time.delta * 0.00000000005);
        //transform.rotateY(Time.delta * 0.00000000004);
        //transform.rotateZ(Time.delta * 0.00000000006);
    }
    
    // broadcast messages

    @Override
    public void stateChanged() {
        visible = scene.state != OxoScene.State.INITIALIZING;
    }
    
    
}
