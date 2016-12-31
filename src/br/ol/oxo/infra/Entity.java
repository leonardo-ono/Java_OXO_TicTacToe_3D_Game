package br.ol.oxo.infra;

import java.awt.Graphics2D;
import java.util.List;
import renderer.core.Renderer;
import renderer.core.Transform;
import renderer.math.Vec2;
import renderer.math.Vec4;
import renderer.parser.wavefront.Obj;
import renderer.parser.wavefront.WavefrontParser;

/**
 * Entity abstract class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public abstract class Entity<T extends Scene> {
    
    public String name;
    public T scene;
    public boolean visible = false;
    public Transform transform = new Transform();
    public List<Obj> mesh;
    
    public Entity(String name, T scene) {
        this.name = name;
        this.scene = scene;
    }
    
    public void init() throws Exception {
        transform.setIdentity();
    }
    
    public void update(Renderer renderer) {
    }
    
    public void preDraw(Renderer renderer) {
        renderer.setBackfaceCullingEnabled(true);
        renderer.setShader(scene.engine.gouraudShader);
        renderer.setMatrixMode(Renderer.MatrixMode.MODEL);
        renderer.setModelTransform(transform);
        renderer.setBackfaceCullingEnabled(false);
    }
    
    public void draw(Renderer renderer) {
        if (!visible || mesh == null) {
            return;
        }
        for (Obj obj : mesh) {
            renderer.setMaterial(obj.material);
            renderer.begin();
            for (WavefrontParser.Face face : obj.faces) {
                for (int f=0; f<3; f++) {
                    Vec4 v = face.vertex[f];
                    Vec4 n = face.normal[f];
                    Vec2 t = face.texture[f];
                    renderer.setTextureCoordinates(t.x, t.y);
                    renderer.setNormal(n.x, n.y, n.z);
                    renderer.setVertex(v.x, v.y, v.z);
                }
            }
            renderer.end();            
        }        
    }

    public void draw(Renderer renderer, Graphics2D g) {
    }
    
}
