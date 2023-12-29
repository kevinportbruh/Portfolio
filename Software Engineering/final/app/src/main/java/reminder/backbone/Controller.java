package reminder.backbone;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;


import reminder.Interfaces.ReminderManagerInterface;
import reminder.Interfaces.TrayManger;
import reminder.Interfaces.ConstantsInterface;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File; 
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.*;





/*
 * This is the brains (or heart?) of the program.
 * This class will control most of the logic of the program.
 * this is the class that will be instantiated in the main method.
 * this is intended to be all we need to interact with to make our program do what we would like it to.
 * This class implements the reminderManagerInterface so it can schedule the reminders.
 * 
 * @author Kevin Portillo self-proclaimed coding wizard 🧙
 */
public class Controller implements ReminderManagerInterface, ConstantsInterface{
    //fields
    private CalendarData calendarData; 
    private List<Reminder> reminders; // this will hold all the reminders that the user wants to be reminded of

    //constructor
    //this class should print,import/export .ics', and ...
    public Controller(){
        this.calendarData = new CalendarData();
        this.reminders = new ArrayList<>();
    }
    
    
    public Controller(String path)throws IOException, ParserException, URISyntaxException{
  
        this.reminders = new ArrayList<>();
        this.calendarData = new CalendarData(readCalendar(path));
        calendarData.populateEvents();
    }

    public Controller(File file)throws IOException, ParserException, URISyntaxException{

        this.reminders = new ArrayList<>();
        this.calendarData = new CalendarData(readCalendar(file));
        //calendarData.populateEvents();
    }
    public Controller(CalendarData calendar){
        this.reminders = new ArrayList<>();
        this.calendarData = calendar;
        calendarData.populateEvents();
    }

    //methods


    public Reminder createReminder(int event, int daysB4){
        return new Reminder(calendarData.getEvent(event), daysB4);
    }
    /*
     * adds a reminder to the reminder list
     */
    public void addReminderToList(Reminder r){
       
        reminders.add(r);
    }
    
    /*
     * Takes the vevent from the reminder object and adds it to the calendarData <VEVENT> reminder list
     */
    public void addReminderToIcs(Reminder r){
        calendarData.addReminder(r.getReminderEvent());
    }
    
    /*
     * Takes the vevent from the reminder object and adds it to the calendarData <VEVENT> list
     */
    public void nukeAllReminders(){
        reminders.clear();
        calendarData.getReminderEvents().clear();
    }
    
    /*
     * Takes the vevent from the reminder object and adds it to the calendarData <VEVENT> list
     */
    public void nukeOgEvents(){
        calendarData.getEvents().clear();
    } 
    
    /*
     * this method reads the .ics file and sets the calendar field and populates the events list
     * can throw alot of exceptions so when called in main handle by making user reenter path/url
     * @param path - string - the path to the .ics file 
     * @return Calendar - the calendar object that was read from the .ics file
    */
    protected Calendar readCalendar(String icsInput) throws IOException, ParserException, URISyntaxException {
 
        CalendarBuilder builder = new CalendarBuilder();
        Calendar outCal = null;
        if (icsInput.startsWith("http")) {
            // If it starts with "http", treat it as a URL
            outCal = builder.build(new URI(icsInput).toURL().openStream());
        } else {
            // Otherwise, treat it as a local file
            try (FileInputStream fin = new FileInputStream(icsInput)) {
                outCal = builder.build(fin);
            }
        }
       
        return outCal; // now if outCal is null it wont crash 
    }

    //overloaded readCalendar method takes in a file (expected to be a .ics file)
    private Calendar readCalendar(File file) throws IOException, ParserException, URISyntaxException {
        CalendarBuilder builder = new CalendarBuilder();
        Calendar outCal = null;
        try (FileInputStream fin = new FileInputStream(file)) {
            outCal = builder.build(fin);
        }
        return outCal; // now if outCal is null it wont crash
    }

    /*
     * this method invokes the populateEvents method from the CalendarData class
     * it populates the events list in the CalendarData class
    */
    public void populateEvents(){
        
        calendarData.populateEvents();
    }   

    /*
     * this method should export the .ics file to the path specified by the user
     * @param path - string - the path to the .ics file 
    */
    public void exportCalender(String path){
      
        try{
            File file = new File(path, "newCal.ics");
            FileOutputStream fout = new FileOutputStream(file);
            CalendarOutputter outputter = new CalendarOutputter();

            outputter.output(calendarData.outputCalendar(), fout);
        }catch(Exception ex){
            System.out.println("Error exporting calender");
            ex.printStackTrace();
        }
    }
  
    /*
     * This method invokes the printEventFromList method from the CalendarData class
     * It prints the events to the console
    */
    public void print(int printType){

        if(printType == PRINTOG){
            calendarData.printEventFromList(calendarData.getEvents());
        }
        else if(printType == PRINTREMINDER){
            calendarData.printEventFromList(calendarData.getReminderEvents());
        }
        else if(printType == PRINTALL){
            calendarData.printEventFromList(calendarData.getEvents());
            calendarData.printEventFromList(calendarData.getReminderEvents());
        }
        else{
            System.out.println("Invalid print type");
        }
    }

    /*
     *  This method invokes the printV2 method from the CalendarData class
     *  it returns an arraylist that we can use to display info in the gui
     */
    public ArrayList<String> getDetailsListFromCalendarData(int printType){
        ArrayList<String> details = new ArrayList<String>();
        if(printType == PRINTOG){
            details = calendarData.printV2(calendarData.getEvents());
        }
        else if(printType == PRINTREMINDER){
            details = calendarData.printV2(calendarData.getReminderEvents());
        }
        else if(printType == PRINTALL){
            details = calendarData.printV2(calendarData.getEvents());
            details.addAll( calendarData.printV2(calendarData.getReminderEvents()));
        }
        else{
            System.out.println("Invalid print type");
        }
        return details;
    }

    /*
     * Starts the scheuling of the reminders through the ReminderManagerInterface
     */
    @Override
    public void startReminders(boolean test) {
       
       ReminderManagerInterface.startReminders(TrayManger.trayIcon, reminders, this, test);
    }

    public boolean isDuplicateReminder(Reminder rfake){
        for(Reminder r : reminders){
            if(r.equals(rfake)){
                return true;
            }
        }
        return false;
    }

    public void setCalanderData(CalendarData cd){
        this.calendarData = cd;
    }
    public CalendarData getCalanderData(){
        return this.calendarData;
    }
    public List<Reminder> getReminderList(){
        return reminders;
    }


}
