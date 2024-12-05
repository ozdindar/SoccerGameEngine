package soccer.internal;


import movement.Renderable;
import movement.StaticInfo;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class ScoreBoardImage implements Renderable {


    public static final float ScoreBoardCellWidth = 15;
    public static final float ScoreBoardCellHeight = 20;
    public static final Color ScoreBoardColor = Color.white;
    public static final float ScoreBoardMargin = 2;
    ScoreBoard scoreBoard;


    public void setScoreBoard(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }
    @Override
    public void render(StaticInfo pos, Graphics g) {
        float currentX = (float) pos.getPosition().x();
        float currentY = (float) pos.getPosition().y();
        String team0Name = scoreBoard.getTeam(0).substring(0,2);
        String team1Name = scoreBoard.getTeam(1).substring(0,2);
        g.drawString(team0Name,currentX,currentY);

        currentX += g.getFont().getWidth(team0Name)+ScoreBoardMargin;

        g.setColor(SimpleSoccerPlayer.PlayerColors[0]);

        g.fillRect(currentX,currentY,ScoreBoardCellWidth,ScoreBoardCellHeight);
        g.setColor(SimpleSoccerPlayer.PlayerColors[1]);
        g.fillRect(currentX+ScoreBoardCellWidth,currentY,ScoreBoardCellWidth,ScoreBoardCellHeight);

        g.setColor(ScoreBoardColor);
        g.drawRect(currentX,currentY,ScoreBoardCellWidth,ScoreBoardCellHeight);
        g.drawRect(currentX+ScoreBoardCellWidth,currentY,ScoreBoardCellWidth,ScoreBoardCellHeight);


        g.drawString(""+scoreBoard.getScore(0),currentX+ScoreBoardMargin, currentY+ScoreBoardMargin);
        g.drawString(""+scoreBoard.getScore(1),currentX+ScoreBoardCellWidth+ScoreBoardMargin, currentY+ScoreBoardMargin);

        currentX += 2*ScoreBoardCellWidth+ ScoreBoardMargin;
        g.drawString(team1Name,currentX,currentY);
    }

    @Override
    public double getRadius() {
        return 0;
    }
}
