package visitor.Datastruct;


import visitor.Iterator.Container;
import visitor.Iterator.DFS;
import visitor.Iterator.Iterator;
import visitor.Iterator.PetIterator;

/**
 * This class is the goofiest "binary search tree" I've ever typed up 😭😭😭
 * follows my special rules for the tree:
 * root node is always a PersonNode (cannot place petNode as root)
 * all right children of PersonNodes are PersonNodes
 * all left sub trees of PersonNodes are PetNodes
 * any child of petNode is pet of the last parent PersonNode it branched from
 *     
 * 
 *     root: PersonNode
 *          /         \
 *     PetNode     PersonNode
 *       /         /         \
 *      PetNode  PetNode   PersonNode
 * 
 * 
 * @author Kevin Portillo, self-proclaimed coding wizard 🧙‍♂️.
 */
public class BST implements Container {
    
    private Node root; // The root node of the tree.
    private PersonNode lastPerson;

    public BST() {
        this.root = null;
        this.lastPerson = null;
    }
    
    @Override
    public Iterator getDfsIterator() {
        return new DFS(root);
    }
    

    @Override
    public Iterator getPetIterator() {
        return new PetIterator(root);
    }



    public void insert(Node node) {
        if (this.root == null) {
            if (node instanceof PersonNode) {
                this.root = node;
                this.lastPerson = (PersonNode) node;
            } else {
                System.out.println("Root node must be a PersonNode.");
            }
        } else {
            insert(root, node);
        }
    }

    private void insert(Node current, Node newNode) {
        //new node is a person
        //current node is a person
        // new node goes to the right of current node
        //can order by person age later
        if (newNode instanceof PersonNode) {
            if (current.getRightNode() == null) {
                current.setRightNode(newNode);
                newNode.setPrev(current);
                this.lastPerson = (PersonNode) newNode;
            } else {
                insert(current.getRightNode(), newNode);
            }
            
        } else if (newNode instanceof PetNode) { // new node is a pet
            if (current instanceof PersonNode) { // current node is a person
                //make sure the pet is under the correct person
                //no owner? make last person the owner
                if (((PetNode) newNode).getOwner() == null) {
                    ((PetNode) newNode).setOwner(this.lastPerson);
                }
                
                while(((PetNode) newNode).getOwner() != current){ // while the pet is not under the correct person
                    current=current.getRightNode();//go to the next person till you find the correct person
                    if(current==null){
                        System.out.println("Person not found");
                        System.out.println("not inserted");
                        return;
                    }
                }
                //if here then the pet is under the correct person
                //insert it into this current person's left sub tree at the next available spot
                if (current.getLeftNode() == null) {
                    current.setLeftNode(newNode);
                    newNode.setPrev(current);
                } else {
                    insert(current.getLeftNode(), newNode);
                }
                
            } else {
               //current node is a pet insert pet to next avalible spot left or right
                if (current.getLeftNode() == null) {
                    current.setLeftNode(newNode);
                    newNode.setPrev(current);
                } else if (current.getRightNode() == null) {
                    current.setRightNode(newNode);
                    newNode.setPrev(current);
                } else {
                    insert(current.getLeftNode(), newNode);
                }

            }
        }
    }


    

    
}
