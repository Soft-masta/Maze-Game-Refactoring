import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

public class TheArchitect extends JFrame {

    int foundPlayer = 0;
    String[][] updatedMatrix;
    int WallXCord;
    int WallYCord;
    int collected = 0;
    boolean level;
    int globalTotalDimonds = 0;

    public void setExit(int x, int y) {
        WallXCord = x;
        WallYCord = y;
    }

    public void playerMove(int xScale, int yScale, String[][] currentMatrix, int totalDimonds) throws StupidAssMove {
        int x = 0;
        int y = 0;
        int found = 0;
        
        globalTotalDimonds = totalDimonds; //conteo diamantes
        nextLevel(false);
        String[][] junkMatrix = currentMatrix;
        for (int i = 0; i < currentMatrix.length; i++) {
            for (int j = 0; j < currentMatrix[i].length; j++) {
                if (currentMatrix[i][j].equals("P")) //posicionMatriz == "P"
                {
                    x = i;//record the players position
                    y = j;
                    found = 1;
                    break;
                }
            }
        }
        updatedMatrix = validarElemento(currentMatrix, x, y, xScale, yScale, totalDimonds);
    }
    
    public String[][] validarElemento(String[][] currentMatrix, int x, int y, int xScale, int yScale, int totalDimonds){
        boolean diamanteEscondido = currentMatrix[x + xScale][y + yScale].equals("H");
        boolean diamante = currentMatrix[x + xScale][y + yScale].equals("D");
        boolean pared = currentMatrix[x + xScale][y + yScale].equals("M");
//        boolean paredMovil = currentMatrix[x + (xScale * 2)][y + (yScale * 2)].equals("N");
        boolean nada = currentMatrix[x + xScale][y + yScale].equals("N");
        boolean salida = currentMatrix[x + xScale][y + yScale].equals("E");
        
        if (diamanteEscondido|| diamante) {
            reassign(currentMatrix, x, y, xScale, yScale);
            collected += 1;
        } else if (pared && currentMatrix[x + (xScale * 2)][y + (yScale * 2)].equals("N")) {
            reassign(currentMatrix, x, y, xScale, yScale);
            currentMatrix[x + (xScale * 2)][y + (yScale * 2)] = "M";
        } else if (nada) {
            reassign(currentMatrix, x, y, xScale, yScale);
        } else if (salida) {
            reassign(currentMatrix, x, y, xScale, yScale);
            nextLevel(true);
        } else {
            throw new StupidAssMove("Ass Hole hit wall!");
        }
        if (collected == totalDimonds) {
            showWall();
        }
        
        return currentMatrix;
    }

    private void reassign(String[][] sampleMtrx, int x, int y, int xShifted, int yShifted) {
        sampleMtrx[x][y] = "N";
        sampleMtrx[x + xShifted][y + yShifted] = "P";

    }
    
    public void showWall() {
        updatedMatrix[WallXCord][WallYCord] = "E";
    }

    public void nextLevel(boolean tOrF) { //true = nextLevel, false = updateLevelGUI
        level = tOrF;
    }

    private class StupidAssMove extends RuntimeException {

        public StupidAssMove(String event) {
            JFrame frame = new JFrame("Warning");
            JOptionPane.showMessageDialog(frame, "You Stupid Ass, Ran into something did you?");
        }
    }

    public boolean getLevel() {
        return level;
    }

    public int getDimondsLeft() {
        return globalTotalDimonds - collected;//diamantesTotal - diamantesRecolectados.
    }

    public String[][] getUpdatedMatrix()//returns the updated matrix for the gui to display
    {
        return updatedMatrix;
    }
}
