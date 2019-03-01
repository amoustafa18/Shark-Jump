package org.bboed.students.ahmedmoustafa18;

import org.bboed.students.ahmedmoustafa18.states.SharkJumpGame;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.io.File;

public class SharkJump extends StateBasedGame {

    private static final String GAME_TITLE = "Shark Jump";
    public static final float GAME_WIDTH = 1200;
    public static final float GAME_HEIGHT = 600;
    private static final boolean SHOW_FPS = false; //for debugging purposes

    public static void main(String[] args) throws SlickException {
        System.setProperty("java.library.path", "lib");
        System.setProperty("org.lwjgl.librarypath", new File("native/windows").getAbsolutePath());

        AppGameContainer appgc = new AppGameContainer(new SharkJump(GAME_TITLE), (int) GAME_WIDTH, (int) GAME_HEIGHT, false);

        appgc.setUpdateOnlyWhenVisible(false);
        appgc.setShowFPS(SHOW_FPS);
        appgc.start();
    }

    public SharkJump(String name) {
        super(name);
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.addState(new SharkJumpGame());
    }
}
