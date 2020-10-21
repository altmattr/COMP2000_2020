import java.awt.Graphics;
import java.util.List;

public class CPUMoving implements GameState {
    @Override
    public void mouseClick(int x, int y, Stage s) {
    }

    @Override
    public void paint(Graphics g, Stage s) {
        for(Actor a: s.actors){
            if (!a.isTeamRed()) {
                List<Cell> possibleLocs = s.getClearRadius(a.loc, a.moves, true);
                Cell nextLoc = a.strat.chooseNextLoc(possibleLocs);
                a.setLocation(nextLoc);
            }
        }
        s.currentState = new ChoosingActor();
        for(Actor a: s.actors){
            a.turns = 1;
        }
    }   
}
