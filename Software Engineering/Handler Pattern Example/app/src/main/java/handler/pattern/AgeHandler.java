package handler.pattern;
import java.util.ArrayList;
/*
 * This class will handle(filter) the age of the pet in our pet array
 * @author Kevin Portillo, self-proclaimed coding wizard
 */
 
public class AgeHandler extends BaseHandler{
    public AgeHandler(Handler n){
        super(n);
    }
    /*
     *  This will filter or search for the specified age of the pet
     * @param P is the pet array that will be filtered or searched through
     * @param searchType is a boolean that will determine if we are searching for a specific age or filtering out a specific age
     * @param age is the age limt(ie. pet.age <= age) we are searching for or filtering out
     * @return the filtered or searched pet array
     */
    @Override
    public ArrayList<Pet> handle(ArrayList<Pet> p, Boolean searchType,int age, String... s){
        ArrayList<Pet> temp = new ArrayList<Pet>();
        if (p == null|| age ==-1 || age == Integer.MAX_VALUE) {
            return this.getNext().handle(p, searchType,age,s);
        } else {
            if (searchType){
                for (Pet i :p){
                    if (i.getAge() <= age){
                        temp.add(i);
                    }
                }
                return this.getNext().handle(temp, searchType,age,s);
            }else{
                ArrayList<Pet> temp2  = new ArrayList<Pet>();
                temp2.addAll(p);
                for (Pet i :p){
                    if (i.getAge() <= age){
                        temp2.remove(i);
                    }
                }
                return this.getNext().handle(temp2, searchType,age,s);
            }
        }
    }
}
