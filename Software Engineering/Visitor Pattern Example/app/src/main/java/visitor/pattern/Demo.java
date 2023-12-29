package visitor.pattern;

import visitor.Datastruct.*;
import visitor.Iterator.*;
import visitor.visitor.*;
/*
 * demo for the visitor pattern
 * everything is explained in the start method block comment and as it goes along
 * some test prints are commented out but can be uncommented to see what is going on
 * comments pretty much detail everything.
 * @author Kevin Portillo, self-proclaimed coding wizard 🧙‍♂️.
 */
public class Demo {



    
    /* the tree should look like after insertions
    *    see BST.java for the rules this specific tree follows
    *s
    *             Kevin
    *           /      \
    *      Nigel            Cloned Kevin
    *       / \              /         \
    *   Grof   Yammei   Cloned grof     Good Kevin
    *    /                              /           \
    *   King Jeff III                          Good grof      Evil Kevin       
    *                               
    *                                             
    */
    // method which just inserts elements into the tree
    public static BST start(){
        BST bst = new BST();
        PersonNode p1 = new PersonNode(null, null, null, "Kevin", 21);
        PersonNode p2 = new PersonNode(null, null, null, "Cloned Kevin", 21);
        PersonNode p3 = new PersonNode(null, null, null, "Good Kevin", 19);
        PersonNode p4 = new PersonNode(null, null, null, "Evil Kevin", 22);

        
        PetNode pet1 = new PetNode(null, null, null, "Nigel", 13);
        pet1.setOwner(p1); //nigel is kevin's pet

        PetNode pet2 = new PetNode(null, null, null, "Cloned Grof", 2);
        pet2.setOwner(p2); //cloned grof is cloned kevin's pet but will be inserted at after some other person
       
        PetNode pet3 = new PetNode(null, null, null, "Grof", 2);
        pet3.setOwner(p1); //grof is kevin's pet

        PetNode pet4 = new PetNode(null, null, null, "Good Grof", 2); // will be good kevins pet but will not be given an owner 
        //pet4 will be inserted directly after good kevin
        PetNode pet5 = new PetNode(null, null, null, "Evil Grof", 2);
        PetNode pet6 = new PetNode(null, null, null, "Yammei", 2);
        pet6.setOwner(p1);//yammei is kevin's pet
        PetNode pet7 = new PetNode(null, null, null, "King Jeff III", 2);
        pet7.setOwner(p1);//king jeff iii is kevins pet
        
        //total age of every one is 106


        //inserting elements
        bst.insert(p1); //kevin root
        bst.insert(pet1);//nigel<3
        
       // bst.insert(pet2); //test insert//cloned grof -owner not in list yet so will not be inserted should get print statment
       
        bst.insert(p2); //cloned kevin - kevins right node 
        bst.insert(pet2);// owner in list so will be inserted - cloned grof should be inserted under cloned kevin's left tree

        bst.insert(p3); //good kevin - cloned kevins right node
        bst.insert(pet4);//should assign good grof to good kevin because good kevin is the last person inserted

       // System.out.println(pet4.getOwner().getName()); //should print good kevin 

        bst.insert(p4); //evil kevin - good kevins right node
        bst.insert(pet5); //evil grof - evil kevin should be 'lastPerson' so evil grof should be inserted under evil kevin
        bst.insert(pet3); //grof - kevins pet should be in kevins left subtree
       
       // System.out.println(pet5.getOwner().getName()); //should print evil kevin 

        bst.insert(pet6); //yammei - kevins pet should be in kevins left subtree
        bst.insert(pet7); //king jeff iii - kevins pet should be in kevins left subtree

        return bst;

    }
    


    public static void main(String[] args) {
        //bst and a few elements
        BST bst =start();
        
        //print tree using the dfs iterator and the print tree visitor
        CombinedAgeVisitor combinedAgeVisitor = new CombinedAgeVisitor();
        PrintTreeVisitor printTreeVisitor = new PrintTreeVisitor();
        Iterator dfsIterator = bst.getDfsIterator();
        Iterator petIterator = bst.getPetIterator();

        // use both vistors with dfs iterator
                //print combined age of all nodes
            while (dfsIterator.hasNext()) {
                Node node = (Node) dfsIterator.next();
                node.accept(combinedAgeVisitor);
                node.accept(printTreeVisitor);
            }

            int combinedAgeAll = combinedAgeVisitor.getCombinedAge();
    
            System.out.println("If everything being in our tree was Akira'd into one person, their age would be: "
                        + combinedAgeAll);    

            System.out.println("\n\n\n==================DONE WITH DFS ITERATOR NOW PET ONLY ITERATOR - CLEAR YOUR EYES THE FOLLOWING IS USING PET ITERATOR==================\n\n\n");
           

         
        //use both vistors with pet iterator
            //print combined age of all nodes (should only be pets)
            //print only pets
            //reseting combined age visitor
           combinedAgeVisitor = new CombinedAgeVisitor();
            while(petIterator.hasNext()){
                Node node = (Node) petIterator.next();
                node.accept(combinedAgeVisitor);
                node.accept(printTreeVisitor);
            }
            int combinedAgePet = combinedAgeVisitor.getCombinedAge();

            System.out.println("If everything pet in our tree was Akira'd into one person, their age would be: "
                        + combinedAgePet);
       
            
        
        //we can figure out combined age of all people by doing combinedAgeAll - combinedAgePet
         System.out.println("If everything person in our tree was Akira'd into one person, their age would be: "
                        + (combinedAgeAll - combinedAgePet));


        //the tree should've organized as shown above the start() method
        //total age should be 108 = 21+21+19+22+13+2+2+2+2+2+2, 
        //total pet age should be 25 = 13+2+2+2+2+2+2, 
        //total person age should be 83 = 21+21+19+22 but since no person iterator we have to do combinedAgeAll - combinedAgePet

        System.out.println("the thing works....");

        
        
    }
}
