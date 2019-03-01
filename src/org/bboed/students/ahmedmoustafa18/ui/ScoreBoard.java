package org.bboed.students.ahmedmoustafa18.ui;

import org.bboed.students.ahmedmoustafa18.DoesRenderAndUpdate;
import org.bboed.students.ahmedmoustafa18.SharkJump;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import java.awt.*;

public class ScoreBoard extends TopDownPanningBoard implements DoesRenderAndUpdate {

    public static final String IMAGE_PATH = "assets/images/ui/scoreboard.png";
    public static final float SCOREBOARD_WIDTH = 600;
    public static final float SCOREBOARD_HEIGHT = 300;
    private static final Color SCORE_TEXT_COLOR = new Color(149, 129, 22);

    private int score;
    private java.awt.Font font;
    private String scoreText = "SCORE: ";
    private TrueTypeFont ttf;

    public ScoreBoard(Vector2f position, float panSpeed, float finalY, int score) {
        super(position, IMAGE_PATH, panSpeed, finalY);

        this.score = score;
        this.font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 46);
        this.ttf = new TrueTypeFont(font, true);
    }

    public ScoreBoard(Vector2f position, float finalY, int score) {
        this(position, TopDownPanningBoard.DEFAULT_PAN_SPEED, finalY, score);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        super.render(gameContainer, stateBasedGame, graphics);
        // initialise the font

        ttf.drawString(((SharkJump.GAME_WIDTH / 2) - (ttf.getWidth(scoreText) / 2)), getPosition().getY() + 50, scoreText, SCORE_TEXT_COLOR);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        super.update(gameContainer, stateBasedGame, delta);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        this.scoreText += score;
    }
}
