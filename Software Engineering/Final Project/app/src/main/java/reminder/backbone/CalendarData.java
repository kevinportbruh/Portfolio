package reminder.backbone;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*; 
import java.time.*; 

import reminder.Interfaces.ConstantsInterface;

import java.time.format.FormatStyle;
import java.time.format.DateTimeFormatter;
/*
 * This class will hold all the data we read from the .ics file
 * @author Kevin Portillo self-proclaimed coding wizard 🧙
 */
public class CalendarData implements ConstantsInterface {
 
    
    //fields
    private Calendar calendar;// the .ics file in object form
    private HashMap<Integer, VEvent> events; // the events in the .ics file int lets us get event by index
    private HashMap<Integer, VEvent> reminderEvents; // this will be the reminderEvent that is y days before x event, this is for .ics export not the task scheduler (for desktop notifications)
    private String path; // the path to the .ics file (does this need to be in this class?)
    
    //constructor
    /*
     * Default constructor, this will be called first before taking in inputs to set up everything
     */
    public CalendarData(){
        this.calendar = null;
        this.events = new HashMap<Integer, VEvent>();
        this.reminderEvents = new HashMap<Integer, VEvent>();
        this.path = null;
    }

    /*
     * constructor that takes in a calander object
     * used for testing
     */
    public CalendarData(Calendar calendar){
        this.calendar = calendar;
        this.events = new HashMap<Integer, VEvent>();
        this.reminderEvents = new HashMap<Integer, VEvent>();
        this.path = null;
        populateEvents();
    }
    /*
     * this is the constructor that will be used in the main method
     */
    public CalendarData(String path)throws IOException, ParserException, URISyntaxException{
        this.calendar = readCalendar(path);
        this.events = new HashMap<Integer, VEvent>();
        this.reminderEvents = new HashMap<Integer, VEvent>();
        this.path = null;
        populateEvents();
        
    }
    

    //this should only only worry about calander data, print, adding events, etc.


    //methods
    
    //printing from list
    protected void printEventFromList(HashMap<Integer, VEvent> e){
        try{
            for (Map.Entry<Integer, VEvent> entry : e.entrySet()) {
                int key = entry.getKey();
                VEvent event = entry.getValue();

                System.out.println("-----EVENT #" + key + "-----");
                // Class of the assignment
                event.getCategories().ifPresent(categories ->
                    System.out.println("Class: " + categories.getValue())); 
                //name of assignment
                event.getSummary().ifPresent(summary  ->
                    System.out.println("Title: " + summary.getValue()));

                //due date prints like this "Nov 20, 2023, 8:00:00 AM"
                event.getDateTimeEnd().ifPresent(dateTime -> {
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTime.getValue(), INPUTFORMATTER).withZoneSameInstant(ZoneId.of("America/New_York"));                       
                    System.out.println("Due Date : " + DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM).format(zonedDateTime));
                });

                System.out.println("-----------------------");
            }
        }catch(Exception ex){
            System.out.println("Error printing events");
            ex.printStackTrace();
        }
      
    }

    //print method which returns a list of strings instead of printing to console
   
    protected ArrayList<String> printV2(HashMap<Integer, VEvent> e){
        ArrayList <String> list = new ArrayList<String>();
        
        try{
            for (Map.Entry<Integer, VEvent> entry : e.entrySet()) {
                final StringBuilder stringBuilder = new StringBuilder();
                int key = entry.getKey();
                VEvent event = entry.getValue();

                if(e == this.events){
                    stringBuilder.append("Original");
                }
                else{
                    stringBuilder.append("Reminder");
                }
                
              
                String temp = (" EVENT # " + key + "\n");
                stringBuilder.append(temp);
                // Class of the assignment
                event.getCategories().ifPresent(categories ->
                    stringBuilder.append("Class: " + categories.getValue()+ "\n")); 
                //name of assignment
                event.getSummary().ifPresent(summary  ->
                    stringBuilder.append("Title: " + summary.getValue()+ "\n"));

                //due date prints like this "Nov 20, 2023, 8:00:00 AM"
                event.getDateTimeEnd().ifPresent(dateTime -> {
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTime.getValue(), INPUTFORMATTER).withZoneSameInstant(ZoneId.of("America/New_York"));                       
                    stringBuilder.append("Due Date : " + DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM).format(zonedDateTime));
                });
                
                list.add(stringBuilder.toString());
                //System.out.println(stringBuilder.toString());
                
            }
        }catch(Exception ex){
            System.out.println("Error printing events");
            ex.printStackTrace();
        }
        return list;
    }
 
    /*
     * this is a helper method that populates the events list from the calendar object
     * this should only be called after the calendar object is set and will not add duplicate events.
     */
    protected void populateEvents(){
        int i = 0;
        for (Object o : this.calendar.getComponents()) {
            if(events.containsValue(o)){
                continue;
            }
            VEvent event = (VEvent) o;
            this.events.put(i, event);
            i++;
        }
    }  





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

   




    //helper method for exportCalender() in controller.java which creates a new calender object with the reminder events and og events
    public Calendar outputCalendar(){
        Calendar outCal = new Calendar();
        if (this.events != null) {
            for (VEvent event : this.events.values()) {
                outCal.add(event);
            }
        }
    
        if (this.reminderEvents != null) {
            for (VEvent event : this.reminderEvents.values()) {
                outCal.add(event);
            }
        }
        return outCal;
    }
    




    //getters and setters
    //for list
    public void addReminder(VEvent event){
        this.reminderEvents.put(this.reminderEvents.size(), event);
    }


    public void removeReminder(VEvent event, int index){
        this.reminderEvents.remove(index);
    }
    
    public HashMap<Integer, VEvent> getEvents(){
        return this.events;
    }
    
    public void setEvents(HashMap<Integer, VEvent> events){
        this.events = events;
    }

    public HashMap<Integer, VEvent> getReminderEvents(){
        return this.reminderEvents;
    }

    public void setReminderEvents(HashMap<Integer, VEvent> reminderEvents){
        this.reminderEvents = reminderEvents;
    }


    //for map of x events
    public VEvent getReminder(int key){
        return this.reminderEvents.get(key);
    }
    public VEvent getEvent(int key){
        return this.events.get(key);
    }


    //f stands for field getCalander is a method in the calander class
    public Calendar getCalendarF(){
        return this.calendar;
    }    
    public void setCalendarF(Calendar calendar){
        this.calendar = calendar;
    }
    public String getPath(){
        return this.path;
    }
    public void setPath(String path){
        this.path = path;
    }
    
}
