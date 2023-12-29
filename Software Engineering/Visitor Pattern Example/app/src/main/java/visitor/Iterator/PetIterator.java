package visitor.Iterator;

import java.util.Stack;

import visitor.Datastruct.*;


/**
 * this class will only iterate through the left sub trees of all parent nodes
 * DFS in preorder traversal ignoring any parent nodes
 * @author Kevin Portillo, self-proclaimed coding wizard.
 */
public class PetIterator implements Iterator{
    private Stack<Node> stack = new Stack<>();

    public PetIterator(Node root) {
        if (root != null) {
            stack.push(root);
        }
    }


    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public Node next() {
        while (hasNext()) {
            Node currentNode = stack.pop();

            // Skip parent nodes (PersonNode) and return child nodes (PetNode)
            if (currentNode instanceof PetNode) {
                // Add right node to the stack first (if exists)
                if (currentNode.getRightNode() != null) {
                    stack.push(currentNode.getRightNode());
                }
                // Add left node to the stack (if exists)
                if (currentNode.getLeftNode() != null) {
                    stack.push(currentNode.getLeftNode());
                }

                return currentNode;
            } else {
                // Skip parent nodes and continue to the next node
                // This loop will continue until a child node (PetNode) is found
                if (currentNode.getRightNode() != null) {
                    stack.push(currentNode.getRightNode());
                }
                if (currentNode.getLeftNode() != null) {
                    stack.push(currentNode.getLeftNode());
                }
            }
        }

        return null; // No more child nodes to visit
    }
}

