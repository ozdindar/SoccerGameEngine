package movement;

import org.newdawn.slick.Graphics;

public interface Renderable {
    void render(StaticInfo gameEntity, Graphics graphics);

    double getRadius();


}
