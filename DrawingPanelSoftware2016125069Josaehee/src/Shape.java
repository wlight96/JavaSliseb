import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.JPanel;

public abstract class Shape extends JPanel implements Serializable
{

    public void draw( Graphics g ) {
    }

    public void setColor(Color color) {
    }

    public void setThickness( int t ) {
    }

    public void setFill( boolean b ) {
    }

    public Color getColor() {
        return Color.BLACK;
    }

    public int getThickness() {
        return 0;
    }

    public boolean getFill() {
        return true;
    }

    public void lighting_edge( Graphics g ) {

    }

    public int getX1() {
        return 0;
    }

    public int getY1() {
        return 0;
    }

    public int getWidth() {
        return 0;
    }

    public int getHeight() {
        return 0;
    }

    public void setWidth(int W){

    }
    public void setHeight(int H){

    }

    public void shapeChange( int x1, int y1, int width, int height, Color color) {
    }

    public int getInfo() {
        return 0;
    }

    public double getDistance( int x, int y ) {
        return 0;
    }

    public void change_point( int x, int y ) {
    }

}
