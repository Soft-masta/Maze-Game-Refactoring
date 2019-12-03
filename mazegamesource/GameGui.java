import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import maze.TheArchitect;

public class GameGui extends JFrame implements ActionListener
{
    public HighScore hs;  
    private int catFileName=01;
    private Container cp;
    public FileLoader fl = new FileLoader();
    //create menu items
    public JMenuBar menuBar;
    public JMenu newMenu;
    public JMenuItem itemExit;
    public JMenuItem newGameItem;
    public JMenuItem openFileItem;
    public JMenuItem itemEnterName;
    public JMenuItem itemHighScore;
    public JMenuItem itemSaveScore;
    //end create menu items
    public JLabel shagLabel;
    private int ix;
    private int jx;
    private int timeLeft;
    private JPanel progBarPanel;
    private JLabel[][] labelMatrix;
    private TimeCalculator timeCalc;
    private  JProgressBar progressBar;
    private mazeObject mo;
    private JPanel newPanel;// = new JPanel();
    private TheArchitect theArc = new TheArchitect();
    private String[][] scrapMatrix; 
    private  Timer timely; 
    public TimeKeeper tk;
    private  String playerName;
    private int levelNum=1;

    public static void main(String[] args)
    {
        new GameGui();
    }

    public GameGui()
    {
        super("Maze Game"); //call super to initilize title bar of G.U.I.
        cp=getContentPane();
        shagLabel = new JLabel("",new ImageIcon("yeababyyea.jpg"),JLabel.LEFT);//GUI background for initial load
        cp.add(shagLabel);
        //Add Exit & New Game Menu Items
        itemExit = new JMenuItem("Exit");
        itemExit.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_X, KeyEvent.CTRL_MASK));//press CTRL+X to exit if you want
        itemSaveScore = new JMenuItem("Save High Score");
        itemSaveScore.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_S, KeyEvent.CTRL_MASK));//press CTRL+S to save high score if you want
        itemHighScore=new JMenuItem("High Score");
        itemHighScore.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_H, KeyEvent.CTRL_MASK));//press CTRL+H to view high score if you want
        itemEnterName = new JMenuItem("Enter Player Name");
        itemEnterName.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_N, KeyEvent.CTRL_MASK));//press CTRL+N to enter your name if you want
        newGameItem = new JMenuItem("New Game");
        openFileItem = new JMenuItem("Open Maze File.");
        openFileItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_O, KeyEvent.CTRL_MASK));//press CTRL+O to open a level if you want
        newGameItem.setActionCommand("New Game");
        newGameItem.addActionListener(this);
        itemEnterName.setActionCommand("EnterName");
        itemEnterName.addActionListener(this);
        itemSaveScore.setActionCommand("SaveScore");
        itemSaveScore.addActionListener(this);
        itemHighScore.setActionCommand("HighScore");
        itemHighScore.addActionListener(this);
        itemExit.setActionCommand("Exit");
        itemExit.addActionListener(this);
        openFileItem.setActionCommand("Open");
        openFileItem.addActionListener(this);
        newMenu = new JMenu("File");
        newMenu.add(newGameItem);
        newMenu.add(itemEnterName);
        newMenu.add(openFileItem);
        newMenu.add(itemHighScore);
        newMenu.add(itemSaveScore);
        newMenu.add(itemExit);
        
        //Add Exit Menu Item
        //Add Menu Bar
        menuBar = new JMenuBar();
        menuBar.add(newMenu);
        setJMenuBar(menuBar);
        //Add Menu Bar     
        newPanel = new JPanel();
        hs = new HighScore();
        tk=new TimeKeeper();
        pack();
        setVisible (true);//show our menu bar and shagLabel.. Yea baby Yea! Whoa.. to much java.
    }//end constructor
     
    private class MyKeyHandler extends KeyAdapter //captures arrow keys movement
    {
        @Override
        public void keyPressed (KeyEvent theEvent)
       {         
            moveDirections(theEvent);
            JLabel mainLabel=new JLabel("Total Dimonds Left to Collect"+theArc.getDimondsLeft()+"", JLabel.CENTER);//show how many dimonds are left to collect on the gui!
            JPanel dimondsPanel = new JPanel();
            dimondsPanel.add(mainLabel);
            cp.add(dimondsPanel,BorderLayout.SOUTH);
       }//end method
   }//end inner class
    
    public void moveDirections(KeyEvent theEvent){
        switch (theEvent.getKeyCode())
           {
               case KeyEvent.VK_UP:
               {
                    theArc.playerMove(-1,0,scrapMatrix,fl.dimondCount());//let the Architect know we moved, along with the current matrix
                    loadMatrixGui("updateLoad");//reload the gui to show the move
                    if (theArc.getLevel()==true)
                    {
                    nextLevelLoad();//if the player hit an exit door, load the next level
                    }
                    break;
                }
                case KeyEvent.VK_DOWN:
                {
                    theArc.playerMove(1,0,scrapMatrix,fl.dimondCount());//see above
                    loadMatrixGui("updateLoad");//see above
                    if (theArc.getLevel()==true)//see above
                    {
                        nextLevelLoad();//see above
                    }
                    break;
                }
                case KeyEvent.VK_LEFT:
                {
                    theArc.playerMove(0,-1,scrapMatrix,fl.dimondCount());//see above
                    loadMatrixGui("updateLoad");//see above
                    if (theArc.getLevel()==true)//see above
                    {
                        nextLevelLoad();//see above
                    }
                    break;
                }
                case KeyEvent.VK_RIGHT:
                { 
                    theArc.playerMove(0,1,scrapMatrix,fl.dimondCount()); //see above
                    loadMatrixGui("updateLoad");//see above
                    if (theArc.getLevel()==true)
                    {
                       nextLevelLoad();//see above
                    }
                    break;   
                }
            }//end switch
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand()) {
        //exit on the menu bar
            case "Exit":
                new Timer(1000, updateCursorAction).stop();
                System.exit(0); //exit the system.   
        //new game on the menu bar
        //end New Game Command
            case "New Game":
                return; //maybe implent this feature later
        //Allows user to enter their name for high score
            case "EnterName":
                JOptionPane optionPane = new JOptionPane();
                playerName=JOptionPane.showInputDialog("Please Enter your Earth Name");
                break;
        //Displays the high scores
            case "HighScore":
                ScoreGui sg = new ScoreGui();
                sg.ScoreGui();
                break;
        //allows the user to save their score at any time.
            case "SaveScore":
                while (playerName == null){
                    optionPane = new JOptionPane();
                    playerName=optionPane.showInputDialog("Please Enter your Earth Name");
                }
                hs.addHighScore(playerName,tk.getMinutes(),tk.getSeconds(),levelNum);
                break;
        //to start the game you have to open a maze file. this is on the menu
            case "Open":
                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog(this);
                if(returnVal == JFileChooser.APPROVE_OPTION)
                {
                    fl.loadFile(chooser.getSelectedFile().getName());//load the file we need
                    theArc.setExit(fl.ExitXCord(),fl.ExitYCord());
                    loadMatrixGui("newLoad");
                }   break;
            default:
                break;
        }
     }//end actionPerformed method
     
     public void loadMatrixGui(String event)
     {
        if ("newLoad".equals(event))
         {       
             remove(newPanel);//remove the previous level's game from the screen
             if(progBarPanel !=null)//remove the progress bar from the gui as long as its already been created.
                remove(progBarPanel);
             String[][] temp = fl.getGameMatrix();
             scrapMatrix = new String[fl.getMatrixSizeRow()][fl.getMatrixSizeColumn()];   
             for (int i = 0; i < scrapMatrix.length; i++){
                 System.arraycopy(temp[i], 0, scrapMatrix[i], 0, scrapMatrix[i].length); //create a new matrix so we dont have a refrence to another objects matrix!
             }//end double for loop
             timeCalc = new TimeCalculator();//create the time calculator used to determine how much time each level is given.
             timeCalc.calcTimeforMaze(fl.dimondCount(),fl.getMatrixSizeRow(),fl.getMatrixSizeColumn());//let time calculator know the parameters of the game 
             timeLeft=timeCalc.getMinutes();//get the minutes allowed for the level
             ix=timeCalc.getSeconds();//get the seconds allowed for the level;
             jx=0;//reset the variable used for keeping time to zero since its a new level
             timely = new Timer(1000,updateCursorAction);//create a timer to update the progress bar
             timely.start();//start the timer
             progBarPanel = new JPanel();//panel for progress bar
             progressBar = new JProgressBar(0, timeCalc.getMinutes()*100);//minutes returns a single digit, we have to multiply it for Bar.
             progressBar.setStringPainted(true);
             progBarPanel.add(progressBar);
             cp.add(progBarPanel,BorderLayout.NORTH);
             newPanel = new JPanel();
             newPanel.setLayout(new GridLayout(fl.getMatrixSizeRow(),fl.getMatrixSizeColumn()));//set our panel for the game to the size of the matrix      
             labelMatrix=new JLabel[fl.getMatrixSizeRow()][fl.getMatrixSizeColumn()];
             newPanel.addKeyListener( new MyKeyHandler() );
        }//end if
        else if(event =="updateLoad")//every time the player moves the gui must be updated.
        {
            scrapMatrix = theArc.getUpdatedMatrix();//get the new matrix to be displayed from the architect
            remove(newPanel);//remove the old game
            newPanel = new JPanel();
            newPanel.setLayout(new GridLayout(fl.getMatrixSizeRow(),fl.getMatrixSizeColumn()));
            newPanel.addKeyListener( new MyKeyHandler() );
            newPanel.grabFocus();        
        }
        for (int i = 0; i < labelMatrix.length; i++)
        {
            for (int j = 0; j < labelMatrix[i].length; j++)
            {
                  labelMatrix[i][j]=  mo=new mazeObject(scrapMatrix[i][j]);//add our maze images into the gui
            }
        }//end double for loop
        cp.add(newPanel);
        remove(shagLabel);//remove the constructors initial background
        System.gc();//force java to clean up memory use.
        pack();
        setVisible (true);
        newPanel.grabFocus();  
     }//end loadMatrixGui method
 
    public class mazeObject extends JLabel//inner class for each maze object, aka wall, player etc
    {
    private JLabel imageLabel;
        public mazeObject(String fileName)
        {
            fileName+=".png";
            JLabel fancyLabel;
            fancyLabel = new JLabel("",new ImageIcon(fileName),JLabel.LEFT);
            newPanel.add(fancyLabel);
        }
    }//end inner class
        
    public void nextLevelLoad()
    {
        levelNum+=1;
        tk.TimeKeeper(timeLeft,ix);//The TimeKeeper object keeps a running tab of the total time the player has used.(for high score)
        timely.stop();//dont count while we are loading the next level.
        theArc = new TheArchitect();//flush everything from TheArchitect so we dont get goffee results
        catFileName+=01;//the next file to be loaded (number)
        String fileName="level"+catFileName+".maz";
        System.gc();
        fl.loadFile(fileName);//load the file we need
        scrapMatrix=fl.getGameMatrix();//get the new matrix from the fileloader for the next level.
        theArc.setExit(fl.ExitXCord(),fl.ExitYCord());
        loadMatrixGui("newLoad");         
    }
 
    Action updateCursorAction = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e)throws SlowAssPlayer //this inner class generates an exeption if the player takes to long to finish a level 
    {
        ix-=1;
        jx+=1;
        if(ix<0)
        {
            ix=60;
            timeLeft-=1;
        }
        if(timeLeft==0 && ix==0)
        {
            timely.stop();
            JLabel yousuckLabel = new JLabel("",new ImageIcon("yousuck.jpg"),JLabel.LEFT);
            cp.add(yousuckLabel);
            remove(newPanel);
            remove(progBarPanel);
            pack();
            setVisible (true);
            timely.stop();
            catFileName-=01;
            
        }//end first if
        progressBar.setValue(jx);
        progressBar.setString(timeLeft+":"+ix);
    }//end actionPerformed
};//end class

    private void LoadLevel()
    {
        if(catFileName<01)
            throw new SlowAssPlayer("Slow ass took to long.");
        else
            loadMatrixGui("newLoad");
    }
    private class SlowAssPlayer extends RuntimeException
    {
        public SlowAssPlayer(String event)
        {
            //the game is over, here we must tell our high score method to recond the details.
            hs.addHighScore(playerName,tk.getMinutes(),tk.getSeconds(),levelNum);
            JFrame frame = new JFrame("Warning");
            JOptionPane.showMessageDialog(frame, "You hit something");//the entire game has ended.
        }
    }//end class
}//end class    