/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rouge_v2;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author Luke
 */
public class Rouge_v2
{

    // main function
    public static void main(String[] args)
    {   
        // creates a new instace of the Level class
        Level level1 = new Level(32);
        
        int mapName = JComponent.WHEN_IN_FOCUSED_WINDOW;

        InputMap imap = level1.display.getInputMap(mapName);
        
        // creates key bindings
        imap.put(KeyStroke.getKeyStroke('w'), "up");
        imap.put(KeyStroke.getKeyStroke('a'), "left");
        imap.put(KeyStroke.getKeyStroke('s'), "down");
        imap.put(KeyStroke.getKeyStroke('d'), "right");
        imap.put(KeyStroke.getKeyStroke(' '), "space");
        
        // creates extensions of AbstractAction, for an action to happed on key stroke
        Level.MoveUP moveU = level1.new MoveUP();
        Level.MoveDown moveD = level1.new MoveDown();
        Level.MoveLeft moveL = level1.new MoveLeft();
        Level.MoveRight moveR = level1.new MoveRight();
        Level.Move0 space = level1.new Move0();
        
        // execute the atctions on input
        level1.display.getActionMap().put("up", moveU);
        level1.display.getActionMap().put("left", moveL);
        level1.display.getActionMap().put("down", moveD);
        level1.display.getActionMap().put("right", moveR);
        level1.display.getActionMap().put("space", space);
    }
    
}
