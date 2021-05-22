import java.util.Arrays;
import java.util.Random;

public class battlefield {
    public char[][] board = createBoard('o');

    battlefield(){
        ships[] allShips = new ships[5];
        allShips[0]=new ships("Carrier",5,'c');
        allShips[1]=new ships("Battleship",4,'b');
        allShips[2]=new ships("Cruiser",3,'r');
        allShips[3]=new ships("Submarine",3,'s');
        allShips[4]=new ships("Destroyer",2,'d');
        placeShips(board,allShips[0],allShips[0].initial);
        placeShips(board,allShips[1],allShips[1].initial);
        placeShips(board,allShips[2],allShips[2].initial);
        placeShips(board,allShips[3],allShips[3].initial);
        placeShips(board,allShips[4],allShips[4].initial);
        this.board=getBoard();
    }
    public char getChar(battlefield battlefield,int row,int column){
        return board[row][column];
    }
    public char[][] createBoard(char water){
        char[][] board = new char[10][10];
        for (char[] row: board){
            Arrays.fill(row,water);
        }
        return board;
    }

    public char[][] getBoard(){
        return board;
    }

    public char[][] placeShips(char[][] board,ships ship,char initial){
        boolean noFits_flag=false;
        int start=0,direction=0,len=0,end=0;
        do{
            Random random = new Random();
            direction=random.nextInt(2);
            start=random.nextInt(10);
            len= ship.getLength();
            end=start+len;
            if (end>9)
                start-=len;
            noFits_flag=false;
            //check if there is stg there
            if (direction==0){
                for (int i=start;i<start+len;i++){
                    if (board[start][i]!='o'){
                        noFits_flag=true;
                        break;
                    }
                }
            }
            else {
                for (int i=start;i<start+len;i++){
                    if(board[i][start]!='o'){
                        noFits_flag=true;
                        break;
                    }
                }

            }
        }while(noFits_flag);
        if (direction==0){
            for (int i=start;i<start+len;i++){
                board[start][i]=initial;
            }
        }
        else {
            for (int i=start;i<start+len;i++){
                board[i][start]=initial;
            }

        }

        return board;
    }

}
