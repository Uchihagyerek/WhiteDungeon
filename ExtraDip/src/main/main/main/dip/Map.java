package main.dip;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;
import java.util.Vector;


public class Map extends Canvas {
    JFrame frame=null;
    public int roomCount;
    public int maxRooms;
    public int tries;
    public int x;
    public int y;
    public int startX;
    public int startY;
    public int myX;
    public int myY;
    public int map[][];
    public int mapsize=15;
    public int monsters=0;
    public boolean boss=false;
    public boolean playerDead=false;
    public static Player player;
    public Random random = new Random();
    public boolean defeated=false;
    public Point[] deltas = {
            new Point(0, 1),
            new Point(0, -1),
            new Point(1, 0),
            new Point(-1, 0)
    };

    Map(){
        setSize(new Dimension (900, 900));
        tries=0;
        addKeyListener(new KeyAdapter () {
            @Override
            public void keyPressed(KeyEvent evt) {
                moveIt(evt);
            }

        });
        if(player==null)
        player=new Player("valami","valami", this);

    }

    public void moveIt(KeyEvent evt) {
try {
    switch (evt.getKeyCode()) {
        case KeyEvent.VK_DOWN:
            if (map[myY + 1][myX] > 0)
                myY += 1;
            break;
        case KeyEvent.VK_UP:
            if (map[myY - 1][myX] > 0)
                myY -= 1;
            break;
        case KeyEvent.VK_LEFT:
            if (map[myY][myX - 1] > 0)
                myX -= 1;
            break;
        case KeyEvent.VK_RIGHT:
            if (map[myY][myX + 1] > 0)
                myX += 1;
            break;
        case KeyEvent.VK_NUMPAD0:
            player.die();
            break;


    }
}
catch (ArrayIndexOutOfBoundsException ex){

}
        repaint ();

    }



    void generateMap(){
        if(playerDead=true) {
            player = new Player(player.name, player.playerClass, this);
            playerDead=false;
        }
        map=new int[mapsize][mapsize];
        clearMap ();
        fillMap();

    }
    private void clearMap(){
        for (int i = 0; i < mapsize; i++) {
            for (int j = 0; j < mapsize; j++) {
                map[i][j]=0;
            }

        }
    }
    private void fillMap(){
        getStart ();
        int maxTreasure=0;
        map[startX][startY]=1;
        roomCount=0;
        maxRooms=20;
        x=startX;
        y=startY;
        map[x][y]=1;
        while(roomCount<maxRooms-1){
            rollRoom(x, y);
            if (map[x][y]==0){
                roomCount++;
                if (maxTreasure<5)
                map[x][y]=random.nextInt(3)+1; //2: monster, 3:treasure
                else
                    map[x][y]=random.nextInt(2)+1;
                if (map[x][y]==3){
                    maxTreasure++;
                }
                if (map[x][y]==2){
                    monsters++;
                }
            }
        }

        rollRoom(x,y);
        map[x][y]=4;   //boss
        for (int i = 0; i <mapsize ; i++) {
            for (int j = 0; j < mapsize; j++) {
                System.out.print(map[j][i]);
            }
            System.out.println();
        }
        repaint();
    }

    @Override
    public void update(Graphics g) {paint(g);}


    @Override
    public void paint(Graphics ig){
        BufferedImage image= new BufferedImage(1200,900, BufferedImage.TYPE_INT_RGB);
        Graphics2D g=image.createGraphics();
        g.setColor(Color.DARK_GRAY);
        g.fillRect (0,0,900,900);
        int roomSize = 50;
        int roomSize2 = 35;
        for (int oszlop = 0; oszlop < mapsize; oszlop++) {
            for (int sor = 0; sor < mapsize; sor++) {
                if (map[oszlop][sor]>0) {
                    g.setColor (Color.BLACK);
                    g.fillRect (sor* roomSize, oszlop* roomSize, roomSize, roomSize);
                    g.setColor (Color.GRAY);
                    g.fillRect ((sor * roomSize) + 7, ((oszlop) * roomSize) + 7, roomSize2, roomSize2);
                    if (checkWalls(sor-1,oszlop)) {
                        g.fillRect(sor* roomSize, ((oszlop)* roomSize)+(roomSize /3), 10, roomSize /3);  //left door
                    }

                    if (checkWalls(sor,oszlop-1)) {
                        g.fillRect((sor* roomSize)+(roomSize /3), oszlop* roomSize, roomSize /3, 10); //top door
                    }

                    if (checkWalls(sor+1,oszlop)) {
                        g.fillRect(((sor+1)* roomSize)-10, ((oszlop)* roomSize)+(roomSize /3), 10, roomSize /3); //right door
                    }

                    if (checkWalls(sor,oszlop+1)) {
                        g.fillRect((sor* roomSize)+(roomSize /3), ((oszlop+1)* roomSize)-10, roomSize /3, 10);//bottom door
                    }
                }

            }

        }
        boss=bossCheck();
        if (boss) {

            g.setColor(Color.RED);
            g.fillRect((y * roomSize) + 7, ((x) * roomSize) + 7, roomSize2, roomSize2);

            if(map[myY][myX]==4){
                if(!defeated){
                    System.out.println("Boss battle starts");
                    startBattle ();
                }

                else {
                    map[myY][myX] = 1;
                    defeated=false;
                    generateMap ();
                }
            }
        }

        g.setColor (Color.CYAN);
        g.fillRect ((startY * roomSize) + 7, ((startX) * roomSize) + 7, roomSize2, roomSize2);
        g.setColor (Color.GREEN);
        g.fillOval ((myX*roomSize)+15, (myY*roomSize)+15, 20,20);
        if(map[myY][myX]==3) {
            g.setColor (Color.YELLOW);
        g.fillRect (750,500,100,50);
        map[myY][myX]=1;
            System.out.println("You found treasure!");
        }
        else if (map[myY][myX]==2){
            if(!defeated) {
                System.out.println ("You encountered a monster!");
                startBattle ();
            }
            else {
                map[myY][myX]=1;
                monsters--;
                defeated=false;

            }
        }
        ig.drawImage(image, 0,0,this);
    }


    private void startBattle() {
        this.setFocusable(false);
        Battle battle=new Battle(this, player);
        JFrame btl= new JFrame("Battle");
        battle.ui(btl);
        btl.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        btl.getContentPane().add(battle);

        btl.pack();
        btl.setVisible(true);
        btl.setResizable(true);
        btl.requestFocus();
    }

    private void getStart(){
        startX=map[0].length/2;
        startY=map[0].length/2;
        myX=startX;
        myY=startY;
    }
    private void rollRoom(int x, int y) {
        Point actual = new Point(x, y);
        Point nextPoint = checkStep(actual);
        if (nextPoint != null) {
            this.x = nextPoint.x;
            this.y = nextPoint.y;
        } else if(tries>4){
            roomCount=maxRooms;
        }
        else {
            tries++;
            rollRoom(startX, startY);
        }
    }

    private Point checkStep(Point point){
        Vector<Point> vector = new Vector<Point>();
        for (Point p : deltas) {
            if (checkPoint(point.add(p))) vector.add(point.add(p));
        }
        if (vector.size() == 0) {
            return null;
        }
        return vector.get(random.nextInt(vector.size()));
    }

    private boolean checkPoint(Point p) {
        if (p.x < 0) return false;
        if (p.y < 0) return false;
        if (p.x >= mapsize) return false;
        if (p.y >= mapsize) return false;
        return map[p.x][p.y] == 0;
    }

    private boolean checkWalls(int checkx, int checky){

        try {
            return ((map[checky][checkx] > 0));
        }catch (IndexOutOfBoundsException ex){
            return false;
        }

        //returns if it does need a wall;
    }
    private boolean bossCheck(){
        return monsters<=0;
    }
    public  void start(){
        this.generateMap ();
        if(frame!=null) {
            frame.dispose ();

        }
        frame = new JFrame("Basic Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        this.requestFocus();
    }



}
