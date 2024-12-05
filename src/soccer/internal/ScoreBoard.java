package soccer.internal;


import movement.BasicGameEntity;
import movement.StaticInfo;

public class ScoreBoard extends BasicGameEntity implements GoalListener {
    private int[] score= new int[2];
    private String teams[]= new String[2];

    public ScoreBoard(StaticInfo pos, String[] teams) {
        super(new ScoreBoardImage(), pos);
        this.teams = teams;

        ((ScoreBoardImage)shape).setScoreBoard(this);
    }

    @Override
    public void goalScored(int team) {
        score[team]++;
    }


    public int getScore(int team) {
        return score[team];
    }

    public String getTeam(int team)
    {
        return teams[team];
    }
}
