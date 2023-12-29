package visitor.visitor;
import visitor.Datastruct.PersonNode;
import visitor.Datastruct.PetNode;
/**
 * Visitor interface
 * @author Kevin Portillo, self-proclaimed coding wizard 🧙‍♂️.
 */
public interface Visitor {
    public void visit(PersonNode a);
    public void visit(PetNode b);
}

