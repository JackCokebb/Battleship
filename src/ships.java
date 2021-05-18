public class ships {
    String name;
    int length;
    char initial;
    ships(String name, int length,char initial){
        this.name= name;
        this.length=length;
        this.initial=initial;
    }
    public int getLength(){
        return this.length;
    }
}
