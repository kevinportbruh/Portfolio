package visitor.Iterator;

/*
 * container interface
 * need two methods for 2 types of iterators
 * @author Kevin Portillo, self-proclaimed coding wizard 🧙‍♂️.
 */

public interface Container {
    public Iterator getDfsIterator();

    public Iterator getPetIterator();
    
}
