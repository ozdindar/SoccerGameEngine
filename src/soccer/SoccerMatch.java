package soccer;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Bootstrap;
import soccer.internal.SimpleSoccerPlayer;
import soccer.internal.SoccerGameState;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class SoccerMatch extends StateBasedGame {
    private static final int DemoWidth = 1400;
    private static final int DemoHeight = 800;
    private final int pitch1 =0;
    private final int pitch2 =1;


    public SoccerMatch(String name, long maxTime) {
        super(name);

        addState(new SoccerGameState(pitch1,maxTime));
        addState(new SoccerGameState(pitch2,maxTime));
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        getState(pitch1).init(gameContainer,this);
        getState(pitch2).init(gameContainer,this);
        enterState(pitch1);
    }

    SoccerGameState getGameState(int id)
    {
        return (SoccerGameState) getState(id);
    }

    public void printMatchScore()
    {
        String team1= getGameState(0).scoreBoard.getTeam(0);
        String team2= getGameState(0).scoreBoard.getTeam(1);
        int team1Score= getGameState(0).scoreBoard.getScore(0)+ getGameState(1).scoreBoard.getScore(1);
        int team2Score= getGameState(0).scoreBoard.getScore(1)+ getGameState(1).scoreBoard.getScore(0);
        System.out.println("MATCH SCORE: ["+ team1+"] "+ team1Score + "-" + team2Score + " ["+ team2+ "]");
    }

    public static SoccerMatch createMatch(String team1Function,String  team2Function, int matchTime)
    {
        SoccerMatch match = new SoccerMatch("Soccer Demo",matchTime);

        try {
            Method team1Method = TeamGenerator.class.getMethod(team1Function, SoccerGameState.class,int.class);
            Method team2Method = TeamGenerator.class.getMethod(team2Function, SoccerGameState.class,int.class);
            try {
                List<SimpleSoccerPlayer> team11 = (List<SimpleSoccerPlayer>) team1Method.invoke(null,match.getGameState(0),0);
                List<SimpleSoccerPlayer> team21 = (List<SimpleSoccerPlayer>) team2Method.invoke(null,match.getGameState(0),1);

                List<SimpleSoccerPlayer> team12 = (List<SimpleSoccerPlayer>) team1Method.invoke(null,match.getGameState(1),1);
                List<SimpleSoccerPlayer> team22 = (List<SimpleSoccerPlayer>) team2Method.invoke(null,match.getGameState(1),0);

                match.getGameState(0).addPlayers(team11,0);
                match.getGameState(0).addPlayers(team21,1);

                match.getGameState(1).addPlayers(team12,1);
                match.getGameState(1).addPlayers(team22,0);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return match;
    }

    public static void main(String[] args) {


        // TODO: You can change one of the teams TeamGenerator.YourTeam
        SoccerMatch match = createMatch(TeamGenerator.Simple,TeamGenerator.YourTeam,60000);


        Bootstrap.runAsApplication(match,DemoWidth,DemoHeight,false);

        match.printMatchScore();
    }


}
