import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.util.*;

public class Stage {
    Grid grid;
    ArrayList<Actor> actors;
    List<Cell> cellOverlay;
    List<MenuItem> menuOverlay;
    Optional<Actor> actorInAction;

    enum State {ChoosingActor, SelectingNewLocation, CPUMoving, SelectingMenuItem, SelectingTarget}
    State currentState = State.ChoosingActor;

    public Stage(){
        grid = new Grid();
        actors = new ArrayList<Actor>();
        cellOverlay = new ArrayList<Cell>();
        menuOverlay = new ArrayList<MenuItem>();
        currentState = State.ChoosingActor;
    }

    public void paint(Graphics g, Point mouseLoc){

        // do we have AI moves to make
        if (currentState == State.CPUMoving){
            for(Actor a: actors){
                if (!a.isTeamRed()){
                    List<Cell> possibleLocs = getClearRadius(a.loc, a.moves, true);

                    Cell nextLoc = a.strat.chooseNextLoc(possibleLocs);

                    a.setLocation(nextLoc);
                }
            }
            currentState = State.ChoosingActor;
            for(Actor a: actors){
                a.turns = 1;
            }
        }
        grid.paint(g,mouseLoc);
        grid.paintOverlay(g, cellOverlay, new Color(0f, 0f, 1f, 0.5f));

        for(Actor a: actors){
            a.paint(g);   
        }
        // state display
        g.setColor(Color.DARK_GRAY);
        g.drawString(currentState.toString(),720,20);

        // display cell
        Optional<Cell> cap = grid.cellAtPoint(mouseLoc);
        if (cap.isPresent()){
            Cell capc = cap.get();
            g.setColor(Color.DARK_GRAY);
            g.drawString(String.valueOf(capc.col) + String.valueOf(capc.row), 720, 50);
            g.drawString(capc.description, 820, 50);
            g.drawString("crossing time", 720, 65);
            g.drawString(String.valueOf(capc.crossingTime()), 820, 65);
        } 
        // agent display
        int yloc = 138;
        for(int i = 0; i < actors.size(); i++){
            Actor a = actors.get(i);
            g.drawString(a.getClass().toString(),720, yloc + 70*i);
            g.drawString("location:", 730, yloc + 13 + 70 * i);
            g.drawString(Character.toString(a.loc.col) + Integer.toString(a.loc.row), 820, yloc + 13 + 70 * i);
            g.drawString("redness:", 730, yloc + 26 + 70*i);
            g.drawString(Float.toString(a.redness), 820, yloc + 26 + 70*i);
            g.drawString("strat:", 730, yloc + 39 + 70*i);
            g.drawString(a.strat.toString(), 820, yloc + 39 + 70*i);
        }

        // menu overlay (on top of everything else)
        for(MenuItem mi: menuOverlay){
            mi.paint(g);
        }
    }

    public List<Cell> getClearRadius(Cell from, int size, boolean considerTime){
        List<Cell> init = grid.getRadius(from, size, considerTime);
        for(Actor a: actors){
            init.remove(a.loc);
        }
        return init;
    }

    public void mouseClicked(int x, int y){
        switch (currentState) {
            case ChoosingActor:
                actorInAction = Optional.empty();
                for (Actor a : actors) {
                    if (a.loc.contains(x, y) && a.isTeamRed() && a.turns > 0) {
                        cellOverlay = grid.getRadius(a.loc, a.moves, true);
                        actorInAction = Optional.of(a);
                        currentState = State.SelectingNewLocation;
                    }
                }
                if(!actorInAction.isPresent()){
                    currentState = State.SelectingMenuItem;
                    menuOverlay.add(new MenuItem("Oops", x, y, () -> currentState = State.ChoosingActor));
                    menuOverlay.add(new MenuItem("End Turn", x, y+MenuItem.height, () -> currentState = State.CPUMoving));
                    menuOverlay.add(new MenuItem("End Game", x, y+MenuItem.height*2, () -> System.exit(0)));
                }
                break;
            case SelectingNewLocation:
                Optional<Cell> clicked = Optional.empty();
                for (Cell c : cellOverlay) {
                    if (c.contains(x, y)) {
                        clicked = Optional.of(c);
                    }
                }
                if (clicked.isPresent() && actorInAction.isPresent()) {
                    cellOverlay = new ArrayList<Cell>();
                    actorInAction.get().setLocation(clicked.get());
                    actorInAction.get().turns--;
                    menuOverlay.add(new MenuItem("Fire", x, y, () -> {
                        cellOverlay = grid.getRadius(actorInAction.get().loc, actorInAction.get().range, false);
                        cellOverlay.remove(actorInAction.get().loc);
                            currentState = State.SelectingTarget;
                    }));
                    currentState = State.SelectingMenuItem;
                } 
                break;
                case SelectingMenuItem:
                for(MenuItem mi : menuOverlay){
                    if (mi.contains(x,y)){
                        mi.action.run();
                        menuOverlay = new ArrayList<MenuItem>();
                    }
                }
                break;
            case SelectingTarget:
                for(Cell c: cellOverlay){
                    if (c.contains(x, y)){
                        Optional<Actor> oa = actorAt(c);
                        if (oa.isPresent()){
                            oa.get().makeRedder(0.1f);
                        }
                    }
                }
                cellOverlay = new ArrayList<Cell>();
                currentState = State.ChoosingActor;
                break;
            default:
                System.out.println(currentState);
                break;
        }
    }

    public Optional<Actor> actorAt(Cell c) {
        for(Actor a: actors){
            if (a.loc == c){
                return Optional.of(a);
            }
        }
        return Optional.empty();
    }
}
