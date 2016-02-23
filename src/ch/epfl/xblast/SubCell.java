package ch.epfl.xblast;

public final class SubCell {

    private int x;
    private int y;
    private static final int GRANULARITY = 16;
    private static final int COLUMNS = GRANULARITY * Cell.COLUMNS;
    private static final int ROWS = GRANULARITY * Cell.ROWS;


    public SubCell(int x, int y) {
        this.x = x;
        this.y = y;

        normalize();
    }

    public static SubCell centralSubCellOf (Cell cell) {
        int xSubCell = (cell.x() * GRANULARITY) + (GRANULARITY / 2);
        int ySubCell = (cell.y() * GRANULARITY) + (GRANULARITY / 2);
        
        return new SubCell(xSubCell, ySubCell);

    }

    public int distanceToCentral() {
        SubCell centralSubcell = centralSubCellOf(containingCell());
        int xDistance = Math.abs(centralSubcell.x() - x);
        int yDistance = Math.abs(centralSubcell.y() - y);
        
        return xDistance + yDistance;
    }

    public boolean isCentral() {
        SubCell centralSubCell = centralSubCellOf(containingCell());

        if (centralSubCell.equals(this)) {
            return true;
        } else {
            return false;
        }
    }

    public SubCell neighbor(Direction d) {
        SubCell neighbor;
        switch (d) {
        case N :
            neighbor = new SubCell(x, y - 1);
            break;
        case S:
            neighbor = new SubCell(x, y + 1);
            break;
        case E:
            neighbor = new SubCell(x + 1, y);
            break;
        case W:
            neighbor = new SubCell(x - 1, y);
            break;
        default :
            neighbor = new SubCell(x, y);
        }

        return neighbor;
    }

    public Cell containingCell() {
        int xCell = x / GRANULARITY;
        int yCell = y / GRANULARITY;

        return new Cell(xCell, yCell);
    }

    public boolean equals (Object that) {
        if (that.getClass() == SubCell.class) {
            if (((SubCell)that).x == x && ((SubCell)that).y == y) {
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


}
