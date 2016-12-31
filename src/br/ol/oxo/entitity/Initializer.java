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
public class Initializer extends OxoEntity {

    public Initializer(String name, OxoScene scene) {
        super(name, scene);
    }

    @Override
    public void updateInitializing(Renderer renderer) {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    while (System.currentTimeMillis() - waitTime < 3000) {
                        break yield;
                    }
                    scene.broadcastMessage("fadeOut");
                    instructionPointer = 2;
                case 2:
                    boolean fadeEffectFinished = scene.getProperty("fadeEffectFinished", Boolean.class);
                    if (!fadeEffectFinished) {
                        break yield;
                    }
                    scene.setState(OxoScene.State.OL_PRESENTS);
                    break yield;
            }
        }
    }
    
}
