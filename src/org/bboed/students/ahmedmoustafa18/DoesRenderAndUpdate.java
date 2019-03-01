package org.bboed.students.ahmedmoustafa18;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public interface DoesRenderAndUpdate {
    void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException;
    void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException;
}
