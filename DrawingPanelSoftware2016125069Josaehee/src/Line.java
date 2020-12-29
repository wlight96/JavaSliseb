import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class Line extends Shape
{
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private Color myColor;
    private int thickness;
    private int mytype;

    public Line( int x1, int y1, int x2, int y2, Color color, int thickness )
    {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.myColor = color;
        this.thickness = thickness;
        this.mytype = 1;
    }

    @Override
    public void setColor( Color color ) {
        myColor = color;
    }

    @Override
    public void setThickness( int thickness ) {
        this.thickness = thickness;
    }

    @Override
    public Color getColor() {
        return myColor;
    }

    @Override
    public int getThickness() {
        return thickness;
    }

    @Override
    public void lighting_edge( Graphics g )
    {
        g.drawLine( x1, y1 - 10, x2, y2 - 10 );
        g.drawLine( x1, y1 + 10, x2, y2 + 10 );
        g.drawLine( x1, y1 - 10, x1, y1 + 10 );
        g.drawLine( x2, y2 - 10, x2, y2 + 10 );
    }

    @Override
    public int getX1() {
        return x1;
    }

    @Override
    public int getY1() {
        return y1;
    }

    @Override
    public int getWidth() {
        return (x1-x2);
    }
    @Override
    public int getHeight() {
        return Math.abs(y1-y2);
    }

    @Override
    public void draw( Graphics g )
    {
        g.setColor( myColor );
        Graphics2D g2 = ( Graphics2D ) g;
        g2.setStroke( new BasicStroke( thickness*10 + 5 ) );
        g2.drawLine( x1, y1, x2, y2 );

    }

    @Override
    public void shapeChange( int x1, int y1, int x2, int y2, Color color )
    {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.myColor = color;
    }

    @Override
    public int getInfo() {
        return mytype;
    }

    @Override
    public boolean getFill() {
        return false;
    }

    @Override
    public double getDistance( int x, int y )
    {
        boolean isitin = false;
        double dis = 0;
        int tempx1 = ( x1 < x2 ) ? x1 : x2;
        int tempx2 = ( x1 > x2 ) ? x1 : x2;
        int tempy1 = ( y1 < y2 ) ? y1 : y2;
        int tempy2 = ( y1 > y2 ) ? y1 : y2;
        if( ( x >= tempx1 && x <= tempx2 ) && ( y >= tempy1 -20 && y <= tempy2 + 20 ) )
            if( ( y >=  ( y1  + ( y2 - y1 ) * ( x - x1 ) / ( x2 - x1 ) - 20 ) ) && ( y <= ( y1 + ( y2 -y1 ) * ( x - x1 ) / ( x2 - x1 ) + 20 ) ) )
                isitin = true;
        if( !isitin )
        {
            dis = Math.sqrt( Math.pow( x1 - x, 2 ) + Math.pow( y1 - y, 2 ) );
            if( dis > Math.sqrt( Math.pow( x1 - x, 2 ) + Math.pow( y2 - y, 2 ) ) )
                dis = Math.sqrt( Math.pow( x1 - x, 2 ) + Math.pow( y2 - y, 2 ) );
            if( dis > Math.sqrt( Math.pow( x2 - x, 2 ) + Math.pow( y1 - y, 2 ) ) )
                dis = Math.sqrt( Math.pow( x2 - x, 2 ) + Math.pow( y1 - y, 2 ) );
            if( dis > Math.sqrt( Math.pow( x2 - x, 2 ) + Math.pow( y2 - y, 2 ) ) )
                dis = Math.sqrt( Math.pow( x2 - x, 2 ) + Math.pow( y2 - y, 2 ) );
        }
        return dis;
    }

    @Override
    public void change_point( int x, int y )
    {
        this.x1 += x;
        this.x2 += x;
        this.y1 += y;
        this.y2 += y;
    }
}

