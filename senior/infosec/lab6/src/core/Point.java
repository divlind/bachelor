package core;

public class Point {
    private int x;
    private int y;


    Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Point p = ((Point) obj);
        return this.x == p.getX() && this.y == p.getY();
    }

    @Override
    public int hashCode() {

        return new Integer(x+y).hashCode();
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
