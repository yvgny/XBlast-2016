package ch.epfl.xblast;

public enum Direction {
    N, E, S, W;
    
    public Direction opposite() {
        switch (this) {
        case N:
            return S;
            
        case E:
            return W;
            
        case S:
            return N;
            
        case W:
            return E;
        }
        
        return null;
    }
    
    public boolean isHorizontal() {
        if (this == E || this == W) {
            return true;
        }
       
        return false;
        
    }
    
    public boolean isParallelTo(Direction that) {
        if (this == that || this.opposite() == that) {
            return true;
        }
        
        return false;
    }

}
