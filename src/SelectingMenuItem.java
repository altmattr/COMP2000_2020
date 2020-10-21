import java.awt.Graphics;
import java.util.ArrayList;

public class SelectingMenuItem implements GameState {
    @Override
    public void mouseClick(int x, int y, Stage s) {
        for(MenuItem mi : s.menuOverlay){
            if (mi.contains(x,y)){
                mi.action.run();
                s.menuOverlay = new ArrayList<MenuItem>();
            }
        }
    }

    @Override
    public void paint(Graphics g, Stage s) {
    }
}
