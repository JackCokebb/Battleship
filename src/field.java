



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
    JPanel title_panel=new JPanel();
    JPanel player_panel= new JPanel();
    JPanel twoField=new JPanel();
    JPanel button_panel1=new JPanel();
    JPanel button_panel2=new JPanel();
    JLabel textField= new JLabel();
    JLabel Player_A= new JLabel();
    JLabel Player_B= new JLabel();
    
    JButton[] buttons1 = new JButton[100];
    JButton[] buttons2 = new JButton[100];
    battlefield battlefieldA;
    battlefield battlefieldB;
    String level;
    int lives = 10;
    field(battlefield A, battlefield B, String level){
        this.battlefieldA = A;
        this.battlefieldB = B;
        this.level = level;

        ImageIcon image = new ImageIcon("bs.png");
        frame.setIconImage(image.getImage());

        frame.setTitle("BATTLESHIP");
        frame.setResizable(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1650,950);
        frame.getContentPane().setBackground(new Color(50,50,50));
        frame.setLayout(new BorderLayout() );
        frame.setVisible(true);

        textField.setBackground(new Color(25,25,25));
        textField.setForeground(new Color(255,0,144));
        textField.setPreferredSize(new Dimension(100,50));                                
        textField.setFont(new Font("MV Boli",Font.BOLD,20));
        textField.setHorizontalAlignment(JLabel.CENTER);
        textField.setText("BATTLESHIP");
        textField.setOpaque(true);

        player_panel.setLayout(new FlowLayout());
        player_panel.setPreferredSize(new Dimension(1650,50));
        Player_A.setBackground(new Color(255,0,0));
        Player_A.setForeground(new Color(0,0,0));
        Player_A.setPreferredSize(new Dimension(810,50)); 
        Player_A.setFont(new Font("MV Boli",Font.BOLD,20));
        Player_A.setText("Player A");
        Player_A.setOpaque(true);
        Player_B.setBackground(new Color(0,0,255));
        Player_B.setForeground(new Color(0,0,0));
        Player_B.setPreferredSize(new Dimension(810,50)); 
        Player_B.setFont(new Font("MV Boli",Font.BOLD,20));
        Player_B.setText("Player B");
        Player_B.setOpaque(true);
        player_panel.add(Player_A);
        player_panel.add(Player_B);
        
        title_panel.setLayout(new BorderLayout());
        title_panel.setBounds(0,0,800,100);
        
        twoField.setLayout(new FlowLayout());

        button_panel1.setPreferredSize(new Dimension(800,800));
        button_panel1.setLayout(new GridLayout(10,10));
        button_panel1.setBackground(new Color(150,150,150));
        
        button_panel2.setPreferredSize(new Dimension(800,800));
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
            //buttons[i].setPreferredSize(new Dimension(80,80));
            button_panel1.add(buttons1[i]);
            buttons1[i].setFont(new Font("MV Boli",Font.BOLD,30));
            buttons1[i].setFocusable(false);
            buttons1[i].addActionListener(this);
            buttons1[i].setName(String.valueOf(bA[i]));
        }
        
        for (int i=0;i<100;i++){
            buttons2[i]=new JButton();
            //buttons[i].setPreferredSize(new Dimension(80,80));
            button_panel2.add(buttons2[i]);
            buttons2[i].setFont(new Font("MV Boli",Font.BOLD,30));
            buttons2[i].setFocusable(false);
            buttons2[i].addActionListener(this);
            buttons2[i].setName(String.valueOf(bB[i]));
        }
        
       
        frame.add(title_panel,BorderLayout.NORTH); 
        title_panel.add(textField);
        
        frame.add(twoField,BorderLayout.SOUTH);
        twoField.add(button_panel1);
        twoField.add(button_panel2);
        frame.add(player_panel,BorderLayout.CENTER);
       
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
                   /* else if(this.level.equals("hard")){
                        String buttonName= ((JButton)e.getSource()).getName();
                        if (!buttonName.equals("o")){
                            buttons1[i].setForeground(new Color(0,0,0));
                            buttons1[i].setText("x");
                            writer.println("[HIT]"+i+" "+buttonName);
                            enable=false;
                            hitCount++;
                        }
                        else{
                            buttons1[i].setForeground(new Color(0,0,225));
                            buttons1[i].setText("o");
                            lives--;
                            if (lives==0){
                                gameOver();
                            }
                        }
                    }  */
                }
        }

    }

    public void gameOver(){
        frame.dispose();
        ending gameEnd = new ending();
    }

	@Override
	 public void run(){

	    String msg;                             // 서버로부터의 메시지

	    try{

	    while((msg=reader.readLine())!=null){

	 

	        if(msg.startsWith("[HIT]")){     // 상대편 move
	        	System.out.println("hit in");

	          String temp=msg.substring(5);

	          int x=Integer.parseInt(temp.substring(0,temp.indexOf(" ")));

	          String buttonName = temp.substring(temp.indexOf(" ")+1);

	          buttons2[x].setForeground(new Color(0,0,0));
              buttons2[x].setText(buttonName);   // 상대편의 좌표 그린다.

	          setEnable(true);        // 사용자 차례.

	        }
	        
	        else if(msg.startsWith("[START]")) {
	        	System.out.println("ok");
	        	writer.println("[START]");
	        	
	        }
	        
	        else if(msg.startsWith("[OVER]")) {
	        	gameOver();
	        }
	        
	        else if(msg.startsWith("[MISS]")){     // 상대편 move

		          String temp=msg.substring(6);

		          int x=Integer.parseInt(temp);
		          buttons2[x].setForeground(new Color(0,0,225));
	              buttons2[x].setText("o");   // 상대편
	              System.out.println("miss in");

		          setEnable(true);        // 사용자 차례.

		        }
	       	 

	        
	        else if(msg.startsWith("[COLOR]")){          // 색을 부여받는다.

	          String color=msg.substring(7);

	          startGame(color);                      // 게임을 시작한다.

	        }


	        else if(msg.startsWith("[WIN]"))              // 이겼으면

	          gameOver();

	 

	        else if(msg.startsWith("[LOSE]"))            // 졌으면

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
	
	public void startGame(String col){     // 게임을 시작한다.

	    if(col.equals("BLACK")){              // 흑이 선택되었을 때

	      enable=true; 
	     ready = true;
	      color=BLACK;
	      System.out.println("good to go B");


	    }   

	    else{                                // 백이 선택되었을 때

	      enable=false;
	      ready = true;
	      color=WHITE;
	      System.out.println("good to go w");

	    }

	  }
	 public void connect(){                    // 연결

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
