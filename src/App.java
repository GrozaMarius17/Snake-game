import javax.swing.JFrame;

import javax.swing.*;
public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 600;
        int boardHeight = boardWidth;

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight); /*dimensiunea ecranului */
        frame.setLocationRelativeTo(null); /*deschide fereastra pe mijlocul ecranului */
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);/*programul se inchide cand utilizatorul da X */
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
        frame.add(snakeGame);
        frame.pack();/*ca dimensiunea ecranului aplicatie sa fie 600 fara bara alba de sus */
        snakeGame.requestFocus();

    }
}
