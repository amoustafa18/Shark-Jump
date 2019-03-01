package org.bboed.students.ahmedmoustafa18;

import org.bboed.students.ahmedmoustafa18.entities.powerup.Barricade;
import org.bboed.students.ahmedmoustafa18.entities.powerup.BarricadeBoat;
import org.bboed.students.ahmedmoustafa18.entities.powerup.Heal;
import org.bboed.students.ahmedmoustafa18.entities.powerup.PowerUp;
import org.bboed.students.ahmedmoustafa18.entities.Shark;
import org.newdawn.slick.geom.Vector2f;

import java.util.List;

public abstract class GameFactory {

    public static int SHARK_WINDOW_MARGIN = 248;

    public static Shark createShark(double swimSpeed) {
        return createShark(new Vector2f(SharkJump.GAME_WIDTH, SharkJump.GAME_HEIGHT - SHARK_WINDOW_MARGIN), swimSpeed);
    }

    public static Shark createShark(Vector2f position, double swimSpeed) {
        return new Shark(position, swimSpeed);
    }

    public static PowerUp createPowerUp(Vector2f position, PowerUp.POWER_UP type) {
        switch(type) {
            case HEAL:
                return new Heal(position);
            case DOUBLE_POINTS:
                return null;
            case BARRICADE:
                return new Barricade(position);
            default:
                return null;
        }
    }

    public static BarricadeBoat createBarricadeBoat(float baseY, List<Shark> livingShars, int maxHealth, Vector2f position) {
        return new BarricadeBoat(baseY, livingShars, maxHealth, position);
    }
}
