package visitor.Datastruct;

import visitor.visitor.Visitor;

/*
 * Person Node class
 * has name and age
 * 
 * @Kevin Portillo, self-proclaimed coding wizard 🧙‍♂️.
 */

public class PersonNode extends Node{

   
    private String name;
    private int age;

    //constructor

    public PersonNode(Node prev, Node rightNode, Node leftNode, String name, int age) {
        super(prev, rightNode, leftNode);
        
        this.name = name;
        this.age = age;
    }


    //setters and getters

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age){
        this.age = age;
    }

    
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
    
}
