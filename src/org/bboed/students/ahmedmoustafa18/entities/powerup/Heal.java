package org.bboed.students.ahmedmoustafa18.entities.powerup;

import org.bboed.students.ahmedmoustafa18.entities.Boat;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Vector2f;

public class Heal extends PowerUp {

    private static final String HEAL_IMAGE_PATH = "assets/images/sprites/powerups/heal.png";
    private static final int HEAL_AMOUNT = 10;

    private Sound healSound;

    public Heal(Vector2f position) {
        super(HEAL_IMAGE_PATH, position, POWER_UP.HEAL);

        try {
            this.healSound = new Sound("assets/sound/heal.ogg");
        } catch(SlickException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void executePower(Boat boat) {
        int originalHealth = (int) boat.getHealth();
        boat.setHealth(boat.getHealth() + HEAL_AMOUNT);

        if(originalHealth < boat.getHealth()) {
            boat.playHealParticles();
            healSound.play();
        }

        setVisible(false);
        setActive(false);
    }
}
