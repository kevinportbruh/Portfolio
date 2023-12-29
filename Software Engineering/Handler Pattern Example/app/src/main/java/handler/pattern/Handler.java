package handler.pattern;
import java.util.ArrayList;

public interface Handler {
    //return type will change later to an array and param will be a string
    //the ArrayList would be more efficent as a hash map but for simplicity sake we will use an arraylist
    public ArrayList<Pet> handle(ArrayList<Pet> p,Boolean searchType, int age,String ... s);
}