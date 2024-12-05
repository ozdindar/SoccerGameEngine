package soccer.hw3;


import math.geom2d.Vector2D;
import movement.steering.SteeringBehavior;
import movement.steering.SteeringInfo;
import soccer.internal.PlayerAI;
import soccer.internal.SoccerGame;
import soccer.internal.SoccerPlayer;

public class YourAI implements PlayerAI {


    @Override
    public void init(SoccerPlayer soccerPlayer, SoccerGame game) {
        // TODO: Whatever you want to do at the beginning of the match do here
    }

    @Override
    public SteeringBehavior getSteering(SoccerPlayer soccerPlayer, SoccerGame game) {
        // TODO: Returns the steering behavior (movement) of the player
        //       Currently it always return Zero-Steering.
        return (s,k)->SteeringInfo.getNoSteering();
    }

    @Override
    public SteeringInfo getBallSteering(SoccerPlayer soccerPlayer, SoccerGame game) {

         // TODO: Returns the steering behavior of the ball  ( when player can kick the ball)
        //        Currently it always return Zero-Steering.


        return SteeringInfo.getNoSteering();

    }

    @Override
    public String getTeamName() {
        // TODO: Change it to your team name

        return "Your Team";
    }

    @Override
    public String talk(SoccerPlayer soccerPlayer, SoccerGame game) {
        // TODO: Implement necessary piece of code you want your player
        //       something when a certain conditions happens to be true
        return "";
    }

    @Override
    public void hear(int playerId, String talk) {
        // TODO: Implement the necessary updates-operations (if any)
        //       when the player hear something(talk) from the player with playerId

    }
}
