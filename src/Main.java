import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

class Main extends JFrame {

    class App extends JPanel {

        Stage stage;

        public App() {
            setPreferredSize(new Dimension(880, 720));
            stage = StageReader.readStage("data/stage1.rvb");
        }

        @Override
        public void paint(Graphics g) {
            stage.paint(g, getMousePosition());
        }

    }

    final App canvas;

    public static void main(String[] args) throws Exception {
        Main window = new Main();
        window.run();
    }

    private Main() {
        super("Grid");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new App();
        this.setContentPane(canvas);
        this.pack();
        this.setVisible(true);
    }

    public void run() {
        // updates the window only when the mouse moves
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                canvas.repaint();
            }
        });
    }
}