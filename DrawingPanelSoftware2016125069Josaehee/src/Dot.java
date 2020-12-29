
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.util.ArrayList;

public class Dot extends Shape
{

    public class Coordinate{
        public int x1, y1;
        public Coordinate( int x, int y ) {
            this.x1 = x;
            this.y1 = y;
        }
        public int x() {
            return x1;
        }
        public int y() {
            return y1;
        }
    }

    private ArrayList<Coordinate> co = new ArrayList<Coordinate>();
    private Color myColor;
    private int thickness;
    private int mytype;

    public Dot( int x1, int y1, Color color, int thickness )
    {
        co.add( new Coordinate( x1, y1 ) );
        this.myColor = color;
        this.thickness = thickness;
        this.mytype = 4;
    }

    @Override
    public void setColor( Color color ) {
        myColor = color;
    }

    @Override
    public void draw( Graphics g )
    {
        g.setColor( myColor );
        Graphics2D g2 = ( Graphics2D ) g;
        for( Coordinate dot : co )
            g2.fillOval( dot.x(), dot.y(), thickness*10 + 5 , thickness*10 + 5 );
    }

    @Override
    public void shapeChange( int x1, int y1, int x2, int y2, Color color ) {
        co.add( new Coordinate( x2, y2 ) );
    }

    @Override
    public void change_point( int x, int y ) {
    }

    @Override
    public int getInfo() {
        return mytype;
    }

    @Override
    public Color getColor() {
        return myColor;
    }

    @Override
    public double getDistance( int x, int y ) {
        return 50;
    }

}

