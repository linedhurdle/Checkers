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
    private final int playerOnePiece = 1;
    private final int playerTwoPiece = 2;
    private final int playerOneKing = 3;
    private final int playerTwoKing = 4;
    private final int empty = 0;

    private JPanel outerUI = new JPanel(new BorderLayout(10, 10));
    private JPanel checkersBoard;
    private CheckersButton[][] checkersSquares = new CheckersButton[8][8];
    private JPanel resignScreen = new JPanel(new FlowLayout());
    private BufferedImage blackCheckerBufferedImage = ImageIO.read(ClassLoader.getSystemResource("Photos/Black.png"));
    private BufferedImage whiteCheckerBufferedImage = ImageIO.read(ClassLoader.getSystemResource("Photos/Red.png"));
    private BufferedImage blackCheckerKingBufferedImage = ImageIO.read(ClassLoader.getSystemResource("Photos/Black King.png"));
    private BufferedImage whiteCheckerKingBufferedImage = ImageIO.read(ClassLoader.getSystemResource("Photos/Red King.png"));


    private JLabel playerOneWin = new JLabel("Player One Won the Game");
    private JLabel playerTwoWin = new JLabel("Player Two Won the Game");

    private ImageIcon blackChecker = new ImageIcon(blackCheckerBufferedImage);
    private ImageIcon whiteChecker = new ImageIcon(whiteCheckerBufferedImage);
    private ImageIcon blackKingChecker = new ImageIcon(blackCheckerKingBufferedImage);
    private ImageIcon whiteCheckerKing = new ImageIcon(whiteCheckerKingBufferedImage);

    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean pieceSelected = false;

    private int whitePieceCount = 12;
    private int blackPieceCount = 12;

    boolean activeWhiteTurn = true;


    public CheckersBoard() throws IOException {
        instance = this;
        initalize();
    }

    public static CheckersBoard getInstance() {
        return instance;
    }

    public void initalize() throws IOException {
        //Setting up outerUI
        outerUI.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar jToolBar = new JToolBar();
        jToolBar.setFloatable(false);
        outerUI.add(jToolBar, BorderLayout.SOUTH);

        JButton stalemate = new JButton("Stalemate?");
        jToolBar.add(stalemate);

        stalemate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkersBoard.setVisible(false);
                if(activeWhiteTurn){
                    playerTwoWin.setText("Player Two Won the Game");
                }
                else{
                    playerTwoWin.setText("Player One Won the Game");
                }
                playerTwoWin.setVisible(true);
                outerUI.revalidate();
                outerUI.repaint();
            }
        });

        playerOneWin.setHorizontalAlignment(SwingConstants.CENTER);
        playerTwoWin.setHorizontalAlignment(SwingConstants.CENTER);
        playerOneWin.setVisible(false);
        playerTwoWin.setVisible(false);
        outerUI.add(playerOneWin, BorderLayout.NORTH);
        outerUI.add(playerTwoWin, BorderLayout.NORTH);

        Dimension labelSize = new Dimension(300, 50);
        playerOneWin.setPreferredSize(labelSize);
        playerTwoWin.setPreferredSize(labelSize);

        checkersBoard = new JPanel(new GridLayout(0, 8));
        checkersBoard.setBorder(new LineBorder(Color.BLACK));
        outerUI.add(checkersBoard);

        //Setting up white/black spaces
        for (int i = 0; i < checkersSquares.length; i++) {
            for (int j = 0; j < checkersSquares[0].length; j++) {
                int id = (i + j) % 2;
                CheckersButton button = new CheckersButton(0);
                button.setOpaque(true);

                switch (id) {
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
        //adding Icons and Id values for White Checkers Pieces
        for (int i = 0; i < 3; i++) {
            for (int j = 1; j < checkersSquares[0].length; j += 2) {
                if (i == 1) {
                    checkersSquares[i][j - 1].setId(2);
                    checkersSquares[i][j - 1].setIcon(whiteChecker);

                } else {
                    checkersSquares[i][j].setId(2);
                    checkersSquares[i][j].setIcon(whiteChecker);
                }
            }
        }
        //same but black pieces
        for (int i = 5; i < checkersSquares[0].length; i++) {
            for (int j = 0; j < checkersSquares.length; j += 2) {
                if (i == 6) {
                    if (!(j - 1 > 8)) {
                        checkersSquares[i][j + 1].setId(1);
                        checkersSquares[i][j + 1].setIcon(blackChecker);
                    }
                } else {
                    checkersSquares[i][j].setId(1);
                    checkersSquares[i][j].setIcon(blackChecker);
                }

            }
        }
        //Adding buttons to board
        for (int i = 0; i < checkersSquares.length; i++) {
            for (int j = 0; j < checkersSquares[0].length; j++) {
                checkersBoard.add(checkersSquares[i][j]);
                System.out.println("[" + i + "][" + j + "] id = " + checkersSquares[i][j].getId());
            }
        }
    }

    public JComponent getUI() {
        return outerUI;
    }

    public CheckersButton[][] getCheckersSquares() {
        return checkersSquares;
    }

    public boolean checkIfEmptySpace(int row, int col) {
        return checkersSquares[row][col].getId() == empty;
    }

    public void checkPromotion() {
        for (int j = 0; j < 8; j++) {
            if (checkersSquares[0][j].getId() == playerOnePiece) {
                checkersSquares[0][j].setId(playerOneKing);
                checkersSquares[0][j].setIcon(blackKingChecker);
            }
            if (checkersSquares[7][j].getId() == playerTwoPiece) {
                checkersSquares[7][j].setId(playerTwoKing);
                checkersSquares[7][j].setIcon(whiteCheckerKing);
            }
        }
    }

    public void switchPlayerTurn() {
        if (activeWhiteTurn) {
            activeWhiteTurn = false;
        } else {
            activeWhiteTurn = true;
        }
    }

    public boolean checkIfValidMove(int startingRowIndex, int startingColumnIndex, int endRowIndex, int endColumnIndex) {
        int startingPieceId = checkersSquares[startingRowIndex][startingColumnIndex].getId();
        int rowInBetween = (startingRowIndex + endRowIndex) / 2;
        int colInBetween = (startingColumnIndex + endColumnIndex) / 2;
        int rowIndexDifference = startingRowIndex - endRowIndex;
        int columnIndexDifference = startingColumnIndex - endColumnIndex;

        if (!activeWhiteTurn && (startingPieceId == 1 || startingPieceId == 3)) {
            System.out.println("Did not select Black piece");
            System.out.println("Checkers Squares piece ID: " + checkersSquares[startingRowIndex][startingColumnIndex].getId());
            return false;
        }
        if (activeWhiteTurn && (startingPieceId == 2 || startingPieceId == 4)) {
            System.out.println("Did not select Black piece");
            System.out.println("Checkers Squares piece ID: " + checkersSquares[startingRowIndex][startingColumnIndex].getId());
            return false;
        }

        if (!checkIfEmptySpace(endRowIndex, endColumnIndex)) {
            System.out.println("Not Empty Space");
            return false;
        }
        //Player One Normal Checker movement
        if (startingPieceId == playerTwoPiece) {
            System.out.println("startingPieceId == playerOnePiece Check is good");
            if (rowIndexDifference == -1 && (columnIndexDifference == 1 || columnIndexDifference == -1)) {
                System.out.println("Checking diagonal movement up");

                return true;
            }
        } else if (startingPieceId == playerOnePiece) {
            System.out.println("startingPiece is playerTwoPiece");
            if (rowIndexDifference == 1 && (columnIndexDifference == 1 || columnIndexDifference == -1)) {

                //checking functionality
                System.out.println("Checking diagonal movement down");
                return true;
            }
        } else if (startingPieceId == playerOneKing || startingPieceId == playerTwoKing) {
            System.out.println("Checking if kings");
            if ((rowIndexDifference == -1 || rowIndexDifference == 1) && (columnIndexDifference == 1 || columnIndexDifference == -1)) {

                //checking functionality
                System.out.println("Checking diagonal movement in both directions");

                return true;
            }

        }

        return false;
    }

    public boolean checkIfValidMoveIsTakingPiece(int startingRowIndex, int startingColumnIndex, int endRowIndex, int endColumnIndex) {
        int startingPieceId = checkersSquares[startingRowIndex][startingColumnIndex].getId();
        int rowInBetween = (startingRowIndex + endRowIndex) / 2;
        int colInBetween = (startingColumnIndex + endColumnIndex) / 2;
        int rowIndexDifference = startingRowIndex - endRowIndex;
        int columnIndexDifference = startingColumnIndex - endColumnIndex;

        System.out.println("checkersSquares[" + rowInBetween + "][" + colInBetween + "].getId = " + checkersSquares[rowInBetween][colInBetween].getId());

        if (!activeWhiteTurn && (startingPieceId == 1 || startingPieceId == 3)) {
            System.out.println("Did not select Black piece");
            System.out.println("Checkers Squares piece ID: " + checkersSquares[startingRowIndex][startingColumnIndex].getId());
            return false;
        }
        if (activeWhiteTurn && (startingPieceId == 2 || startingPieceId == 4)) {
            System.out.println("Did not select Black piece");
            System.out.println("Checkers Squares piece ID: " + checkersSquares[startingRowIndex][startingColumnIndex].getId());
            return false;
        }

        if (!checkIfEmptySpace(endRowIndex, endColumnIndex)) {
            System.out.println("Not Empty Space");
            return false;
        }

        if (startingPieceId == playerTwoPiece) {
            System.out.println("startingPieceId == playerOnePiece Check is good");
            if (rowIndexDifference == -2 && (columnIndexDifference == 2 || columnIndexDifference == -2) && (checkersSquares[rowInBetween][colInBetween].getId() == 1 || checkersSquares[rowInBetween][colInBetween].getId() == 3)) {
                System.out.println("Checking skipped spot for playertwopiece");
                return true;
            }
        } else if (startingPieceId == playerOnePiece) {
            System.out.println("startingPiece is playerTwoPiece");
            if (rowIndexDifference == 2 && (columnIndexDifference == 2 || columnIndexDifference == -2) && (checkersSquares[rowInBetween][colInBetween].getId() == 2 || checkersSquares[rowInBetween][colInBetween].getId() == 4)) {
                //checking functionality
                System.out.println("Checking skipped spot for playerOnePiece");
                return true;
            }
        } else if (startingPieceId == playerOneKing || startingPieceId == playerTwoKing) {
            System.out.println("Checking if kings");
            if ((rowIndexDifference == -2 || rowIndexDifference == 2) && (columnIndexDifference == 2 || columnIndexDifference == -2)) {
                if ((startingPieceId == playerOneKing) && (checkersSquares[rowInBetween][colInBetween].getId() == 2 || checkersSquares[rowInBetween][colInBetween].getId() == 4)) {
                    return true;
                }

                if ((startingPieceId == playerTwoKing) && (checkersSquares[rowInBetween][colInBetween].getId() == 1 || checkersSquares[rowInBetween][colInBetween].getId() == 3)) {
                    return true;
                }
            }
        }
        System.out.println("Nothing done...");
        return false;
    }

    public boolean checkOpportunityForTakeNextTurn(int startingRowIndex, int startingColumnIndex) {
        System.out.println("Check Opportunity run");
        System.out.println("checkersSquares [" + startingRowIndex + "][" +startingColumnIndex + "]: " + checkersSquares[startingRowIndex][startingColumnIndex].getId());

        if (checkersSquares[startingRowIndex][startingColumnIndex].getId() == 2) {
            System.out.println("Id = 2");
            if(startingRowIndex + 2 <= 7 && startingRowIndex + 2 >= 0) {
                if(startingColumnIndex + 2 <= 7 && startingColumnIndex + 2 >= 0){
                    if (checkIfValidMoveIsTakingPiece(startingRowIndex, startingColumnIndex, startingRowIndex + 2, startingColumnIndex + 2)) {
                        return true;
                    }
                }
                if(startingColumnIndex - 2 <= 7 && startingColumnIndex - 2 >= 0)
                {
                    if (checkIfValidMoveIsTakingPiece(startingRowIndex, startingColumnIndex, startingRowIndex + 2, startingColumnIndex - 2)) {
                        return true;
                    }
                }

            }
        }
        if (checkersSquares[startingRowIndex][startingColumnIndex].getId() == 1) {
            System.out.println("Id = 1");
            if(startingRowIndex - 2 <= 7 && startingRowIndex - 2 >= 0){
                if(startingColumnIndex + 2 <= 7 && startingColumnIndex + 2 >= 0){
                    if (checkIfValidMoveIsTakingPiece(startingRowIndex, startingColumnIndex, startingRowIndex - 2, startingColumnIndex + 2)) {
                        return true;
                    }
                }
                if(startingColumnIndex - 2 <= 7 && startingColumnIndex - 2 >= 0) {
                    if (checkIfValidMoveIsTakingPiece(startingRowIndex, startingColumnIndex, startingRowIndex - 2, startingColumnIndex - 2)) {
                        return true;
                    }
                }
            }
        }
        if (checkersSquares[startingRowIndex][startingColumnIndex].getId() == 3 || checkersSquares[startingRowIndex][startingColumnIndex].getId() == 4) {
            System.out.println("Id = 3 or 4");
            if(startingRowIndex - 2 <= 7 && startingRowIndex - 2 >= 0) {
                if(startingColumnIndex + 2 >= 0 && startingColumnIndex + 2 <= 7) {
                    if (checkIfValidMoveIsTakingPiece(startingRowIndex, startingColumnIndex, startingRowIndex - 2, startingColumnIndex + 2)) {
                        return true;
                    }
                }
                if(startingColumnIndex -2 <= 7 && startingColumnIndex - 2 >= 0) {
                    if (checkIfValidMoveIsTakingPiece(startingRowIndex, startingColumnIndex, startingRowIndex - 2, startingColumnIndex - 2)) {
                        return true;
                    }
                }
            }
            if(startingRowIndex + 2 <= 7 && startingRowIndex + 2 >= 0) {
                if(startingColumnIndex + 2 >= 0 && startingColumnIndex + 2 <= 7) {
                    if (checkIfValidMoveIsTakingPiece(startingRowIndex, startingColumnIndex, startingRowIndex + 2, startingColumnIndex + 2)) {
                        return true;
                    }
                }
                if(startingColumnIndex -2 <= 7 && startingColumnIndex - 2 >= 0) {
                    if (checkIfValidMoveIsTakingPiece(startingRowIndex, startingColumnIndex, startingRowIndex + 2, startingColumnIndex - 2)) {
                        return true;
                    }
                }
            }
            System.out.println("None...");
        }
        return false;
    }

    public String checkWinner(){
        if(whitePieceCount == 0){
            checkersBoard.setVisible(false);
            playerTwoWin.setVisible(true);
            return "White wins";

        }
        if(blackPieceCount == 0){
            checkersBoard.setVisible(false);
            playerOneWin.setVisible(true);
            return "Black wins";
        }
        else{
            return null;
        }
    }

    public void movePiece(int startingRow, int startingCol, int endRow, int endCol){
        checkersSquares[endRow][endCol].setId(checkersSquares[startingRow][startingCol].getId());
        checkersSquares[endRow][endCol].setIcon(checkersSquares[startingRow][startingCol].getIcon());
        checkersSquares[startingRow][startingCol].setId(empty);
        checkersSquares[startingRow][startingCol].setIcon(null);
        checkPromotion();
        if(checkWinner() != null){
            checkWinner();
        }

        checkersBoard.repaint();
    }

    public void movePieceCapture(int startingRow, int startingCol, int endRow, int endCol){
        int rowInBetween = (startingRow + endRow)/2;
        int colInBetween = (startingCol + endCol)/2;
        checkersSquares[endRow][endCol].setId(checkersSquares[startingRow][startingCol].getId());
        checkersSquares[endRow][endCol].setIcon(checkersSquares[startingRow][startingCol].getIcon());
        if(checkersSquares[rowInBetween][colInBetween].getId() == 1 || checkersSquares[rowInBetween][colInBetween].getId() == 3){
            whitePieceCount--;
        }
        else{
            blackPieceCount--;
        }
        checkersSquares[rowInBetween][colInBetween].setId(empty);
        checkersSquares[rowInBetween][colInBetween].setIcon(null);
        checkersSquares[startingRow][startingCol].setId(empty);
        checkersSquares[startingRow][startingCol].setIcon(null);
        checkPromotion();
        if(checkWinner() != null){
            checkWinner();
        }

        //checking functionality
        System.out.println("rowInBetween: " + rowInBetween);
        System.out.println("colInBetween: " + colInBetween);
        System.out.println("CheckersSquares [" + endRow +"][" + endCol + "] Id = " + checkersSquares[endRow][endCol].getId());


        if(checkersSquares[endRow][endCol].getIcon() != null){
            System.out.println("This square is not null (has a value)");
        }
        checkersBoard.repaint();

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

        //checking functionality
        System.out.println("Row: "+ row);
        System.out.println("Col: " + col);

        if(!pieceSelected){
            if(checkersSquares[row][col].getId() != empty){
                selectedRow = row;
                selectedCol = col;
                pieceSelected = true;
                checkersSquares[row][col].setBorder(new LineBorder(Color.yellow, 3));

                //Checking functionality
                System.out.println("Selected Row: " + row);
                System.out.println("Selected Col: " + col);
                System.out.println("PieceSelected: "+ pieceSelected);
                System.out.println("Active White turn: " + activeWhiteTurn);
            }
        }
        else{
            if(checkIfValidMove(selectedRow, selectedCol, row, col)){
                movePiece(selectedRow, selectedCol, row, col);
                pieceSelected = false;
                checkersSquares[selectedRow][selectedCol].setBorder(null);
                switchPlayerTurn();

                //Checking functionality
                System.out.println("Valid move: moved piece");
                System.out.println("pieceSelected: " + pieceSelected);
                System.out.println("checkersSquares [" + selectedRow + "][" + selectedCol + "] setBorder: " + checkersSquares[selectedRow][selectedCol].getBorder());
                System.out.println("Active Player Status: " + activeWhiteTurn);

            }
            if(checkIfValidMoveIsTakingPiece(selectedRow, selectedCol, row, col)){
                movePieceCapture(selectedRow, selectedCol, row, col);
                pieceSelected = false;
                checkersSquares[selectedRow][selectedCol].setBorder(null);
                if(!checkOpportunityForTakeNextTurn(row, col)){
                    switchPlayerTurn();
                }
                //Checking functionality
                System.out.println("Valid move: took piece");
                System.out.println("pieceSelected: " + pieceSelected);
                System.out.println("checkersSquares [" + selectedRow + "][" + selectedCol + "] setBorder: " + checkersSquares[selectedRow][selectedCol].getBorder());
                System.out.println("Active Player Status: " + activeWhiteTurn);
            }
            else{
                checkersSquares[selectedRow][selectedCol].setBorder(null);
                pieceSelected = false;

                //Checking functionality
                System.out.println("Not valid move piece unselected");
                System.out.println("checkersSquares [" + selectedRow + "][" + selectedCol + "] setBorder: " + checkersSquares[selectedRow][selectedCol].getBorder());
                System.out.println("PieceSelected: " + pieceSelected);
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

    //Repaint
}