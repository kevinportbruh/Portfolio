package reminder;
import net.fortuna.ical4j.data.ParserException;
import reminder.backbone.CalendarData;
import reminder.backbone.Reminder;
import reminder.backbone.Controller;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.application.Platform;
import reminder.Interfaces.ConstantsInterface;
import reminder.Interfaces.TrayManger;
/*
 * this was the class I used to test that the backbone of the program works.
 * this will print out the calander data and add a reminder to the calander data and print it.
 * @author Kevin Portillo self-proclaimed coding wizard🧙
 */
public class App implements ConstantsInterface, TrayManger{


    public static void main(String[] args) {
       
        //calander data object  
        Reminder r1;
        // Get the path or URL to the .ics file
        System.out.print("Enter the path or URL to the .ics file: ");
        //String icsInput = kb.nextLine();
      
        //we can use this try block in a junit test for a print calander method eventually
        try {

            Controller c1 = new Controller(TESTPATH);
            CalendarData calendarData = new CalendarData(TESTPATH);
            //Controller c1 = new Controller("./app/src/test/resources/icalexport (2).ics");
            //calendarData.populateEvents(); should be called in the constructor


            
            // create a reminder for event 5, make it 3 days before the event
            r1 = c1.createReminder(5, 3);
            //r1 = new Reminder(cd1.getEvent(5), 1);

            // add to the reminder list in calander data
            c1.print(PRINTOG);
            c1.addReminderToList(r1);
            c1.addReminderToIcs(r1);
            Reminder r2= c1.createReminder(5,2);
            //
    
            // 

            c1.print(PRINTREMINDER);
            System.out.println(r1.notifyUser());
            c1.addReminderToList(r2);
            c1.addReminderToIcs(r2);
            c1.print(PRINTREMINDER);

            

            r2 = new Reminder(c1.getCalanderData().getEvent(5), 3);
            System.out.println("is duplicate reminder: " + c1.isDuplicateReminder(r2));
            System.out.println("is r equal to r2: " + r1.equals(r2));
            System.out.println(c1.getCalanderData().getEvents().size() + " " + c1.getCalanderData().getReminderEvents().size()); // should be 6 and 1

            c1.startReminders(testRun);
            // we should verify start called scheduleReminderTest() 1 time
            // also very if that the reminder was printed to the console
            c1.exportCalender(TESTOUTPATH);
            //should assert that the file was created at the path specified
            //verify that calanderData.outputCalender() was called 1 time in in this method.

            for(String s: c1.getDetailsListFromCalendarData(PRINTALL)){
                System.out.println(s);
            }

            TrayManger.createTray();// COMMENT THIS TO STOP THE DESKTOP NOTIFICATIONS
            c1.startReminders(testRun);

            //c1.startReminders(actualRun);



            try{
                Thread.sleep(30000);
            }
            catch(InterruptedException e){
                System.out.println("got interrupted!");
            }
            System.exit(0);
            
            //exports a calander to the path specified
            //c1.exportCalender(TESTOUTPATH);
            
        } catch (IOException | ParserException | URISyntaxException e) {
            //System.out.println("Error, enter 1 to reenter valid path or URL to the .ics file or any number to exit");
            //
           
                e.printStackTrace();
       
           
            
        }
    }

   

}
    

