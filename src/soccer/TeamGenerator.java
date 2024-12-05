package soccer;


import soccer.hw3.YourAI;
import soccer.internal.SimpleSoccerPlayer;
import soccer.internal.SoccerGameState;

import java.util.ArrayList;
import java.util.List;

public class TeamGenerator {

    public static final String DummyTeam = "teamDummy";
    public static final String Simple = "teamSimple";
    public static final String YourTeam = "teamYou";

    public static List<SimpleSoccerPlayer> teamDummy(SoccerGameState gameState, int team)
    {
        List<SimpleSoccerPlayer> players= new ArrayList<>(5);
        players.add(new SimpleSoccerPlayer(gameState,team,1,new DummyAI()));
        players.add(new SimpleSoccerPlayer(gameState,team,2,new DummyAI()));
        players.add(new SimpleSoccerPlayer(gameState,team,3,new DummyAI()));
        players.add(new SimpleSoccerPlayer(gameState,team,4,new DummyAI()));
        players.add(new SimpleSoccerPlayer(gameState,team,5,new DummyAI()));
        return players;
    }

    public static List<SimpleSoccerPlayer> teamSimple(SoccerGameState gameState, int team)
    {
        List<SimpleSoccerPlayer> players= new ArrayList<>(5);
        players.add(new SimpleSoccerPlayer(gameState,team,1,new SimplePlayerAI()));
        players.add(new SimpleSoccerPlayer(gameState,team,2,new SimplePlayerAI()));
        players.add(new SimpleSoccerPlayer(gameState,team,3,new SimplePlayerAI()));
        players.add(new SimpleSoccerPlayer(gameState,team,4,new SimplePlayerAI()));
        players.add(new SimpleSoccerPlayer(gameState,team,5,new SimplePlayerAI()));
        return players;
    }

    public static List<SimpleSoccerPlayer> teamYou(SoccerGameState gameState, int team)
    {
        List<SimpleSoccerPlayer> players= new ArrayList<>(5);
        players.add(new SimpleSoccerPlayer(gameState,team,1,new YourAI()));
        players.add(new SimpleSoccerPlayer(gameState,team,2,new YourAI()));
        players.add(new SimpleSoccerPlayer(gameState,team,3,new YourAI()));
        players.add(new SimpleSoccerPlayer(gameState,team,4,new YourAI()));
        players.add(new SimpleSoccerPlayer(gameState,team,5,new YourAI()));
        return players;
    }


}
