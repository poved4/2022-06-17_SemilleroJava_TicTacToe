
import java.awt.Color;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Grid {

    private final Color colorWin = Color.decode("#FFFF33");
    private final Color colorLose = Color.decode("#3399FF");
    private final Color colorIdle = Color.decode("#FFFFFF");
    private final Color colorTeamX = Color.decode("#FF0000");
    private final Color colorTeamO = Color.decode("#0066FF");

    private int counter;
    private boolean vsIA;
    private boolean player;
    private boolean canPlay;
    private boolean playing;

    private JLabel info;
    private JLabel score;
    private boolean[] xo;
    private JButton[] btns;

    public Grid() {
        this.xo = null;
        this.btns = null;
        this.info = null;
        this.counter = 0;
        this.vsIA = false;
        this.player = true;
        this.playing = false;
        this.canPlay = false;
    }

    public Grid(JButton[] btns, JLabel info, JLabel score) {
        this.btns = btns;
        this.info = info;
        this.score = score;
    }

    public boolean getPlaying() {
        return playing;
    }

    public void setVsIA() {
        this.vsIA = !this.vsIA;
    }

    private void clearFields() {
        for (JButton btn : this.btns) {
            btn.setForeground(colorIdle);
            btn.setText(" ");
        }
    }

    private void checkBoxes() {
        int size = this.btns.length;
        this.xo = new boolean[size];
        String letter = getTeamLetter();

        for (int i = 0; i < size; i++) {
            xo[i] = btns[i].getText().equalsIgnoreCase(letter);
        }

        boolean haveWin = (xo[4] && ((xo[0] && xo[8]) || (xo[2] && xo[6])))
                || ((xo[0] && xo[1] && xo[2]) || (xo[3] && xo[4] && xo[5]) || (xo[6] && xo[7] && xo[8]))
                || ((xo[0] && xo[3] && xo[6]) || (xo[1] && xo[4] && xo[7]) || (xo[2] && xo[5] && xo[8]));

        if (haveWin) {
            this.playing = this.canPlay = false;
            updateScore(letter);
            announces("TEAM  " + letter + "  WINS!!", "img/win.png");
        }

        if (counter == (size - 1) && !haveWin) {
            this.canPlay = this.playing = false;
            announces("EMPATE!!", "img/lose.png");
        }

        this.xo = null;
    }

    public void updateScore(String letter) {

        String[] phrase = score.getText().split("    ");
        String[] teamX = phrase[0].split(":");
        String[] teamO = phrase[1].split(":");

        int scoreX = (int) Integer.parseInt(teamX[1].trim());
        int scoreO = (int) Integer.parseInt(teamO[1].trim());

        if (letter.equals("X")) {
            scoreX++;
        }
        if (letter.equals("O")) {
            scoreO++;
        }

        String toSend = "X : " + scoreX + "    " + "O : " + scoreO;
        this.score.setText(toSend);
    }

    private void announces(String message) {
        this.info.setText(message);
    }

    private void announces(String message, String path) {
        Icon icon = new ImageIcon(getClass().getResource(path));
        JOptionPane.showMessageDialog(null, message, "Triki", JOptionPane.INFORMATION_MESSAGE, icon);
    }

    private String getTeamLetter() {
        String letter = this.player ? "X" : "O";
        return letter.toUpperCase();
    }

    private Color getTeamColor() {
        Color color = this.player ? colorTeamX : colorTeamO;
        return color;
    }

    private void nextTurn() {
        this.counter++;
        this.player = !this.player;
        announces("player " + getTeamLetter() + "'s turn");
    }

    public void setBtnTeam(JButton btn) {
        
        if (this.canPlay && btn.getText().equals(" ")) {
            btn.setForeground(getTeamColor());
            btn.setText(getTeamLetter());
            checkBoxes();
            nextTurn();
        }
        
        if (vsIA && !this.canPlay && btn.getText().equals(" ")) {
            btn.setForeground(getTeamColor());
            btn.setText(getTeamLetter());
            checkBoxes();
            nextTurn();
        }
    }

    public void ia() {
        if (vsIA) {

            this.canPlay = false;
            //Thread.sleep(2000);
            String team = "";
            int size = this.btns.length;
            int[] matriz = new int[size];

            for (int i = 0; i < size; i++) {
                team = btns[i].getText().toUpperCase();
                matriz[i] = team.equals("X") ? 1 : 0;
                matriz[i] = team.equals("O") ? 2 : 0;
            }

            //int diagonalPrincipal = matriz[0] + matriz[1] + matriz[2];
            for (int i = 0; i < size; i++) {
                System.out.println(i + ": " + matriz[i]);
            }

            System.out.println("\n");
            setBtnTeam(btns[4]);
            this.canPlay = true;
        }
    }

    public void start() {
        clearFields();
        this.counter = 0;
        this.player = true;
        this.canPlay = true;
        this.playing = true;
        announces("player X's turn");
    }

}
