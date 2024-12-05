package movement.steering;


import movement.KinematicInfo;
import movement.StaticInfo;

public interface SteeringBehavior {
    SteeringInfo getSteering(StaticInfo staticInfo, KinematicInfo kinematicInfo);


}
