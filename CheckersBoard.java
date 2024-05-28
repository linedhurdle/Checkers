import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;


public class CheckersBoard {

    private static CheckersBoard instance;
    private final int playerOneTurn = 10;
    private final int playerTwoTurn = 12;
    private final int playerOnePiece = 1;
    private final int playerTwoPiece = 2;
    private final int playerOneKing = 3;
    private final int playerTwoKing = 4;
    private final int empty = 0;

    JPanel outerUI = new JPanel(new BorderLayout(10, 10));
    JPanel checkersBoard;
    CheckersButton [][] checkersSquares = new CheckersButton[8][8];
    JPanel resignScreen = new JPanel();
    BufferedImage blackCheckerBufferedImage = ImageIO.read(ClassLoader.getSystemResource("Photos/Black.png"));
    BufferedImage whiteCheckerBufferedImage = ImageIO.read(ClassLoader.getSystemResource("Photos/Red.png"));
    BufferedImage blackCheckerKingBufferedImage = ImageIO.read(ClassLoader.getSystemResource("Photos/Black King.png"));
    BufferedImage whiteCheckerKingBufferedImage = ImageIO.read(ClassLoader.getSystemResource("Photos/Red King.png"));



    ImageIcon blackChecker = new ImageIcon(blackCheckerBufferedImage);
    ImageIcon whiteChecker = new ImageIcon(whiteCheckerBufferedImage);
    ImageIcon blackKingChecker = new ImageIcon(blackCheckerKingBufferedImage);
    ImageIcon whiteCheckerKing = new ImageIcon(whiteCheckerKingBufferedImage);

    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean pieceSelected = false;

    int activeTurn = playerOneTurn;


    public CheckersBoard() throws IOException {
        instance = this;
        initalize();
    }

    public static CheckersBoard getInstance(){
        return instance;
    }

    public void initalize() throws IOException {
        //Setting up outerUI
        outerUI.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar jToolBar = new JToolBar();
        jToolBar.setFloatable(false);
        outerUI.add(jToolBar, BorderLayout.SOUTH);

        JLabel jlabel = new JLabel("GG EZ PERSON WHO DIDN'T RESIGN WON");
        resignScreen.add(jlabel);
        resignScreen.setVisible(false);

        Button play = new Button("Play");
        jToolBar.add(play);

        Button resign = new Button("Resign");
        resign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outerUI.setVisible(false);
                checkersBoard.setVisible(false);
                resignScreen.setVisible(true);
            }
        });
        jToolBar.add(resign);

        checkersBoard = new JPanel(new GridLayout(0, 8));
        checkersBoard.setBorder(new LineBorder(Color.BLACK));
        outerUI.add(checkersBoard);

        //Setting up white/black spaces
        for (int i = 0; i < checkersSquares.length; i++) {
            for (int j = 0; j < checkersSquares[0].length; j++) {
                int id = (i + j) % 2;
                CheckersButton button = new CheckersButton(0);
                button.setOpaque(true);

                switch(id) {
                    case (0):
                        button.setBackground(Color.BLACK);
                        break;
                    case (1):
                        button.setBackground(Color.WHITE);
                }


                // Assign the button to the correct position in the grid
                checkersSquares[i][j] = button;

            }
        }

        for(int i = 0; i < 3; i++) {
            for(int j = 1; j < checkersSquares[0].length; j+=2) {
                if(i == 1) {
                    checkersSquares[i][j-1].setId(1);
                    checkersSquares[i][j-1].setIcon(whiteChecker);
                }
                else {
                    checkersSquares[i][j].setId(1);
                    checkersSquares[i][j].setIcon(whiteChecker);
                }
            }
        }
        for(int i = 5; i < checkersSquares[0].length; i++) {
            for(int j = 0; j < checkersSquares.length; j+=2){
                if (i == 6) {
                    if(!(j-1 > 8)) {
                        checkersSquares[i][j+1].setId(2);
                        checkersSquares[i][j+1].setIcon(blackChecker);
                    }
                }
                else {
                    checkersSquares[i][j].setId(2);
                    checkersSquares[i][j].setIcon(blackChecker);
                }

            }
        }
        for (int i = 0; i < checkersSquares.length; i++) {
            for (int j = 0; j < checkersSquares[0].length; j++) {
                checkersBoard.add(checkersSquares[i][j]);
            }
        }
    }
    public JComponent getUI() {
        return outerUI;
    }

    public CheckersButton [][] getCheckersSquares() {
        return checkersSquares;
    }

    public boolean checkIfEmptySpace(int row, int col) {
        return checkersSquares[row][col].getId() == 0;
    }
    public void checkPromotion(){
        for(int j = 0; j < 8; j++){
            if(checkersSquares[0][j].getId() == playerOnePiece) {
                checkersSquares[0][j].setId(playerOneKing);
                checkersSquares[0][j].setIcon(whiteCheckerKing);
            }
            if(checkersSquares[7][j].getId() == playerTwoPiece){
                checkersSquares[7][j].setId(playerTwoKing);
                checkersSquares[7][j].setIcon(blackKingChecker);
            }
        }
    }
    public boolean checkIfValidMove(int startingRowIndex, int startingColumnIndex, int endRowIndex, int endColumnIndex) {
        int startingPieceId = checkersSquares[startingRowIndex][startingColumnIndex].getId();

        int rowIndexDifference = startingRowIndex - endRowIndex;
        int columnIndexDifference = startingColumnIndex - endColumnIndex;

        if(activeTurn)
        if(checkIfEmptySpace(endRowIndex, endColumnIndex)){
            return false;
        }
        //Player One Normal Checker movement
        if (startingPieceId == playerOnePiece) {
            if(rowIndexDifference == -1 && (columnIndexDifference == 1 || columnIndexDifference == -1)){
                return true;
            }
        }
        else if(startingPieceId == playerTwoPiece){
            if(rowIndexDifference == 1 && (columnIndexDifference == 1 || columnIndexDifference == -1)){
                return true;
            }
        }

        if (startingPieceId == playerOneKing || startingPieceId == playerTwoKing){
            if((rowIndexDifference == - 1 || rowIndexDifference == 1) && (columnIndexDifference == 1 || columnIndexDifference == -1)){
                return true;
            }
        }
        return false;
    }

    public void movePiece(int startingRow, int startingCol, int endRow, int endCol){
        checkersSquares[endRow][endCol].setId(checkersSquares[startingRow][startingCol].getId());
        checkersSquares[endRow][endCol].setIcon(checkersSquares[startingRow][startingCol].getIcon());
        checkersSquares[startingRow][startingCol].setId(empty);
        checkersSquares[startingRow][startingCol].setIcon(null);
        checkPromotion();
    }
    public void handleButtonClick(CheckersButton button){
        int row = -1;
        int col = -1;
        for (int i = 0; i < checkersSquares.length; i++){
            for(int j = 0; j < checkersSquares[0].length; j++){
                if(checkersSquares[i][j] == button){
                    row = i;
                    col = j;
                    break;
                }
            }
        }
        if(!pieceSelected){
            if(checkersSquares[row][col].getId() != empty){
                selectedRow = row;
                selectedCol = col;
                pieceSelected = true;
                checkersSquares[row][col].setBorder(new LineBorder(Color.yellow, 3));
            }
        }
        else{
            if(checkIfValidMove(selectedRow, selectedCol, row, col)){
                movePiece(selectedRow, selectedCol, row, col);
                pieceSelected = false;
                checkersSquares[selectedRow][selectedCol].setBorder(null);
            }
            else{
                checkersSquares[selectedRow][selectedCol].setBorder(null);
            }
        }

    }

    public static void main(String[] args) throws IOException
    {
        Runnable r = new Runnable(){
            @Override
            public void run(){
                JFrame jFrame = new JFrame("Checkers Game");
                CheckersBoard checkersBoard = null;
                try {
                    checkersBoard = new CheckersBoard();
                } catch (IOException e) {

                    throw new RuntimeException(e);
                }
                jFrame.add(checkersBoard.getUI());
                jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                jFrame.setLocationByPlatform(true);
                jFrame.pack();
                jFrame.setMinimumSize(jFrame.getSize());
                jFrame.setVisible(true);
                jFrame.setSize(new Dimension(1440, 1080));
                jFrame.setResizable(false);
            }
        };
        SwingUtilities.invokeLater(r);
    }
}

