package soccer.internal;

import math.geom2d.Vector2D;
import movement.GameEntity;
import movement.KinematicInfo;
import movement.steering.SteeringInfo;

public interface SoccerPlayer extends GameEntity {
    Vector2D getInitialPosition();
    Vector2D getPosition();
    Vector2D getVelocity();
    int getTeam();

    double getStamina();

    String talk();

    void hear(int playerId, String talk);

    SteeringInfo getBallSteering();

    int getID();


    KinematicInfo getKinematicInfo();
}
