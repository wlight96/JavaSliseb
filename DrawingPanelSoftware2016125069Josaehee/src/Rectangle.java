import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class Rectangle extends Shape{
    private int x1;
    private int y1;
    private int width;
    private int height;
    private Color myColor;
    private int x2;
    private int y2;
    private int thickness;
    private int mytype;
    private boolean isitfilled;
    public Rectangle(int x1, int y1, int x2, int y2, Color color, int thickness, boolean isitfilled )
    {

        this.x1 = x1;
        this.y1 = y1;
        width = 20;
        this.x2 = x1 + 20;
        height = 20;
        this.y2 = y1 + 20;
        this.myColor = color;
        this.thickness = thickness;
        this.mytype = 2;
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
        int w = (int)( width * 0.05 );
        int h = (int)( height * 0.05 );
        g.drawRect( x1 - w, y1 - h, width + w*2, height + h*2 );
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
        return width;
    }
    @Override
    public int getHeight() {
        return height;
    }
    @Override
    public void setWidth(int w){
        this.width = w;
    }
    @Override
    public void setHeight(int h){
        this.height = h;
    }

    @Override
    public void draw( Graphics g )
    {
        if( x1 > x2 ) {
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }
        if( y1 > y2 ) {
            int temp = y1;
            y1 = y2;
            y2 = temp;
        }
        g.setColor( myColor );
        Graphics2D g2 = ( Graphics2D ) g;
        g2.setStroke( new BasicStroke( thickness*10 + 5 ) );
        if( isitfilled )
            g2.fillRect( x1, y1, width, height );
        else
            g2.drawRect( x1, y1, width, height );
    }

    @Override
    public void shapeChange( int x1, int y1, int x2, int y2, Color color )
    {
        this.x1 = x1;
        this.y1 = y1;
        width = Math.abs( x2 - x1 );
        this.x2 = x2;
        height = Math.abs( y2 - y1 );
        this.y2 = y2;
        this.myColor = color;
    }

    @Override
    public int getInfo() {
        return mytype;
    }

    @Override
    public double getDistance( int x, int y )
    {
        boolean isitin = false;
        double dis = 0;
        if( x >= x1 && x <= x2  && y >= y1 && y <= y2 )
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
    public void change_point( int x, int y ) {
        this.x1 += x;
        this.x2 += x;
        this.y1 += y;
        this.y2 += y;
    }

}
