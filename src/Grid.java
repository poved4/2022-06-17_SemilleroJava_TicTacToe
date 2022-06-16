
import java.awt.Color;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Grid {

    private final Color colorWin = Color.decode("#FFFF33");
    private final Color colorLose = Color.decode("#3399FF");
    private final Color colorIdle = Color.decode("#FFFFFF");
    private final Color colorTeamX = Color.decode("#FF0000");
    private final Color colorTeamO = Color.decode("#0066FF");

    private boolean player;
    private boolean canPlay;
    private boolean isPlaying;

    private JLabel info;
    private JLabel score;
    private JButton[] btns;

    public Grid() {
        this.btns = null;
        this.info = null;
        this.player = true;
        this.canPlay = false;
        this.isPlaying = false;
    }

    public Grid(JButton[] btns, JLabel info, JLabel score) {
        this.btns = btns;
        this.info = info;
        this.score = score;
    }

    public boolean getCanPlay() {
        return canPlay;
    }

    public boolean getPlaying() {
        return isPlaying;
    }
    
    private void clearFields() {
        for (JButton btn : this.btns) {
            btn.setForeground(colorIdle);
            btn.setText(" ");
        }
    }

    private boolean complete() {
        boolean state = true;

        for (int i = 0; i < btns.length; i++) {
            if (btns[i].getText().equals(" ")) {
                state = !state;
                break;
            }
        }

        return state;
    }

    private boolean[] determinePositions(JButton[] fields, String letter) {
        int size = fields.length;
        boolean[] array = new boolean[size];

        for (int i = 0; i < size; i++) {
            array[i] = fields[i].getText().equalsIgnoreCase(letter);
        }

        return array;
    }

    private boolean playerWins(boolean[] array) {
        boolean diagonally = array[4] && ((array[0] && array[8]) || (array[2] && array[6]));

        boolean row = (array[0] && array[1] && array[2])
                || (array[3] && array[4] && array[5])
                || (array[6] && array[7] && array[8]);

        boolean col = (array[0] && array[3] && array[6])
                || (array[1] && array[4] && array[7])
                || (array[2] && array[5] && array[8]);

        return diagonally || row || col;
    }

    private void checkFields() {
        String letter = getTeamLetter();
        boolean[] array = determinePositions(this.btns, letter);

        if (playerWins(array)) {
            updateScore(letter);
            this.isPlaying = this.canPlay = false;
            announces("TEAM  " + letter + "  WINS!!", "img/win.png");
        } else {
            if (complete()) {
                this.canPlay = this.isPlaying = false;
                announces("EMPATE!!", "img/lose.png");
            }
        }
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
        this.player = !this.player;
        announces("player " + getTeamLetter() + "'s turn");
    }

    private int[] translateArrayIA(JButton[] fields) {

        String team = "";
        int size = fields.length;
        int[] matriz = new int[size];

        for (int i = 0; i < size; i++) {
            team = fields[i].getText().toUpperCase();
            if (team.equals(" ")) {
                matriz[i] = 0;
            }
            if (team.equals("X")) {
                matriz[i] = 1;
            }
            if (team.equals("O")) {
                matriz[i] = 3;
            }
        }

        return matriz;
    }

    public void ia() {
        this.canPlay = false;
        IA intelligence = new IA();
        int[] fields = translateArrayIA(this.btns);
        int emptyBtn = intelligence.getMove(fields);
        if (emptyBtn != -1) setBtnTeam(btns[emptyBtn]);
        this.canPlay = true;
    }

    public boolean setBtnTeam(JButton btn) {
        
        boolean state = false;
        
        if (this.isPlaying && btn.getText().equals(" ")) {
            btn.setForeground(getTeamColor());
            btn.setText(getTeamLetter());
            checkFields();
            state = true;
            nextTurn();
        }
        
        return state;
    }

    public void start() {
        clearFields();
        this.player = true;
        this.canPlay = true;
        this.isPlaying = true;
        announces("player " + getTeamLetter() + "'s turn");
    }

}