import javax.swing.JFrame;

public class PaintTest
{
    public static void main( String[] args )
    {
        PaintingGUI paintingGUI = new PaintingGUI();
        paintingGUI.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        paintingGUI.setSize( 1500, 1000 );
        paintingGUI.setVisible( true );
    }
}
