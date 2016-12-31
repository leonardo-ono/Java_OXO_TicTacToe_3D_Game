package br.ol.oxo.entitity;

import br.ol.oxo.OxoEntity;
import br.ol.oxo.OxoScene;
import renderer.core.Mouse;
import renderer.core.Renderer;

/**
 * Camera class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Camera extends OxoEntity {
    
    public double targetAngleX;
    public double targetAngleY;

    public double angleX;
    public double angleY;

    public double mouseAngleX;
    public double mouseAngleY;
    
    public double vp;
    
    public boolean mouseEnabled = false;
    
    public MouseCursor mouseCursor;
    
    public Camera(String name, OxoScene scene) {
        super(name, scene);
    }
        
    @Override
    public void init() throws Exception {
        transform.setIdentity();
        mouseCursor = scene.getProperty("getMouseCursor", MouseCursor.class);
    }

    @Override
    public void updatePlaying1(Renderer renderer) {
        //updateAngleXY();
        //double dx = Time.delta * 0.0000001;
        //System.out.println(dx);
        //dx = dx > 2 ? 2 :dx;
        //transform.translate(dx, 0, 0);
    }
    
    @Override
    public void update(Renderer renderer) {
        mouseAngleX = mouseAngleY = 0;
        if (mouseEnabled) {
            mouseAngleX = Math.toRadians(mouseCursor.y * 0.05);
            mouseAngleY = Math.toRadians(mouseCursor.x * 0.05);
        }

        angleX += (targetAngleX + mouseAngleX - angleX) * vp;
        angleY += (targetAngleY + mouseAngleY - angleY) * vp;
        transform.setIdentity();
        transform.rotateX(angleX);
        transform.rotateY(-angleY);
    }
    
    public void setTargetAngle(double ax, double ay, double vp) {
        targetAngleX = ax;
        targetAngleY = ay;
        this.vp = vp;
    }
    
    public boolean isRotationToTargetAngleFinished() {
        return Math.abs(targetAngleX + mouseAngleX - angleX) < 0.01 
                && Math.abs(targetAngleY + mouseAngleY - angleY) < 0.01;
        
    }
    
}
