package visitor.Iterator;
import java.util.Stack;

import visitor.Datastruct.*;

/**
 * CRAZY GOOD DFS ITERATOR
 * @author Kevin Portillo, self-proclaimed coding wizard 🧙‍♂️.
 */
public class DFS implements Iterator {
    private Stack<Node> stack = new Stack<>();

    public DFS(Node root) {
        if (root != null) {
            stack.push(root);
        }
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public Object next() {
        if (!hasNext()) {
            return null; // No more elements to visit, return null or handle accordingly
        }

        Node currentNode = stack.pop();

        if (currentNode.getRightNode() != null) {
            stack.push(currentNode.getRightNode());
        }
        if (currentNode.getLeftNode() != null) {
            stack.push(currentNode.getLeftNode());
        }

        return (Node) currentNode;
    }
}