public class IA {

    private int[][] winningOptions;

    public IA() {
        this.winningOptions = new int[][]{
            {0, 4, 8},
            {2, 4, 6},
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8}};
    }

    /* returns an array of integers with the sum of the winning combinations */
    private int[] fieldSums(int[] fields) {
        return new int[]{
            (fields[0] + fields[4] + fields[8]),//0. main diagonal
            (fields[2] + fields[4] + fields[6]),//1. secondary diagonal
            (fields[0] + fields[1] + fields[2]),//2. row
            (fields[3] + fields[4] + fields[5]),//3. row
            (fields[6] + fields[7] + fields[8]),//4. row
            (fields[0] + fields[3] + fields[6]),//5. col
            (fields[1] + fields[4] + fields[7]),//6. col
            (fields[2] + fields[5] + fields[8]),//7. col
        };
    }

    /* detects the losing possibility  */
    private int threatenedField(int[] fields) {
        int position = -1;
        boolean endangered = false;

        for (int i = 0; i < fields.length; i++) {
            
            if (fields[i] == 2 || fields[i] == 6) 
                { position = i; endangered = true; }
            
            if (position != -1 && fields[position] == 2 && fields[i] == 6)
                { position = i; endangered = true; }
            
        }

        return endangered ? position : -1;
    }

    /* occupies the opponent's winning field */
    private int blockThreat(int[] fields, int row) {

        int position = -1;

        for (int col = 0; col < winningOptions[row].length; col++) {
            if (fields[winningOptions[row][col]] == 0) {
                position = winningOptions[row][col];
                break;
            }
        }

        return position;
    }

    /* freely moves */
    private int freeMovement(int[] fields) {
        int position = -1;

        for (int i = 0; i < fields.length; i++) {
            if (fields[i] == 0) {
                position = i;
                break;
            }
        }

        return position;
    }

    /* returns an integer with the position to occupy */
    public int getMove(int[] fields) {
        int[] vectorSums = fieldSums(fields);
        int endangered = threatenedField(vectorSums);
        return (endangered == -1)
                ? freeMovement(fields)
                : blockThreat(fields, endangered);
    }

}