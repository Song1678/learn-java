package com.learn.ch01_basics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ch01 练习4：井字棋")
class Ex04_TicTacToeTest {

    /** 用 "XO X  O X" 这种 9 个字符的字符串快速构造棋盘 */
    private static char[][] board(String s) {
        char[][] b = new char[3][3];
        for (int i = 0; i < 9; i++) {
            b[i / 3][i % 3] = s.charAt(i);
        }
        return b;
    }

    @Test
    @DisplayName("4.1 判断胜者")
    void winner() {
        assertEquals('X', Ex04_TicTacToe.winner(board("XXXOO    ")));
        assertEquals('O', Ex04_TicTacToe.winner(board("XOXXO  O ")));
        assertEquals('X', Ex04_TicTacToe.winner(board("XO OX O X")));
        assertEquals('O', Ex04_TicTacToe.winner(board("XXO O OXX")));
        assertEquals(' ', Ex04_TicTacToe.winner(board("XOXXOOOXX")));
        assertEquals(' ', Ex04_TicTacToe.winner(board("         ")));
    }

    @Test
    @DisplayName("4.2 棋盘是否下满")
    void isFull() {
        assertTrue(Ex04_TicTacToe.isFull(board("XOXXOOOXX")));
        assertFalse(Ex04_TicTacToe.isFull(board("XOXXOOOX ")));
    }

    @Test
    @DisplayName("4.3 渲染棋盘")
    void render() {
        assertEquals("X|O| \n |X| \nO| |X", Ex04_TicTacToe.render(board("XO  X O X")));
    }
}
