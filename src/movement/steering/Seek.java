package movement.steering;

import movement.KinematicInfo;
import movement.StaticInfo;
import math.geom2d.Vector2D;


public class Seek implements SteeringBehavior {

    private static final double SeekThreshold = 0.01;
    Vector2D targetPosition;

    double maxacceleration = 0.5;

    public Seek(Vector2D targetPosition) {
        this.targetPosition = targetPosition;
    }

    @Override
    public SteeringInfo getSteering(StaticInfo staticInfo, KinematicInfo kinematicInfo) {
        if (targetPosition == null) {
            return SteeringInfo.getNoSteering();
        }
        assert staticInfo != null;
        Vector2D linear = targetPosition.minus(staticInfo.getPosition());
        double rotation =0;

        if (linear.norm()<SeekThreshold)
            return SteeringInfo.getNoSteering();
        if (linear.norm()>maxacceleration)
        {
            linear= linear.normalize().times(maxacceleration);
        }

        return new SteeringInfo(linear,rotation, SteeringInfo.SteeringType.Dynamic);
    }


}
