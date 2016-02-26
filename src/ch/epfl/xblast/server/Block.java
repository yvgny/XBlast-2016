package ch.epfl.xblast.server;

/**
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public enum Block {
    FREE,
    INDESTRUCTIBLE_WALL,
    DESTRUCTIBLE_WALL,
    CRUMBLING_WALL;
    
    public boolean isFree(){
        if (this == Block.FREE) {
            return true;
        } else {
            return false;
        }
        
    }
    
    public boolean canHostPlayer() {
        if (this == Block.FREE) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean castsShadow() {
        if (this == Block.INDESTRUCTIBLE_WALL || this == Block.CRUMBLING_WALL || this == Block.INDESTRUCTIBLE_WALL) {
            return true;
        } else {
            return false;
        }
    }
    
}
