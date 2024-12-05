package movement;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class BasicGameEntity implements GameEntity {

    protected Renderable shape;
    protected StaticInfo staticInfo;

    public BasicGameEntity(Renderable shape, StaticInfo staticInfo) {
        this.shape = shape;
        this.staticInfo = staticInfo;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) {

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {
        shape.render(staticInfo,graphics);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) {

    }

    protected Renderable getShape() {
        return shape;
    }
}
