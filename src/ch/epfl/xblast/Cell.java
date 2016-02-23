package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.Math;

public final class Cell {

    private int x;
    private int y;
    public static final int COLUMNS = 15;
    public static final int ROWS = 13;
    public static final int COUNT = COLUMNS * ROWS;
    public static List<Cell> ROW_MAJOR_ORDER = Collections.unmodifiableList(rowMajorOrder());
    public static List<Cell> SPIRAL_ORDER = Collections.unmodifiableList(spiralOrder());

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;

        normalize();
    }

    private static ArrayList<Cell> rowMajorOrder(){
        ArrayList<Cell> rowMajor = new ArrayList<Cell>();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                rowMajor.add(new Cell(j, i));
            }
        }

        return rowMajor;

    }

    private static ArrayList<Cell> spiralOrder(){
        ArrayList<Integer> ix = new ArrayList<Integer>();
        ArrayList<Integer> iy = new ArrayList<Integer>();
        ArrayList<Integer> i1;
        ArrayList<Integer> i2;
        ArrayList<Cell> spiral = new ArrayList<Cell>();
        Integer c2;

        boolean horizontal = true;

        for (int i = 0; i < COLUMNS; i++) {
            ix.add(i);
        }

        for (int i = 0; i < ROWS; i++) {
            iy.add(i);
        }

        while(ix.size() != 0 && iy.size() != 0){
            i1 = horizontal ? ix : iy;
            i2 = horizontal ? iy : ix;

            c2 = i2.get(0);
            i2.remove(0);

            for (Integer c1 : i1) {
                if (horizontal) {
                    spiral.add(new Cell(c1, c2));
                } else {
                    spiral.add(new Cell(c2, c1));
                }
            }

            Collections.reverse(i1);
            horizontal = !horizontal;
        }

        return spiral;
    }

    private void normalize() {
        x = Math.floorMod(x, COLUMNS);
        y = Math.floorMod(y, ROWS);

    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int rowMajorIndex() {
        return ROW_MAJOR_ORDER.indexOf(this);
    }

    public Cell neighbor (Direction dir) {
        Cell neighbor;
        switch (dir) {
        case N :
            neighbor = new Cell(x, y - 1);
            break;
        case S:
            neighbor = new Cell(x, y + 1);
            break;
        case E:
            neighbor = new Cell(x + 1, y);
            break;
        case W:
            neighbor = new Cell(x - 1, y);
            break;
        default :
            neighbor = new Cell(x, y);
        }

        return neighbor;
    }

    public boolean equals(Object that) {
        if (that.getClass() == Cell.class) {
            if (((Cell)that).x() == x && ((Cell)that).y() == y) {
                return true;

            } else {
                return false;

            }
        } else {
            return false;

        }
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }


}
