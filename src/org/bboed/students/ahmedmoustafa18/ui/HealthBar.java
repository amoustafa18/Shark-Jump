package org.bboed.students.ahmedmoustafa18.ui;

import org.bboed.students.ahmedmoustafa18.DoesRenderAndUpdate;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tests.SoundTest;
import org.w3c.dom.css.Rect;

import javax.xml.bind.annotation.XmlType;
import java.util.HashMap;
import java.util.Map;

public class HealthBar implements DoesRenderAndUpdate {

    public static final Color DEFAULT_BACKGROUND_COLOR = Color.white;
    public static final Color DEFAULT_FILL_COLOR = Color.green;
    public static final Map<Double, Color> DEFAULT_COLOR_THRESHOLDS;
    public static final Map<Double, Color> NO_COLOR_THRESHOLDS = null; //only use fillColor
    private static final double DEFAULT_COLOR_THRESHOLD_GREEN = 100;
    private static final double DEFAULT_COLOR_THRESHOLD_ORANGE = 60;
    private static final double DEFAULT_COLOR_THRESHOLD_RED = 25;
    private static final double FILL_PADDING = 2;

    private double health;
    private double maxHealth;
    private double width;
    private double height;
    private Vector2f position;
    private Rectangle background;
    private Rectangle fill;
    private Color backgroundColor;
    private Color fillColor;
    private Map<Double, Color> colorThresholds;

    static {
        DEFAULT_COLOR_THRESHOLDS = new HashMap<>();
        DEFAULT_COLOR_THRESHOLDS.put(DEFAULT_COLOR_THRESHOLD_GREEN, Color.green);
        DEFAULT_COLOR_THRESHOLDS.put(DEFAULT_COLOR_THRESHOLD_ORANGE, Color.orange);
        DEFAULT_COLOR_THRESHOLDS.put(DEFAULT_COLOR_THRESHOLD_RED, Color.red);
    }

    public HealthBar(double health, double maxHealth, double width, double height, Vector2f position, Color backgroundColor, Color fillColor,
                     Map<Double, Color> colorThresholds) {
        this.health = health;
        this.maxHealth = maxHealth;
        this.width = width;
        this.height = height;
        this.position = position;
        this.background = new Rectangle(position.getX(), position.getY(), (float) width, (float) height);
        this.fill = new Rectangle((float) (position.getX() + FILL_PADDING), (float) (position.getY() + FILL_PADDING),
                (float) (width - FILL_PADDING * 2), (float) (height - FILL_PADDING * 2));
        this.backgroundColor = backgroundColor;
        this.fillColor = fillColor;
        this.colorThresholds = colorThresholds;
    }

    public HealthBar(double health, double maxHealth, double width, double height, Vector2f position) {
        this(health, maxHealth, width, height, position, DEFAULT_BACKGROUND_COLOR, DEFAULT_FILL_COLOR, DEFAULT_COLOR_THRESHOLDS);
    }

    public HealthBar(double health, double maxHealth, double width, double height, Vector2f position, Map<Double, Color> colorThresholds) {
        this(health, maxHealth, width, height, position, DEFAULT_BACKGROUND_COLOR, DEFAULT_FILL_COLOR, colorThresholds);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.setColor(backgroundColor);
        graphics.fill(background);
        graphics.setColor(fillColor);
        graphics.fill(fill);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {

    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        if(health <= maxHealth && health >= 0) {
            this.health = health;
        }

        if(health > maxHealth) {
            this.health = maxHealth;
        }

        if(health < 0) {
            this.health = 0;
        }

        if(colorThresholds != NO_COLOR_THRESHOLDS) {
            double lowestKeyGreaterThanHealth = Double.MAX_VALUE;
            for(Map.Entry<Double, Color> entry : colorThresholds.entrySet()) {
                if(this.health <= entry.getKey() && entry.getKey() < lowestKeyGreaterThanHealth) {
                    lowestKeyGreaterThanHealth = entry.getKey();
                }
            }

            fillColor = colorThresholds.get(lowestKeyGreaterThanHealth);
        }

        fill.setBounds((float) (position.getX() + FILL_PADDING), (float) (position.getY() + FILL_PADDING),
                (float) ((this.health / maxHealth) * (width - FILL_PADDING * 2)), (float) (height - FILL_PADDING * 2));
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }
}
