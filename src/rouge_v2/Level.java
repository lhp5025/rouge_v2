/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rouge_v2;


import java.util.Random;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Level
{

    // Class variales
    protected int size;
    public char level[][];
    protected int playerX;
    protected int playerY;
    public int enemyCount;
    public int blockSize = 20; 
    
    // new JFrame for displaying the level 
    public JFrame scene = new JFrame();
    Level.Display display;

    // Constructor for the level class, with input size
    public Level(int _size)
    {
        size = _size;
        level = new char[size][size];
        enemyCount = 0;
        Populate();
        scene.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scene.setSize(blockSize*size, blockSize*size + blockSize);
        scene.setVisible(true);
        scene.setResizable(false);
        display = new Level.Display();
        scene.add(display);
    }
    // used to determin if the specified coordinates has a wall or enemy
    protected boolean Collision(int y, int x)
    {
        boolean TORETURN = true;

        if (level[y][x] == 'w')
        {
            TORETURN = false;
        }

        if (level[y][x] == 'X')
        {
            level[y][x] = ' ';
            enemyCount--;
            display.repaint();
            TORETURN = false;
        }

        return TORETURN;
    }
    // special collison for the AI (i.e interacting with other AI's and the player)
    // Also used to determine if the player dies
    protected boolean AICollision(int y, int x)
    {
        boolean TORETURN = true;

        if (level[y][x] == 'w')
        {
            TORETURN = false;
        }

        if (level[y][x] == 'X')
        {
            TORETURN = false;
        }

        if (level[y][x] == 'P')
        {
            level[y][x] = ' ';
            if (JOptionPane.showConfirmDialog(null, "YOU DIED!\n" + enemyCount + " ENEMIES LEFT", "LOSE", JOptionPane.OK_OPTION) == JOptionPane.OK_OPTION)
            {
                System.exit(0);
            }
            else
            {
                enemyCount = 0;
                Populate();
            }

        }

        return TORETURN;
    }

    // Populates the level, with walls and enemies
    private void Populate()
    {
        Random rng = new Random();

        for (int y = 0; y < size; y++)
        {
            for (int x = 0; x < size; x++)
            {
                level[y][x] = 'o';
                if (y == 0 || x == 0 || y == size - 1 || x == size - 1 || rng.nextInt(4) == 0)
                {
                    level[y][x] = 'w';
                }
            }
        }
        DeadSpace(size / 2, size / 2);
        for (int y = 0; y < size; y++)
        {
            for (int x = 0; x < size; x++)
            {
                if (level[y][x] == 'o')
                {
                    level[y][x] = 'w';
                }
            }
        }
        for (int y = 0; y < size; y++)
        {
            for (int x = 0; x < size; x++)
            {
                if (x == (size) / 2 && y == (size) / 2)
                {
                    level[y][x] = 'P';
                    playerY = y;
                    playerX = x;
                }
                else if (rng.nextInt(10) == 0 && level[y][x] != 'w')
                {
                    level[y][x] = 'X';
                    enemyCount++;
                }
            }
        }
    }
    // Movement of the AI
    protected void AI()
    {
        
        Random rng = new Random();
        for (int y = 0; y < size; y++)
        {
            for (int x = 0; x < size; x++)
            {
                if (level[y][x] == 'X')
                {
                    switch (rng.nextInt(4))
                    {
                        case 0:
                            if (AICollision(y - 1, x))
                            {
                                level[y][x] = 'x';
                                char pH = level[y - 1][x];
                                level[y - 1][x] = level[y][x];
                                level[y][x] = pH;
                            }
                            break;
                        case 1:
                            if (AICollision(y + 1, x))
                            {
                                level[y][x] = 'x';
                                char pH = level[y + 1][x];
                                level[y + 1][x] = level[y][x];
                                level[y][x] = pH;
                            }
                            break;
                        case 2:
                            if (AICollision(y, x - 1))
                            {
                                level[y][x] = 'x';
                                char pH = level[y][x - 1];
                                level[y][x - 1] = level[y][x];
                                level[y][x] = pH;
                            }
                            break;
                        case 3:
                            if (AICollision(y, x + 1))
                            {
                                level[y][x] = 'x';
                                char pH = level[y][x + 1];
                                level[y][x + 1] = level[y][x];
                                level[y][x] = pH;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        for (int y = 0; y < size; y++)
        {
            for (int x = 0; x < size; x++)
            {
                if (level[y][x] == 'x')
                {
                    level[y][x] = 'X';
                }
            }
        }
        
        if (enemyCount > 0)
        {
            display.repaint();
            System.out.println(enemyCount);
        }
        else
        {
            if (JOptionPane.showConfirmDialog(null, "ALL ENEMIES DEAD, YOU WIN", "WIN", JOptionPane.OK_OPTION) == JOptionPane.OK_OPTION)
            {
                System.exit(0);
            }
        }
    }
    // Eliminates unaccesable areas from the level
    public boolean DeadSpace(int y, int x)
    {
        boolean TORETURN = false;

        if ((y >= 0 && y < size) && (x >= 0 && x < size))
        {
            level[y][x] = ' ';
            TORETURN = true;
            if (Collision(y - 1, x) && level[y - 1][x] != ' ')
            {
                DeadSpace(y - 1, x);
            }
            if (Collision(y + 1, x) && level[y + 1][x] != ' ')
            {
                DeadSpace(y + 1, x);
            }
            if (Collision(y, x - 1) && level[y][x - 1] != ' ')
            {
                DeadSpace(y, x - 1);
            }
            if (Collision(y, x + 1) && level[y][x + 1] != ' ')
            {
                DeadSpace(y, x + 1);
            }

        }
        return TORETURN;
    }
    // Dislpay class of the leve class
    public class Display extends JPanel
    {
        @Override
        public void paint(Graphics g)
        {
            super.paint(g);
            for (int y = 0; y < size; y++)
            {
                for (int x = 0; x < size; x++)
                {
                    if (level[y][x] == 'X')
                    {
                        g.setColor(Color.red);
                    }
                    else if (level[y][x] == ' ')
                    {
                        g.setColor(Color.gray);
                    }
                    else if (level[y][x] == 'P')
                    {
                        g.setColor(new Color(16, 190, 7));
                    }
                    else
                    {
                        g.setColor(Color.black);
                    }
                    //g.drawString(Character.toString(level[y][x]), x * blockSize , y * blockSize+blockSize/2 );
                    g.fillRect(x * blockSize, y * blockSize, blockSize, blockSize);
                }
            }
        }
    }

    public class MoveUP extends AbstractAction
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            //System.out.println("UP");
            if (Collision(playerY - 1, playerX))
            {
                char pH = level[playerY - 1][playerX];
                level[playerY - 1][playerX] = level[playerY][playerX];
                level[playerY][playerX] = pH;
                playerY--;

                display.repaint();

                AI();
            }
        }
    }

    public class MoveDown extends AbstractAction
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            //System.out.println("DOWN");
            if (Collision(playerY + 1, playerX))
            {
                char pH = level[playerY + 1][playerX];
                level[playerY + 1][playerX] = level[playerY][playerX];
                level[playerY][playerX] = pH;
                playerY++;

                display.repaint();
                AI();
            }
        }
    }

    public class MoveLeft extends AbstractAction
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            //System.out.println("LEFT");
            if (Collision(playerY, playerX - 1))
            {
                char pH = level[playerY][playerX - 1];
                level[playerY][playerX - 1] = level[playerY][playerX];
                level[playerY][playerX] = pH;
                playerX--;

                display.repaint();

                AI();
            }
        }
    }

    public class MoveRight extends AbstractAction
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            //System.out.println("RIGHT");
            if (Collision(playerY, playerX + 1))
            {
                char pH = level[playerY][playerX + 1];
                level[playerY][playerX + 1] = level[playerY][playerX];
                level[playerY][playerX] = pH;
                playerX++;

                display.repaint();
                AI();
            }
        }
    }
    
    public class Move0 extends AbstractAction
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            AI();
        }
    }
}

