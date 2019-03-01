package org.bboed.students.ahmedmoustafa18.entities;

import org.bboed.students.ahmedmoustafa18.DoesRenderAndUpdate;
import org.bboed.students.ahmedmoustafa18.SharkJump;
import org.bboed.students.ahmedmoustafa18.states.SharkJumpGame;
import org.bboed.students.ahmedmoustafa18.ui.HealthBar;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.StateBasedGame;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Boat implements DoesRenderAndUpdate {

    private static final String IMAGE_PATH = "assets/images/sprites/boat.png";
    public static final float MAX_JUMP_SPEED = 385; //pixels per second
    public static final float BOAT_WINDOW_MARGIN = 375;
    private static final float HITBOX_PADDING = 5;
    private static final float BOAT_HEALTH_BAR_MARGIN = 10;
    private static final float PARTICLE_MARGIN = -10;
    private static final float WATER_SOUND_PITCH = .5f;
    private static final float WATER_SOUND_VOLUME = .75f;
    private static final float HEALTH_BAR_WIDTH = 150;
    private static final int HEAL_DURATION = 1800;

    private Image renderImage;
    private float baseY; //lowest y-value the top pixel of the ship can reach
    private double maxHealth;
    private double health;
    private Vector2f position;
    private Shape hitbox;
    private boolean alive;
    private boolean jumping;
    private float jumpVelocity;
    private HealthBar healthBar;
    private ParticleSystem pSystem;
    private ConfigurableEmitter pEmitter;
    private ParticleSystem healthPSystem;
    private ConfigurableEmitter healthPEmitter;
    private Sound bite;
    private Sound jump;
    private Sound water;
    private Sound splash;
    private boolean waterSoundStarted;
    private List<Shark> livingSharks;
    private boolean healing;
    private int msSinceHealingStarted;

    public Boat(String imagePath, List<Shark> livingSharks,  float baseY, double maxHealth, Vector2f position) {
        try {
            this.renderImage = new Image(imagePath);
        } catch(SlickException ex) {
            ex.printStackTrace();
        }

        this.baseY = baseY;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.position = position;
        this.hitbox = new Rectangle(position.getX() + HITBOX_PADDING, position.getY() + HITBOX_PADDING,
                renderImage.getWidth() - HITBOX_PADDING * 2, renderImage.getHeight() - HITBOX_PADDING * 2);
        this.alive = true;
        this.jumping = false;
        this.jumpVelocity = 0;
        this.healthBar = new HealthBar(maxHealth, maxHealth, HEALTH_BAR_WIDTH, 10,
                new Vector2f(position.getX() + renderImage.getWidth() / 2 - HEALTH_BAR_WIDTH / 2, position.getY() + renderImage.getHeight() + BOAT_HEALTH_BAR_MARGIN));
        try {
            this.pSystem = ParticleIO.loadConfiguredSystem("assets/particles/water.xml");
            this.pSystem.setPosition(position.getX(), position.getY() + renderImage.getHeight() + PARTICLE_MARGIN);
            this.pSystem.getEmitter(0).setEnabled(false); // disable the initial emitter
            this.pSystem.setRemoveCompletedEmitters(true); // remove emitters once they finish
            /** Create a new emitter based on the explosionSystem - set disabled */
            this.pEmitter = (ConfigurableEmitter)pSystem.getEmitter(0);
            this.pEmitter.setEnabled(true);
            this.pSystem.addEmitter(pEmitter);

            this.healthPSystem = ParticleIO.loadConfiguredSystem("assets/particles/heal.xml");
            this.healthPSystem.setPosition(position.getX() + renderImage.getWidth() / 2, position.getY() - renderImage.getHeight());
            this.healthPSystem.getEmitter(0).setEnabled(false); // disable the initial emitter
            this.healthPSystem.setRemoveCompletedEmitters(false); // remove emitters once they finish
            /** Create a new emitter based on the explosionSystem - set disabled */
            this.healthPEmitter = (ConfigurableEmitter)healthPSystem.getEmitter(0);
            this.healthPEmitter.setEnabled(false);
            this.healthPSystem.addEmitter(healthPEmitter);
            this.bite = new Sound("assets/sound/bite.ogg");
            this.jump = new Sound("assets/sound/jump.ogg");
            this.water = new Sound("assets/sound/water.ogg");
            this.splash = new Sound("assets/sound/splash.ogg");
        } catch(IOException | SlickException ex) {
            ex.printStackTrace();
        }

        this.waterSoundStarted = false;
        this.livingSharks = livingSharks;
        this.healing = false;
        this.msSinceHealingStarted = 0;
    }


    public Boat(float baseY, List<Shark> livingSharks, double maxHealth, Vector2f position) {
        this(IMAGE_PATH, livingSharks, baseY, maxHealth, position);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        pSystem.render();
        renderImage.draw(getPosition().getX(), getPosition().getY());
        healthBar.render(gameContainer, stateBasedGame, graphics);
        if(healing) {
            healthPSystem.render();
        }
        //graphics.draw(hitbox);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        if(!waterSoundStarted) {
            this.water.loop(WATER_SOUND_PITCH, WATER_SOUND_VOLUME);
            waterSoundStarted = true;
        }
        if(isJumping()) {
            if(water.playing()) {
                water.stop();
            }
//            pSystem.getEmitter(0).setEnabled(false);
            if(pSystem.getEmitterCount() > 0) {
                pSystem.getEmitter(0).wrapUp();
            }
            position.y = position.getY() - (delta/1000.0f * jumpVelocity);
            healthPSystem.setPosition(position.getX() + renderImage.getWidth() / 2, position.getY() + renderImage.getHeight());
            hitbox.setY(position.getY());
            jumpVelocity -= SharkJumpGame.G_FIELD_STRENGTH * delta/1000.0f;

            if(position.y > baseY) {
                setJumping(false);
                playSplashSound();
                ConfigurableEmitter e = pEmitter.duplicate(); // copy initial emitter
                e.setEnabled(true); // enable
                water.loop(WATER_SOUND_PITCH, WATER_SOUND_VOLUME);
                pSystem.addEmitter(e);
                position.y = baseY;
                healthPSystem.setPosition(position.getX() + renderImage.getWidth() / 2, position.getY() + renderImage.getHeight());
                hitbox.setY(baseY);
            }
        }

        Shark collidingShark = getCollidingShark(livingSharks);

        if(collidingShark != null) {
            livingSharks.remove(collidingShark);
            setHealth(getHealth() - SharkJumpGame.DEFAULT_DAMAGE_FROM_SHARK);
            playBiteSound();
        }

        healthBar.update(gameContainer, stateBasedGame, delta);
        pSystem.update(delta);

        if(healing) {
            msSinceHealingStarted += delta;

            if(msSinceHealingStarted >= HEAL_DURATION) {
                healing = false;
                msSinceHealingStarted = 0;
                healthPSystem.getEmitter(0).setEnabled(false);
            }

            healthPSystem.update(delta);
        }
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        if(health > 0 && health <= maxHealth) {
            this.health = health;
            healthBar.setHealth(this.health);
        }

        if(health <= 0) {
            this.health = 0;
            healthBar.setHealth(this.health);
            alive = false;
            stopWaterSound();
        }
    }

    public Vector2f getPosition() {
        return position;
    }

    public Shape getHitbox() {
        return hitbox;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;

        if(jumping) {
            jumpVelocity = MAX_JUMP_SPEED;
        } else {
            jumpVelocity = 0;
        }
    }

    public float getJumpVelocity() {
        return jumpVelocity;
    }

    public void setJumpVelocity(float jumpVelocity) {
        if(jumpVelocity < MAX_JUMP_SPEED && jumpVelocity > -MAX_JUMP_SPEED) {
            this.jumpVelocity = jumpVelocity;
        }

        if(jumpVelocity > MAX_JUMP_SPEED) {
            this.jumpVelocity = MAX_JUMP_SPEED;
        }

        if(jumpVelocity < -MAX_JUMP_SPEED) {
            this.jumpVelocity = -MAX_JUMP_SPEED;
        }
    }

    public void playBiteSound() {
        bite.play(1, .8f);
    }

    public void playJumpSound() {
        jump.play(.5f, .1f);
    }

    public void playSplashSound() {
        splash.play(1, .5f);
    }

    public void stopWaterSound() {
        if(water.playing()) {
            water.stop();
        }
    }

    public int getImageWidth() {
        return renderImage.getWidth();
    }

    public int getImageHeight() {
        return renderImage.getHeight();
    }

    public Shark getCollidingShark(List<Shark> sharks) {
        for(int i = 0; i < sharks.size(); i++) {
            if (sharks.get(i).getHitbox().intersects(this.getHitbox())) {
                return sharks.get(i);
            }
        }

        return null;
    }

    public List<Shark> getLivingSharks() {
        return livingSharks;
    }

    public void playHealParticles() {
        if(!healing) {
            healing = true;
            healthPSystem.getEmitter(0).setEnabled(true);
        }
    }
}
