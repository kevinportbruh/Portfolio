package handler.pattern;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class will handle(filter) the species of the pet in our pet array
 * @author Kevin Portillo, self-proclaimed coding wizard
 */
public class SpeciesHandler extends BaseHandler{

    public SpeciesHandler(Handler n){
        super(n);
    }
    
    /*
     * This will filter or search for the specified species of the pet
     * @param P is the pet array that will be filtered or searched through
     * @param searchType is a boolean that will determine if we are searching for a specific species or filtering out a specific species
     * @param s is a string array that will contain the species we are searching for or filtering out
     * age and agetype are irrelevant here.
     */
    @Override
    public ArrayList<Pet> handle(ArrayList<Pet> p,Boolean searchType, int age,String ... s){
        //if pet[] is null or if strings then move to next handler till base handler
        ArrayList<Pet> temp = new ArrayList<Pet>(); 
        
        boolean doWeSkip = true;
        
        for (String j:s){
            if (j.toLowerCase().equals("dog") ||j.toLowerCase().equals("cat")
                ||j.toLowerCase().equals("bird") || j.toLowerCase().equals("rabbit")){
                doWeSkip = false;
                break;
            }
        }
      
        if (p==null || s == null || doWeSkip){
            return this.getNext().handle(p,searchType, age,s);
        }else{
            //if searchType is true then we are searching for a specific species
            if (searchType){
                //loop through pet array
                for (Pet i: p) {
                    
                    //if any of the strings is a speices (dog,cat,bird) then return a Pet[] only containg that species
                    //then pass to next handler after filtering all pets.
                    for(String j : s){
                        if (i.getType().toLowerCase().equals(j.toLowerCase())){
                            temp.add(i);
                            break; // break the inner loop as we've modified the list
                        }
                    }
                }
                //done keep filtering.
                return this.getNext().handle(temp,searchType,age,s);
            }else{
                //fitlering out specific species from results
                //set temp arraylist ==to p
                ArrayList<Pet> temp2  = new ArrayList<Pet>();
                temp2.addAll(p);
                for (Pet i:p) {
                    //for each pet i in p which is also in temp2 match string filter and remove from temp2 
                    for (String j : s) {
                        if (i.getType().toLowerCase().equals(j.toLowerCase())) {
                            temp2.remove(i);
                            break; // break the inner loop as we've modified the list
                        }
                    }
                }
                //done pass on
                return this.getNext().handle(temp2,searchType,age,s);
            }
        }
    }
    
}
