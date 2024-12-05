package movement.steering;



import movement.KinematicInfo;
import movement.StaticInfo;
import math.geom2d.Vector2D;

/**
 * Created by dindar.oz on 9/19/2017.
 */
public class NoSteering implements SteeringBehavior {



    @Override
    public SteeringInfo getSteering(StaticInfo staticInfo , KinematicInfo kinematicInfo)
    {


        SteeringInfo steeringOutput = new SteeringInfo(new Vector2D(0,0),0, SteeringInfo.SteeringType.Dynamic);
        return steeringOutput;
    }



}
