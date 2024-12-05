package movement.steering;



import movement.KinematicInfo;
import movement.StaticInfo;
import math.geom2d.Vector2D;



/**
 * Created by dindar.oz on 9/19/2017.
 */
public class Arrive implements SteeringBehavior {


    private static final double maxAcceleration = 0.3;
    private static final double maxSpeed =1;

    private static final double targetRadius = 1;
    private static final double slowDownRadius =20;

    private static final double timeToTarget= 0.1;

    Vector2D targetPosition;


    public Arrive(Vector2D ts) {
        this.targetPosition = ts;
    }

    @Override
    public SteeringInfo getSteering(StaticInfo staticInfo , KinematicInfo kinematicInfo)
    {
        Vector2D direction = targetPosition.minus(staticInfo.getPosition());
        double distance = direction.norm();

        if (distance<targetRadius)
            return SteeringInfo.getNoSteering();

        double targetSpeed = (distance>slowDownRadius)?  maxSpeed: maxSpeed*distance/slowDownRadius;

        Vector2D targetVelocity = direction;
        targetVelocity = targetVelocity.normalize();
        targetVelocity = targetVelocity.times(targetSpeed);

        Vector2D linear = targetVelocity.minus(kinematicInfo.getVelocity());
        linear = linear.times(1/timeToTarget);

        if (linear.norm()>maxAcceleration)
        {
            linear = linear.normalize();
            linear = linear.times(maxAcceleration);
        }

        return new SteeringInfo(linear,0, SteeringInfo.SteeringType.Dynamic);
    }


}
