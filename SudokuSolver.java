public class SudokuSolver {
    /**
     * Controlla se il Sudoku è risolvibile
     * 
     * @param SD Sudoku Grid
     * @return se è risolvibe o no
     */
    static boolean isSolvable(SudokuGrid SD){
        int un[] = Unasigned(SD);

        SudokuGrid SDtmp = new SudokuGrid(-1, SD.getDifficulty(), SD.getN(), 'T');
        SDtmp.resetGrid();

        for(int row = 0; row < SD.getN(); row++){
            for(int col = 0; col < SD.getN(); col++){
                SDtmp.getCells()[row][col].setValue(SD.getCells()[row][col].getValue());
            }
        }

        return solve(SDtmp, un[0], un[1]);
    }

    static boolean solve(SudokuGrid SD){
        int un[] = Unasigned(SD);
        
        if(un[0] == -1 || un[1] == -1) return true;
        return solve(SD, un[0], un[1]);
    }

    /**
     * Risolve il Sudoku
     * 
     * @param SD SudokuGrid 
     * @param i posizione x 
     * @param j posizione y
     * @return se la cella inserita è valida 
     */
    static boolean solve(SudokuGrid SD, int i, int j){
        for(int num = 1; num <= SD.getN(); num++){
            if(SudokuValidator.isSafe(i, j, num, SD)){
                SD.getCells()[i][j].setValue(num);

                int un[] = Unasigned(SD);
                if(un[0] == -1 || un[1] == -1) return true;
                if(solve(SD, un[0], un[1]))
                    return true;

                SD.getCells()[i][j].setValue(-1);
            }
        }

        return false;
    }
    
    /**
     * Ritorna la prima cella libera
     * 
     * @param SD SudokuGrid
     * @return un array con la riga e la colonna
     */
    static int[] Unasigned(SudokuGrid SD){
        int res[] = new int[2];
        res[0] = -1;
        res[1] = -1;

        for(int row = 0; row < SD.getN(); row++){
            for(int col = 0; col < SD.getN(); col++){
                if(SD.getCells()[row][col].getValue() == -1){
                    res[0] = row;
                    res[1] = col;
                    return res;
                }
            }
        }
        return res;
    }

    /**
     * Controlla se la Grid è valida
     * 
     * @param SD Sudoku Grid 
     * @return se la grid è valida
     */
    static boolean isGridValid(SudokuGrid SD){
        for(int row = 0; row < SD.getN(); row++){
            for(int col = 0; col < SD.getN(); col++){
                if(!SudokuValidator.isSafe2(row, col, SD.getCells()[row][col].getValue(), SD) || SD.getCells()[row][col].getValue() == -1) return false;
            }
        }

        return true;
    }
    
}
