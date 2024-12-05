package soccer.internal;



import movement.Ball;
import movement.StaticInfo;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import static soccer.internal.SoccerGame.FatigueStamina;
import static soccer.internal.SoccerGame.MaxStamina;

public class SoccerPlayerImage extends Ball {

    private static final float StaminaBarWidth = 20;
    private static final float StaminaBarHeight = 5;
    private static final Color StaminaBarColor = Color.yellow;
    private static final Color FatigueStaminaBarColor = Color.red;

    public void setPlayer(SoccerPlayer player) {
        this.player = player;
    }

    SoccerPlayer player;

    public SoccerPlayerImage(Color color, float radius) {
        super(color, radius);
    }



    @Override
    public  void _render(StaticInfo pos, Graphics g) {
        super._render( pos,g);

        if (player.getStamina()<FatigueStamina)
            g.setColor(FatigueStaminaBarColor);
        else g.setColor(StaminaBarColor);
        g.fillRect((float) pos.getPosition().x(),(float)( pos.getPosition().y()+2*radius), (float) (StaminaBarWidth*player.getStamina()/MaxStamina), StaminaBarHeight);
    }
}
