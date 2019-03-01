package org.bboed.students.ahmedmoustafa18.states;

import org.bboed.students.ahmedmoustafa18.GameFactory;
import org.bboed.students.ahmedmoustafa18.SharkJump;
import org.bboed.students.ahmedmoustafa18.entities.Boat;
import org.bboed.students.ahmedmoustafa18.entities.Shark;
import org.bboed.students.ahmedmoustafa18.entities.powerup.Barricade;
import org.bboed.students.ahmedmoustafa18.entities.powerup.PowerUp;
import org.bboed.students.ahmedmoustafa18.ui.ScoreBoard;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;

public class SharkJumpGame extends BasicGameState {

    private static final float INITIAL_BOAT_X = 150;
    private static final float INITIAL_BOAT_Y = SharkJump.GAME_HEIGHT - Boat.BOAT_WINDOW_MARGIN;
    private static final String SCENE_BACKGROUND_IMAGE_PATH = "assets/images/scenes/game_scene/background.png";
    public static final double G_FIELD_STRENGTH = 725; //pixels/sec^2
    private static final double DEFAULT_SHARK_SPAWN_RATE = 7; //percent per tick
    private static final double DEFAULT_SHARK_SWIM_SPEED = 375; //pixels per second
    private static final double MAX_SHARK_SWIM_SPEED = 700; //pixels per second
    public static final double DEFAULT_DAMAGE_FROM_SHARK = 10;
    private static final double DEFAULT_SCORE_PER_SECOND = 15;
    private static final double DEFAULT_SHARK_BATCH_SPAWN_RATE = 25; //percent per spawn trigger
    private static final double MINIMUM_LEFT_LEFT_SHARK_DISTANCE = 390;
    private static final double MINIMUM_LEFT_LEFT_SHARK_IN_BATCH_DISTANCE = 86;
    private static final double CLOSEST_SHARKS_DISTANCE_IN_BATCH = 25;
    private static final double MAX_TIME_BEFORE_SHARK_SPAWN = 8000; //milliseconds
    private static final double DEFAULT_POWER_UP_SPAWN_RATE = .38; //percent per tick .38
    private static final double HEAL_SPAWN_RATE = 25; //percent per spawn trigger
    private static final double BARRICADE_SPAWN_RATE = 75;
    private static final int MS_PER_TICK = 125;
    public static final float ENTITY_WINDOW_MARGIN = 191;

    private Image backgroundImage;
    private Boat boat;
    private List<Shark> livingSharks;
    private List<PowerUp> visiblePowerUps;
    private List<PowerUp> activePowerUps;
    private double sharkSpawnRate;
    private double sharkSwimSpeed;
    private double damageFromShark;
    private double difficultyMultiplier;
    private double score;
    private Color scoreTextColor;
    private double scorePerSecond;
    private double sharkBatchSpawnRate;
    private double millisSinceLastSharkSpawn;
    private ScoreBoard scoreboard;
    private int msSinceLastTick = 0;

    @Override
    public int getID() {
        return StatesList.GAME_STATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        backgroundImage = new Image(SCENE_BACKGROUND_IMAGE_PATH);
        livingSharks = new ArrayList<>();
        boat = new Boat(INITIAL_BOAT_Y, livingSharks, 100, new Vector2f(INITIAL_BOAT_X, INITIAL_BOAT_Y));
        visiblePowerUps = new ArrayList<>();
        activePowerUps = new ArrayList<>();
        sharkSpawnRate = DEFAULT_SHARK_SPAWN_RATE;
        sharkSwimSpeed = DEFAULT_SHARK_SWIM_SPEED;
        damageFromShark = DEFAULT_DAMAGE_FROM_SHARK;
        difficultyMultiplier = 1;
        score = 0;
        scoreTextColor = Color.white;
        scorePerSecond = DEFAULT_SCORE_PER_SECOND;
        sharkBatchSpawnRate = DEFAULT_SHARK_BATCH_SPAWN_RATE;
        millisSinceLastSharkSpawn = 0;
        scoreboard = new ScoreBoard(new Vector2f((SharkJump.GAME_WIDTH / 2) - (ScoreBoard.SCOREBOARD_WIDTH / 2), -ScoreBoard.SCOREBOARD_HEIGHT),
                (SharkJump.GAME_HEIGHT / 2) - (ScoreBoard.SCOREBOARD_HEIGHT / 2), 0);
        msSinceLastTick = 0;
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        backgroundImage.draw(0, 0);
        if(boat.isAlive()) {
            boat.render(gameContainer, stateBasedGame, graphics);

            for(int i = 0; i < livingSharks.size(); i++) {
                livingSharks.get(i).render(gameContainer, stateBasedGame, graphics);
            }

            for(int i = 0; i < visiblePowerUps.size(); i++) {
                visiblePowerUps.get(i).render(gameContainer, stateBasedGame, graphics);
            }

            for(int i = 0; i < activePowerUps.size(); i++) {
                activePowerUps.get(i).render(gameContainer, stateBasedGame, graphics);
            }

            graphics.setColor(scoreTextColor);
            graphics.drawString("SCORE: " + (int) score, 10, 10);
        } else {
            scoreboard.render(gameContainer, stateBasedGame, graphics);
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        msSinceLastTick += delta;
        if(gameContainer.getInput().isKeyDown(Input.KEY_SPACE) && !boat.isJumping()) {
            boat.setJumping(true);
            boat.playJumpSound();
        }

        if(boat.isAlive()) {
            boat.update(gameContainer, stateBasedGame, delta);
        } else if(livingSharks.size() > 0) {
            for(int i = 0; i < livingSharks.size(); i++) {
                livingSharks.remove(i);
            }
        } else {
            if(!scoreboard.isPanStarted()) {
                scoreboard.setScore((int) score);
                scoreboard.setShouldPan(true);
                scoreboard.setPanStarted(true);
            }
            scoreboard.update(gameContainer, stateBasedGame, delta);
        }

        if(msSinceLastTick >= MS_PER_TICK) {
            msSinceLastTick = 0;
            if(((Math.random() * 101 <= sharkSpawnRate) || (millisSinceLastSharkSpawn >= MAX_TIME_BEFORE_SHARK_SPAWN)) && boat.isAlive()) {
                //find the right-most shark
                float rightMostSharkX = Math.max(getRightMostPowerUpX(), getRightMostSharkX());

                if(rightMostSharkX > SharkJump.GAME_WIDTH - MINIMUM_LEFT_LEFT_SHARK_DISTANCE) {
                    livingSharks.add(GameFactory.createShark(new Vector2f((float) (rightMostSharkX + MINIMUM_LEFT_LEFT_SHARK_DISTANCE),
                            SharkJump.GAME_HEIGHT - GameFactory.SHARK_WINDOW_MARGIN), sharkSwimSpeed));
                    rightMostSharkX += MINIMUM_LEFT_LEFT_SHARK_DISTANCE;
                } else {
                    livingSharks.add(GameFactory.createShark(new Vector2f((float) (SharkJump.GAME_WIDTH),
                            SharkJump.GAME_HEIGHT - GameFactory.SHARK_WINDOW_MARGIN), sharkSwimSpeed));
                    rightMostSharkX = SharkJump.GAME_WIDTH;
                }

                millisSinceLastSharkSpawn = 0;

                if(Math.random() * 101 <= sharkBatchSpawnRate) {
                    //spawn batch

                    livingSharks.add(GameFactory.createShark(
                            new Vector2f((float) (rightMostSharkX + (Math.random() * (MINIMUM_LEFT_LEFT_SHARK_IN_BATCH_DISTANCE -
                                    CLOSEST_SHARKS_DISTANCE_IN_BATCH)) + CLOSEST_SHARKS_DISTANCE_IN_BATCH),
                                    SharkJump.GAME_HEIGHT - GameFactory.SHARK_WINDOW_MARGIN), sharkSwimSpeed));
                }
            } else {
                millisSinceLastSharkSpawn += delta;
            }

            if(Math.random() * 101 <= DEFAULT_POWER_UP_SPAWN_RATE && boat.isAlive()) {

                float rightMostSharkX = getRightMostSharkX();
                float powerUpSpawnX = (float) Math.max(getRightMostPowerUpX() + MINIMUM_LEFT_LEFT_SHARK_DISTANCE, Math.max(rightMostSharkX + MINIMUM_LEFT_LEFT_SHARK_DISTANCE, SharkJump.GAME_WIDTH));

                double randomNumber = Math.random() * 101;

                boolean flag = true;

                if (randomNumber <= HEAL_SPAWN_RATE) {
                    visiblePowerUps.add(GameFactory.createPowerUp(new Vector2f(powerUpSpawnX, PowerUp.Y_POSITION), PowerUp.POWER_UP.HEAL));
                } else {
                    for(PowerUp pUP : activePowerUps) {
                        if(pUP instanceof Barricade) {
                            flag = false;
                            break;
                        }
                    }

                    for(PowerUp pUP : visiblePowerUps) {
                        if(pUP instanceof Barricade) {
                            flag = false;
                            break;
                        }
                    }

                    if(flag) {
                        visiblePowerUps.add(GameFactory.createPowerUp(new Vector2f(powerUpSpawnX, PowerUp.Y_POSITION), PowerUp.POWER_UP.BARRICADE));
                    }
                }
            }
        }

        if(boat.isAlive()) {
            score += scorePerSecond * delta / 1000.0;
        }

        for(int i = 0; i < livingSharks.size(); i++) {
            if(!livingSharks.get(i).isAlive()) {
                livingSharks.remove(i);
                continue;
            }

            livingSharks.get(i).update(gameContainer, stateBasedGame, delta);
        }

        for(int i = 0; i < visiblePowerUps.size(); i++) {
            if(!visiblePowerUps.get(i).isVisible()) {
                visiblePowerUps.remove(i);
                continue;
            }

            visiblePowerUps.get(i).update(gameContainer, stateBasedGame, delta);
            if(visiblePowerUps.get(i).getHitbox().intersects(boat.getHitbox())) {
                visiblePowerUps.get(i).executePower(boat);
                activePowerUps.add(visiblePowerUps.get(i));
                visiblePowerUps.remove(i);
            }
        }

        for(int i = 0; i < activePowerUps.size(); i++) {
            if(!activePowerUps.get(i).isActive()) {
                activePowerUps.remove(i);
                continue;
            }

            activePowerUps.get(i).update(gameContainer, stateBasedGame, delta);
        }

    }

    private float getRightMostSharkX() {
        float rightMostSharkX = Float.MIN_VALUE;

        for(Shark shark : livingSharks) {
            if(shark.getHitbox().getX() > rightMostSharkX) {
                rightMostSharkX = shark.getHitbox().getX();
            }
        }

        return rightMostSharkX;
    }

    private float getRightMostPowerUpX() {
        float rightMostPowerUpX = Float.MIN_VALUE;

        for(PowerUp powerUp : visiblePowerUps) {
            if(powerUp.getHitbox().getX() > rightMostPowerUpX) {
                rightMostPowerUpX = powerUp.getHitbox().getX();
            }
        }

        return rightMostPowerUpX;
    }
}
