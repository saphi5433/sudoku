import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
public class SudokuStore {    

    /** 
     * Se SudokuDS.txt non esiste chiama il metodo per crearlo
     * e legge i dati dal file 
     * 
     * @return lista di Sudoku Grid 
    */
    static ArrayList<SudokuGrid> getStore(){
        File SudokuDSFile = new File("SudokuDS.txt");

        if(!SudokuDSFile.exists()) createSudokuDS(SudokuDSFile);

        return readSudokuDS(SudokuDSFile);
    }

    /**
     * Crea il file SudokuDS.txt
     * 
     * @param SudokuDS gli viene passato il file
     */
    static void createSudokuDS(File SudokuDS){
        try {
            SudokuDS.createNewFile();
        } catch (IOException e) {}
    }

    /**
     * Legge dal file SudokuDS.txt e inserisci i dati in una lista di Sudoku Grid
     * 
     * @param SudokuDSFile
     * @return
     */
    static ArrayList<SudokuGrid> readSudokuDS(File SudokuDSFile){ 
        ArrayList<SudokuGrid> SudokuDS = new ArrayList<SudokuGrid>();
        try {
            Scanner SudokuDSReader = new Scanner(SudokuDSFile);
            while (SudokuDSReader.hasNextLine()) {
                int id = Integer.parseInt(SudokuDSReader.nextLine());
                int N = Integer.parseInt(SudokuDSReader.nextLine());
                int dif = Integer.parseInt(SudokuDSReader.nextLine());
                char status = SudokuDSReader.nextLine().toCharArray()[0];
                SudokuCell cells[][] = new SudokuCell[N][N];
                for(int row = 0; row < N; row++) {
                    String line = SudokuDSReader.nextLine();
                    String data[] = line.split(" ");
                    for(int col = 0; col < N; col++) {
                        cells[row][col] = new SudokuCell(Integer.parseInt(data[col]), false);
                    }
                }
                for(int row = 0; row < N; row++) {
                    String data[] = SudokuDSReader.nextLine().split(" ");
                    for(int col = 0; col < N; col++) {
                        cells[row][col].setEditable(data[col].equals("T") ? true : false);
                    }
                }
                SudokuGrid tmp = new SudokuGrid(id, dif, N, 'U');
                tmp.resetGrid();
                tmp.setCells(cells);
                tmp.setStatus(status);
                SudokuDS.add(tmp);
            }
            SudokuDSReader.close();

        } catch (IOException e) {} 

        return SudokuDS;
    }

    /**
     * Salva le informazioni del gioco nel file SudokuDS.txt
     * 
     * @param SudokuDS Lista di Studoku Grid da salvare
     */
    static void SaveSudokuDS(ArrayList<SudokuGrid> SudokuDS){
        try {
            FileWriter SudokuWriter = new FileWriter("SudokuDS.txt");
            for(int i = 0; i < SudokuDS.size(); i++){
                SudokuGrid tmp = SudokuDS.get(i);
                SudokuWriter.write(tmp.getId() + "\n");
                SudokuWriter.write(tmp.getN() + "\n");
                SudokuWriter.write(tmp.getDifficulty() + "\n");
                SudokuWriter.write(tmp.getStatus() + " " + "\n");
                for(int row = 0; row < tmp.getN(); row++){
                    String tmpVal = "";
                    for(int col = 0; col < tmp.getN(); col++){
                        tmpVal += tmp.getCells()[row][col].getValue() + " ";
                    }
                    SudokuWriter.write(tmpVal + "\n");
                }
                for(int row = 0; row < tmp.getN(); row++){
                    String tmpVal = "";
                    for(int col = 0; col < tmp.getN(); col++){
                        tmpVal += (tmp.getCells()[row][col].getIsEditable() ? "T" : "F") + " ";
                    }
                    SudokuWriter.write(tmpVal + "\n");
                }
            }
            SudokuWriter.close();
        } catch (IOException e) {}
    }

    static int getPositionFromId(ArrayList<SudokuGrid> SudokuDS, int id){
        for(int i = 0; i < SudokuDS.size(); i++){
            if(id == SudokuDS.get(i).getId()) return i;
        }


        return -1;
    }

    static int nextId(ArrayList<SudokuGrid> SudokuDS){
        int lastId[] = {-1, 0}; // 0 postion 1 value
        for(int i = 0; i < SudokuDS.size(); i++){
            if(lastId[1] < SudokuDS.get(i).getId()){
                lastId[1] = SudokuDS.get(i).getId();
                lastId[0] = i;
            }
        }

        return lastId[1] + 1;
    }
    
}