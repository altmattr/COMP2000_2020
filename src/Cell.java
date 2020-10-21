import java.awt.*;

abstract class Cell extends Rectangle {
    // fields
    static int size = 35;
    char col;
    int row;
    String description;
    Color colour;

    //constructors
    public Cell(char col, int row, int x, int y){
        super(x,y,size,size);
        this.col = col;
        this.row = row;
    }

    //methods
    void paint(Graphics g, Point mousePos){
        if(contains(mousePos)){
            g.setColor(new Color(0.5f,0.5f, 0.5f, AnimationBeat.getInstance().phaseCompletion()/100f));
        } else {
            g.setColor(this.colour);
        }
        g.fillRect(x,y,size,size);
        g.setColor(Color.BLACK);
        g.drawRect(x,y,size,size);
    }

    public abstract int crossingTime();

    public boolean contains(Point p){
        if (p != null){
            return super.contains(p);
        } else {
            return false;
        }
    }

    public int leftOfComparison(Cell c){
        return Character.compare(col, c.col);
    }

    public int aboveComparison(Cell c){
        return Integer.compare(row, c.row);
    }
}