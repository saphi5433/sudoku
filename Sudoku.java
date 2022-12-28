import java.util.ArrayList;

public class Sudoku {
    public ArrayList<SudokuGrid> SudokuDS = new ArrayList<SudokuGrid>();
    private int position[] = new int[2];

    Sudoku() {
        ArrayList<SudokuGrid> tmp = SudokuStore.getStore();

        for(int i = 0; i < tmp.size(); i++) {
            this.SudokuDS.add(tmp.get(i));
        }
    }

    
    /**
     * Chiama il metodo SudokuStore.SaveSudokuDS il quale salva i dati nel file SudokuDS.txt
     * */ 
    void save(){
        SudokuStore.SaveSudokuDS(this.SudokuDS);
    }

    /**
     * È il metodo principale, ininzialmente fa delle chiamate a SudokuInterface per ottenere le informazioni iniziali successivamente eseguei l codice del gioco 
     * */ 
    void main(){
        int isCorrect = 0; // 0 ignore  -  1 not valid   - 2 valid  
        int id = SudokuInterface.InitialInfo(this.SudokuDS);
        if(id == -1) return;
 
        save();
        boolean autoControl = SudokuInterface.autoControl();
        SudokuGrid actualGrid = this.SudokuDS.get(SudokuStore.getPositionFromId(SudokuDS, id));

        do {
            char choose = SudokuInterface.playGame(actualGrid, this.position, isCorrect);

            switch (choose){
                case 'w':
                    if(this.position[1] > 0) this.position[1] -= 1;
                    isCorrect = 0;
                    break;
                case 's':
                    if(this.position[1] < 8) this.position[1] += 1;
                    isCorrect = 0;
                    break;
                case 'a':
                    if(this.position[0] > 0) this.position[0] -= 1;
                    isCorrect = 0;
                    break;
                case 'd':
                    if(this.position[0] < 8) this.position[0] += 1;
                    isCorrect = 0;
                    break;
                case '1', '2', '3', '4', '5', '6', '7', '8', '9': 
                    int value = Integer.parseInt(choose + "");
                    if(actualGrid.getCells()[this.position[1]][this.position[0]].getIsEditable())
                        if(autoControl){
                            if(SudokuValidator.isSafe(this.position[1], this.position[0], value, actualGrid)){
                                actualGrid.getCells()[this.position[1]][this.position[0]].setValue(value);
                                isCorrect = 0;
                            } else {
                                isCorrect = 1;
                            }
                        } else {
                            actualGrid.getCells()[this.position[1]][this.position[0]].setValue(value);
                            isCorrect = 0;
                        }
                    break;
                case 'x':
                    if(actualGrid.getCells()[this.position[1]][this.position[0]].getIsEditable())
                        actualGrid.getCells()[this.position[1]][this.position[0]].setValue(-1);
                    isCorrect = 0;
                    break;
                case 'n': 
                    id = SudokuStore.nextId(SudokuDS);
                    this.SudokuDS.add(SudokuGenerator.get(id, SudokuInterface.setDifficulty(true), 9));
                    actualGrid = this.SudokuDS.get(SudokuStore.getPositionFromId(SudokuDS, id));
                    position[0] = 0;
                    position[1] = 0;
                    isCorrect = 0;
                    save(); 
                    break;
                case 'u':
                    save();
                    break;
                case 'q':
                    save();
                    isCorrect = 0;
                    return;
                case 'c':
                    if(SudokuSolver.isGridValid(actualGrid)){
                        isCorrect = 2;
                        actualGrid.setStatus('F');
                    } 
                    else isCorrect = 1;
                    break;
                case 'r':
                    if(!SudokuSolver.solve(actualGrid)) isCorrect = 1;
                    else isCorrect = 2;
                    break;
                default:
                    System.err.println("Guardare la procedura printGrid, non ha controllato l'interazione di un tasto"); 
            }
        } while(true);
    }
}
