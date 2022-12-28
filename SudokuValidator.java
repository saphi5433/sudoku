public class SudokuValidator {

    /**
     * Controlla se la cella fosse valida con il valore passato in input
     * 
     * @param row
     * @param column
     * @param value
     * @param SD
     * @return se la cella è valida
     */
    static boolean isSafe(int row, int column, int value, SudokuGrid SD) {
        if(row(row, value, SD) &&
           column(column, value, SD) &&
           box(row - row % SD.getSRN(), column - column % SD.getSRN(), value, SD))
          return true;

        return false;
    }
  
    /**
     * Controlla se il valore passato in input è già all'interno del box
     * 
     * @param rowStart cordinata x della prima cella del box
     * @param columnStart cordinata y della prima cella del box
     * @param value
     * @param SD
     * @return se la cella nel box è valida
     */
    static boolean box(int rowStart, int columnStart, int value, SudokuGrid SD) {
        for(int i = 0; i < SD.getSRN(); i++)
            for(int j = 0; j < SD.getSRN(); j++)
                if(SD.getCells()[rowStart + i][columnStart + j].getValue() == value)
                    return false;
        return true;
    }
    
    /**
     * Controlla se il valore passato in input c'è già una volta nella riga
     * 
     * @param row
     * @param value
     * @param SD
     * @return se la cella è valida
     */
    static private boolean row(int row, int value, SudokuGrid SD){
        for(int i = 0; i < SD.getN(); i++)
            if(SD.getCells()[row][i].getValue() == value)
                return false;
        
        return true;
    }

    /**
     * Controlla se il valore passato in input c'è già una volta nella colonna
     *
     * @param column
     * @param value
     * @param SD
     * @return se la cella è valida
     */
    static private boolean column(int column, int value, SudokuGrid SD){
        for(int i = 0; i < SD.getN(); i++)
            if(SD.getCells()[i][column].getValue() == value) 
                return false;

        return true;
    }


    /**
     * Controlla se la cella è valida
     * 
     * @param row
     * @param column
     * @param value
     * @param SD
     * @return
     */
    static boolean isSafe2(int row, int column, int value, SudokuGrid SD){
        if(row2(row, value, SD) &&
           column2(column, value, SD) &&
           box2(row - row % SD.getSRN(), column - column % SD.getSRN(), value, SD))
          return true;

        return false;
    }

    /**
     * Controlla se il valore della cella c'è una sola volta nel box
     * 
     * @param rowStart cordinata x della prima cella del box
     * @param columnStart cordinata y della prima cella del box
     * @param value
     * @param SD
     * @return
     */
    static private boolean box2(int rowStart, int columnStart, int value, SudokuGrid SD) {
        int cnt = 0;
        for(int i = 0; i < SD.getSRN(); i++)
            for(int j = 0; j < SD.getSRN(); j++)
                if(SD.getCells()[rowStart + i][columnStart + j].getValue() == value) cnt++;
        
        return cnt == 1;
    }

    /**
     * Controlla se il valore della cella c'è pì di una volta nella colonna
     * 
     * @param column
     * @param value
     * @param SD
     * @return
     */
    static private boolean column2(int column, int value, SudokuGrid SD){
        int cnt = 0;
        for(int row = 0; row < SD.getN(); row++)
            if(SD.getCells()[row][column].getValue() == value) cnt++;
        
        return cnt == 1;
    }

    /**
     * Controlla se il valore della cella c'è più di una volta nella riga
     * 
     * @param row
     * @param value
     * @param SD
     * @return
     */
    static private boolean row2(int row, int value, SudokuGrid SD){
        int cnt = 0;
        for(int col = 0; col < SD.getN(); col++)
            if(SD.getCells()[row][col].getValue() == value) cnt++;
        

        return cnt == 1;
    }
}
