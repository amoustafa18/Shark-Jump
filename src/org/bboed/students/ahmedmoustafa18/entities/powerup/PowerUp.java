package org.bboed.students.ahmedmoustafa18.entities.powerup;

import org.bboed.students.ahmedmoustafa18.DoesRenderAndUpdate;
import org.bboed.students.ahmedmoustafa18.SharkJump;
import org.bboed.students.ahmedmoustafa18.entities.Boat;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public abstract class PowerUp implements DoesRenderAndUpdate {

    public static final float DEFAULT_SPEED = 375;
    public static final float POWER_UP_WINDOW_MARGIN = 251;
    public static final float Y_POSITION = SharkJump.GAME_HEIGHT - POWER_UP_WINDOW_MARGIN;

    private Image renderImage;
    private Vector2f position;
    private Rectangle hitbox;
    private POWER_UP type;
    private float speed;
    private boolean visible;
    private boolean isActive;

    public enum POWER_UP {
        HEAL, DOUBLE_POINTS, BARRICADE
    }

    public PowerUp(String renderImagePath, Vector2f position, POWER_UP type, float speed) {
        try {
            this.renderImage = new Image(renderImagePath);
        } catch(SlickException ex) {
            ex.printStackTrace();
        }
        this.position = position;
        this.hitbox = new Rectangle(position.getX(), position.getY(), renderImage.getWidth(), renderImage.getHeight());
        this.type = type;
        this.speed = speed;
        this.visible = true;
        this.isActive = false;
    }

    public PowerUp(String renderImagePath, Vector2f position, POWER_UP type) {
        this(renderImagePath, position, type, DEFAULT_SPEED);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        if(visible) {
            renderImage.draw(position.getX(), position.getY());
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        if(visible){
            position.x -= speed * delta / 1000.0;
            hitbox.setX((float) (hitbox.getX() - speed * delta / 1000.0));
        }
    }

    public abstract void executePower(Boat boat);

    public Rectangle getHitbox() {
        return hitbox;
    }

    public float getX() {
        return position.getX();
    }

    public float getY() {
        return position.getY();
    }

    public void setX(float x) {
        position.x = x;
    }

    public void setY(float y) {
        position.y = y;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public POWER_UP getType() {
        return type;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getImageWidth() {
        return renderImage.getWidth();
    }

    public int getImageHeight() {
        return renderImage.getHeight();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
