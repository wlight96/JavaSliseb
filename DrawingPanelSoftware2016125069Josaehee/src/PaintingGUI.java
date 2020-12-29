import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PaintingGUI extends JFrame{

    private JPanel mousePanel;
    private JFrame mouseFrame;
    private JLabel statusBar;
    private JList colorJList; // list to display colors

    private JRadioButtonMenuItem colorItems[]; // color 상태  items
    private JRadioButtonMenuItem shapeStates[]; // shape 상태 menu
    private JRadioButtonMenuItem thickState[]; // 두께 상태 menu내 표기
    private JRadioButtonMenuItem stuffItems[];  // 편집 상태 menu내 표기
    private JRadioButtonMenuItem fillStates[];  // 채움 상태 menu내 표기

    private ButtonGroup colorButtonGroup;
    private ButtonGroup shapeButtonGroup;
    private ButtonGroup thickButtonGroup;
    private ButtonGroup stuffButtonGroup;
    private ButtonGroup fillButtonGroup;

    private final String colorNames[] = { "Red", "Orange", "Yellow",
            "Green", "Cyan", "Blue", "Black", "Dark Gray",
            "Gray", "Light Gray", "Magenta", "Pink", "White" };

    private final Color colorValues[] = { Color.RED, Color.ORANGE, Color.YELLOW,
            Color.GREEN, Color.CYAN, Color.BLUE, Color.BLACK, Color.DARK_GRAY,
            Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.PINK, Color.WHITE };

    private int x1, y1, x2, y2, choice, thickness;
    private Color mycolor;

    private final String fillIcons[] = { "notfill.png", "fill.png" };
    private final String stuffIcons[] = {"\uD83D\uDD8C", " ↗ ", " r→R ", "\uD83D\uDDD1", "eraser" };
    private final String shapeIcons[] = { "○","ㅡ", "□", "△", "\uD83D\uDD8C", "T" };
    private final String thicknessIcons[] = { "small.png", "medium.png", "large.png" };

    private final String shapeName[] = { "Cirlce", "Line", "Rectangle", "Triangle", "Brush", "Text" };
    private final String thickName[] = { "thin", "medium", "thick" };
    private final String fillName[] = { "Not Filled", "Filled" };
    private final String stuffName[] = { "New", "Move", "Resize", "Delete", "Eraser" };

    // 선택 관련
    private int shapeCount, mode, shapeChoice;
    private boolean fillMode;
    private boolean tempFlag;
    private String text;


    // 저장공간을 arrayList로 선언 하여 동적 할당 하였다.
    private ArrayList<Shape> shapes = new ArrayList<Shape>();
    private ArrayList<Shape> temp = new ArrayList<Shape>();
    // 이미지
    //private ArrayList<Image> background = new ArrayList<Image>();

    public PaintingGUI()
    {
        super( "그림판" );
        tempFlag = false;
        shapeCount = shapeChoice = -1; // 아직 아무 도형도 그려지지 않은 상태임 맨 처음부터 사용하기 위해 -1 사용
        mycolor = Color.BLACK;
        mode = 0;

        //file menu
        JMenu fileMenu = new JMenu( "\uD83D\uDCC1 File" );
        fileMenu.setMnemonic( 'F' );
        //open in file menu
        JMenuItem openItem = new JMenuItem( "\uD83D\uDCC2 Open" );
        openItem.setMnemonic( 'O' );
        fileMenu.add( openItem );
        openItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed( ActionEvent event ) {
                        BufferedImage bi = null;
                        FileInputStream fis = null;
                        ObjectInputStream ois = null;
                        JFileChooser fileChooser = new JFileChooser();
                        int returnValue = fileChooser.showOpenDialog( null );
                        if( returnValue == JFileChooser.APPROVE_OPTION ) { //실제 파일 오픈
                            //선택된 파일의 경로 얻기
                            File selectedFile = fileChooser.getSelectedFile();
                            int isimage = 0;
                            try {
                                if( ImageIO.read( selectedFile ) != null )
                                    isimage = 1;
                            } catch ( IOException e1 ) {
                                e1.printStackTrace();
                            }
                            if( isimage == 1 ) {
                                try {
                                    bi = ImageIO.read( selectedFile );
                                    fis = new FileInputStream( selectedFile );
                                } catch ( FileNotFoundException e ) {
                                    e.printStackTrace();
                                    // 파일선택 잘못
                                }catch( IOException e ) {}
                                int w = getWidth(), h = getHeight();
                                if( bi.getWidth() > w )
                                    w = bi.getWidth();
                                if( bi.getHeight() > h )
                                    h = bi.getHeight();
                                setSize( w, h );
                                Graphics g = mousePanel.getGraphics();
                                g.drawImage( bi, 0, 0, null );
                                g.dispose();
                                stuffItems[1].setSelected( true );
                                mode = 1;
                            }
                            else {
                                try {
                                    fis = new FileInputStream( selectedFile );
                                    ois = new ObjectInputStream( fis );
                                } catch ( IOException e ) {
                                    e.printStackTrace();
                                }
                                try {
                                    Shape s = ( Shape )ois.readObject();
                                    shapeCount++;
                                    while( s != null ) {
                                        shapes.add(s);
                                        s = ( Shape )ois.readObject();
                                        shapeCount++;
                                    }
                                } catch ( ClassNotFoundException e ) {
                                } catch ( IOException e ) {}
                            }
                        }
                        else { //사용자 취소 동작이 있음
                        }
                        refresh();
                    }
                }
        );

        //save in file menu
        JMenuItem tempsaveItem = new JMenuItem( " Save");
        tempsaveItem.setMnemonic( 'S' );
        fileMenu.add( tempsaveItem );
        tempsaveItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed( ActionEvent event ) {
                        FileOutputStream fos = null;
                        ObjectOutputStream oos = null;
                        String fileName = JOptionPane.showInputDialog( null,
                                "저장할 파일의 이름을 지정하세요.", "저장",
                                JOptionPane.INFORMATION_MESSAGE );
                        if( fileName != null && fileName.length() != 0 ) {
                            try {
                                fos = new FileOutputStream( fileName );
                                oos = new ObjectOutputStream( fos );
                                for( int count = 0; count < shapes.size() ; count++ ) {
                                    Shape s = shapes.get( count );
                                    oos.writeObject( s );
                                }
                                oos.close();
                            }catch( Exception e ) {
                                e.printStackTrace();
                            }finally {
                                if( fos != null )try { fos.close(); }catch( IOException e ){}
                                if( oos != null )try { oos.close(); }catch (IOException e ){}
                            }
                        }
                    }
                }
        );

        //save in file menu
        JMenuItem saveItem = new JMenuItem( "\uD83D\uDDBC Save Image");
        saveItem.setMnemonic( 'I' );
        fileMenu.add( saveItem );
        saveItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed( ActionEvent event ) {
                        String fileName = JOptionPane.showInputDialog( null,
                                "저장할 파일의 이름을 지정하세요.", "저장",
                                JOptionPane.INFORMATION_MESSAGE );
                        if( fileName != null && fileName.length() != 0 ) {
                            try {
                                BufferedImage bufferedImage = new BufferedImage( mousePanel.getWidth(), mousePanel.getHeight(), BufferedImage.TYPE_INT_RGB );
                                Graphics2D g = ( Graphics2D ) bufferedImage.getGraphics();
                                g.setBackground( Color.WHITE );
                                g.fillRect( 0, 0, mousePanel.getWidth(), mousePanel.getHeight() );

                                for( int count = 0; count < shapes.size() ; count++ )
                                    shapes.get( count ).draw(g);
                                String savelocation = "C:\\Users\\wligh\\Desktop\\" + fileName + ".jpg";
                                File outFile = new File( savelocation );
                                ImageIO.write( bufferedImage, "jpg", outFile );
                            }catch( Exception e ) {
                                e.printStackTrace();
                            }finally {
                            }
                        }
                    }
                }
        );

        //exit in file menu
        JMenuItem exitItem = new JMenuItem( "\uD83D\uDEAA Exit");
        exitItem.setMnemonic( 'x' );
        fileMenu.add( exitItem );
        exitItem.addActionListener(new ExitItemListener());

        //edit in mode menu
        JMenu editMenu = new JMenu( "Edit" );
        editMenu.setMnemonic( 'E' );
        stuffItems = new JRadioButtonMenuItem[ stuffName.length ];
        stuffButtonGroup = new ButtonGroup();
        for ( int count = 0; count < stuffName.length; count++ ) {
            stuffItems[ count ] =
                    new JRadioButtonMenuItem( stuffName[ count ] );
            editMenu.add( stuffItems[ count ] );
            stuffButtonGroup.add( stuffItems[ count ] );
            stuffItems[ count ].addActionListener( new StuffItemListener() );
        }

        //shape menu in set menu
        JMenu shapeMenu = new JMenu( "Shape" );
        shapeMenu.setMnemonic( 'S' );
        shapeStates = new JRadioButtonMenuItem[ shapeName.length ];
        shapeButtonGroup = new ButtonGroup();
        for ( int count = 0; count < shapeName.length; count++ ) {
            shapeStates[ count ] =
                    new JRadioButtonMenuItem( shapeName[ count ] );
            shapeMenu.add( shapeStates[ count ] );
            shapeButtonGroup.add( shapeStates[ count ] );
            shapeStates[ count ].addActionListener( new ShapeStateListener());
        }

        //color menu in set menu
        JMenu colorMenu = new JMenu( "Color" );
        colorMenu.setMnemonic( 'C' );
        colorItems = new JRadioButtonMenuItem[ colorValues.length ];
        colorButtonGroup = new ButtonGroup();
        for ( int count = 0; count < colorNames.length; count++ ) {
            colorItems[ count ] = new JRadioButtonMenuItem( colorNames[ count ] );
            colorMenu.add( colorItems[ count ] );
            colorButtonGroup.add( colorItems[ count ] );
            colorItems[ count ].addActionListener( new ColorItemListener() );
        }// 색선택 버튼 추가
        JButton moreColor = new JButton( new ImageIcon( "color.png" ) );
        moreColor.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked( MouseEvent e ) {
                JButton btnMoreColor = ( JButton ) e.getSource();
                Color in = btnMoreColor.getBackground();
                mycolor = JColorChooser.showDialog( btnMoreColor, "Choose more color", in );
                if( shapeChoice > -1 ) {
                    shapes.get( shapeChoice ).setColor( mycolor );
                    refresh();
                }
            }
        });
        colorMenu.add( moreColor );

        //thickness menu in set menu
        JMenu thickMenu = new JMenu( "Thickness" );
        thickMenu.setMnemonic( 'T' );
        thickState = new JRadioButtonMenuItem[ thickName.length ];
        thickButtonGroup = new ButtonGroup();
        for ( int count = 0; count < thickName.length; count++ ) {
            thickState[ count ] =
                    new JRadioButtonMenuItem( thickName[ count ] );
            thickMenu.add( thickState[ count ] );
            thickButtonGroup.add( thickState[ count ] );
            thickState[ count ].addActionListener(new ThickItemListener());
        }

        //fill menu in set menu
        JMenu fillMenu = new JMenu( "Fill" );
        fillMenu.setMnemonic( 'F' );
        fillStates = new JRadioButtonMenuItem[ fillName.length ];
        fillButtonGroup = new ButtonGroup();
        for ( int count = 0; count < fillName.length; count++ ) {
            fillStates[ count ] = new JRadioButtonMenuItem( fillName[ count ] );
            fillMenu.add( fillStates[ count ] );
            fillButtonGroup.add( fillStates[ count ] );
            fillStates[ count ].addActionListener( new FillItemListener() );
        }

        JMenu setMenu = new JMenu( "Set" );
        setMenu.setMnemonic( 'S' );
        setMenu.add( shapeMenu );
        setMenu.add( colorMenu );
        setMenu.add( thickMenu );
        setMenu.add( fillMenu );

        //mode menu
        JMenu exeMenu = new JMenu( "Execution" );
        exeMenu.setMnemonic( 'E' );
        JMenuItem copyItem = new JMenuItem( "COPY ✂" );
        copyItem.setMnemonic( 'C' );
        exeMenu.add( copyItem );
        copyItem.addActionListener( new CopyItemListener());

        JMenuItem redoItem = new JMenuItem( "Paste \uD83D\uDCCB");
        redoItem.setMnemonic( 'V' );
        exeMenu.add( redoItem );
        redoItem.addActionListener(new PasteItemListener());

        //clear in file menu
        JMenuItem clearItem = new JMenuItem( "Clear \uD83D\uDDD1" );
        clearItem.setMnemonic( 'C' );
        exeMenu.add( clearItem );
        clearItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed( ActionEvent event ) {
                        int check = JOptionPane.showConfirmDialog( null, "Erase All", "Caution",
                                2, JOptionPane.WARNING_MESSAGE );
                        if( check == 0 ) {
                            shapeCount = shapeChoice = -1;
                            temp.clear();
                            tempFlag = false;
                            shapes.clear();
                            // 이미지 로드
                            //background.clear();
                            mousePanel.paint( mousePanel.getGraphics() );
                        }
                    }
                });


        // 툴바 선언 및 툴바에 버튼 추가
        JToolBar toolBar = new JToolBar();
        add( toolBar, "North" );

        for( int count = 0; count < stuffIcons.length; count ++ ) {
            int choose = count;
            JButton btnStuff = new JButton( stuffIcons[count] );
            btnStuff.setFont(new Font("맑은고딕",Font.BOLD,20));
            if( count == 0 )
                btnStuff.setToolTipText( "NEW" );
            if( count == 1 )
                btnStuff.setToolTipText( "MOVE" );
            if( count == 2 )
                btnStuff.setToolTipText( "RESIZE" );
            if( count == 3 )
                btnStuff.setToolTipText( "DELETE" );
            if( count == 4 )
                btnStuff.setToolTipText( "ERASER" );

            btnStuff.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent arg0 ) {
                    stuffItems[choose].setSelected( true );
                    mode = choose;
                }
            });
            toolBar.add( btnStuff );
        }

        JSeparator separator = new JSeparator();
        separator.setOrientation(1);
        toolBar.add( separator );

        for( int i = 0; i < shapeIcons.length; i ++ ) {
            int pick = i;
            JButton btnshape = new JButton(shapeIcons[i]);
            btnshape.setFont(new Font("맑은고딕",Font.BOLD,30));
            if( i == 0 )
                btnshape.setToolTipText( "CIRCLE" );
            if( i == 1 )
                btnshape.setToolTipText( "LINE" );
            if( i == 2 )
                btnshape.setToolTipText( "RECTANGLE" );
            if( i == 3 )
                btnshape.setToolTipText( "TRIANGLE" );
            if( i == 4 )
                btnshape.setToolTipText( "BRUSH" );
            if( i == 5 )
                btnshape.setToolTipText( "TEXT" );

            btnshape.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent arg0 ) {
                    stuffItems[0].setSelected( true );
                    mode = 0;
                    // 도형 선택
                    choice =  pick;
                    shapeStates[pick].setSelected( true );
                    if( choice == 5 )
                        text = JOptionPane.showInputDialog( null," 글자를 입력하시오", "확인",JOptionPane.INFORMATION_MESSAGE );
                }
            });
            toolBar.add( btnshape );
        }

        separator = new JSeparator();
        separator.setOrientation(1);
        toolBar.add( separator );
        for(int i = 0; i < thicknessIcons.length; i ++ ) {
            int pick = i;
            JButton btnThickness = new JButton(new ImageIcon(thicknessIcons[i]));
            if( i == 0 )
                btnThickness.setToolTipText( "THIN BORDERS / SMALL LETTERS" );
            if( i == 1 )
                btnThickness.setToolTipText( "MEDIUM BORDERS / MEDIUM LETTERS" );
            if( i == 2 )
                btnThickness.setToolTipText( "THICK BORDERS / BIG LETTERS" );
            btnThickness.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent arg0 ) {
                    thickness = pick;
                    thickState[pick].setSelected( true );
                    if( shapeChoice > -1 ) {
                        shapes.get( shapeChoice ).setThickness( thickness );
                        refresh();
                    }
                }
            });
            toolBar.add( btnThickness );
        }

        separator = new JSeparator();
        separator.setOrientation(1);
        toolBar.add(separator);
        for( int i = 0; i < fillIcons.length; i ++ ) {
            int pick = i;
            JButton btnFill = new JButton( new ImageIcon( fillIcons[i] ) );
            if( i == 0 )
                btnFill.setToolTipText( "NOT FILLED" );
            if( i == 1 )
                btnFill.setToolTipText( "FILLED" );
            btnFill.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent arg0 ) {
                    fillStates[pick].setSelected( true );
                    fillMode = ( pick == 1 );
                    if( shapeChoice > -1 ) {
                        shapes.get( shapeChoice ).setFill( fillMode );
                        refresh();
                    }
                }
            });
            toolBar.add( btnFill );
        }

        separator = new JSeparator();
        separator.setOrientation(1);
        toolBar.add( separator );
        JButton btnundo = new JButton( "COPY ✂" );
        btnundo.setToolTipText( "copy" );
        btnundo.setFont(new Font("맑은고딕",Font.BOLD,30));
        toolBar.add( btnundo );
        btnundo.addActionListener(new CopyItemListener());

        JButton btnredo = new JButton( "Paste \uD83D\uDCCB" );
        btnredo.setFont(new Font("맑은고딕",Font.BOLD,30));
        btnredo.setToolTipText( "paste" );
        toolBar.add( btnredo );
        btnredo.addActionListener(new PasteItemListener());

        JButton btnclear = new JButton( "🗑" );
        btnclear.setToolTipText( "CLEAR" );
        btnclear.setFont(new Font("맑은고딕",Font.BOLD,30));
        toolBar.add( btnclear );
        btnclear.addActionListener(
                new ActionListener() {
                    public void actionPerformed( ActionEvent event ) {
                        int check = JOptionPane.showConfirmDialog( null, "Erase All", "Caution",
                                2, JOptionPane.WARNING_MESSAGE );
                        if( check == 0 ) {
                            shapeCount = shapeChoice = -1;
                            tempFlag = false;
                            temp.clear();
                            shapes.clear();
                            // 이미지 로드
                            //background.clear();
                            mousePanel.paint( mousePanel.getGraphics() );
                        }
                    }
                });

        colorItems[0].setSelected( true );
        stuffItems[0].setSelected( true );
        shapeStates[0].setSelected( true );
        thickState[0].setSelected( true );
        fillStates[0].setSelected( true );

        //create menu bar
        JMenuBar bar = new JMenuBar();
        setJMenuBar( bar );// add menu bar to application
        bar.add( fileMenu );
        bar.add( editMenu );
        bar.add( setMenu );
        bar.add( exeMenu );
        //setLayout( new FlowLayout() ); // set frame layout

        //create mousepanel
        mousePanel = new JPanel();
        mousePanel.setBackground( Color.WHITE );
        add( mousePanel, BorderLayout.CENTER );
        MouseHandler handler = new MouseHandler();
        mousePanel.addMouseListener( handler );
        mousePanel.addMouseMotionListener( handler );

        // 색선택
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
        Vector<ImageIcon> icons = new Vector<ImageIcon>();
        for ( int i = 0; i < colorValues.length; i++ ) {
            BufferedImage bi = getImage( 40, colorValues[i] );
            images.add( bi );
            ImageIcon imi = new ImageIcon( bi );
            icons.add( imi );
        }

        colorJList = new JList( icons ); // create with colorNames
        colorJList.setVisibleRowCount( 5 ); // display five rows at once
        colorJList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );// do not allow multiple selections
        add( colorJList, BorderLayout.WEST );
        colorJList.addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged( ListSelectionEvent event ) {
                        mycolor =  colorValues[ colorJList.getSelectedIndex()];
                        colorItems[ colorJList.getSelectedIndex() ].setSelected( true );
                        if( mode != 0 && shapeChoice != -1 ) {
                            shapes.get( shapeChoice ).setColor( mycolor );
                            refresh();
                        }
                    } // end method valueChanged
                });// end anonymous inner class
        colorJList.setSelectionBackground( Color.BLACK );
        statusBar = new JLabel();
        add( statusBar, BorderLayout.SOUTH );
    }

    public static BufferedImage getImage( int size ,Color color ) {
        BufferedImage i = new BufferedImage( size, size, BufferedImage.TYPE_INT_RGB );
        Graphics2D g = i.createGraphics();
        g.setColor( color );
        g.fillRect( 0, 0, 40, 40 );
        g.dispose();
        return i;
    }

    private class MouseHandler implements MouseListener, MouseMotionListener
    {
        public void mousePressed( MouseEvent event )
        {
            x1 = event.getX();
            y1 = event.getY();
            if( mode == 0 ) {
                setCursor( Cursor.getPredefinedCursor( Cursor.CROSSHAIR_CURSOR ) );
                if( choice == 0 )shapes.add( new Circle( x1, y1, x1, y1, mycolor, thickness, fillMode ) );
                if( choice == 1 )shapes.add( new Line( x1, y1, x1, y1, mycolor, thickness) );
                if( choice == 2 )shapes.add( new Rectangle( x1, y1, x1, y1, mycolor, thickness, fillMode ) );
                if( choice == 3 )shapes.add( new Triangle( x1, y1, x1, y1, mycolor, thickness, fillMode ) );
                if( choice == 4 )shapes.add( new Dot( x1, y1, mycolor, thickness ) );
                if( choice == 5 ) {
                    setCursor( Cursor.getPredefinedCursor( Cursor.TEXT_CURSOR ) );
                    shapes.add( new Text( x1, y1, x1, y1, mycolor, thickness, text ) );
                }
                shapes.get( ++shapeCount ).draw( mousePanel.getGraphics() );
            }
            if( mode == 1 || mode == 2 ) {
                shapeChoice = find_min_dis( x1, y1 );
                if( shapeChoice > -1 && shapeCount != -1 ) {
                    thickness = shapes.get( shapeChoice ).getThickness();
                    thickState[ thickness ].setSelected( true );
                    choice = shapes.get( shapeChoice ).getInfo();
                    shapeStates[ choice ].setSelected( true );
                    mycolor = shapes.get( shapeChoice ).getColor();
                    for( int i = 0; i < colorValues.length; i++ ) {
                        if( mycolor == colorValues[i] ) {
                            colorItems[i].setSelected( true );
                            colorJList.setSelectedIndex(i);
                            break;
                        }
                    }
                    fillMode = shapes.get( shapeChoice ).getFill();
                    if( fillMode )
                        fillStates[1].setSelected( true );
                    else
                        fillStates[0].setSelected( true );
                }
            }
            if( mode == 4 ) {
                shapes.add( new Dot( x1, y1, Color.WHITE, ( thickness+1 ) * 2 ) );
                shapeCount++;
            }
            refresh();
        }
        public void mouseDragged( MouseEvent event ) {
            x2 = event.getX();
            y2 = event.getY();
            statusBar.setText( String.format( "X: %d   Y: %d    도형 갯수: %d    선택 도형: %s    선 굵기: %s",
                    x2, y2, shapes.size(), shapeName[choice], (fillMode) ? "filled" : thickName[thickness] ) );
            if( mode == 0 )
                shapes.get( shapeCount ).shapeChange( x1, y1, x2, y2, mycolor );
            if( mode == 1 ){
                if( shapeCount != -1 && shapeChoice > -1 ) {
                    setCursor( Cursor.getPredefinedCursor( Cursor.MOVE_CURSOR ) );
                    shapes.get( shapeChoice ).change_point( x2 - x1, y2 - y1 );
                }
                x1 = x2;
                y1 = y2;
            }
            if( mode == 2 ){
                if( shapeChoice > -1 ) {
                    if( choice == 1 )
                        shapes.get( shapeChoice ).shapeChange( shapes.get( shapeChoice ).getX1(), shapes.get( shapeChoice ).getY1(), x2, y2, mycolor );
                    else {
                        if( shapes.get( shapeChoice ).getX1() <= x2 && shapes.get( shapeChoice ).getY1() <= y2 )
                            shapes.get( shapeChoice ).shapeChange( shapes.get( shapeChoice ).getX1(), shapes.get( shapeChoice ).getY1(), x2, y2, mycolor );
                    }
                }
                setCursor( Cursor.getPredefinedCursor( Cursor.SE_RESIZE_CURSOR ) );
            }
            if( mode == 3 ) {
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                if( shapeChoice > -1 && shapeCount > -1 ) {
                    //temp.add( shapes.get( shapeChoice ) );
                    //tempCount++;
                    shapes.remove( shapeChoice );
                    shapeCount--;
                    shapeChoice = -1;
                }
            }
            if( mode == 4 ) {
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                setCursor( Toolkit.getDefaultToolkit().createCustomCursor(
                        new ImageIcon( "eraser.png" ).getImage(), new Point( 0, 0 ), "custom cursor" ) );
                shapes.get( shapeCount ).shapeChange( x1, y1, x2, y2, Color.WHITE );
                x1 = x2;
                y1 = y2;
            }
            refresh();
        } // end method mouseDragged
        public void mouseMoved( MouseEvent event ){
            statusBar.setText( String.format( "X: %d   Y: %d    도형 : %d 개    선택 도형: %s    선 두깨: %s",
                    event.getX(), event.getY(), shapes.size(), shapeName[choice], (fillMode) ? "filled" : thickName[thickness] ) );
        }
        public void mouseReleased( MouseEvent event ) {
            setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
            if( mode != 0 && shapeChoice > -1 ) {
                shapes.get( shapeChoice ).lighting_edge( mousePanel.getGraphics() );
            }
        }
        //unused functions
        public void mouseClicked( MouseEvent event ) {}
        public void mouseEntered( MouseEvent event ){}
        public void mouseExited( MouseEvent event ) {}
    }

    private void refresh()
    {
        Graphics g = mousePanel.getGraphics();
        mousePanel.print(g);
        for( Shape shape : shapes )
            shape.draw(g);
        g.dispose();
    }

    private int find_min_dis( int x, int y ) {
        int index = 0;
        double distance = 50;

        for( int i = 0; i <= shapeCount; i++ ) {
            double temp = shapes.get(i).getDistance( x, y );
            if( distance >=temp ) {
                distance = temp;
                index = i;
            }
        }
        if( distance >= 30 )
            return -1;
        else
            return index;
    }// end class MouseTrackerFrame


    // ExitItem itemhandler
    class ExitItemListener implements ActionListener{
        @Override
        public void actionPerformed( ActionEvent event ) {
            int check = JOptionPane.showConfirmDialog( null, "Quit Program", "Exit",
                    2, JOptionPane.QUESTION_MESSAGE );
            if( check == 0 )
                System.exit( 0 );
        }
    }
    // stuffItem itemhandeler
    class StuffItemListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            for ( int count = 0; count < stuffItems.length; count++ ) {
                if ( stuffItems[ count ].isSelected() ) {
                    mode = count;
                    // new
                    if( count == 0 ) {
                        shapeChoice = -1;
                        refresh();
                    }
                    break;
                }
            }
        }
    }
    // shapeitemhandler
    class ShapeStateListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            for ( int count = 0; count < shapeStates.length; count++ ) {
                if ( shapeStates[ count ].isSelected() ) {
                    if( choice != count )
                        mode = 0;
                    choice = count;
                    break;
                }
            }
        }
    }
    // 색선택 버튼 눌렀을 시
    class ColorItemListener implements ActionListener{
        @Override
        public  void actionPerformed(ActionEvent event){
            for ( int count = 0; count < colorItems.length; count++ ) {
                if ( colorItems[ count ].isSelected() ) {
                    mycolor = colorValues[ count ];
                    if( mode != 0 && shapeChoice != -1 ) {
                        shapes.get( shapeChoice ).setColor( mycolor );
                        refresh();
                    }
                    break;
                }
            }
        }
    }
    //thick
    class ThickItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            for (int count = 0; count < thickState.length; count++) {
                if (thickState[count].isSelected()) {
                    thickness = count;
                    if (mode != 0 && shapeChoice != -1) {
                        shapes.get(shapeChoice).setThickness(thickness);
                        refresh();
                    }
                    break;
                }
            }
        }
    }
    class FillItemListener implements ActionListener{
        @Override
        public void actionPerformed( ActionEvent event ) {
            for ( int count = 0; count < fillStates.length; count++ ) {
                if ( fillStates[ count ].isSelected() ) {
                    fillMode = ( count == 0 ) ? false : true ;
                    if( mode != 0 && shapeChoice != -1 ) {
                        shapes.get( shapeChoice ).setFill( fillMode );
                        refresh();
                    }
                    break;
                }
            }
        }
    }
    class CopyItemListener implements ActionListener{
        @Override
        public void actionPerformed( ActionEvent event ) {
            if( shapeCount != -1 ) {
                temp.clear();
                if( shapes.get(shapeChoice).getInfo() == 0 ) {
                    temp.add(new Circle(x1, y1, 20, 20,
                            shapes.get(shapeChoice).getColor(), shapes.get(shapeChoice).getThickness(), shapes.get(shapeChoice).getFill()));
                    temp.get(0).setHeight(shapes.get(shapeChoice).getHeight());
                    temp.get(0).setWidth(shapes.get(shapeChoice).getWidth());
                }if( shapes.get(shapeChoice).getInfo() == 1 ) {
                    temp.add(new Line(x1, y1, shapes.get(shapeChoice).getWidth(), shapes.get(shapeChoice).getHeight(),
                            shapes.get(shapeChoice).getColor(), shapes.get(shapeChoice).getThickness()));
                }if( shapes.get(shapeChoice).getInfo() == 2 ){
                    temp.add( new Rectangle( x1, y1, shapes.get(shapeChoice).getWidth(), shapes.get(shapeChoice).getHeight(),
                            shapes.get(shapeChoice).getColor(), shapes.get(shapeChoice).getThickness(), shapes.get(shapeChoice).getFill() ) );
                    temp.get(0).setHeight(shapes.get(shapeChoice).getHeight());
                    temp.get(0).setWidth(shapes.get(shapeChoice).getWidth());
                }if(shapes.get(shapeChoice).getInfo()  == 3 )temp.add( new Triangle( x1, y1, x1, y1, mycolor, thickness, fillMode ) );
                tempFlag = true;
            }refresh();
            System.out.println("shapechoice wedth :" + temp.get(shapeChoice).getWidth());
        }

    }
     class PasteItemListener implements ActionListener{
        @Override
        public void actionPerformed( ActionEvent event ) {
            if( tempFlag == true ) {
                System.out.println("Bshapesize : "+ shapes.size());
                if( temp.get(0).getInfo() == 0 ) {
                    shapes.add(new Circle(x1, y1, 20, 20,
                            temp.get(0).getColor(), temp.get(0).getThickness(), temp.get(0).getFill()));
                    System.out.println("Ashapesize : "+ shapes.size());
                    shapes.get(shapes.size()-1).setHeight(temp.get(0).getHeight());
                    shapes.get(shapes.size()-1).setWidth(temp.get(0).getWidth());
                }if( temp.get(0).getInfo() == 1 ) {
                    shapes.add(new Line(x1, y1, temp.get(0).getWidth(), temp.get(0).getHeight(),
                            temp.get(0).getColor(), temp.get(0).getThickness()));
                }if( temp.get(0).getInfo() == 2 ){
                    shapes.add( new Rectangle( x1, y1, temp.get(0).getWidth(), temp.get(0).getHeight(),
                            temp.get(0).getColor(), temp.get(0).getThickness(), temp.get(0).getFill() ) );
                    shapes.get(shapes.size()-1).setHeight(temp.get(0).getHeight());
                    shapes.get(shapes.size()-1).setWidth(temp.get(0).getWidth());
                }if(temp.get(0).getInfo()  == 3 )shapes.add( new Triangle( x1, y1, x1, y1, mycolor, thickness, fillMode ) );
                System.out.println(shapeChoice);
                shapeCount++;
                System.out.println(shapes.get(shapeCount));
                System.out.println(shapes.get(shapeCount).getWidth());
                System.out.println("shapes size is : "+shapes.size());
            }
            refresh();
        }
    }
}