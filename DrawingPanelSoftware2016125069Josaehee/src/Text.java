import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.*;
import javax.swing.JPanel;

public class Text extends Shape{

    private int x1;
    private int y1;
    private Color myColor;
    private int thickness;
    private int mytype;
    private String mytext;

    public Text( int x1, int y1, int x2 ,int y2, Color color, int thickness, String text )
    {
        this.x1 = x1;
        this.y1 = y1;
        this.myColor = color;
        this.mytype = 5;
        this.mytext = text;
        this.thickness = thickness;
    }

    @Override
    public void setColor( Color color ) {
        myColor = color;
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
        return false;
    }

    @Override
    public int getInfo() {
        return 5;
    }

    @Override
    public void setThickness( int t ) {
        this.thickness = t;
    }

    @Override
    public void draw( Graphics g ) {
        g.setColor( myColor );
        Graphics2D g2 = ( Graphics2D ) g;
        g2.setColor( myColor);
        g2.setFont( new Font( "SansSerif", Font.PLAIN, thickness*10 + 20 ) );
        g2.drawString( mytext, x1, y1 );
    }

    @Override
    public double getDistance( int x, int y ) {
        boolean isitin = false;
        double dis = 50;
        int x2, y2, x_center, y_center;
        x2 = x1 + mytext.length() * ( thickness*10 + 20 )/2;
        y2 = y1 - ( thickness*10 + 20 )/2;
        x_center = ( x1 + x2 )/2;
        y_center = ( y1 + y2 )/2;

        if( x >= x1 && x <= x2 && y <= y1 && y >= y2 ) {
            isitin = true;
            dis = 0;
        }

        if( !isitin )
            dis = Math.sqrt( Math.pow( x_center - x, 2 ) + Math.pow( y_center - y, 2 ) );

        return dis;
    }

    @Override
    public void change_point( int x, int y )
    {
        x1 += x;
        y1 += y;
    }
}
