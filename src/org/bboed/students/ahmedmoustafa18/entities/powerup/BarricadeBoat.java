package org.bboed.students.ahmedmoustafa18.entities.powerup;

import org.bboed.students.ahmedmoustafa18.entities.Boat;
import org.bboed.students.ahmedmoustafa18.entities.Shark;
import org.newdawn.slick.geom.Vector2f;

import java.util.List;

public class BarricadeBoat extends Boat {

    public BarricadeBoat(float baseY, List<Shark> livingSharks, double maxHealth, Vector2f position) {
        super(baseY, livingSharks, maxHealth, position);
    }

    @Override
    public void setJumping(boolean jumping) {
        //intentionally left blank
    }
}
