package br.ol.oxo.entitity;

import br.ol.oxo.OxoEntity;
import br.ol.oxo.OxoScene;
import renderer.core.Renderer;
import renderer.core.Time;
import renderer.parser.wavefront.WavefrontParser;

/**
 * X class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class X extends OxoEntity {

    private int col;
    private int row;

    public X(String name, OxoScene scene, int col, int row) {
        super(name, scene);
        this.col = col;
        this.row = row;        
    }

    @Override
    public void init() throws Exception {
        mesh = WavefrontParser.load("/res/x.obj", 100);
        transform.translate(200 * (col - 1), 200 * (row - 1), -500);
        transform.rotateX(Math.toRadians(90));
        //transform.rotateZ(Math.toRadians(90));
    }
    
    @Override
    public void updateOlPresents(Renderer renderer) {
        transform.rotateZ(-Time.delta * 0.000000001);
    }

    @Override
    public void updateTitle(Renderer renderer) {
        transform.rotateZ(-Time.delta * 0.000000001);
    }
    
    @Override
    public void updatePlaying1(Renderer renderer) {
        updatePlaying(renderer);
    }

    @Override
    public void updatePlaying2(Renderer renderer) {
        updatePlaying(renderer);
    }

    
    private void updatePlaying(Renderer renderer) {
        visible = scene.model.getValue(col, row).equals("X");
        if (scene.model.isWinPiece(col, row) && scene.model.checkWin2()) {
            visible = (int) (System.nanoTime() * 0.000000005) % 2 == 0;
        }
        
        transform.rotateZ(-Time.delta * 0.000000001);
    }

    @Override
    public void updateEnd(Renderer renderer) {
        updatePlaying(renderer);
    }
    
    
    // broadcast messages

    @Override
    public void stateChanged() {
        switch (scene.state) {
            case OL_PRESENTS:
            case TITLE:
                visible = row == 1 && col == 1;
                break;
            case SELECT_START_PLAYER:
            case INITIALIZING:
                visible = false;
            default:
                
        }
    }
    
}
