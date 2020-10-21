import java.awt.Graphics;
import java.util.Optional;

public class ChoosingActor implements GameState {
    @Override
    public void mouseClick(int x, int y, Stage s) {
        s.actorInAction = Optional.empty();
        for (Actor a : s.actors) {
            if (a.loc.contains(x, y) && a.isTeamRed() && a.turns > 0) {
                s.cellOverlay = s.grid.getRadius(a.loc, a.moves, true);
                s.actorInAction = Optional.of(a);
                s.currentState = new SelectingNewLocation();
            }
        }
        if(!s.actorInAction.isPresent()){
            s.currentState = new SelectingMenuItem();
            s.menuOverlay.add(new MenuItem("Oops", x, y, () -> s.currentState = new ChoosingActor()));
            s.menuOverlay.add(new MenuItem("End Turn", x, y+MenuItem.height, () -> s.currentState = new CPUMoving()));
            s.menuOverlay.add(new MenuItem("End Game", x, y+MenuItem.height*2, () -> System.exit(0)));
        }
    }

    @Override
    public void paint(Graphics g, Stage s) {
    }
}