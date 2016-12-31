package br.ol.oxo.infra;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import renderer.core.Keyboard;
import renderer.core.Mouse;
import renderer.core.Renderer;
import static renderer.core.Renderer.MatrixMode.*;
import renderer.core.Shader;
import renderer.core.Time;
import renderer.shader.GouraudShaderWithTexture;

/**
 * Engine class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Engine extends Canvas {
    
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    public static final int SCREEN_WIDTH = 440;
    public static final int SCREEN_HEIGHT = 330;
    
    public boolean running = false;
    public BufferStrategy bs;

    public Renderer renderer;
    public Thread thread;
    
    public Shader gouraudShader = new GouraudShaderWithTexture();
    public Scene scene;
    
    public double sx, sy;

    private Font fpsFont = new Font("Arial", Font.PLAIN, 10);

    public Engine(Scene scene) {
        addKeyListener(new KeyHandler());
        scene.setEngine(this);
        this.scene = scene;
    }
    
    public void init() throws Exception {
        MouseHandler mouseHandler = new MouseHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        
        createBufferStrategy(1);
        bs = getBufferStrategy();
        renderer = new Renderer(SCREEN_WIDTH, SCREEN_HEIGHT);
        
        sx = SCREEN_WIDTH / (double) WINDOW_WIDTH;
        sy = SCREEN_HEIGHT / (double) WINDOW_HEIGHT;
        
        renderer.setShader(gouraudShader);
        
        renderer.setMatrixMode(PROJECTION);
        renderer.setPerspectiveProjection(Math.toRadians(90));
        
        renderer.setClipZNear(-1);
        renderer.setClipZfar(-15000);
        
        scene.init();
        
        running = true;
        thread = new Thread(new MainLoop());
        thread.start();
    }
    
    public void update() {
        scene.update(renderer);
    }

    private void draw() {
        renderer.clearAllBuffers(); 
        scene.draw(renderer);
    }
    
    public void draw(Graphics2D g) {
        Graphics2D g2d = (Graphics2D) renderer.getColorBuffer().getColorBuffer().getGraphics();

        scene.draw(renderer, g2d);
        
        g2d.setFont(fpsFont);
        g2d.setColor(Color.BLACK);
        g2d.drawString("FPS: " + Time.fps, 5, 10);
        
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);        
        g2d.drawImage(renderer.getColorBuffer().getColorBuffer(), 0, 0, 400, 300, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        
        //g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);        
        g.drawImage(renderer.getColorBuffer().getColorBuffer(), 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, 0, 0, 400, 300, null);
    }
    
    private class MainLoop implements Runnable {

        @Override
        public void run() {
            while (running) {
                Time.update();
                update();
                draw();
                Graphics2D g = (Graphics2D) bs.getDrawGraphics();
                draw(g);
                g.dispose();
                bs.show();
            }
        }
        
    }

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseMoved(MouseEvent e) {
            Mouse.x = (e.getX() - WINDOW_WIDTH * 0.5) * sx;
            Mouse.y = (WINDOW_HEIGHT * 0.5 - e.getY()) * sy;
        }

        @Override
        public void mousePressed(MouseEvent me) {
            Mouse.pressed = true;
            Mouse.pressedConsumed = false;
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            Mouse.pressed = false;
            Mouse.pressedConsumed = false;
        }
        
        
        
        
        
    }

    private class KeyHandler extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            Keyboard.keyDown[e.getKeyCode()] = true;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            Keyboard.keyDown[e.getKeyCode()] = false;
        }
        
    }
    
}
