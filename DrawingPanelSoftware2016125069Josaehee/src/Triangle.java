import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Triangle extends Shape{
    private Color myColor;
    private int thickness;
    private int[] xpoints = new int[3];
    private int[] ypoints = new int[3];
    private int mytype;
    private boolean isitfilled;

    public Triangle( int x1, int y1, int x2, int y2, Color color, int thickness, boolean isitfilled )
    {
        this.myColor = color;
        this.thickness = thickness;
        this.xpoints[2] = x1 + 20;
        this.xpoints[0] = ( 2*x1 + 20 )/2;
        this.ypoints[1] = y1 + 20;
        this.ypoints[2] = y1 + 20;
        this.ypoints[0] = y1;
        this.xpoints[1] = x1;
        this.mytype = 3;
        this.isitfilled = isitfilled;
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
    public void setFill( boolean b ) {
        this.isitfilled = b;
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
    public boolean getFill() {
        return isitfilled;
    }

    @Override
    public void lighting_edge( Graphics g )
    {
        int[]x = new int[3], y = new int[3];
        int width = Math.abs( xpoints[1] - xpoints[2] );
        int height = Math.abs( ypoints[0] - ypoints[1] );
        int y_mid = ( ypoints[0] + ypoints[2] )/2;
        for( int i = 0; i < 3; i++ ) {
            if( xpoints[i] > xpoints[0] )
                x[i] = xpoints[i] + (int)( width * 0.05 );
            else if( xpoints[i] == xpoints[0] )
                x[i] = xpoints[i];
            else
                x[i] = xpoints[i] - (int)( width * 0.05 );
            if( ypoints[i] > y_mid )
                y[i] = ypoints[i] + (int)( height * 0.05 );
            else
                y[i] = ypoints[i] - (int)( height * 0.05 );
        }
        g.drawPolygon( x, y, 3 );
    }

    @Override
    public int getX1() {
        return xpoints[1];
    }

    @Override
    public int getY1() {
        return ypoints[0];
    }

    @Override
    public void draw( Graphics g ) {
        g.setColor( myColor );
        Graphics2D g2 = ( Graphics2D ) g;
        g2.setStroke( new BasicStroke( thickness*10 + 5 ) );
        if( isitfilled )
            g2.fillPolygon( xpoints, ypoints, 3 );
        else
            g2.drawPolygon( xpoints, ypoints, 3 );
    }

    @Override
    public void shapeChange( int x1, int y1, int x2, int y2, Color color ) {
        this.xpoints[2] = x2;
        this.xpoints[0] = ( x1 + x2 )/2;
        this.ypoints[1] = y2;
        this.ypoints[2] = y2;
        this.ypoints[0] = y1;
        this.xpoints[1] = x1;
        this.myColor = color;
    }

    @Override
    public int getInfo() {
        return mytype;
    }

    @Override
    public double getDistance( int x, int y ) {
        boolean isitin = false, reverse = ypoints[0] > ypoints[1];
        double dis = 0;
        int x1 = ( xpoints[1] < xpoints[2] ) ? xpoints[1] : xpoints[2];
        int x2 = ( xpoints[1] > xpoints[2] ) ? xpoints[1] : xpoints[2];
        int y1 = ( ypoints[0] < ypoints[2] ) ? ypoints[0] : ypoints[2];
        int y2 = ( ypoints[0] > ypoints[2] ) ? ypoints[0] : ypoints[2];

        if( x >= x1 && x <= x2 && y >= y1 && y <= y2 ) {
            if( !reverse ) {
                if( ( y >= y1 - ( y2 - y1 ) * ( x - x1 ) / ( ( x2 - x1 )/2 ) ) && ( y >= y1 - ( y2 - y1 ) * ( x2 - x ) / ( ( x2 - x1 )/2 ) ) ){
                    isitin = true;
                }
            }
            else if(  ( y <= y1 + ( y2 - y1 ) * ( x - x1 ) / ( ( x2 - x1 )/2 ) ) && ( y <= y1 + ( y2 - y1 ) * ( x2 - x ) / ( ( x2 - x1 )/2 ) )  ){
                isitin = true;
            }
        }

        if( !isitin ) {
            dis = Math.sqrt( Math.pow( xpoints[0] - x, 2 ) + Math.pow( ypoints[0] - y, 2 ) );
            for( int i = 1; i < 3; i++ ) {
                double temp = Math.sqrt( Math.pow( xpoints[i] - x, 2 ) + Math.pow( ypoints[i] - y, 2 ) );
                if( dis > temp )
                    dis = temp;
            }
        }
        return dis;
    }

    @Override
    public void change_point( int x, int y )
    {
        for( int i = 0; i < 3; i++ ) {
            xpoints[i] += x;
            ypoints[i] += y;
        }
    }

}
