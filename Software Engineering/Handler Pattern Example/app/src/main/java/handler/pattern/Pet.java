package handler.pattern;

/*
 * Pet class for handler pattern
 * will have a name "", age #, pottytrained ? ,and type ""
 * This class will be extended into concrete classes which in turn will be stored in an "adoption center" (array)
 * In demo it will be used to show how handler pattern works
 * @author Kevin Portillo, self-proclaimed coding wizard
 */
public class Pet {
    private String name;
    private int age;
    private boolean pottyTrained;
    private String type;//speices of pet
    private String desc;//description of pet
    private String sex;

    //constructor for pet
    //paramaters are the fields
    public Pet(String n, int a, boolean p, String t, String d, String s){
        this.name = n;
        this.age = a;
        this.pottyTrained = p;
        this.type = t;
        this.desc = d;
        this.sex=s;
    }

    //getters and setters
    public String getName(){
        return this.name;
    }
    public void setName(String n){
        this.name = n;
    }
    public int getAge(){
        return this.age;
    }
    public void setAge(int a){
        this.age = a;
    }
    public boolean getPottyTrained(){
        return this.pottyTrained;
    }
    public void setPottyTrained(boolean p){
        this.pottyTrained = p;
    }
    public String getType(){
        return this.type;
    }
    public void setType(String t){
        this.type = t;
    }
   
    public String getDesc(){
        return this.desc;
    }
    public void setDesc(String d){
        this.desc = d;
    }
    public String getSex(){
        return this.sex;
    }
    public void setSex(String s){
        this.sex=s;
    }

    //toString method
    public String toString(){
       return String.format("Entry for: %s - Sex: %s\nAge: %d - Type: %s\n"+
                            "Potty-Trained? - %s - Description: %s", 
       this.name, this.sex, this.age, this.type, (this.pottyTrained ? "yes":"no"),this.desc);
    }
    
    
}
