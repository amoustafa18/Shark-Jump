package org.bboed.students.ahmedmoustafa18.ui;

import org.bboed.students.ahmedmoustafa18.DoesRenderAndUpdate;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class TopDownPanningBoard implements DoesRenderAndUpdate {

    public static final float DEFAULT_PAN_SPEED = 1000; //pixels per second

    private Image renderImage;
    private Vector2f position;
    private float finalY;
    private float panSpeed;
    private boolean shouldPan;
    private boolean panStarted;

    public TopDownPanningBoard(Vector2f position, String imagePath, float panSpeed, float finalY) {
        try {
            renderImage = new Image(imagePath);
        } catch(SlickException ex) {
            ex.printStackTrace();
        }
        this.position = position;
        this.finalY = finalY;
        this.panSpeed = panSpeed;
        shouldPan = false;
        panStarted = false;
    }

    public TopDownPanningBoard(Vector2f position, String imagePath, float finalY) {
        this(position, imagePath, DEFAULT_PAN_SPEED, finalY);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        renderImage.draw(position.getX(), position.getY());
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        if(shouldPan) {
            if(position.getY() >= finalY) {
                shouldPan = false;
                position.y = finalY;
            } else {
                position.y += panSpeed * (delta / 1000.0);
            }
        }
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public boolean isShouldPan() {
        return shouldPan;
    }

    public void setShouldPan(boolean shouldPan) {
        this.shouldPan = shouldPan;
    }

    public boolean isPanStarted() {
        return panStarted;
    }

    public void setPanStarted(boolean panStarted) {
        this.panStarted = panStarted;
    }
}
