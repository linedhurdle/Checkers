
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheckersButton extends JButton {
    private int pieceID = 0;
    public CheckersButton() {
        this(0);
    }

    public CheckersButton(int ID) {
        pieceID = ID;
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleClick();
            }
        });
    }

    public void setId(int id) {
        pieceID = id;
    }

    public int getId() {
        return pieceID;
    }

    private void handleClick() {
        CheckersBoard.getInstance().handleButtonClick(this);
    }
}