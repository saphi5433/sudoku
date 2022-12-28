public class SudokuGrid {
    private int id;
    private char status;
    private SudokuCell[][] cells;
    private int difficulty;
    private int N;
    private int SRN; // Square root name 

    SudokuGrid(int id, int difficulty, int N, char status){
        this.id = id;
        this.difficulty = difficulty;
        this.N = N;
        this.SRN = (int) Math.sqrt(N);
        this.status = status;
        cells = new SudokuCell[N][N];
    }

    /**
     * Ritorna l'id
     * @return id
     */
    int getId() {
        return id;
    }

    /**
     * Ritorna lo stato
     * 
     * @return status
     */
    char getStatus() {
        return status;
    }

    /**
     * Imposta lo stato
     * 
     * @param status
     */
    void setStatus(char status) {
        this.status = status;
    }

    /**
     * Ritorna la variabile privata cells[][]
     * 
     * @return SudokuCell[][]
     */
    SudokuCell[][] getCells() {
        return cells;
    }

    /**
     * Ritorna SRN
     * 
     * @return SRN 
     */
    int getSRN() {
        return SRN;
    }

    /**
     * Ritorna la lunghezza
     * 
     * @return N
    */
    int getN() {
        return N;
    }

    /** 
     * default resetGrid
    */
    void resetGrid(){
        resetGrid(false);
    }

    /**
     * Imposta tutte le celle della grid a -1 
     * 
     * @param isEditable determina se le celle sono editabili
     */
    void resetGrid(boolean isEditable) {
        for(int row = 0; row < N; row++)
            for(int col = 0; col < N; col++)
                this.cells[row][col] = new SudokuCell(-1, isEditable);
    }

    /**
     * Imoposta la grid con la grid passata
     * 
     * @param cells 
     */
    void setCells(SudokuCell[][] cells) {
        for(int row = 0; row < N; row++)
            for(int col = 0; col < N; col++)
                this.cells[row][col] = cells[row][col];
    }

    /**
     * Imposta la difficolta
     * 
     * @param difficulty
     */
    void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Ritorna la difficolta
     * 
     * @return difficulty
     */
    int getDifficulty() {
        return difficulty;
    }

    /**
     * Ritorna la difficolta in formato scelta
     * 
     * @return difficulty
     */
    int getDifficultyToChoose() {
        switch(this.difficulty) {
            case 10: 
                return 1;
            case 25: 
                return 2;
            case 42:
                return 3;
            case 50: 
                return 4;
            case 60: 
                return 5;
        }

        return 0;
    }
}
