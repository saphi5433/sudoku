public class SudokuCell {
    private int value;
    private boolean isEditable;

    SudokuCell(int value, boolean isEditable){
        this.value = value;
        this.isEditable = isEditable;
    }

    /**
     * Imposta se la cella è editabile o no
     * 
     * @param isEditable 
     */
    void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    /**
     * Imposta il valore
     * 
     * @param value
     */
    void setValue(int value) {
        this.value = value;
    }

    /**
     * Ritorna il valore
     * 
     * @return value
     */
    int getValue() {
        return this.value;
    }
    
    /**
     * Ritorna se la cella è editabile
     * 
     * @return isEditable
     */
    boolean getIsEditable() {
        return this.isEditable;
    }
}
