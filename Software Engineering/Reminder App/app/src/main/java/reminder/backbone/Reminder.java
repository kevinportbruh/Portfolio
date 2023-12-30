package reminder.backbone;

//change imports later
import net.fortuna.ical4j.model.component.VEvent;
import java.time.*; 
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import reminder.Interfaces.ConstantsInterface;


/*
 * This class will hold the reminder data that the user requested to be reminded of.
 * This class will be controlled by other classes and will be used moreso as items in a list.
 * 
 * Here are some rules to this class for logic:
 * the due date for the reminder VEvent will be formatted like this: (the original event is at date x)
 * the reminder will be due at date x - y days
 * the event start time will be at 6 am, the event end time AND the event due time will be at 1pm 
 *                  ^ This is so ASUlearn can notify the user of the reminder at noon (this assumes they have their notifcations set to 1 hour before the assignment is 'due')
 * these are DTSTART and DTEND (they are the same date in the .ics file)        
 * 
 * @author Kevin Portillo self-proclaimed coding wizard 🧙
 */
public class Reminder implements ConstantsInterface {
    //fields
    private VEvent ogEvent; // the original event that the user wants to be reminded of gotten from the CalendarData class in later logic
    private VEvent reminderEvent; // the event created that represents the reminder
    private ZonedDateTime reminderDate; // the date of the reminder event (this is the due date minus the requested amount of time)
    private Temporal ogStartTime; 
    private String description = "";
    private int daysB4;
    
   

    //constructor
    public Reminder(){
        this.ogEvent = null;
        this.reminderEvent = null;
        this.reminderDate = null;
        daysB4 = 0;
    }

    public Reminder(VEvent ogEvent){
        this.ogEvent = ogEvent;
        this.reminderEvent = null;
        this.reminderDate = null;
        daysB4 = 0;
    }

    //create a new reminder based off of an event and the amount of days before the event the user wants to be reminded
    public Reminder(VEvent ogEvent, int daysB4){
        this.ogEvent = ogEvent;
        this.reminderEvent = null;
        this.reminderDate = null;
        this.daysB4 = daysB4;
        createVEvent(daysB4);
    }

    //methods


    public String notifyUser(){
        return description;
    }


    //this is called after ogEvent is populated
    //takes the og event and creates a reminder event from it
    //uses reminderDate to set the end time of the reminder event
    //the Summary will be the summary of the og event + "Reminder"
    //the description will be "have have an assignment due in x days"
    //everything else is the same as the og event 
    public void createVEvent(int daysB4) {
        StringBuilder descB = new StringBuilder();
        descB.append("This is a reminder that: ");


        ogEvent.getDateTimeEnd().ifPresent(dateTime -> {
            this.reminderDate = ZonedDateTime.parse(dateTime.getValue(), INPUTFORMATTER).withHour(18).minusDays(daysB4+1); // set time to 18 utc to come out to 1pm est
        });
        ogEvent.getDateTimeStart().ifPresent(dateTime -> {
            this.ogStartTime = dateTime.getDate().minus(daysB4, ChronoUnit.DAYS);

        });  
        String[] summaryHolder = new String[1]; // Array to hold the summary value
        ogEvent.getSummary().ifPresent(sum -> summaryHolder[0] = sum.getValue());
        String summary = summaryHolder[0] + " Reminder";

        descB.append(summaryHolder[0]);
        descB.append(" for the Class:\n");
        ogEvent.getCategories().ifPresent(categories -> descB.append(categories.getValue()));
        
        Temporal endTime = reminderDate;
        this.reminderEvent = new VEvent(ogStartTime, endTime, summary);
        setDescription(descB.toString()); // this will be the message displayed for the notifcation
    }

    public void setOgEvent(VEvent ogEvent){
        this.ogEvent = ogEvent;
    }
    public VEvent getOgEvent(){
        return this.ogEvent;
    }
   
    public void setReminderEvent(VEvent reminderEvent){
        this.reminderEvent = reminderEvent;
    }
    public VEvent getReminderEvent(){
        return this.reminderEvent;
    }
   
    public void setReminderDate(ZonedDateTime reminderDate){
        this.reminderDate = reminderDate;
    }
    public ZonedDateTime getReminderDate(){
        return this.reminderDate;
    }
    
    public void setOgStartTime(Temporal ogStartTime){
        this.ogStartTime = ogStartTime;
    }
    public Temporal getOgStartTime(){
        return this.ogStartTime;
    }

    public void setDescription(String description){
        this.description = description;
    }
   
    public int getDaysB4(){
        return this.daysB4;
    }
    
    /**
     * This method will compare two reminder objects and return true if they are equal and false if they are not
     * comparasion is based off of the ogEvent and the daysB4 
     * @param r
     * @return boolean True -same event and same daysB4, false if not
     */
    public boolean equals(Reminder r){
        if(this.ogEvent.equals(r.getOgEvent()) && this.daysB4==r.getDaysB4()){
            return true;
        }
        else{
            return false;
        }
    }
}  
