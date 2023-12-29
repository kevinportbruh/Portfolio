package visitor.visitor;

import visitor.Datastruct.*;

/**
 * a vistor that prints information about what ever node it visits
 * @author Kevin Portillo, self-proclaimed coding wizard 🧙‍♂️.
 */
public class PrintTreeVisitor implements Visitor {

    @Override
    public void visit(PersonNode personNode) {
        System.out.println("==========Person Node========== ");
        System.out.println("Name: " + personNode.getName() + ", Age: " + personNode.getAge());
        System.out.println("Prev Person: "
                        + (personNode.getPrev() == null ? "No one" : ((PersonNode)personNode.getPrev()).getName()) 
            +" , Next Person: " 
                        + (personNode.getRightNode() == null ? "No one" : ((PersonNode)personNode.getRightNode()).getName()));
        System.out.println("---My pets are---");
    }

    @Override
    public void visit(PetNode petNode) {
        System.out.println("Pet Name: " + petNode.getPetName()
             + ", Age: " + petNode.getAge()+", My Owner: "+petNode.getOwner().getName());
        //print left and right childs if any
        System.out.println("\nV(prev, left, right nodes respectively)V\n\nMy siblings:  "
                        + (petNode.getPrev() instanceof PersonNode ||petNode.getPrev() == null ? "null" : ((PetNode)petNode.getPrev()).getPetName()) 
            +",  "     
                        + (petNode.getLeftNode() == null ? "null" : ((PetNode)petNode.getLeftNode()).getPetName())
            +",  "     
                        + (petNode.getRightNode() == null ? "null" : ((PetNode)petNode.getRightNode()).getPetName()));
                        System.out.println("------");
             
    }
    
}
