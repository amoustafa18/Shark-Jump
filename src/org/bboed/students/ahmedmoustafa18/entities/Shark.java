package org.bboed.students.ahmedmoustafa18.entities;

import org.bboed.students.ahmedmoustafa18.DoesRenderAndUpdate;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class Shark implements DoesRenderAndUpdate {

    private static final String IMAGE_PATH = "assets/images/sprites/shark.png";
    private static final float HITBOX_PADDING = 5;

    private Image renderImage;
    private Vector2f position;
    private Shape hitbox;
    private double swimSpeed;
    private boolean isAlive;

    public Shark(Vector2f position, double swimSpeed) {
        try {
            this.renderImage = new Image(IMAGE_PATH);
        } catch(SlickException ex) {
            ex.printStackTrace();
        }
        this.position = position;
        this.hitbox = new Rectangle(position.getX() + HITBOX_PADDING, position.getY() + HITBOX_PADDING,
                renderImage.getWidth() - HITBOX_PADDING * 2, renderImage.getHeight() - HITBOX_PADDING * 2);
        this.swimSpeed = swimSpeed;
        this.isAlive = true;
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        renderImage.draw(position.getX(), position.getY());
        //graphics.draw(hitbox);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        position.x -= swimSpeed * delta/1000.0f;
        hitbox.setX(position.x);

        if(position.getX() < -renderImage.getWidth()) {
            isAlive = false;
        }
    }

    public Shape getHitbox() {
        return hitbox;
    }

    public boolean isAlive() {
        return isAlive;
    }
}
