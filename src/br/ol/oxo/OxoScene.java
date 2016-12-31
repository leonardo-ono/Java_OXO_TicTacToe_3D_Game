package br.ol.oxo;

import br.ol.oxo.entitity.Background;
import br.ol.oxo.entitity.PlayManager;
import br.ol.oxo.entitity.Camera;
import br.ol.oxo.entitity.End;
import br.ol.oxo.entitity.SelectStartPlayer;
import br.ol.oxo.entitity.O;
import br.ol.oxo.entitity.Selection;
import br.ol.oxo.entitity.Sharp;
import br.ol.oxo.entitity.FadeEffect;
import br.ol.oxo.entitity.HUD;
import br.ol.oxo.entitity.Initializer;
import br.ol.oxo.entitity.MouseCursor;
import br.ol.oxo.entitity.No;
import br.ol.oxo.entitity.OLPresents;
import br.ol.oxo.entitity.Title;
import br.ol.oxo.entitity.X;
import br.ol.oxo.entitity.Yes;
import br.ol.oxo.infra.Entity;
import br.ol.oxo.infra.Scene;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.lang.reflect.Method;

/**
 * OxoScene class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class OxoScene extends Scene {
    
    public static enum State { INITIALIZING, OL_PRESENTS, TITLE, SELECT_START_PLAYER, PLAYING_1, PLAYING_2, END }
    public State state = State.INITIALIZING;
    public OxoModel model = new OxoModel();

    public OxoScene() {
        camera = new Camera("camera", this);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        if (state != this.state) {
            this.state = state;
            broadcastMessage("stateChanged");
        }
    }
    
    @Override
    public void init() throws Exception {
        model.start();
        
        entities.add(new Initializer("initializer", this));
        entities.add(new Background("background", this));
        entities.add(new OLPresents("ol_presents", this));
        
        entities.add(new Sharp("sharp", this));
        
        for (int row=0; row<3; row++){
            for (int col=0; col<3; col++){
                entities.add(new Selection("selection", this, col, row));
                entities.add(new O("o_" + col + "_" + row, this, col, row));
                entities.add(new X("x_" + col + "_" + row, this, col, row));
            }
        }
        
        entities.add(new Title("title", this));
        entities.add(new SelectStartPlayer("start_select_player", this));
        entities.add(new Yes("yes", this));
        entities.add(new No("no", this));
        entities.add(new HUD("hud", this));
        entities.add(new End("end", this));
        entities.add(new PlayManager("cpu", this));
        entities.add(new MouseCursor("mouse_cursor", this));
        entities.add(new FadeEffect("fade_effect", this));
        super.init();
    }

    public <T> T getProperty(String property, Class<T> returnType) {
        for (Entity entity : entities) {
            try {
                Method method = entity.getClass().getMethod(property);
                if (method != null) {
                    Object r = method.invoke(entity);
                    return returnType.cast(r);
                }
            } catch (Exception ex) {
            }
        };
        return null;
    }

    public void drawText(Graphics2D g, Font font, String text, int x, int y, Color color) {
        g.setFont(font);
        g.setColor(color);
        for (int y2=y-2; y2<=y+2; y2++) {
            for (int x2=x-2; x2<=x+2; x2++) {
                g.drawString(text, x2, y2);
            }
        }
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);
    }
    
    public int getTextWidth(Graphics2D g, Font font, String text) {
        g.setFont(font);
        return g.getFontMetrics().stringWidth(text);
    }

}
