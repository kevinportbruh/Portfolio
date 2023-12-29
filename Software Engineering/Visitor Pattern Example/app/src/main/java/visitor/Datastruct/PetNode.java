package visitor.Datastruct;

import visitor.visitor.Visitor;

/**
 * Pet Node class
 * has pet name and age
 * and owner (PersonNode)
 * 
 * @author Kevin Portillo, self-proclaimed coding wizard 🧙‍♂️.
 */

public class PetNode extends Node {
    
    private String petName;
    private int age;
    private PersonNode owner;
    
    //constructor

    public PetNode(Node prev, Node rightNode, Node leftNode, String petName, int age) {
        super(prev, rightNode, leftNode); //super class constructor (Node
        this.petName = petName;
        this.age = age;
        owner = null;
    }

    //setters and getters

    public String getPetName() {
        return petName;
    }
    public void setPetName(String petName) {
        this.petName = petName;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public PersonNode getOwner() {
        return owner;
    }
    public void setOwner(PersonNode owner) {
        this.owner = owner;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
    
}
