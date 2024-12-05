package soccer.internal;


import movement.KinematicInfo;
import movement.StaticInfo;
import movement.steering.SteeringBehavior;
import movement.steering.SteeringInfo;

import static soccer.internal.SoccerGame.FatigeSlowDownRatio;
import static soccer.internal.SoccerGame.FatigueSpeed;

public class FatigueBehavior implements SteeringBehavior {


    @Override
    public SteeringInfo getSteering(StaticInfo staticInfo, KinematicInfo kinematicInfo) {
        if (kinematicInfo.getVelocity().norm()> FatigueSpeed)
        {
            return new SteeringInfo(kinematicInfo.getVelocity().opposite().normalize().times(FatigeSlowDownRatio),0, SteeringInfo.SteeringType.Dynamic);
        }
        return SteeringInfo.getNoSteering();
    }


}
