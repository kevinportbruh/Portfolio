# Software Engineering Work
This section of my portfolio showcases my software engineering projects and highlights my skills and expertise in the field. It provides an overview of the projects I have worked on, the technologies I have used, and the impact they have made. Explore this section to gain insights into my software engineering journey and the value I can bring to future projects.


(This is a brief overview of each project, more in-depth information on each project will be avalible in their respective page)


# Advanced Assginment Reminder App (Final Project)
This program was a solo semester long project for my Software Engineering class.
This was my first attempt at making a GUI with UX in mind, I am very proud of the way this program turned out.

The prompt we were given was simply to 'make something', So I began to think of something useful to make. 
I then decided to work on an assignment reminder app; the app will let the user upload their school E calendar in the form of a .ics file and allow the user to select specific events that they would like to be reminded of before the assignment is due.

The program uses Java to implement the backend logic. I wrote all the code for this program with Software Design Principles in mind. It uses the Ical4 API to parse .ics files. I then used Scenebuilder to create the GUI layout. This is done with JavaFX and produces JavaFX FXML files, Scenebuilder then provides skeleton class files which allow for simple implementaion of button logic/event handling. I also used Junit & Mockito for system testing. 

The application was designed for the assignment notification system at Appalaichain state university. It is fully functional and can be toyed with by using the provided test .ics file.

(This was a very fun project that I used to test my limits, please feel free to check out more indepth information about this one in the README)

# Vistor Pattern 
This was a short assignment but one I enjoyed to work on. This was for an assignment to apply my knowledge of the visitor/iterator software desgin pattern. The only constraints for this assignment was to create any data structure from scratch, then correctly apply the vistor/iterator desgin pattern twice. 

I had some fun with this and created a Binary tree from scratch but this worked with a few twists. 

-The Data Structure
The tree is a family tree, where the root node is always a Person. Any left subtree of a Person node are all of the Pets that Person has. Any node to the right of a Person node must be the next Person in the family.

-The pattern
The Iterator pattern was applied by using Depth First Search with pre-order traversal. The two iterators are dfsIterator which iterates through the ENTIRE tree, the second is petIterator, which also iterates through the tree with pre-ordered DFS but only considers the Pet Nodes as valid ones to visit.

The Vistor pattern was applied in the PrintTreeVisitor & CombinedAgeVisitor classes. Both vistors accept both types of nodes; The Printing visitor prints information that is formatted for each type of node. Since we are using preorder DFS, when run using the dfsIterator, it prints the Person node then all of their pets before moving on to the next person. The CombinedAgeVistor adds the age of each node to a counter for display after the iterator is done.

The program is fully functional with detailed comments that make it easy to follow al.ong. Try running it and follow the comments in demo.java. Run it in the terminal with ./gradlew run