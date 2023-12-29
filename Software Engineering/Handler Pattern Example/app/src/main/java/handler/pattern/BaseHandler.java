package handler.pattern;
import java.util.ArrayList;

public class BaseHandler implements Handler{
    private Handler next;

    public BaseHandler(Handler n){
        this.next = n;
    }
    
    public void setNext(Handler n){
        this.next = n;
    }
    public Handler getNext(){
        return this.next;
    }
    
    //does nothing will be used as the null handler
    @Override
    public ArrayList<Pet> handle(ArrayList<Pet> p,Boolean searchType,int age, String ... s){
        return p;
    }
    
}
