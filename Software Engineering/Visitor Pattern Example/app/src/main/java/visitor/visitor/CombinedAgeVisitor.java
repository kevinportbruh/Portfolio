package visitor.visitor;

import visitor.Datastruct.*;



/**
 * Visit every node and tell us what the combined age of all the nodes is.
 * Essentailly the answer to, how old would they be if they were akira'd into one person.
 * @author Kevin Portillo, self-proclaimed coding wizard 🧙‍♂️.
 */
public class CombinedAgeVisitor implements Visitor{

    private int combinedAge = 0;

    @Override
    public void visit(PersonNode personNode) {
        combinedAge += personNode.getAge();
    }

    @Override
    public void visit(PetNode petNode) {
        combinedAge += petNode.getAge();
    }

    public int getCombinedAge() {
        return combinedAge;
    }
    
}
