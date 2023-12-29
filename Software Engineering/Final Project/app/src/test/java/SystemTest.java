import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.mockito.Spy;

import java.io.ByteArrayOutputStream;

import java.io.PrintStream;

import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

import reminder.backbone.Controller;
import reminder.backbone.CalendarData;
import reminder.backbone.Reminder;
import net.fortuna.ical4j.model.component.VEvent;

import reminder.Interfaces.ConstantsInterface;

/*
 * This will test the backbone of the program.
 * This test simulates the things done in App.java (a user simulation using the program).
 * App.java creates a controller object, read the .ics file and it will create calander data and populate the events.
 * we then print out the list of Original events (print(PRINTOG))
 * then we create a reminder and add it to the reminder list in calander data
 * then we print out the list of reminders (print(PRINTREMINDER)) using both prints is the same as testing print(PRINTALL)
 * 
 * The app then prints out the descriptions of the reminders, (this is what is printed during the desktop notification)
 * 
 * The testing of my gui was done while creating it, I did not use any testing framework for it.
 * 
 * The HelloFX class class and scene controllers are not tested as I could not figure out how to test them.
 * Run HelloFX to actually interact and use the application.
 * @author Kevin Portillo self-proclaimed coding wizard 🧙
 * 
 */

public class SystemTest implements ConstantsInterface{

   
    private Controller testController; // Reminder is actually a dependency of controller but we need to use the controller to make a reminder
    @Spy
    private CalendarData depCalendarData;


    private PrintStream oldOut;
    private ByteArrayOutputStream baos;
    
    private Reminder r1;// will be used to test the createReminder method

    

  
    private final String TESTDESCRIPTION = "This is a reminder that: HW 12 is due for the Class:\nSTT3850102-12022202340 (FALL 2023)";
    private final String TESTPATH = "..\\app\\src\\test\\resources\\icalexport (2).ics";
    private final int TESTEVENT = 5;
    private final int TESTDAYS = 3;

 
    @BeforeEach
    public void setup() {
        try{
            depCalendarData = Mockito.spy(new CalendarData(TESTPATH));
            testController = new Controller(depCalendarData);
              baos = new ByteArrayOutputStream();
                oldOut = System.out;
                System.setOut(new PrintStream(baos));
            getOutput();// some stuff is printed when calanderdata is read, clear it
        }catch(Exception e){
           
            fail("failure in setup", e);
        }
      
      

       

    }
        
        
    @Test
    public void testPrintOg(){
        testController.print(PRINTOG);
        assertEquals(PRINTOGRESULT, getOutput());
    }
    @Test
    public void testPrintReminder(){
        r1 = testController.createReminder(TESTEVENT, TESTDAYS);
        foo(r1);
        
        testController.print(PRINTREMINDER);
        assertEquals(PRINTREMINDERRESULT1, getOutput());
        
        r1 = testController.createReminder(TESTEVENT, TESTDAYS-1);
        foo(r1);

        testController.print(PRINTREMINDER);
        assertEquals(PRINTREMINDERRESULT1+PRINTREMINDERRESULT2, getOutput());
    }
    
    @Test
    public void detailsList(){
        //test the printV2 method

        r1 = testController.createReminder(TESTEVENT, TESTDAYS);
        foo(r1);
        r1 = testController.createReminder(TESTEVENT, TESTDAYS-1);
        foo(r1);

        ArrayList<String> details = testController.getDetailsListFromCalendarData(PRINTALL);
        assertNotNull(details);// means calendar data was populated with events and reminders
        printFromList(details);
        assertEquals(PRINTALLFROMLISTRESULT, getOutput());
    }

    private void printFromList(ArrayList<String> list){
        for(String s: list){
            System.out.println(s);
        }
    }
 
    @Test
    public void createAndAddReminder(){
        

       r1 = testController.createReminder(TESTEVENT, TESTDAYS);
        assertNotNull(r1);
        testController.addReminderToList(r1);
        assertEquals(1, testController.getReminderList().size());
        
        testController.addReminderToIcs(r1);
        verify(depCalendarData, times(1)).addReminder(any(VEvent.class));
        //make sure the same reminder event is the one placed into the calendar data
        assertEquals(r1.getReminderEvent(), depCalendarData.getReminderEvents().get(0));
        assertEquals(1, depCalendarData.getReminderEvents().size());
    }


    @Test
    public void duplicateReminderTest(){
        r1 = testController.createReminder(TESTEVENT, TESTDAYS);
        foo(r1);
        Reminder r2= testController.createReminder(TESTEVENT,TESTDAYS);
        assertTrue(testController.isDuplicateReminder(r2));
        assertTrue(r1.equals(r2) && r2.equals(r1));
    }  


    @Test
    public void testNukes(){
       
        r1 = testController.createReminder(TESTEVENT, TESTDAYS);
        foo(r1);
        Reminder r2= testController.createReminder(TESTEVENT,TESTDAYS-1);
        foo(r2);

        assertEquals(2, testController.getReminderList().size());
        assertEquals(2, depCalendarData.getReminderEvents().size());

        clearInvocations(depCalendarData);

        testController.nukeAllReminders();
            assertEquals(0, depCalendarData.getReminderEvents().size());
            assertEquals(0, testController.getReminderList().size());

        //there should be 6 events in the calendar data
        assertEquals(6, depCalendarData.getEvents().size());

        //create the 1 reminder again and add it to the list and calendar data
        r1 = testController.createReminder(TESTEVENT, TESTDAYS);
        foo(r1);
        assertEquals(7, testController.getReminderList().size()+ testController.getCalanderData().getEvents().size());
        assertEquals(7, testController.getCalanderData().getReminderEvents().size()+ testController.getCalanderData().getEvents().size());
        //then nuke the OG events
        clearInvocations(depCalendarData);
        testController.nukeOgEvents();
        verify(depCalendarData, times(1)).getEvents();
        assertEquals(1, testController.getReminderList().size());// one in the list of reminder objects
        assertEquals(1, testController.getCalanderData().getReminderEvents().size());// one in the calendar data
        //now print there only should be 1 reminder
        testController.print(PRINTREMINDER);
        assertEquals(PRINTREMINDERRESULT1, getOutput());
    }

    @Test
    public void testNoification(){
        r1 = testController.createReminder(TESTEVENT, TESTDAYS);
        foo(r1);
        r1 = testController.createReminder(TESTEVENT, TESTDAYS-1);
        foo(r1);

        testController.startReminders(testRun); // will schedule the reminders
        //if you change testRun to actualRun you will get a desktop notification but test will fail
        
        //wait 15 seconds to make sure both notifcations are printed 
        try{
            Thread.sleep(15000);
        }catch(Exception e){
            fail("exception thrown in testStartReminders", e);
        }

        assertEquals(TESTNOTIFICATIONS_STRING, getOutput());


    }
     
    public void foo(Reminder r){
        assertNotNull(r1);
        testController.addReminderToList(r1);
        testController.addReminderToIcs(r1);
        verify(depCalendarData, times(1)).addReminder(any(VEvent.class));
        clearInvocations(depCalendarData);
       


    }


    @AfterEach
    public void tearDown() {
        System.setOut(oldOut);
    }  

    private String getOutput(){
        System.out.flush();
        String outp = baos.toString().replaceAll("\r", "");
        baos.reset();
        return outp;
    }    

    

}