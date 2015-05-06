package com.skala.gameoflife.surfaceobject;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Skala
 */
public class GameBoard implements SurfaceObject {
    public final static int DEFAULT_ROW_CELL = 10;
    public final static int DEFAULT_COLUMN_CELL = 10;

    private ArrayList<Cell> mCellList;
    private int mRowCell;
    private int mColumnCell;

    public GameBoard(int sizeBoard, int offsetX, int offsetY) {
        this(sizeBoard, offsetX, offsetY, DEFAULT_ROW_CELL, DEFAULT_COLUMN_CELL);
    }

    public GameBoard(int sizeBoard, int offsetX, int offsetY, int numberRow, int numberColumn) {
        mRowCell = numberRow;
        mColumnCell = numberColumn;

        int sizeCell = sizeBoard / mRowCell;
        mCellList = createBoard(sizeCell, offsetX, offsetY);
    }

    @Override
    public void onDraw(Canvas canvas) {
        int size = mCellList.size();
        for (int i = 0; i < size; i++) {
            mCellList.get(i).onDraw(canvas);
        }
    }

    @Override
    public boolean isClicked(int x, int y) {
        int size = mCellList.size();
        for (int i = 0; i < size; i++) {
            Cell cell = mCellList.get(i);
            if (cell.isClicked(x, y)) {
                cell.toggleAlive();
                return true;
            }
        }
        return false;
    }

    public void game() {
        int count = mCellList.size();
        for (int i = 0; i < count; i++) {
            Cell cell = mCellList.get(i);
            int countAliveNeight = cell.getAliveNeight();
            boolean isCellAlive;
            if (cell.isAlive()) {
                isCellAlive = countAliveNeight == 2 || countAliveNeight == 3;
            } else {
                isCellAlive = countAliveNeight == 3;
            }
            cell.setIsAliveNext(isCellAlive);
        }

        for (int i = 0; i < count; i++) {
            mCellList.get(i).saveState();
        }
    }

    private ArrayList<Cell> createBoard(int sizeCell, int offsetX, int offsetY) {
        ArrayList<Cell> arrayList = new ArrayList<>();

        // distance between cells
        int spaceX, spaceY = 1;

        Cell cell;
        for (int i = 0; i < mColumnCell; i++) {
            int top = i * sizeCell + offsetY + spaceY;
            int bottom = top + sizeCell;

            spaceX = 1;
            spaceY++;
            for (int j = 0; j < mRowCell; j++) {
                int left = j * sizeCell + offsetX + spaceX;
                int right = left + sizeCell;
                cell = new Cell(left, top, right, bottom);
                arrayList.add(cell);

                spaceX++;
            }
        }

        setNeighbour(arrayList);

        return arrayList;
    }

    private void setNeighbour(ArrayList<Cell> board) {
        Cell cell;

        int size = board.size();
        for (int i = 0; i < size; i++) {
            cell = board.get(i);

            int right = getRightCell(i);
            cell.setRightNeight(board.get(right));

            int left = getLeftCell(i);
            cell.setLeftNeigh(board.get(left));

            int top = getTopCell(i);
            cell.setTopNeigh(board.get(top));

            int bottom = getBottomCell(i);
            cell.setBottomNeight(board.get(bottom));

            int rt = getTopCell(right);
            cell.setRTNeigh(board.get(rt));

            int rb = getBottomCell(right);
            cell.setRBNeigh(board.get(rb));

            int lt = getTopCell(left);
            cell.setLTNeigh(board.get(lt));

            int lb = getBottomCell(left);
            cell.setLBNeigh(board.get(lb));

            //Log.d("GameView", "Aktualny: " + i + " left: " + left + " right: " + right + " top: " + top + " bottom: " + bottom + " lt: " + lt + " lb: " + lb + " rt: " + rt + " rb: " + rb);
        }
    }

    private int getRightCell(int number) {
        int integerDivision = number / mRowCell;
        int modulo = (number + 1) % mRowCell;
        return integerDivision * mRowCell + modulo;
    }

    private int getLeftCell(int number) {
        int integerDivision = number / mRowCell;
        int modulo = number % mRowCell;
        if (modulo == 0) {
            modulo = mRowCell;
        }
        modulo--;

        return integerDivision * mRowCell + modulo;
    }

    private int getTopCell(int number) {
        int top = (number - mRowCell);
        if (top < 0) {
            top += mColumnCell * mRowCell;
        }
        return top;
    }

    private int getBottomCell(int number) {
        int bottom = (number + mRowCell) % (mColumnCell * mRowCell);
        return bottom;
    }

    public void resetBoard() {
        int size = mCellList.size();
        for (int i = 0; i < size; i++) {
            mCellList.get(i).setIsAlive(false);
        }
    }

    public void randomBoard() {
        Random random = new Random();
        int size = mCellList.size();
        for (int i = 0; i < size; i++) {
            mCellList.get(i).setIsAlive(random.nextBoolean());
        }
    }
}
