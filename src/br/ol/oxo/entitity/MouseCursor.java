package br.ol.oxo.entitity;

import br.ol.oxo.OxoEntity;
import br.ol.oxo.OxoScene;
import br.ol.oxo.infra.Engine;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import renderer.core.Mouse;
import renderer.core.Renderer;

/**
 * MouseCursor class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class MouseCursor extends OxoEntity {

    public static enum Mode { MOUSE, MANUAL }
    
    private BufferedImage image;
    public double x, y;
    public int internalX, internalY;
    public Mode mode = Mode.MOUSE;
    public double manualTargetX, manualTargetY, manualV;
    public boolean locked;
    
    public long blinkTime;
    public boolean blinkVisible;
    
    public MouseCursor(String name, OxoScene scene) {
        super(name, scene);
    }

    @Override
    public void init() throws Exception {
        image = ImageIO.read(getClass().getResourceAsStream("/res/mouse_cursor.png"));
    }

    @Override
    public void update(Renderer renderer) {
        blinkVisible = true;
        if (System.currentTimeMillis() - blinkTime < 2000) {
            blinkVisible = (int) (System.nanoTime() * 0.00000001) % 2 == 0;
        }
        if (locked) {
            return;
        }
        switch (mode) {
            case MOUSE:
                x = Mouse.x;
                y = Mouse.y;
                //System.out.println("mouse position = (" + Mouse.x + ", " + Mouse.y + ")");
                break;
            case MANUAL:
                x += (manualTargetX - x) * manualV;
                y += (manualTargetY - y) * manualV;
                break;
        }
        internalX = (int) (x + Engine.SCREEN_WIDTH / 2);
        internalY = (int) (Engine.SCREEN_HEIGHT / 2 - y);
    }

    @Override
    public void draw(Renderer renderer, Graphics2D g) {
        if (!visible) {
            return;
        }
        if (blinkVisible) {
            g.drawImage(image, internalX, internalY, null);
        }
    }
    
    // broadcast messages

    @Override
    public void stateChanged() {
//        visible = scene.state != OxoScene.State.INITIALIZING
//                && scene.state != OxoScene.State.OL_PRESENTS;
    };

    public void setManualTarget(int x, int y) {
        manualTargetX = x;
        manualTargetY = y;
    }
        
    public void blink() {
        blinkTime = System.currentTimeMillis();
    }
    
    // public properties
    
    public MouseCursor getMouseCursor() {
        return this;
    }
    
    public boolean isManualTargetPositionReached() {
        return Math.abs(manualTargetX - x) < 1 
                &&  Math.abs(manualTargetY - y) < 1;
    }
    
}
