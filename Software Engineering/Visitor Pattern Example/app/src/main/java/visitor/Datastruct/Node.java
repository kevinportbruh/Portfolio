package visitor.Datastruct;

import visitor.visitor.*;

/*
 * GENERIC NODE CLASS
 * two children and one parent
 * bst node
 * 
 * @author Kevin Portillo, self-proclaimed coding wizard 🧙‍♂️.
 */
public class Node {
    private Node prev; // parent node
    private Node rightNode; // sibling node
    private Node leftNode; // child node

    //constructor
    public Node(Node prev, Node rightNode, Node leftNode) {
        this.prev = prev;
        this.rightNode = rightNode;
        this.leftNode = leftNode;
    }
    //getters adn setters
    public Node getPrev() {
        return prev;
    }
    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getRightNode() {
        return rightNode;
    }
    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    public Node getLeftNode() {
        return leftNode;
    }
    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }

    //overwrite in class
    public void accept(Visitor v){
        
    }
    
}
