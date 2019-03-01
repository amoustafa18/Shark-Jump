package org.bboed.students.ahmedmoustafa18.entities.powerup;

import org.bboed.students.ahmedmoustafa18.GameFactory;
import org.bboed.students.ahmedmoustafa18.SharkJump;
import org.bboed.students.ahmedmoustafa18.entities.Boat;
import org.bboed.students.ahmedmoustafa18.states.SharkJumpGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class Barricade extends PowerUp {

    private static final String IMAGE_PATH = "assets/images/sprites/powerups/boat.png";
    private static final int MIN_HEALTH = 100;
    private static final int MAX_HEALTH = 250;
    private static final int BARRICADE_X_POS = 600;
    private Sound drill;
    private Sound hammer;

    private BarricadeBoat barricade;

    public Barricade(Vector2f position) {
        super(IMAGE_PATH, position, POWER_UP.BARRICADE, PowerUp.DEFAULT_SPEED);

        try {
            drill = new Sound("assets/sound/drill.ogg");
            hammer = new Sound("assets/sound/hammer.ogg");
        } catch(SlickException ex) {

        }
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        super.render(gameContainer, stateBasedGame, graphics);

        if(isActive() && !isVisible()) {
            barricade.render(gameContainer, stateBasedGame, graphics);
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        super.update(gameContainer, stateBasedGame, delta);

        if(isActive() && !isVisible()) {
            barricade.update(gameContainer, stateBasedGame, delta);

            if(!barricade.isAlive()) {
                setActive(false);
            }
        }
    }

    @Override
    public void executePower(Boat boat) {
        barricade = GameFactory.createBarricadeBoat(SharkJump.GAME_HEIGHT - Boat.BOAT_WINDOW_MARGIN,
                boat.getLivingSharks(),
                ((int) Math.random() * (MAX_HEALTH - MIN_HEALTH) + MIN_HEALTH),
                new Vector2f(BARRICADE_X_POS, SharkJump.GAME_HEIGHT - Boat.BOAT_WINDOW_MARGIN));
        setVisible(false);
        setActive(true);
        hammer.play();
        drill.play();
    }
}
