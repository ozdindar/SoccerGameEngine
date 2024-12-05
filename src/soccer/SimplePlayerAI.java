package soccer;



import math.geom2d.Vector2D;
import movement.steering.Seek;
import movement.steering.SteeringBehavior;
import movement.steering.SteeringInfo;
import soccer.internal.PlayerAI;
import soccer.internal.SoccerGame;
import soccer.internal.SoccerPlayer;
import util.RandomUtils;

public class SimplePlayerAI implements PlayerAI {


    Vector2D kickTarget;

    @Override
    public void init(SoccerPlayer soccerPlayer, SoccerGame game) {
        kickTarget = game.getGoalAreaCenter(game.opponent(soccerPlayer.getTeam()));
    }

    @Override
    public SteeringBehavior getSteering(SoccerPlayer soccerPlayer, SoccerGame game) {

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new Seek(game.getBallPosition());
    }

    @Override
    public SteeringInfo getBallSteering(SoccerPlayer soccerPlayer, SoccerGame game) {

        if (kickTarget!= null)
            return new SteeringInfo(kickTarget.minus(soccerPlayer.getPosition()),0, SteeringInfo.SteeringType.Kinematic);
        else
        {
            System.out.println("kickTarget:null");
            return SteeringInfo.getNoSteering();
        }

    }

    @Override
    public String getTeamName() {
        return "SP";
    }

    @Override
    public String talk(SoccerPlayer soccerPlayer, SoccerGame game) {
        if (RandomUtils.nextBoolean(0.1))
            return "HELLO";
        else return null;
    }

    @Override
    public void hear(int playerId, String talk) {

    }
}
