package com.learn.ch01_basics;

/**
 * 练习 4：井字棋判胜 —— 把你在 React 里写过的 calculateWinner 用 Java 再写一遍。
 *
 * <p>棋盘是 3x3 的 char 二维数组，每格是 'X'、'O' 或 ' '（空格表示没下）。
 */
public class Ex04_TicTacToe {

    /** 空格子 */
    public static final char EMPTY = ' ';

    /**
     * 4.1 判断胜者：返回 'X' 或 'O'；没有人赢返回 EMPTY。
     * <pre>
     * X X X
     * O O
     *          -> 'X'
     * </pre>
     * 提示：检查 3 行、3 列、2 条对角线。
     */
    public static char winner(char[][] board) {
        // @answer
        for (int i = 0; i < 3; i++) {
            // 第 i 行
            if (board[i][0] != EMPTY && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0];
            }
            // 第 i 列
            if (board[0][i] != EMPTY && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return board[0][i];
            }
        }
        if (board[1][1] != EMPTY) {
            if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
                return board[1][1];
            }
            if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
                return board[1][1];
            }
        }
        return EMPTY;
        // @end
    }

    /**
     * 4.2 棋盘是否下满了。
     */
    public static boolean isFull(char[][] board) {
        // @answer
        for (char[] row : board) {
            for (char c : row) {
                if (c == EMPTY) {
                    return false;
                }
            }
        }
        return true;
        // @end
    }

    /**
     * 4.3 把棋盘转成字符串，每行用 '|' 分隔格子、行与行之间用 '\n' 分隔，最后一行后面没有换行。
     * <pre>
     * X|O|
     * |X|
     * O| |X      （注意空格子也要原样输出空格）
     * </pre>
     * 例如 {{'X','O',' '},{' ','X',' '},{'O',' ','X'}} -> "X|O| \n |X| \nO| |X"
     */
    public static String render(char[][] board) {
        // @answer
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (j > 0) {
                    sb.append('|');
                }
                sb.append(board[i][j]);
            }
            if (i < board.length - 1) {
                sb.append('\n');
            }
        }
        return sb.toString();
        // @end
    }
}
