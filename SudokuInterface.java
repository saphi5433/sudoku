import java.util.ArrayList;
import java.util.Scanner;

public class SudokuInterface {
    static Scanner sc = new Scanner(System.in);

    /**
     * Chiede all'untente delle informazioni per sapere quale sudoku deve ritornare
     * 
     * @param SudokuDS lista do Sudoku Grid
     * @return l'id del sudoku che si vuole giocare
     */
    static int InitialInfo(ArrayList<SudokuGrid> SudokuDS) {
        System.out.println(ChessUtils.ANSI_BLUE + "Informazioni iniziali: " + ChessUtils.ANSI_RESET);
        System.out.println();

        
        String choose = "";
        do {
            System.out.print(ChessUtils.ANSI_YELLOW + "Vuoi riprendere una partita? (y / n) " + ChessUtils.ANSI_RESET);
            choose = sc.nextLine().toLowerCase();
        } while (!(choose.equals("y") || choose.equals("n")));
            
        if (choose.equals("y"))
            return continueGame(SudokuDS);
        else {
            do {
                System.out.print(ChessUtils.ANSI_YELLOW + "Vuoi inserire manualmente la griglia iniziale del Sudoku? (y / n) " + ChessUtils.ANSI_RESET);
                choose = sc.nextLine().toLowerCase();
            } while(!(choose.equals("y") || choose.equals("n")));
            if(choose.equals("y"))
                return startGenerateManual(SudokuDS);

        }
        
        int id = SudokuStore.nextId(SudokuDS);
        SudokuDS.add(configurationAutoGenerateSudoku(id));
        return id;
    }
        
    /**
     * Ritorna un sudoku generato randomicamente
     * 
     * @param id del nuovo sudoku
     * @return SudokuGrid 
     */
    static SudokuGrid configurationAutoGenerateSudoku(int id){
        int difficulty = setDifficulty(true);

        int N = 9;
        SudokuGrid sudokuGrid = SudokuGenerator.get(id, difficulty, N);

        return sudokuGrid;
    }

    /** 
     * @param DS la Sudoku Grid da modificare
     * @param position [0] = x, [1] = y 
     * @param isCorrect
     * @return la selezione dell'utente
     */
    static char playGame(SudokuGrid DS, int position[], int isCorrect){
        clearTerminal();
        return printSudokuGridGamplay(DS, position[0], position[1], true, isCorrect, false);
    }

    /**
     * Fa il clear del terminale
     */
    private static void clearTerminal(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
        
    /**
     * Gestisce la creazione manuale di un Sudoku
     * 
     * @param SudokuDS lista dei sudoku
     * @return id del sudoku inserito dall'utente
     */
    static int startGenerateManual(ArrayList<SudokuGrid> SudokuDS){
        int id = SudokuStore.nextId(SudokuDS);
        int difficulty = setDifficulty(false);
        SudokuGrid SG = new SudokuGrid(id, difficulty, 9, 'U');

        SG.resetGrid(true);

        char choose = ' ';
        int position[] = {0, 0};
        int isCorrect = 0;
        do {
            clearTerminal();
            choose = printSudokuGridGamplay(SG, position[0], position[1], false, isCorrect, true);
            if(choose == 'f')
                System.out.println(ChessUtils.ANSI_RED + "Il sudoku non è risolvibile" + ChessUtils.ANSI_RESET);
            
            switch (choose){
                case 'w': 
                    if(position[1] > 0) 
                        position[1] -= 1;
                    isCorrect = 0;
                    break;
                case 's':
                    if(position[1] < 8) 
                        position[1] += 1;
                    isCorrect = 0;
                    break;
                case 'a':
                    if(position[0] > 0) 
                        position[0] -= 1;
                    isCorrect = 0;
                    break;
                case 'd':
                    if(position[0] < 8) 
                        position[0] += 1;
                    isCorrect = 0;
                    break;
                case '1', '2', '3', '4', '5', '6', '7', '8', '9': 
                    SG.getCells()[position[1]][position[0]].setValue(Integer.parseInt(choose + ""));
                    SG.getCells()[position[1]][position[0]].setEditable(false);
                    isCorrect = 0;
                    break;
                case 'x':
                    SG.getCells()[position[1]][position[0]].setValue(-1);
                    SG.getCells()[position[1]][position[0]].setEditable(true);
                    isCorrect = 0;
                    break;
                case 'q':
                    System.out.println("Sicuro? Tutti gli inserimenti andranno persi (y / n) ");
                    String choose2 = "";
                    do {
                        choose2 = sc.nextLine().toLowerCase();
                    } while (!(choose2.equals("y") || choose2.equals("n")));
                    if(choose2.equals("y")) return -1;
                    isCorrect = 0;
                    break;
                case 'f':
                    isCorrect = 1;
                    break;
                default:
                    System.err.println("Guardare la procedura printGrid, non ha controllato l'interazione di un tasto"); 
            }
        } while (!(choose == 'f') && SudokuSolver.isSolvable(SG));

            SudokuDS.add(SG);
        return id;
    }

    /**
     * Salva il sudoku inserito manualmente
     * 
     * @param SudokuDS lista dei Sudoku
     * @param id del sudoku
     * @param SG Sudoku Grid
     */
    static void save(ArrayList<SudokuGrid> SudokuDS, int id, SudokuGrid SG){
        if(SudokuStore.getPositionFromId(SudokuDS, id) == -1)
            SudokuDS.add(SG);

        SudokuStore.SaveSudokuDS(SudokuDS);
    }

    /**
     * Chiede all'utente se vuole attivare il controllo all'insierimento dei valori
     * 
     * @return boolean la scelta dell'utente
     */
    static boolean autoControl(){
        String choose = "";
        do {
            System.out.println(ChessUtils.ANSI_YELLOW + "Vuoi attivare il controllo all'inserimento dei valori? (y / n) " + ChessUtils.ANSI_RESET);
            try {
                choose = sc.nextLine().toLowerCase();
            } catch (Exception e) {}
        } while(!(choose.equals("y") || choose.equals("n")));
        if(choose.equals("y")) return true;
        return false;
    }

    /**
     * stampa la lista di sudoku
     * 
     * @param SudokuDS Lista di Sudoku Grid
     */
    static void printSudokuDSList(ArrayList<SudokuGrid> SudokuDS){
        for(int i = 0; i < SudokuDS.size(); i++){
            printSudoku(SudokuDS.get(i));
            System.out.println();
        }
        if(SudokuDS.size() == 0) System.out.println("Non ci sono partite salvate");
    }
        
    /**
     * Stampa la lista de sudoku e chiede all'utente quale vuole continuare
     * 
     * @param SudokuDS La lista dei sudoku 
     * @return l'id del sudoku da riprendere
     */
    static int continueGame(ArrayList<SudokuGrid> SudokuDS){
        printSudokuDSList(SudokuDS);
        int id = -1;
        String choose = "";
        do {
            System.out.print("Inserisci l'id del Sudoku che vuoi riprendereo o 'q' per uscire: ");
            choose = sc.nextLine().toLowerCase();

            if(choose.equals("q")) return -1;
            else 
                try {
                    id = Integer.parseInt(choose);
                } catch (Exception e){};

        } while(SudokuStore.getPositionFromId(SudokuDS, id) == -1);
        
        return id;
    }

    /**
     * Stampa una linea vuota
     */
    static private void newLine (){
        System.out.println();
    }

    /**
     * Chide all'utente la difficolta e la ritorna
     * 
     * @param autoGenerate serve a determinare la frase da dire all'utente
     * @return ritorna la difficoltà
     */
    static int setDifficulty(boolean autoGenerate){
        if(autoGenerate)
            System.out.println(ChessUtils.ANSI_YELLOW + "Prima di generare il Sudoku devi selezionare una difficolta: ");
        else 
            System.out.println(ChessUtils.ANSI_YELLOW + "Prima di inserire il Sudoku seleziona una difficolta: ");
        System.out.println("1. Molto Facile");
        System.out.println("2. Facile");
        System.out.println("3. Normale");
        System.out.println("4. Difficile");
        System.out.println("5. Molto difficile" + ChessUtils.ANSI_RESET);

        int dif = 0;
        int difficulty = 0;
        int cnt = 0;
        do {    
            try {
                dif = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {}

            if(cnt > 0)
                System.out.println(ChessUtils.ANSI_RED + "Inserisci un numero valido" + ChessUtils.ANSI_RESET);
            cnt++;
        } while (!(dif == 1 || dif == 2 || dif == 3 || dif == 4 || dif == 5));

        switch(dif){
            case 1: 
                difficulty = 10;
                break;
            case 2: 
                difficulty = 25;
                break;
            case 3:
                difficulty = 42;
                break;
            case 4: 
                difficulty = 50;
                break;
            case 5: 
                difficulty = 60;
                break;
        }

        return difficulty;
    }

    /**
     * Ritorna il testo della dfficoltà
     * 
     * @param difficulty
     * @return il livello di difficoltà formato String
     */
    static String getLevel(int difficulty){
        switch(difficulty){
            case 10: 
                return "Molto facile";
            case 25:
                return "Facile";
            case 42:
                return "Normale";
            case 50:
                return "Difficile";
            case 60:
                return "Molto difficile";
            default:
                return "error";
        }
    }

    /**
     * Stampa li sudoku con informazioni base
     * 
     * @param DS Sudoku Grid
     */
    static void printSudoku(SudokuGrid DS){
        String space = "     ";

        for(int i = 0; i < DS.getN(); i++){
            System.out.print(ChessUtils.ANSI_BLUESOFT + (i == DS.getN() - 1 ? "+---+" : "+---") + ChessUtils.ANSI_RESET);
        }

        System.out.print(ChessUtils.ANSI_BLUE + space + "id: " + DS.getId() + ChessUtils.ANSI_RESET);

        newLine();

        for(int row = 0; row < DS.getN(); row++){
            for(int col = 0; col < DS.getN(); col++){
                String color;
                if(col % 3 == 0)
                    color = ChessUtils.ANSI_BLUESOFT;
                else
                    color = ChessUtils.ANSI_GREENSOFT;
                

                System.out.print(color + (col == 0 ? "| " : " | ") + ChessUtils.ANSI_RESET);

                String valueToPrint = (DS.getCells()[row][col].getValue() == -1 ? " " : String.valueOf(DS.getCells()[row][col].getValue()));
                
                if (DS.getCells()[row][col].getIsEditable())
                    System.out.print(ChessUtils.ANSI_CYAN + valueToPrint + ChessUtils.ANSI_RESET);
                else 
                    System.out.print(ChessUtils.ANSI_WHITE + valueToPrint + ChessUtils.ANSI_RESET);

                if(col == DS.getN() - 1)
                    System.out.print(ChessUtils.ANSI_BLUESOFT + " |" + ChessUtils.ANSI_RESET);
            }

            if(row == 0) System.out.print(ChessUtils.ANSI_BLUE + space + "stato: " + ((DS.getStatus() + "").equals("F") ? "Finito" : "Non finito")+ ChessUtils.ANSI_RESET);            
    
            
            

            newLine();

            for(int i = 0; i < DS.getN(); i++){
                String color;
                String color2;
                if(row % 3 == 2)
                    color = ChessUtils.ANSI_BLUESOFT;
                else
                    color = ChessUtils.ANSI_GREENSOFT;

                if(i % 3 == 0 || row % 3 == 2)
                    color2 = ChessUtils.ANSI_BLUESOFT;
                else
                    color2 = ChessUtils.ANSI_GREENSOFT;

                System.out.print(color2 + "+" + color + "---" + (i == DS.getN() - 1 ? ChessUtils.ANSI_BLUESOFT + "+": "") + ChessUtils.ANSI_RESET);

                
            }

            if(row == 0) System.out.print(ChessUtils.ANSI_BLUE + space + "livello: " + getLevel(DS.getDifficulty())+ ChessUtils.ANSI_RESET);            

            newLine();
        }
    }
    
    /**
     * Stampa la grid del sudoku con con i controllli di fianco 
     * 
     * @param DS Sudoku Grid
     * @param px posizione asse x
     * @param py posizione asse y
     * @param viewControlls se far vedere i controlli del Gioco
     * @param isCorrect cosa mostrare nello stato, 0 stampa Finito o non Finito, 1 Non valido, 2 Valido
     * @param manualInsert
     * @return input dell'utente sulla scelta
     */
    static char printSudokuGridGamplay(SudokuGrid DS, int px, int py, boolean viewControlls, int isCorrect, boolean manualInsert) { // 0 null 1 not valid 2 vald 
        String space = "     ";
        
        for(int i = 0; i < DS.getN(); i++){
            System.out.print(ChessUtils.ANSI_BLUESOFT + (i == DS.getN() - 1 ? "+---+" : "+---") + ChessUtils.ANSI_RESET);
        }
        
        System.out.print(ChessUtils.ANSI_BLUE + space + "sudoku 1.0.0" + ChessUtils.ANSI_RESET);
        
        newLine();
        
        for(int row = 0; row < DS.getN(); row++){
            for(int col = 0; col < DS.getN(); col++){
                String color;
                if(col % 3 == 0)
                color = ChessUtils.ANSI_BLUESOFT;
                else
                color = ChessUtils.ANSI_GREENSOFT;
                
                
                System.out.print(color + (col == 0 ? "| " : " | ") + ChessUtils.ANSI_RESET);
                
                String valueToPrint = (DS.getCells()[row][col].getValue() == -1 ? " " : String.valueOf(DS.getCells()[row][col].getValue()));
                
                if(px == col && py == row && DS.getCells()[row][col].getIsEditable())
                System.out.print(ChessUtils.ANSI_CYAN_BACKGROUND + ChessUtils.ANSI_BLACK + valueToPrint + ChessUtils.ANSI_RESET);
                else if(px == col && py == row && !DS.getCells()[row][col].getIsEditable())
                System.out.print(ChessUtils.ANSI_CYAN_BACKGROUND + ChessUtils.ANSI_WHITE + valueToPrint + ChessUtils.ANSI_RESET);
                else if (DS.getCells()[row][col].getIsEditable())
                System.out.print(ChessUtils.ANSI_CYAN + valueToPrint + ChessUtils.ANSI_RESET);
                else 
                System.out.print(ChessUtils.ANSI_WHITE + valueToPrint + ChessUtils.ANSI_RESET);
                
                if(col == DS.getN() - 1)
                System.out.print(ChessUtils.ANSI_BLUESOFT + " |" + ChessUtils.ANSI_RESET);
            }
            
            if(row == 0 && viewControlls) System.out.print(ChessUtils.ANSI_BLUE + space + "livello: " + getLevel(DS.getDifficulty())+ ChessUtils.ANSI_RESET);
            if(row == DS.getN() - 7 && (viewControlls || manualInsert)) System.out.print(ChessUtils.ANSI_CYAN + space + "Movimenti" + ChessUtils.ANSI_RESET);
            if(row == DS.getN() - 6 && (viewControlls || manualInsert)) System.out.print(ChessUtils.ANSI_CYAN + space + " a - Sinistra" + ChessUtils.ANSI_RESET);
            if(row == DS.getN() - 5 && (viewControlls || manualInsert)) System.out.print(ChessUtils.ANSI_CYAN + space + " d - Destra" + ChessUtils.ANSI_RESET);
            if(row == DS.getN() - 4 && (viewControlls || manualInsert)) System.out.print(ChessUtils.ANSI_CYAN + space + "Comandi" + ChessUtils.ANSI_RESET);
            if(row == DS.getN() - 3 && (viewControlls || manualInsert)) System.out.print(ChessUtils.ANSI_CYAN + space + " x - Cancella numero" + ChessUtils.ANSI_RESET);
            if(row == DS.getN() - 2 && viewControlls) System.out.print(ChessUtils.ANSI_CYAN + space + " n - Genera nuovo sudoku " + ChessUtils.ANSI_RESET);
            if(row == DS.getN() - 2 && manualInsert) System.out.print(ChessUtils.ANSI_CYAN + space + " q - Esci" + ChessUtils.ANSI_RESET);
            if(row == DS.getN() - 1 && viewControlls) System.out.print(ChessUtils.ANSI_CYAN + space + " u - Salva" + ChessUtils.ANSI_RESET);
            
            
            newLine();
            
            for(int i = 0; i < DS.getN(); i++){
                String color;
                String color2;
                if(row % 3 == 2)
                color = ChessUtils.ANSI_BLUESOFT;
                else
                color = ChessUtils.ANSI_GREENSOFT;
                
                if(i % 3 == 0 || row % 3 == 2)
                color2 = ChessUtils.ANSI_BLUESOFT;
                else
                color2 = ChessUtils.ANSI_GREENSOFT;
                
                System.out.print(color2 + "+" + color + "---" + (i == DS.getN() - 1 ? ChessUtils.ANSI_BLUESOFT + "+": "") + ChessUtils.ANSI_RESET);
            }
            
            if(row == 0 && viewControlls && isCorrect == 0) System.out.print(ChessUtils.ANSI_BLUE + space + "stato: " + ((DS.getStatus() + "").equals("F") ? "Finito" : "Non Finito") + ChessUtils.ANSI_RESET);
            if(row == 0 && viewControlls && isCorrect != 0) System.out.print(ChessUtils.ANSI_BLUE + space + "stato: " + (isCorrect == 1 ? ChessUtils.ANSI_RED + "Non valido " : ChessUtils.ANSI_GREEN  + "Valido") + ChessUtils.ANSI_RESET);
            
            if(row == DS.getN() - 7 && (viewControlls || manualInsert)) System.out.print(ChessUtils.ANSI_CYAN + space + " w - sopra" + ChessUtils.ANSI_RESET);
            if(row == DS.getN() - 6 && (viewControlls || manualInsert)) System.out.print(ChessUtils.ANSI_CYAN + space + " s - sotto" + ChessUtils.ANSI_RESET);
            if(row == DS.getN() - 4 && (viewControlls || manualInsert)) System.out.print(ChessUtils.ANSI_CYAN + space + " 1..9 - Insersci numero" + ChessUtils.ANSI_RESET);
            if(row == DS.getN() - 3 && viewControlls) System.out.print(ChessUtils.ANSI_CYAN + space + " c - Controlla sudoku " + ChessUtils.ANSI_RESET);
            if(row == DS.getN() - 3 && manualInsert) System.out.print(ChessUtils.ANSI_CYAN + space + " f - Finito di inserire i dati" + ChessUtils.ANSI_RESET);
            if(row == DS.getN() - 2 && viewControlls) System.out.print(ChessUtils.ANSI_CYAN + space + " r - Risolvi sudoku " + ChessUtils.ANSI_RESET);
            if(row == DS.getN() - 1 && viewControlls) System.out.print(ChessUtils.ANSI_CYAN + space + " q - Esci" + ChessUtils.ANSI_RESET);
            
            newLine();

        }

        char res = ' ';
        do {
            System.out.print("Inserisci l'azione che vuoi eseguire: " + ChessUtils.ANSI_CYAN);
            try{
                res = sc.nextLine().toLowerCase().toCharArray()[0];  
            } catch(Exception e) {System.out.println(ChessUtils.ANSI_RED + "Inserisci una lettera valida" + ChessUtils.ANSI_RESET);};
        } while (!charIsValid(res, manualInsert ? "manualInsert" :"AutoGenerate"));

        System.out.print(ChessUtils.ANSI_RESET);
        return res;
    }

    /**
     * Controlla se il carattere passato in entrata è valido
     * 
     * @param c carattere
     * @param state Indica se che tipo di caratteri può supportare
     * @return se il carattere è valido
     */
    static boolean charIsValid(char c, String state) {
        if(state.equals("AutoGenerate")) 
            if(c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' ||
               c == 'w' || c == 's' || c == 'd' || c == 'a' || c == 'c' || c == 'x' || c == 'n' || c == 'q' || c == 'r' || 
               c == 'u') return true;

        if(state.equals("manualInsert"))
            if(c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' ||
               c == 'w' || c == 's' || c == 'd' || c == 'a' || c == 'x' || c == 'q' || c == 'f') return true;
        return false;
    }
}
