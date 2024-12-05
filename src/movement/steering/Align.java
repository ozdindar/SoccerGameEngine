package movement.steering;


import movement.KinematicInfo;
import movement.StaticInfo;
import math.geom2d.Vector2D;


/**
 * Created by dindar.oz on 9/19/2017.
 */
public class Align implements SteeringBehavior {

    StaticInfo target;

    private static final double maxAngularAcceleration = 0.1;
    private static final double maxRotation =0.1;

    private static final double targetRadius = 0.001;
    private static final double slowDownRadius =0.2;

    private static final double timeToTarget= 0.1;

    public Align(StaticInfo target) {
        this.target = target;
    }

    @Override
    public SteeringInfo getSteering(StaticInfo staticInfo , KinematicInfo kinematicInfo)
    {
       double rotation = target.getOrientation()-staticInfo.getOrientation();

       rotation = mapToRange(rotation);
       double rotationSize= Math.abs(rotation);

       if (rotationSize<targetRadius)
           return SteeringInfo.getNoSteering();

       double targetRotation = (rotationSize>slowDownRadius)? maxRotation: maxRotation*rotationSize/slowDownRadius;
       targetRotation *= (rotation/rotationSize);
       double angular = targetRotation-kinematicInfo.getRotation();
       angular /= timeToTarget;
       double angularAccleration = Math.abs(angular);
       if (angularAccleration>maxAngularAcceleration)
       {
           angular /= angularAccleration;
           angular*= maxAngularAcceleration;
       }

       return new SteeringInfo(new Vector2D(0,0),angular, SteeringInfo.SteeringType.Dynamic);
    }



    private double mapToRange(double rotation) {
        while (rotation<-Math.PI)
            rotation += Math.PI*2;
        while (rotation>Math.PI)
            rotation-= Math.PI*2;
        return rotation;
    }

}
