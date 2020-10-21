import java.io.*;
import java.util.*;
import java.util.regex.*;

class StageReader {
    public static Stage readStage(String path){
        Stage stage = new Stage();
        try{
            Properties props = (new Properties());
            props.load(new FileInputStream(path));
            for (String key : props.stringPropertyNames()) {
                String value = props.getProperty(key);
                Pattern range = Pattern.compile("(.)(\\d+)->(.)(\\d+)");
                Pattern cell = Pattern.compile("(.)(\\d+)");
                List<Cell> cellsInQuestion = new ArrayList<Cell>();
                Matcher rangeMatcher = range.matcher(key);
                Matcher cellMatcher = cell.matcher(key);
                if(rangeMatcher.matches()) {
                    cellsInQuestion = stage.grid.cellsInRange(rangeMatcher.group(1).charAt(0),
                                                              Integer.parseInt(rangeMatcher.group(2)),
                                                              rangeMatcher.group(3).charAt(0),
                                                              Integer.parseInt(rangeMatcher.group(4))
                                                             );
                }
                else if (cellMatcher.matches()) {
                    stage.grid.cellAtColRow(cellMatcher.group(1).charAt(0), Integer.parseInt(cellMatcher.group(2)))
                            .ifPresent(cellsInQuestion::add);
                } else {System.out.println("no match");}
                for (Cell c : cellsInQuestion) {
                    if (value.equals("wall")) {
                        stage.grid.replaceCell(c, new Wall(c.col, c.row, c.x, c.y));
                    } else if (value.equals("oasis")) {
                        stage.grid.replaceCell(c, new Oasis(c.col, c.row, c.x, c.y));
                    } else if (value.equals("palm tree")) {
                        stage.grid.replaceCell(c, new PalmTree(c.col, c.row, c.x, c.y));
                    } else if (value.equals("puppy red")) {
                        stage.actors.add(new Puppy(c, 1.0f));
                    } else if (value.equals("puppy blue")) {
                        stage.actors.add(new Puppy(c, 0.0f));
                    } else if (value.equals("lion red")) {
                        stage.actors.add(new Lion(c, 1.0f));
                    } else if (value.equals("lion blue")) {
                        stage.actors.add(new Lion(c, 0.0f));
                    } else if (value.equals("bunny red")) {
                        stage.actors.add(new Rabbit(c, 1.0f));
                    } else if (value.equals("bunny blue")) {
                        stage.actors.add(new Rabbit(c, 0.0f));
                    } else if (value.equals("camel")) {
                        stage.actors.add(new Camel(c, 0.0f));
                    }
                }
            }
        } catch (IOException e){

        }
        return stage;
    }
}