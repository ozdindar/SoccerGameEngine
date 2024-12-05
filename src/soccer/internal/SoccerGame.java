package soccer.internal;

import math.geom2d.Vector2D;

/**
 * @author dindar.oz
 */
public interface SoccerGame {

    public static final float FRAME_WIDTH = 40;
    public static final double KickDistance = 2;
    public static final float CenterCircleRadius = 80;
    public static final float PenaltyAreaHeight = 300;
    public static final float PenalyAreaWidth = 200;
    public static final float GoalAreaHeight = 140;
    public static final float GoalAreaWidth = 70;
    public static final float GoalHeight = 80;
    public static final float CenterPointRadius = 5;



    public static final double MaxBallSpeed = 15;
    public static final float BallRadius = 5;
    public static final double FrictionCoefficient = 0.35;

    public static final double MaxSpeed = 3;
    public static final double FatigueStamina = 50;
    public static final double FatigeSlowDownRatio = 0.6;
    public static final double FatigueSpeed = 0.1;
    public static final float PlayerRadius = 10;

    public static final double StaminaIncrement = 0.8;
    public static final double MaxStamina = 200;
    public static final double MinStamina = 0;
    public static final double StaminaPenalty = 0.05;


    Vector2D getBallPosition();
    Vector2D getBallVelocity();
    int getPlayerCount(int team);
    SoccerPlayer getPlayer(int team, int index);

    // returns the opponent team
    int opponent(int team);

    //returns the middle point of GoalArea of team
    Vector2D getGoalAreaCenter(int team);

    float getWidth();
    float getHeight();



}
