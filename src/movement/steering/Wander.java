package movement.steering;


import movement.KinematicInfo;
import movement.StaticInfo;
import util.RandomUtils;
import math.geom2d.Vector2D;



/**
 * Created by dindar.oz on 9/22/2017.
 */
public class Wander implements SteeringBehavior {

    double wanderRadius = 30;
    double wanderOffset= 40;

    double wanderRate =2 ;

    double maximumAcceleration = 0.1;

    public Wander() {

    }

    @Override
    public SteeringInfo getSteering(StaticInfo staticInfo, KinematicInfo kinematicInfo) {

        double wanderOrientation = RandomUtils.randomBinomial()*wanderRate;

        double targetOrientation = wanderOrientation+ staticInfo.getOrientation();


        Vector2D targetV = staticInfo.getPosition().plus(Vector2D.createPolar(wanderOffset,staticInfo.getOrientation()));
        targetV = targetV.plus(Vector2D.createPolar(wanderRadius,targetOrientation));


        StaticInfo newTarget = new StaticInfo(targetV,0);
        Face face = new Face(newTarget);
        SteeringInfo steeringOutput = face.getSteering(staticInfo,kinematicInfo);

        steeringOutput.linear = Vector2D.createPolar(maximumAcceleration,staticInfo.getOrientation()) ;
        return  steeringOutput;
    }


}
