package soccer.internal;


import movement.KinematicInfo;
import movement.MovingEntity;
import movement.SimpleCollision;
import movement.StaticInfo;
import movement.steering.SteeringBehavior;
import movement.steering.SteeringInfo;
import util.VectorUtils;
import math.geom2d.Vector2D;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.util.concurrent.*;

import static soccer.internal.SoccerGame.*;

public class SimpleSoccerPlayer extends MovingEntity implements SoccerPlayer,GoalListener {

    private static final double MaxAcceleration = 1;
    private static final double FatigueRecoverStamina = 60;
    private static final String NO_TALK = "";

    boolean disqualified = false;

    public static final Color[] PlayerColors = new Color[]{Color.red, Color.blue};
    private static final SteeringBehavior FatigueSteering = new FatigueBehavior();



    protected final SoccerGameState gameState;
    private final int team;


    private Vector2D initialPosition;
    private SteeringInfo ballSteering;

    private PlayerAI playerAI;
    private double stamina;

    private String talk;

    ExecutorService executor = Executors.newFixedThreadPool(1);
    private final int id;


    public void setPlayerAI(PlayerAI playerAI) {
        this.playerAI = playerAI;
    }


    void setInitialPosition(Vector2D pos)
    {
        initialPosition = pos;
    }

    public SimpleSoccerPlayer(SoccerGameState gameState, int team, int id ,PlayerAI playerAI ) {
        super(new SoccerPlayerImage(PlayerColors[team],PlayerRadius), new StaticInfo(new Vector2D(0,0),0, StaticInfo.OrientationType.VelocityBased), KinematicInfo.Zero(),gameState.getBounds());
        setCollisionHandler(new SimpleCollision(PlayerRadius));
        ((SoccerPlayerImage)shape).setPlayer(this);
        this.playerAI = playerAI;
        this.gameState = gameState;
        this.team = team;
        this.id = id;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) {
        super.init(gameContainer, stateBasedGame);
        staticInfo.setPosition(initialPosition);
        stamina = MaxStamina;
        setBounds(gameState.getBounds());
        playerAI.init(this,gameState);
        disqualified = false;

        //startAILoop();
    }

    @Override
    public final void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) {
        performAICall();

        if (disqualified)
            return;

        applyPlayerLimits();
        updatePlayer(gameContainer,stateBasedGame,i);

    }

    private void updatePlayer(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) {
        SteeringInfo steeringInfo = (steeringBehavior != null) ?
                steeringBehavior.getSteering(staticInfo,kinematicInfo):SteeringInfo.getNoSteering();

        steeringInfo = applySteeringLimits(steeringInfo);

        double maxRotation = 0.1;
        kinematicInfo.update(steeringInfo,i*TIME_COEFFICIENT, MaxSpeed, maxRotation);
        staticInfo.update(kinematicInfo,steeringInfo,i* TIME_COEFFICIENT);

        if (collisionHandler!=null)
            collisionHandler.handle(this);
    }

    private SteeringInfo applySteeringLimits(SteeringInfo steeringInfo) {
        if (steeringInfo.steeringType == SteeringInfo.SteeringType.Kinematic &&  steeringInfo.linear.norm()> MaxSpeed)
            steeringInfo.linear = steeringInfo.linear.normalize().times(MaxSpeed);

        if ( steeringInfo.steeringType== SteeringInfo.SteeringType.Dynamic && steeringInfo.linear.norm()>MaxAcceleration)
                steeringInfo.linear = steeringInfo.linear.normalize().times(MaxAcceleration);

        return steeringInfo;
    }


    private void startAILoop() {
        SoccerPlayer sp = this;
        FutureTask<Integer> task = new FutureTask<Integer>(() -> {
            while (!gameState.gameOver) {
                steeringBehavior = playerAI.getSteering(sp, gameState);
                //if (canKickBall())
                ballSteering = playerAI.getBallSteering(sp, gameState);

                talk = playerAI.talk(sp, gameState);
            }
            return 0;
        });
        executor.execute(task);
    }
    private void performAICall() {
        Integer result = null;

        SoccerPlayer sp = this;
        FutureTask<Integer> task = new FutureTask<Integer>(()-> {
                steeringBehavior = playerAI.getSteering(sp,gameState);
                //if (canKickBall())
                ballSteering = playerAI.getBallSteering(sp,gameState);

                talk = playerAI.talk(sp,gameState);

                return 0;
        });
        executor.execute(task);
/*
        try {
            result = task.get(TimeOut, TimeUnit.MILLISECONDS);

        } catch (TimeoutException ex) {
            if (!gameState.gameOver) {
                System.out.println(ex);
                System.out.println("Continuing with old decisions..");
                //System.out.println("Team" + team + " disqualified due to timeout violation!");
                //disqualify();
                //gameState.disqualify(this);
            }
        } catch (InterruptedException e) {
            System.out.println(e);
            // handle the interrupts
        } catch (ExecutionException e) {
            if (!gameState.gameOver) {
                System.out.println(e);
                System.out.println("Team" + team + " disqualified due to INTERNAL exception!");
                disqualify();
                gameState.disqualify(this);
            }
        }
        finally {
        }

*/
    }

    private void disqualify() {
        disqualified = true;
        ((SoccerPlayerImage)shape).setColor(Color.gray);
    }

    private void applyPlayerLimits() {

        setMaxVelocity(MaxSpeed);
        stamina += StaminaIncrement;
        stamina -= staminaPenalty();
        stamina = (stamina>MaxStamina ) ?  MaxStamina : Math.max(stamina, MinStamina);
        if (stamina < FatigueStamina || (stamina<FatigueRecoverStamina && steeringBehavior== FatigueSteering) )
            steeringBehavior = FatigueSteering;

        if (ballSteering == null || ballSteering.linear==null ||!validBallSteering(ballSteering))
            ballSteering = SteeringInfo.getNoSteering();

    }



    private boolean validBallSteering(SteeringInfo ballSteering) {

        if (!(ballSteering.linear.x()>-2000 && ballSteering.linear.x()<2000) || ! (ballSteering.linear.y()>-2000 && ballSteering.linear.y()<2000))
        {
            return false;
        }
        return true;

    }

    private double staminaPenalty() {
        double speed = kinematicInfo.getVelocity().norm();
        if (speed>FatigueSpeed)
            return StaminaPenalty*(speed-FatigueSpeed);
        return 0;
    }

    private boolean canKickBall() {
        return (VectorUtils.distance(staticInfo.getPosition(),gameState.getBall().getPosition())<3*SoccerGame.KickDistance);
    }


    @Override
    public Vector2D getInitialPosition() {
        return initialPosition.clone();
    }

    public Vector2D getPosition(){
        return staticInfo.getPosition().clone();
    }

    @Override
    public Vector2D getVelocity() {
        return kinematicInfo.getVelocity().clone();
    }

    @Override
    public int getTeam() {
        return team;
    }

    @Override
    public double getStamina() {
        return stamina;
    }

    @Override
    public String talk() {
        if (talk==null|| talk.equals(NO_TALK))
            return NO_TALK;
        String tmp = talk;

       // System.out.println(id + ">> "+ talk);
        talk=NO_TALK;
        return tmp;
    }

    @Override
    public void hear(int playerID,String talk) {
        //System.out.println(id + " : HEARD <"+talk+"> from "+ playerID);
        playerAI.hear(playerID,talk);
    }

    public SteeringInfo getBallSteering() {
        return ballSteering;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public final void goalScored(int team) {
        staticInfo.setPosition(initialPosition.clone());
        kinematicInfo = KinematicInfo.Zero();
        stamina = MaxStamina;
    }

    public String getTeamName() {
        return playerAI.getTeamName();
    }
}
