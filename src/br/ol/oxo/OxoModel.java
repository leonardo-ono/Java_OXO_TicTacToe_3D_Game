package br.ol.oxo;

/**
 * TicTacToeAI class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class OxoModel {

    // 1 2 4 8 16 32 64 128 256
    public int b1;
    public int b2;
    
    private final int[] winCombinations = {7, 56, 73, 84, 146, 273, 292, 448};
    
    public int lastAIMove;
    
    public void start() {
        b1 = b2 = 0;
    }

    public void play(int x, int y) {
        b1 = b1 | (int) Math.pow(2, x + y * 3);
    }

    public int processNextAIMove() {
        lastAIMove = minimax(b1, b2, 0, 1);
        return lastAIMove;
    }
    
    public void commitAIMove() {
        b2 = b2 | lastAIMove;
    }

    private int minimax(int b1, int b2, int p, int j) {
        Integer v = null;
        int bestMove = 0;
        for (int i = 1; i <= 256; i *= 2) {
            if (((b1 | b2) & i) != i) {
                int b1c = (j == 0) ? (b1 | i) : b1;
                int b2c = (j == 1) ? (b2 | i) : b2;
                int pv = 0;
                pv = (j == 0 && checkWin(b1c)) ? -1 : pv;
                pv = (j == 1 && checkWin(b2c)) ? 1 : pv;
                pv = (pv != 0) ? pv : minimax(b1c, b2c, p + 1, (j + 1) % 2);
                if (v == null || (j == 0 && pv < v) || (j == 1 && pv > v) 
                        || (pv == v && (int) (Math.random()*2)==0)) {
                    v = pv;
                    bestMove = i;
                }
            }
        }
        return (p == 0) ? bestMove : ((v == null) ? 0 : v);
    }

    public boolean checkWin1() {
        return checkWin(b1);
    }

    public boolean checkWin2() {
        return checkWin(b2);
    }
    
    public boolean checkWin(int b) {
        boolean win = false;
        for (int winCombination : winCombinations) {
            win = win || ((b & winCombination) == winCombination);
        }
        return win;
    }
    
    public boolean isWinPiece(int x, int y) {
        boolean foundPosition = false;
        int winPositions = 0;
        for (int winCombination : winCombinations) {
            if ((b1 & winCombination) == winCombination) {
                winPositions = winCombination;
                foundPosition = true;
                break;
            }
        }
        if (!foundPosition) {
            for (int winCombination : winCombinations) {
                if ((b2 & winCombination) == winCombination) {
                    winPositions = winCombination;
                    foundPosition = true;
                    break;
                }
            }
        }
        if (!foundPosition) {
            return false;
        }
        
        int i = (int) (Math.pow(2, x + y * 3));
        return(winPositions & i) == i;
    }
    
    public boolean hasMoreMovements() {
        return (b1 | b2) != 511;
    }
    
    public boolean isDraw() {
        return !hasMoreMovements() && !checkWin1() && !checkWin2();
    }
    
    public String getValue(int x, int y) {
        int i = (int) (Math.pow(2, x + y * 3));
        if ((b1 & i) == i) {
            return "O";
        } else if ((b2 & i) == i) {
            return "X";
        } else {
            return " ";
        }
    }
    
}
