package br.ol.oxo.entitity;

import br.ol.oxo.OxoEntity;
import br.ol.oxo.OxoScene;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import renderer.core.Mouse;
import renderer.core.Renderer;
import renderer.math.Vec4;

/**
 * Selection class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Selection extends OxoEntity {
    
    private int col;
    private int row;
    private MouseCursor mouseCursor;
    
    private Polygon polygon = new Polygon();
    private Vec4[] points = {
        new Vec4(-70, -70, -500, 1),
        new Vec4(-70,  70, -500, 1),
        new Vec4( 70,  70, -500, 1),
        new Vec4( 70, -70, -500, 1),
    };
    private boolean selected;
    private Color[][] selectedColors = new Color[2][100];
    private Color selectedColor;
    
    public Selection(String name, OxoScene scene, int col, int row) {
        super(name, scene);
        this.col = col;
        this.row = row;
        
        for (int c = 0; c < selectedColors[0].length; c++) {
            selectedColors[0][c] = new Color(255, 0, 0, c);
            selectedColors[1][c] = new Color(0, 0, 255, c);
        }
        selectedColor = selectedColors[0][0];
    }

    @Override
    public void init() throws Exception {
        mouseCursor = scene.getProperty("getMouseCursor", MouseCursor.class);
        transform.setIdentity();
        transform.translate(200 * (col - 1), 200 * (row - 1), 0);
    }

    private void updateSelectionPolygon(Renderer renderer) {
        preDraw(renderer);
        renderer.begin();
        polygon.reset();
        Vec4 tp = new Vec4();
        int shw = renderer.getColorBuffer().getWidth() / 2;
        int shh = renderer.getColorBuffer().getHeight() / 2;
        for (Vec4 point : points) {
            tp.set(point);
            renderer.getMvp().multiply(tp);
            tp.doPerspectiveDivision();
            polygon.addPoint((int) (tp.x + shw), (int) (shh - tp.y));
        }
    }

    @Override
    public void updateTitle(Renderer renderer) {
        selected = false;
    }

    
    @Override
    public void updateSelectStartPlayer(Renderer renderer) {
        visible = false;
        int option = 0;
        if (col == 0 && row == 1) { // yes
            selected = checkSelection(renderer);
            option = 1;
            updateBlink(0);
            visible = true;
        }
        else if (col == 2 && row == 1) { // no
            selected = checkSelection(renderer);
            option = 2;
            updateBlink(1);
            visible = true;
        }
        if (selected && Mouse.pressed && !Mouse.pressedConsumed) {
            Mouse.pressedConsumed = true;
            switch (option) {
                case 1: scene.setState(OxoScene.State.PLAYING_1); break;
                case 2: scene.setState(OxoScene.State.PLAYING_2); break;
            }
        }
    }

    @Override
    public void updatePlaying1(Renderer renderer) {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    updateBlink(0);
                    selected = checkSelection(renderer);
                    selected = selected && scene.model.getValue(col, row).equals(" ");
                    if (selected && Mouse.pressed && !Mouse.pressedConsumed) {
                        Mouse.pressedConsumed = true;
                        scene.model.play(col, row);
                        mouseCursor.visible = false;
                        mouseCursor.locked = true;
                        visible = false;
                        waitTime = System.currentTimeMillis();
                        instructionPointer = 1;
                    }
                    break yield;
                case 1:
                    while (System.currentTimeMillis() - waitTime < 1000) {
                        break yield;
                    }
                    if (scene.model.checkWin1() || scene.model.isDraw()) {
                        scene.setState(OxoScene.State.END);
                        break yield;
                    }
                    scene.setState(OxoScene.State.PLAYING_2);
                    break yield;
            }
        }
    }

    @Override
    public void updatePlaying2(Renderer renderer) {
        updateBlink(1);
        selected = checkSelection(renderer);
        selected = selected && scene.model.getValue(col, row).equals(" ");
    }

    @Override
    public void updateEnd(Renderer renderer) {
        selected = false;
    }
    
    public boolean checkSelection(Renderer renderer) {
        updateSelectionPolygon(renderer);
        int shw = renderer.getColorBuffer().getWidth() / 2;
        int shh = renderer.getColorBuffer().getHeight() / 2;
        int mx = (int) (mouseCursor.x + shw);
        int my = (int) (shh - mouseCursor.y);
        return polygon.contains(mx, my);
    }
    
    private void updateBlink(int player) {
        int sci = (int) (50 * Math.sin(System.nanoTime() * 0.00000001)) + 50;
        selectedColor = selectedColors[player][sci];
    }
    
//        selected = polygon.contains(mx, my);
//        selected = selected && scene.ai.getValue(col, row).equals(" ");
//        if (selected && Mouse.pressed && !Mouse.pressedConsumed) {
//            Mouse.pressedConsumed = true;
//            scene.ai.play(col, row);
//            scene.ai.playAI();
//        }
//        
//    }
    
    @Override
    public void draw(Renderer renderer, Graphics2D g) {
        if (!visible) {
            return;
        }
        if (selected) {
            g.setColor(selectedColor);
            g.fill(polygon);
        }
    }
    
    // broadcast messages

    @Override
    public void stateChanged() {
        visible = scene.state != OxoScene.State.INITIALIZING
                && scene.state != OxoScene.State.SELECT_START_PLAYER
                && scene.state != OxoScene.State.END;
        
        if (scene.state == OxoScene.State.PLAYING_1 
                || scene.state == OxoScene.State.PLAYING_2) {
            instructionPointer = 0;
        }
    }
   
}
