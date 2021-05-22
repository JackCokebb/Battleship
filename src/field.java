import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class field implements Runnable, ActionListener{
    private boolean enable =false;
    private boolean ready =false;
    public PrintWriter writer;
    private BufferedReader reader;
    private Socket socket;
    private static int hitCount =0;
    public static final int BLACK=1, WHITE=-1;
    private int color=BLACK;

    JFrame frame= new JFrame();
    JPanel player_panel= new JPanel();
    JPanel button_panel1=new JPanel();
    JPanel button_panel2=new JPanel();
    JLabel Player_A= new JLabel();
    JLabel Player_B= new JLabel();

    JButton[] buttons1 = new JButton[100];
    JButton[] buttons2 = new JButton[100];
    battlefield battlefieldA;
    battlefield battlefieldB;
    String level;
  

    field(battlefield A, battlefield B, String level){
        this.battlefieldA = A;
        this.battlefieldB = B;
        this.level = level;

        ImageIcon image = new ImageIcon("bs.png");
        frame.setIconImage(image.getImage());

        frame.setTitle("BATTLESHIP");
        frame.setResizable(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500,800);
        frame.getContentPane().setBackground(new Color(50,50,50));
        frame.setLayout(new BorderLayout() );

        player_panel.setLayout(new FlowLayout());
        player_panel.setPreferredSize(new Dimension(200,80));
        Player_A.setBackground(new Color(255,0,0));
        Player_A.setForeground(new Color(0,0,0));
        Player_A.setPreferredSize(new Dimension(300,40));
        Player_A.setFont(new Font("MV Boli",Font.BOLD,20));
        Player_A.setText("Opponent's Field");
        Player_A.setOpaque(true);
        Player_B.setBackground(new Color(0,0,255));
        Player_B.setForeground(new Color(0,0,0));
        Player_B.setPreferredSize(new Dimension(300,40));
        Player_B.setFont(new Font("MV Boli",Font.BOLD,20));
        Player_B.setText("My Field");
        Player_B.setOpaque(true);
        player_panel.add(Player_A);
        player_panel.add(Player_B);


        button_panel1.setPreferredSize(new Dimension(700,800));
        button_panel1.setLayout(new GridLayout(10,10));
        button_panel1.setBackground(new Color(150,150,150));

        button_panel2.setPreferredSize(new Dimension(700,800));
        button_panel2.setLayout(new GridLayout(10,10));
        button_panel2.setBackground(new Color(150,150,150));


        char[] bA=new char[100];
        char[] bB=new char[100];

        for (int i=0;i<100;){
            for (int j=0;j<10;j++){
                for (int k=0;k<10;k++){
                    bA[i]=battlefieldA.getChar(battlefieldA,j,k);
                    i++;
                }
            }
        }

        for (int i=0;i<100;){
            for (int j=0;j<10;j++){
                for (int k=0;k<10;k++){
                    bB[i]=battlefieldB.getChar(battlefieldB,j,k);
                    i++;
                }
            }
        }

        for (int i=0;i<100;i++){
            buttons1[i]=new JButton();
            button_panel1.add(buttons1[i]);
            buttons1[i].setFont(new Font("MV Boli",Font.BOLD,30));
            buttons1[i].setFocusable(false);
            buttons1[i].addActionListener(this);
            buttons1[i].setName(String.valueOf(bA[i]));
        }

        for (int i=0;i<100;i++){
            buttons2[i]=new JButton();
            button_panel2.add(buttons2[i]);
            buttons2[i].setFont(new Font("MV Boli",Font.BOLD,30));
            buttons2[i].setFocusable(false);
            buttons2[i].addActionListener(this);
            buttons2[i].setName(String.valueOf(bB[i]));
        }

        frame.add(player_panel,BorderLayout.NORTH);
        frame.add(button_panel1,BorderLayout.WEST);
        frame.add(button_panel2,BorderLayout.EAST);

        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(!ready) {
            System.out.println("ready out");
            return;}
        if(!enable) {
            System.out.println("hit");
            return;}
        System.out.println("oh");

        for (int i=0;i<100;i++){
            if (e.getSource()==buttons1[i]){

                if(this.level.equals("easy")){
                    String buttonName= ((JButton)e.getSource()).getName();
                    if (!buttonName.equals("o")){
                        buttons1[i].setForeground(new Color(0,0,0));
                        buttons1[i].setText(buttonName);
                        writer.println("[HIT]"+i+" "+buttonName);
                        enable=false;
                        hitCount++;
                     
                        if(hitCount==17) {
                            gameWin();
                        }

                    }
                    else{
                        buttons1[i].setForeground(new Color(0,0,225));
                        buttons1[i].setText("o");
                        writer.println("[MISS]"+i);
                        enable=false;
                        
                       

                        if (hitCount == 17){
                            try{ Thread.sleep(2000); }catch(Exception se){}
                            writer.println("[OVER]");
                            gameOver();
                        }
                    }
                }
                else if(this.level.equals("hard")){
                    String buttonName= ((JButton)e.getSource()).getName();
                    if (!buttonName.equals("o")){
                        buttons1[i].setForeground(new Color(0,0,0));
                        buttons1[i].setText("x");
                        writer.println("[HIT]"+i+" "+buttonName);
                        enable=false;
                        hitCount++;
                      
                        if(hitCount==17) {
                        	writer.println("[OVER]"); // tell opponent game is over
                            gameWin();
                        }
                    }
                    else{
                        buttons1[i].setForeground(new Color(0,0,225));
                        buttons1[i].setText("o");
                        writer.println("[MISS]"+i);
                        enable=false;
                  
                    }
                }
            }
        }

    }

    public void gameOver(){
        frame.dispose();
        ending gameEnd = new ending();
    }
    public void gameWin() {
        frame.dispose();
        winning gameWin = new winning();
    }

    @Override
    public void run(){

        String msg;
        try{
            while((msg=reader.readLine())!=null){
              
            	if(msg.startsWith("[HIT]")){
                    System.out.println("hit in");
                    String temp=msg.substring(5);
                    int x=Integer.parseInt(temp.substring(0,temp.indexOf(" ")));

                    String buttonName = temp.substring(temp.indexOf(" ")+1);

                    buttons2[x].setForeground(new Color(0,0,0));
                    buttons2[x].setText(buttonName);

                    setEnable(true);
                }
                else if(msg.startsWith("[START]")) {
                    System.out.println("ok");
                    writer.println("[START]");

                }
                else if(msg.startsWith("[OVER]")) {
                    gameOver();
                }
                else if(msg.startsWith("[MISS]")){
                    String temp=msg.substring(6);

                    int x=Integer.parseInt(temp);
                    buttons2[x].setForeground(new Color(0,0,225));
                    buttons2[x].setText("o");
                    System.out.println("miss in");

                    setEnable(true);
                }
                else if(msg.startsWith("[COLOR]")){
                    String color=msg.substring(7);
                    startGame(color);
                }
                else if(msg.startsWith("[WIN]"))
                    gameOver();

                else if(msg.startsWith("[LOSE]"))
                    gameOver();
            }

        }catch(IOException ie){

        }
    }

    public void setEnable(boolean enable){
        this.enable=enable;
    }

    public void setWriter(PrintWriter writer){
        this.writer=writer;
    }

    public void startGame(String col){
        if(col.equals("BLACK")){
            enable=true;
            ready = true;
            color=BLACK;
            System.out.println("good to go B");
        }
        else{
            enable=false;
            ready = true;
            color=WHITE;
            System.out.println("good to go w");
        }

    }
    public void connect(){

        try{
            socket=new Socket("192.168.206.1", 7777);

            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer=new PrintWriter(socket.getOutputStream(), true);

            new Thread(this).start();

            setWriter(writer);
            writer.println("[START]");
        }catch(Exception e){


        }

    }


}