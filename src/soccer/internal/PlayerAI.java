package soccer.internal;


import movement.steering.SteeringBehavior;
import movement.steering.SteeringInfo;

public interface PlayerAI {

    void init(SoccerPlayer soccerPlayer, SoccerGame game);
    SteeringBehavior getSteering(SoccerPlayer soccerPlayer, SoccerGame game);
    SteeringInfo getBallSteering(SoccerPlayer soccerPlayer, SoccerGame game);

    String getTeamName();

    String talk(SoccerPlayer soccerPlayer, SoccerGame game);

    void hear(int playerId,String talk);
}
