package soccer;


import math.geom2d.Vector2D;
import movement.steering.SteeringBehavior;
import movement.steering.SteeringInfo;
import soccer.internal.PlayerAI;
import soccer.internal.SoccerGame;
import soccer.internal.SoccerPlayer;

public class DummyAI implements PlayerAI {


    Vector2D kickTarget;

    @Override
    public void init(SoccerPlayer soccerPlayer, SoccerGame game) {
        kickTarget = game.getGoalAreaCenter(game.opponent(soccerPlayer.getTeam()));
    }

    @Override
    public SteeringBehavior getSteering(SoccerPlayer soccerPlayer, SoccerGame game) {

        return null;
    }

    @Override
    public SteeringInfo getBallSteering(SoccerPlayer soccerPlayer, SoccerGame game) {

        SoccerPlayer p = game.getPlayer(0,0);

        if (kickTarget!= null)
            return new SteeringInfo(kickTarget.minus(soccerPlayer.getPosition()),0, SteeringInfo.SteeringType.Kinematic);
        else return SteeringInfo.getNoSteering();

    }

    @Override
    public String getTeamName() {
        return "DD";
    }

    @Override
    public String talk(SoccerPlayer soccerPlayer, SoccerGame game) {
        return null;
    }

    @Override
    public void hear(int playerId, String talk) {

    }
}
