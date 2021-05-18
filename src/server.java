import java.net.*;

import java.io.*;

import java.util.*;


public class server{
	
	public static int  readyCount = 0;
	public static int  startCount = 0;
	private ServerSocket server;
	private BManager bMan=new BManager();   // �޽��� �����
	private Random rnd= new Random();       // ��� ���� ���Ƿ� ���ϱ� ���� ����
	public server(){}
	void startServer(){                         // ������ �����Ѵ�.

    try{

      server=new ServerSocket(7777);

      System.out.println("���������� �����Ǿ����ϴ�.");
      InetAddress addr = InetAddress.getLocalHost();

   

      String strIP = addr.getHostAddress();

      String strHostName = addr.getHostName();


      System.out.println("IP : " + strIP);

      System.out.println("HOST : " + strHostName);


      while(true){

 

        // Ŭ���̾�Ʈ�� ����� �����带 ��´�.

        Socket socket=server.accept();

        

        // �����带 ����� �����Ų��.

        BattleThread bt=new BattleThread(socket);

        bt.start();

 

        // bMan�� �����带 �߰��Ѵ�.

        bMan.add(bt);

 

        System.out.println("������ ��: "+bMan.size());

      }

    }catch(Exception e){

      System.out.println(e);

    }

  }

  public static void main(String[] args){

    server bserver=new server();

    bserver.startServer();

  }

 

 // Ŭ���̾�Ʈ�� ����ϴ� ������ Ŭ����

  class BattleThread extends Thread{

 
    private Socket socket;              // ����

 

    // ���� �غ� ����, true�̸� ������ ������ �غ� �Ǿ����� �ǹ��Ѵ�.

    //private boolean ready=false;

 

    private BufferedReader reader;     // �Է� ��Ʈ��

    private PrintWriter writer;           // ��� ��Ʈ��

    BattleThread(Socket socket){     // ������

      this.socket=socket;

    }

    Socket getSocket(){               // ������ ��ȯ�Ѵ�.

      return socket;

    }

    /*boolean isReady(){                 // �غ� ���¸� ��ȯ�Ѵ�.

      return ready;

    }*/

    public void run(){
    	
      try{

        reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));  //client�κ��� �Է¹���

        writer=new PrintWriter(socket.getOutputStream(), true);

 

        String msg;                     // Ŭ���̾�Ʈ�� �޽���

 

        while((msg=reader.readLine())!=null){


              //writer.println(msg);

 

          // "[HIT]" �޽����� ������� �����Ѵ�.

         if( msg.startsWith("[HIT]")) {

            bMan.sendTo(socket,msg);
         System.out.println("hit sent");}
         
         if( msg.startsWith("[MISS]")) {

             bMan.sendTo(socket,msg);
          System.out.println("miss sent");}
         
         if( msg.startsWith("[OVER]")) {

             bMan.sendTo(socket,msg);
          System.out.println("hit sent");}
          


 
          // "[START]" �޽����̸�
         
        /* else if (msg.startsWith("[READY]")) {
        	 readyCount ++;
        	 System.out.println("ready count ++");
        	 if (readyCount == 2) {
        		 writer.println("[START]");
        		 bMan.sendTo(socket,"[START]");
        		 System.out.println("i sent start");
        		 readyCount =0;
        	 }
         }*/

          else if(msg.startsWith("[START]")){ 
        	  startCount ++;
        	  if(startCount ==2) {
        	  System.out.println("server start");
        	  startCount =0;


            // �ٸ� ����ڵ� ������ ������ �غ� �Ǿ�����

              // ��� ���� ���ϰ� ����ڿ� ������� �����Ѵ�.

              int a=rnd.nextInt(2);

              if(a==0){

                writer.println("[COLOR]BLACK");

                bMan.sendTo(socket,"[COLOR]WHITE");

              }

              else{

                writer.println("[COLOR]WHITE");

                bMan.sendTo(socket,"[COLOR]BLACK");

              }
          }

 

          // ����ڰ� ������ �����ϴ� �޽����� ������

          /*else if(msg.startsWith("[STOPGAME]"))

            ready=false;


          // ����ڰ� �̰�ٴ� �޽����� ������

          else if(msg.startsWith("[WIN]")){

            ready=false;

            // ����ڿ��� �޽����� ������.

            writer.println("[WIN]");

 

            // ������� ������ �˸���.

            bMan.sendTo(socket,"[LOSE]");

          }  */

        }}

      }catch(Exception e){

      }finally{

        try{

          bMan.remove(this);

          if(reader!=null) reader.close();

          if(writer!=null) writer.close();

          if(socket!=null) socket.close();

          reader=null; writer=null; socket=null;

         
        }catch(Exception e){}

      }

    }

  }

  class BManager extends Vector{       // �޽����� �����ϴ� Ŭ����

    BManager(){}
    Socket s;

    void add(BattleThread bt){           // �����带 �߰��Ѵ�.

      super.add(bt);

    }

    void remove(BattleThread bt){        // �����带 �����Ѵ�.

       super.remove(bt);

    }

    BattleThread getBT(int i){            // i��° �����带 ��ȯ�Ѵ�.

      return (BattleThread)elementAt(i);

    }

    Socket getSocket(int i){              // i��° �������� ������ ��ȯ�Ѵ�.

      return getBT(i).getSocket();

    }

 

    // Ŭ���̾�Ʈ���� �޽����� �����Ѵ�.

    void sendTo(Socket mysocket, String msg){
    	this.s = mysocket;

      try{
    	  for(int j=0;j<size();j++) {
    		  if(getSocket(j)== s)
    			  continue;

        PrintWriter pw= new PrintWriter(getSocket(j).getOutputStream(), true);

        pw.println(msg);
        }

      }catch(Exception e){}  

    }


  }

}
