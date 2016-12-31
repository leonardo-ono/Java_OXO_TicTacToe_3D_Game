package br.ol.oxo;

import br.ol.oxo.infra.Entity;
import renderer.core.Renderer;

/**
 * OxoEntity class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class OxoEntity extends Entity<OxoScene> {
    
    public int instructionPointer;
    public long waitTime;
    
    public OxoEntity(String name, OxoScene scene) {
        super(name, scene);
    }

    @Override
    public void update(Renderer renderer) {
        preUpdate(renderer);
        switch(scene.state) {
            case INITIALIZING: updateInitializing(renderer); break;
            case OL_PRESENTS: updateOlPresents(renderer); break;
            case TITLE: updateTitle(renderer); break;
            case SELECT_START_PLAYER: updateSelectStartPlayer(renderer); break;
            case PLAYING_1: updatePlaying1(renderer); break;
            case PLAYING_2: updatePlaying2(renderer); break;
            case END: updateEnd(renderer); break;
        }
        posUpdate(renderer);
    }
    
    public void preUpdate(Renderer renderer) {
    }

    public void updateInitializing(Renderer renderer) {
    }

    public void updateOlPresents(Renderer renderer) {
    }

    public void updateTitle(Renderer renderer) {
    }

    public void updateSelectStartPlayer(Renderer renderer) {
    }
    
    public void updatePlaying1(Renderer renderer) {
    }

    public void updatePlaying2(Renderer renderer) {
    }

    public void updateEnd(Renderer renderer) {
    }

    public void posUpdate(Renderer renderer) {
    }
    
    // broadcast messages
    
    public void stateChanged() {
    }


}
