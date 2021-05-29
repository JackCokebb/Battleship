import java.net.*;

import java.io.*;

import java.util.*;


public class server{

    public static int  readyCount = 0;
    public static int  startCount = 0;
    private ServerSocket server;
    private M_Manager m_man = new M_Manager();   // Message distributer
    private Random rnd= new Random();       // to choose color of player randomly
    public server(){}
    void startServer(){                         //to start server

        try{

            server=new ServerSocket(7777);

            System.out.println("server socket successfully made");
            InetAddress addr = InetAddress.getLocalHost();



            String strIP = addr.getHostAddress();

            String strHostName = addr.getHostName();


            System.out.println("IP : " + strIP);

            System.out.println("HOST : " + strHostName);


            while(true){

                Socket socket=server.accept(); // get thread that is connected with client


                BattleThread bt=new BattleThread(socket);

                bt.start();   // make thread and run it



                

                m_man.add(bt); // add message distributer to thread



                System.out.println("number of the connected: "+m_man.size());

            }

        }catch(Exception e){

            System.out.println(e);

        }

    }

    public static void main(String[] args){

        server bserver=new server();

        bserver.startServer();

    }



    

    class BattleThread extends Thread{ // thread that communicate with client


        private Socket socket;             
        private BufferedReader reader;     // input stream
        private PrintWriter writer;           // output stream

        BattleThread(Socket socket){     

            this.socket=socket;

        }

        Socket getSocket(){            
            return socket;

        }

        public void run(){

            try{

                reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));  //client�κ��� �Է¹���

                writer=new PrintWriter(socket.getOutputStream(), true);



                String msg;         //message from client



                while((msg=reader.readLine())!=null){


                    if( msg.startsWith("[HIT]")) {

                        m_man.sendTo(socket,msg);
                        System.out.println("hit sent");}

                    if( msg.startsWith("[MISS]")) {

                    	m_man.sendTo(socket,msg);
                        System.out.println("miss sent");}

                    if( msg.startsWith("[OVER]")) {

                    	m_man.sendTo(socket,msg);
                        System.out.println("hit sent");}

                    else if(msg.startsWith("[START]")){
                        startCount ++;                      //count start signal from players
                        if(startCount ==2) {
                            System.out.println("server start");
                            startCount =0;

                            // if 2 players connected to server, randomly choose color of player and send it to each player.

                            int a=rnd.nextInt(2);

                            if(a==0){

                                writer.println("[COLOR]BLACK");

                                m_man.sendTo(socket,"[COLOR]WHITE");

                            }

                            else{

                                writer.println("[COLOR]WHITE");

                                m_man.sendTo(socket,"[COLOR]BLACK");

                            }
                        }
                    }}

            }catch(Exception e){

            }finally{

                try{

                	m_man.remove(this);

                    if(reader!=null) reader.close();

                    if(writer!=null) writer.close();

                    if(socket!=null) socket.close();

                    reader=null; writer=null; socket=null;


                }catch(Exception e){}

            }

        }

    }

    class M_Manager extends Vector{       // message distributer

    	M_Manager(){}
        Socket s;

        void add(BattleThread bt){           // add thread to vector

            super.add(bt);

        }

        void remove(BattleThread bt){        // remove

            super.remove(bt);

        }

        BattleThread getBT(int i){            // return i-th thread in vector.

            return (BattleThread)elementAt(i);

        }

        Socket getSocket(int i){              // return i-th thread's socket in vector.
            return getBT(i).getSocket();

        }


        void sendTo(Socket mysocket, String msg){
            this.s = mysocket;       

            try{
                for(int j=0;j<size();j++) {
                    if(getSocket(j)== s)            //get size of message distributer which means the number of player, and except the player who send message, distribute message to player  
                        continue;

                    PrintWriter pw= new PrintWriter(getSocket(j).getOutputStream(), true);

                    pw.println(msg);
                }

            }catch(Exception e){}

        }


    }

}
