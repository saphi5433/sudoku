public class SudokuGenerator {
    static SudokuGrid get(int id, int difficulty, int N){
        SudokuGrid sudokuData = new SudokuGrid(id, difficulty, N, 'U');
        sudokuData.resetGrid();

        fillDiagonal(sudokuData);
        fillRemaining(0, sudokuData.getSRN(), sudokuData);

        removeValues(sudokuData);
        
        return sudokuData;
    }

    /**
     * Imposta casualmente N celle (in base alla difficulty) a -1 randomicamente
     * 
     * @param SG Sudoku grid
     */
    static private void removeValues(SudokuGrid SG){
        int remainToRemove = SG.getDifficulty();
        while(remainToRemove != 0){
            int cell = rangomGenerator(SG.getN() * SG.getN()) - 1;

            int i = cell / SG.getN();
            int j = cell % SG.getN();

            if(SG.getCells()[i][j].getValue() != -1){
                SG.getCells()[i][j].setValue(-1);
                SG.getCells()[i][j].setEditable(true);
                remainToRemove--;
            }
        }
    }

    /*
        questo metodo è recursivo
        una volta chiamato chiamato continua fino che ha finito di ricreare un possibile sudoku

        inizia a linea 67 -> dove prova a inserire i 9 numeri
        trovato il numero corretto lo inserisce nella grid
        viene rifatta la call allo stesso metodo
            -> se resituisce true anchesso restituirà trure
            -> se resituisce false reimposta la cella a 0 e restutuisce false 

        con la nuova chiamata esegue dei controlli
            0. se ci si trova all'ultima colonna -> si passa alla prossima riga
            1. se la tabella è completa -> resituisce true  //potrebbe essere inutile, da verificare
            salta i box precedentemente riempiti
                2. se si trova nel primo box -> imposta j al box successivo orrizontalmente 
                3. se si trova nel prima colonna del box centrale -> imposta al box successivo orrizontale
                4. se si trova nell'ultimo box -> ca alla successiva riga partendo dalla prima colonna 
                    5. se si trova all'ultima riga finisce tutto 

    */ 
    /**
     * questo metodo è recursivo
     * una volta chiamato chiamato continua fino che ha finito di ricreare un possibile sudoku
     * 
     * Per facilitare la lettura iniziare dal for
     * Prova ad inserire i 9 numeri
     * trovato un possibile numero corretto lo inserisce nella grid
     * viene rifatta la call allo stesso metodo
     *   -> se resituisce true anchesso restituirà trure
     *   -> se resituisce false reimposta la cella a 0 e restutuisce false 
     * 
     *   con la nuova chiamata esegue dei controlli
     *       0. se ci si trova all'ultima colonna -> si passa alla prossima riga
     *       1. se la tabella è completa -> resituisce true  //potrebbe essere inutile, da verificare
     *       salta i box precedentemente riempiti
     *           2. se si trova nel primo box -> imposta j al box successivo orrizontalmente 
     *           3. se si trova nel prima colonna del box centrale -> imposta al box successivo orrizontale
     *           4. se si trova nell'ultimo box -> va alla successiva riga partendo dalla prima colonna 
     *               5. se si trova all'ultima riga finisce tutto 
     * 
     * 
     * @param i cordinata x della cella
     * @param j cordinata y della cella
     * @param SD Sudoku grid
     * @return true o false se è valido
     */
    static private boolean fillRemaining(int i, int j, SudokuGrid SD){
        if(j >= SD.getN() && i < SD.getN() - 1){ // arrivata alla fine della riga vai alla prossima (tranne l'ultima)
            i += 1;
            j = 0;
        }

        if(i >= SD.getN() && j >= SD.getN()){
            return true; // Sono consapevole che non  è necessario visto che la reale fine è: x = 6 e y = 8
        }
            
        // salta i box già riempiti precedentemente
        if(i < SD.getSRN()){
            if(j < SD.getSRN())
                j = SD.getSRN();
        } else if (i < SD.getN() - SD.getSRN()){
            if(j == (int) (i / SD.getSRN()) * SD.getSRN())
                j += SD.getSRN();
        } else {
            if(j == SD.getN() - SD.getSRN()){
                i += 1;
                j = 0;
                if(i >= SD.getN())
                    return true;
            }
        }


        for(int num = 1; num <= SD.getN(); num++){
            if(SudokuValidator.isSafe(i, j, num, SD)){
                SD.getCells()[i][j].setValue(num);
                SD.getCells()[i][j].setEditable(false);
                if(fillRemaining(i, j + 1, SD))
                    return true;

                SD.getCells()[i][j].setValue(0);
            }
        }

        return false;
    }

    /**
     * Chiama la procedura fil box 3 volte, per i 3 quadrati diagonali
     * 
     * @param SD Sudoku Grid
     */
    static private void fillDiagonal(SudokuGrid SD){
        for(int i = 0; i < SD.getN(); i += SD.getSRN())
           fillBox(i, i, SD);
    }

    /**
     * Riempe il box randomicamente
     * 
     * @param rowStart la cordinata x dove inizia il box
     * @param columnStart la cordinata y dove inizia il box
     * @param SD Sudoku Grid
     */
    static private void fillBox(int rowStart, int columnStart, SudokuGrid SD){
        int value;
        for(int i = 0; i < SD.getSRN(); i++) 
            for(int j = 0; j < SD.getSRN(); j++){
                do {
                    value = rangomGenerator(SD.getN());
                } while (!SudokuValidator.box(rowStart, columnStart, value, SD)); // gira fino quanto il box è valido
                SD.getCells()[rowStart + i][columnStart + j].setValue(value);
            }

    }  

    /**
     * Ritorna un numero casuale
     * 
     * @param num numero massimo che può venire estratto
     * @return Intero casuale
     */
    static private int rangomGenerator(int num){
        return (int) Math.floor(Math.random() * num + 1);
    }
}
