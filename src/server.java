import java.net.*;

import java.io.*;

import java.util.*;


public class server{
	
	public static int  readyCount = 0;
	public static int  startCount = 0;
	private ServerSocket server;
	private BManager bMan=new BManager();   // 메시지 방송자
	private Random rnd= new Random();       // 흑과 백을 임의로 정하기 위한 변수
	public server(){}
	void startServer(){                         // 서버를 실행한다.

    try{

      server=new ServerSocket(7777);

      System.out.println("서버소켓이 생성되었습니다.");
      InetAddress addr = InetAddress.getLocalHost();

   

      String strIP = addr.getHostAddress();

      String strHostName = addr.getHostName();


      System.out.println("IP : " + strIP);

      System.out.println("HOST : " + strHostName);


      while(true){

 

        // 클라이언트와 연결된 스레드를 얻는다.

        Socket socket=server.accept();

        

        // 스레드를 만들고 실행시킨다.

        BattleThread bt=new BattleThread(socket);

        bt.start();

 

        // bMan에 스레드를 추가한다.

        bMan.add(bt);

 

        System.out.println("접속자 수: "+bMan.size());

      }

    }catch(Exception e){

      System.out.println(e);

    }

  }

  public static void main(String[] args){

    server bserver=new server();

    bserver.startServer();

  }

 

 // 클라이언트와 통신하는 스레드 클래스

  class BattleThread extends Thread{

 
    private Socket socket;              // 소켓

 

    // 게임 준비 여부, true이면 게임을 시작할 준비가 되었음을 의미한다.

    //private boolean ready=false;

 

    private BufferedReader reader;     // 입력 스트림

    private PrintWriter writer;           // 출력 스트림

    BattleThread(Socket socket){     // 생성자

      this.socket=socket;

    }

    Socket getSocket(){               // 소켓을 반환한다.

      return socket;

    }

    /*boolean isReady(){                 // 준비 상태를 반환한다.

      return ready;

    }*/

    public void run(){
    	
      try{

        reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));  //client로부터 입력받음

        writer=new PrintWriter(socket.getOutputStream(), true);

 

        String msg;                     // 클라이언트의 메시지

 

        while((msg=reader.readLine())!=null){


              //writer.println(msg);

 

          // "[HIT]" 메시지는 상대편에게 전송한다.

         if( msg.startsWith("[HIT]")) {

            bMan.sendTo(socket,msg);
         System.out.println("hit sent");}
         
         if( msg.startsWith("[MISS]")) {

             bMan.sendTo(socket,msg);
          System.out.println("miss sent");}
         
         if( msg.startsWith("[OVER]")) {

             bMan.sendTo(socket,msg);
          System.out.println("hit sent");}
          


 
          // "[START]" 메시지이면
         
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


            // 다른 사용자도 게임을 시작한 준비가 되었으면

              // 흑과 백을 정하고 사용자와 상대편에게 전송한다.

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

 

          // 사용자가 게임을 중지하는 메시지를 보내면

          /*else if(msg.startsWith("[STOPGAME]"))

            ready=false;


          // 사용자가 이겼다는 메시지를 보내면

          else if(msg.startsWith("[WIN]")){

            ready=false;

            // 사용자에게 메시지를 보낸다.

            writer.println("[WIN]");

 

            // 상대편에는 졌음을 알린다.

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

  class BManager extends Vector{       // 메시지를 전달하는 클래스

    BManager(){}
    Socket s;

    void add(BattleThread bt){           // 스레드를 추가한다.

      super.add(bt);

    }

    void remove(BattleThread bt){        // 스레드를 제거한다.

       super.remove(bt);

    }

    BattleThread getBT(int i){            // i번째 스레드를 반환한다.

      return (BattleThread)elementAt(i);

    }

    Socket getSocket(int i){              // i번째 스레드의 소켓을 반환한다.

      return getBT(i).getSocket();

    }

 

    // 클라이언트에게 메시지를 전송한다.

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
