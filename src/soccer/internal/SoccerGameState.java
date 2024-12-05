package soccer.internal;


import movement.StaticInfo;
import movement.steering.SteeringInfo;
import util.RandomUtils;
import util.VectorUtils;
import math.geom2d.Vector2D;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;



public class SoccerGameState extends BasicGameState implements SoccerGame {


    private static final Vector2D initialPositions[][]= new Vector2D[][]
            {
                    new Vector2D[] {
                            new Vector2D(60,360),
                            new Vector2D(260,160),
                            new Vector2D(260,460),
                            new Vector2D(460,160),
                            new Vector2D(460,460),
                    },
                    new Vector2D[] {
                            new Vector2D(1260,360),
                            new Vector2D(1060,160),
                            new Vector2D(1060,460),
                            new Vector2D(860,160),
                            new Vector2D(860,460),
                    }
            };

    private static final Color FrameColor = Color.orange;
    private static final Color MeasureLineColor = Color.lightGray;
    private static final float MeasureLineLength = 3;
    private static final float GoalMargin = 2;


    long time;

    private SoccerBall ball;
    private List<SimpleSoccerPlayer> players[] = new List[] { new ArrayList<>(), new ArrayList<>()} ;



    private float width;
    private float height;

    private Image image;
    private List<GoalListener> goalListeners = new ArrayList<>();

    public ScoreBoard scoreBoard;
    private GameContainer gameContainer;
    private StateBasedGame game;

    boolean gameOver= false;
    private long MaxTime;
    private long startTime;
    private int id;

    public SoccerGameState(int id,long maxTime)
    {
        this.id=id;
        this.MaxTime = maxTime;
    }

    public void setMaxTime(long maxTime)
    {
        this.MaxTime= maxTime;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }



    public void addPlayer(int team , SimpleSoccerPlayer player)
    {
        players[team].add(player);
        player.setInitialPosition(initialPositions[team][players[team].size()-1].clone());
        goalListeners.add(player);
    }


    @Override
    public int getID() {
        return id;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

        this.gameContainer = gameContainer;
        game = stateBasedGame;
        gameContainer.setForceExit(false);
        gameOver = false;
        startTime = gameContainer.getTime();
        width = gameContainer.getWidth()-2*FRAME_WIDTH;
        height = gameContainer.getHeight()-2*FRAME_WIDTH;


        initBall();
        initStadium();
        initPlayers(gameContainer, stateBasedGame);
    }

    private void initStadium() {
        String[] teams = new String[]{ players[0].get(0).getTeamName(), players[1].get(0).getTeamName()};
        scoreBoard = new ScoreBoard(new StaticInfo(new Vector2D(width-ScoreBoardImage.ScoreBoardCellWidth*4,5),0),teams);
        goalListeners.add(scoreBoard);
        loadPitchImage();
    }

    private void initBall() {
        ball = new SoccerBall(new Vector2D(width/2,height/2),getBounds());
        goalListeners.add(ball);
    }

    private void initPlayers(GameContainer gameContainer, StateBasedGame stateBasedGame) {
        players[0].forEach((e)->e.init(gameContainer,stateBasedGame));
        players[1].forEach((e)->e.init(gameContainer,stateBasedGame));
    }

    private void loadPitchImage() {
        try {

            image = new Image("/res/grass2.jpg");
        } catch (SlickException e) {
            e.printStackTrace();
        };

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        Color c = graphics.getColor();

        renderTimer(gameContainer,stateBasedGame,graphics);
        renderSoccerPitch(gameContainer,stateBasedGame,graphics);
        renderEntities(gameContainer,stateBasedGame,graphics);

        graphics.setColor(c);
    }

    private void renderTimer(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {
        long time = gameContainer.getTime()-startTime;

        graphics.drawString(time+ "",500,0);

    }

    private void renderSoccerPitch(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {
        graphics.setColor(FrameColor);

        drawMeasureLines(graphics);

        scoreBoard.render(gameContainer,stateBasedGame,graphics);

        graphics.translate(FRAME_WIDTH,(float) height+FRAME_WIDTH);
        graphics.scale(1,-1);

        image.draw((float)0,(float)0,(float) width,(float) height);

        drawPitchLines(graphics);
    }

    private void drawPitchLines(Graphics graphics) {
        graphics.setColor(Color.white);
        graphics.setLineWidth(3);
        graphics.drawRect(0.0f,0.0f,(float) width,(float) height);
        graphics.drawLine((float) width*0.5f,0.0f,(float) width*0.5f,(float) height);
        graphics.drawOval((float)width*0.5f-CenterCircleRadius, (float)height*0.5f-CenterCircleRadius,2*CenterCircleRadius,2*CenterCircleRadius);

        graphics.drawRect(0.0f, (float)height*0.5f-PenaltyAreaHeight*0.5f, PenalyAreaWidth,PenaltyAreaHeight);
        graphics.drawRect((float) width-PenalyAreaWidth, (float)height*0.5f-PenaltyAreaHeight*0.5f, PenalyAreaWidth,PenaltyAreaHeight);

        graphics.drawRect(0.0f, (float)height*0.5f-GoalAreaHeight*0.5f, GoalAreaWidth,GoalAreaHeight);
        graphics.drawRect((float) width-GoalAreaWidth, (float)height*0.5f-GoalAreaHeight*0.5f, GoalAreaWidth,GoalAreaHeight);

        graphics.fillOval(width*0.5f-CenterPointRadius,height*0.5f-CenterPointRadius,2*CenterPointRadius,2*CenterPointRadius);

        graphics.setLineWidth(8);
        graphics.setColor(SimpleSoccerPlayer.PlayerColors[0]);
        graphics.drawLine(0.0f,(float) height*0.5f-GoalHeight*0.5f-GoalMargin,0.0f,(float) height*0.5f+GoalHeight*0.5f+GoalMargin);
        graphics.setColor(SimpleSoccerPlayer.PlayerColors[1]);
        graphics.drawLine(width,(float) height*0.5f-GoalHeight*0.5f-GoalMargin,width,(float) height*0.5f+GoalHeight*0.5f+ GoalMargin);


        graphics.setLineWidth(1);
    }

    private void drawMeasureLines(Graphics graphics) {
        graphics.setColor(MeasureLineColor);

        for (int i = 0; i < width; i += 50) {
            drawXLine(graphics,i);
            if(i<height)
                drawYLine(graphics,i);
        }
    }

    private void drawYLine(Graphics graphics, int i) {
        graphics.drawLine((float)FRAME_WIDTH-MeasureLineLength, (float) height + FRAME_WIDTH-i ,(float) (FRAME_WIDTH+MeasureLineLength),(float) height + FRAME_WIDTH-i );
        graphics.drawString(""+i,FRAME_WIDTH-graphics.getFont().getWidth(""+i)-2*MeasureLineLength,(float) height + FRAME_WIDTH - i-graphics.getFont().getHeight("i")/2);
    }

    private void drawXLine(Graphics graphics, int i) {
        graphics.drawLine(i+ FRAME_WIDTH,(float) (height+FRAME_WIDTH-MeasureLineLength),i+FRAME_WIDTH,(float)(height+FRAME_WIDTH+MeasureLineLength));
        graphics.drawString(""+i,i+ FRAME_WIDTH-graphics.getFont().getWidth(""+i)/2,(float) height + FRAME_WIDTH + 2*MeasureLineLength);
    }



    private void renderEntities(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {

        ball.render(gameContainer,stateBasedGame,graphics);

        players[0].forEach((p)->p.render(gameContainer,stateBasedGame,graphics));
        players[1].forEach((p)->p.render(gameContainer,stateBasedGame,graphics));

    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {

        if (checkGameOver(gameContainer, stateBasedGame))
            return;

        ball.update(gameContainer,stateBasedGame,delta);

        checkGoal();

        updatePlayers(delta);

    }

    private void updatePlayers(int delta) {
        int turn = RandomUtils.nextInt(2);
        List<SoccerPlayer> kickCandidates = new ArrayList<>();
        updatePlayersOf(turn,kickCandidates, delta);
        updatePlayersOf((turn+1)%2,kickCandidates, delta);

        if (kickCandidates.isEmpty())
            return;

        performKick(kickCandidates);
    }

    private void performKick(List<SoccerPlayer> kickCandidates) {
        SoccerPlayer kicker = chooseKicker(kickCandidates);


        double maxSpeed = SoccerGame.MaxSpeed *2; // The probability of kicking the ball at max speed is 0.5
        double kickProb = (maxSpeed- kicker.getKinematicInfo().getVelocity().norm())/maxSpeed;
        if (RandomUtils.nextBoolean(kickProb)) {
            performKick(kicker.getBallSteering());
            //System.out.println("kicked with: " + kicker.getBallSteering());
        }else
            ;//System.out.println("missed a shot - kick prob:"+ kickProb);
    }

    private boolean checkGameOver(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        if (gameContainer.getTime()-startTime>MaxTime)
        {
            if (stateBasedGame.getCurrentStateID() ==0 && stateBasedGame.getStateCount()>1) {
                System.out.println("HALF IS OVER!");
                stateBasedGame.enterState(1);
                stateBasedGame.getState(1).init(gameContainer, stateBasedGame);
                return true;
            }
            else {
                System.out.println("GAME IS OVER!");
                gameContainer.exit();
                gameOver= true;
            }
        }
        return false;
    }

    private SoccerPlayer chooseKicker(List<SoccerPlayer> kickCandidates) {
        return kickCandidates.get(RandomUtils.nextInt(kickCandidates.size()));
    }

    private void updatePlayersOf( int team, List<SoccerPlayer> kickCandidates, int delta)
    {

        boolean talked= false;
        for (SoccerPlayer player:players[team])
        {
            updatePlayer(player,kickCandidates,delta);
            if (!talked) {
                String talk = player.talk();
                if (talk!=null && !talk.isEmpty()) {
                    players[team].forEach(p -> p.hear(player.getID(),talk));
                    talked = true;
                }
            }
        }
    }

    private void updatePlayer(SoccerPlayer player, List<SoccerPlayer> kickCandidates, int delta)
    {
        player.update(gameContainer,game,delta); // For Debugging purposes kept 1
        double d = VectorUtils.distance(ball.getPosition(),player.getPosition());
        assert !Double.isNaN(d);
        if (d<KickDistance && player.getBallSteering() != null)
        {
            kickCandidates.add(player);
        }
    }

    private void checkGoal() {
        if (ball.getPosition().y()< (height+GoalHeight)/2 && ball.getPosition().y()> (height-GoalHeight)/2 ) {
            if (ball.getPosition().x() + BallRadius > width  /*&& ball.getKinematicInfo().getVelocity().x()>0*/)
                notifyGoal(0);
            if (ball.getPosition().x() - BallRadius  < 0  /*&& ball.getKinematicInfo().getVelocity().x()<0*/)
                notifyGoal(1);
        }
    }

    private void notifyGoal(int team) {
        goalListeners.forEach((gl)->gl.goalScored(team));
    }

    private void performKick(SteeringInfo ballSteering) {
        ball.setKinematic(ballSteering);
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(-2,-2,width+4,height+4);
    }


    SoccerBall getBall() {
        return ball;
    }

    @Override
    public Vector2D getBallPosition() {
        return ball.getPosition();
    }

    @Override
    public Vector2D getBallVelocity() {
        return ball.getKinematicInfo().getVelocity().clone();
    }

    public int getPlayerCount(int team)
    {
        return players[team].size();
    }
    public SoccerPlayer getPlayer(int team , int playerIndex)
    {
        return players[team].get(playerIndex);
    }

    @Override
    public int opponent(int team) {
        return (team+1)%2;
    }

    @Override
    public Vector2D getGoalAreaCenter(int team) {
        if (team==0)
        {
            return new Vector2D(0,height/2);
        }
        else return new Vector2D(width,height/2);
    }

    public void disqualify(SimpleSoccerPlayer simpleSoccerPlayer) {
        if (!gameOver) {
            System.out.println("Team-" + simpleSoccerPlayer.getTeam() + " is disqualfied.");
            System.out.println("Team-" + opponent(simpleSoccerPlayer.getTeam()) + " won by forfeit");
            gameContainer.exit();
            gameOver = true;
        }
    }

    public void addPlayers(List<SimpleSoccerPlayer> players, int team) {
        for (SimpleSoccerPlayer player:players
             ) {
            addPlayer(team,player);
        }
    }
}
