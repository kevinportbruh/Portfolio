package handler.pattern;
import java.util.ArrayList;

/**
 * this class will handle (filter) the sex of the pet in our pet array
 * @author Kevin Portillo, self-proclaimed coding wizard
 */
public class SexHandler extends BaseHandler {
    public SexHandler(Handler n) {
        super(n);
    }
    /*
     * this will filter or search for specified sex of pet, which is some string in s
     */
    @Override
    public ArrayList<Pet> handle(ArrayList<Pet> p, Boolean searchType,int age, String... s) {
        ArrayList<Pet> temp = new ArrayList<Pet>();
        boolean doWeSkip = true;
        
        for (String j:s){
            if (j.toLowerCase().equals("male") ||j.toLowerCase().equals("female")){
                doWeSkip = false;
                break;
            }
        }

        if (p == null || s == null || doWeSkip) {
            return this.getNext().handle(p, searchType,age,s);
        } else {
            if (searchType) {
                for (Pet i : p) {
                    for (String j : s) {
                        if (i.getSex().toLowerCase().equals(j.toLowerCase())) {
                            temp.add(i);
                            break;
                        }
                    }
                }
                return this.getNext().handle(temp, searchType,age, s);
            } else {
                ArrayList<Pet> temp2  = new ArrayList<Pet>();
                temp2.addAll(p);
                for (Pet i : p) {
                    for (String j : s) {
                        if (i.getSex().toLowerCase().equals(j.toLowerCase())) {
                            temp2.remove(i);
                            break;
                        }

                    }
                }
                return this.getNext().handle(temp2, searchType, age,s);
            }
        }
    }
    
}
