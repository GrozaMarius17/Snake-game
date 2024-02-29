import java.awt.*;/*pachet java care funizeaza componente GUI */
import java.awt.event.*;/*furnizeaza functionalitati:apasara unui buton, tastare, miscarea mousu lui */
import java.util.ArrayList;/*stocheaza segmente din corpul sarpelui */
import java.util.Random;
import java.util.random.*;/*obtine vlori random X si Y pt a afisa punctele pe ecran */
import javax.swing.*;
/*Jpanel-clasa care organizeaza si gestineaza componentele GUI( grafic user interface)  */
/*latimea panoului si inaltimea placii */

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {  /*clasa care reprezinta un sarpe stocheaza coordonatele x si y ale sarpelui  */
        int X;
        int Y;

        Tile(int X, int Y) {
            this.X = X;
            this.Y = Y;
        }
    }

    int boardWidth; /*latimea tablei de joc in patrate */
    int boardHeight;
    int tileSize = 25;/*dimensiunea unui patrat in pixeli */

    //Snake
    Tile snakeHead; /*capul sarpelui */
    ArrayList<Tile> snakeBody;

    //Food
    Tile food;
    Random random;/*plasez mancarea random */

    //game Logic
    int velocityX;
    int velocityY;
    Timer gameLoop;

    boolean gameOver = false;
    

    SnakeGame(int boardWidth, int boardHeight) { /*initializeaza dimeniunile tablei de joc si culorile de fundal */
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);/*pozitia de start */
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 1;
        velocityY = 0;

        //game timer
        gameLoop = new Timer(100, this); //cât durează să pornească cronometrul, milisecundele scurse între cadre
        gameLoop.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);/*apeleaza clasa parinte pt a gestiona desenarea de baza */
        draw(g);/*apeleaza metoda draw pt a desena elemente pe tabla de joc */
    }

    public void draw(Graphics g) {
        //Grid Lines
        for (int i = 0; i < boardWidth/tileSize; i++) {
            //(x1, y1, x2, y2)
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);/*deseneaza liniile grilei */
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        }

        //Food
        g.setColor(Color.red);
        g.fill3DRect(food.X * tileSize, food.Y * tileSize, tileSize, tileSize, true);


        //Snake Head
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.X * tileSize, snakeHead.Y * tileSize, tileSize, tileSize,true);

        //Snake Body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.X * tileSize, snakePart.Y * tileSize, tileSize, tileSize,true);
        }

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
        else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    } 

    public void placeFood() { /*functie care seteaza aleatoriu coordonatele X si Y*/
        food.X = random.nextInt(boardWidth/tileSize); //600/25 = 24
        food.Y = random.nextInt(boardHeight/tileSize);

    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.X == tile2.X && tile1.Y == tile2.Y;
    }

    public void move() {
        //eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.X, food.Y));
            placeFood();
        }

        //Snake Body
        for (int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if(i == 0) {
                snakePart.X = snakeHead.X;
                snakePart.Y = snakeHead.Y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.X = prevSnakePart.X;
                snakePart.Y = prevSnakePart.Y;

            }
        }

        //Snake Head
        snakeHead.X += velocityX;
        snakeHead.Y += velocityY;

        //game over conditions
        for(int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            //collide with the snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
        if (snakeHead.X*tileSize < 0 || snakeHead.X*tileSize > boardWidth || //passed left border or right border
        snakeHead.Y*tileSize < 0 || snakeHead.Y*tileSize > boardHeight ) { //passed top border or bottom border
        gameOver = true;
    }
    }
    @Override
    public void actionPerformed(ActionEvent e) {//called every x milliseconds by gameLoop timer
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }

    }
    //do not read
    @Override
    public void keyTyped(KeyEvent e){}

   

    @Override
    public void keyReleased(KeyEvent e) {}
    
}
