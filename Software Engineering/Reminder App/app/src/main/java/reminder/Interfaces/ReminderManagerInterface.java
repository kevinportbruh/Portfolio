package reminder.Interfaces;

import java.awt.TrayIcon;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import java.util.concurrent.TimeUnit;

import reminder.backbone.Controller;
import reminder.backbone.Reminder;

import java.util.*;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.ChronoUnit;
/*
 * this class manages the scheduling of the reminders
 * does not need to be instantiated
 * @author Kevin Portillo self-proclaimed coding wizard🧙
 */
public interface ReminderManagerInterface {
    
    static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
     
    //call these methods in the controller class 
    void startReminders(boolean test);

    //these will be called by the ones above (controller was getting too messy)
    /*
     * loop through reminders and schedule them
     * we pass controller so we know when to stop the scheduler
     */
    public static void startReminders(TrayIcon trayIcon, List<Reminder> reminders, Controller c1, boolean test) {
        int count = reminders.size();// count of the reminders actually scheduled
        // count is only important if 0 cuz then none of the reminders were scheduled so we should end the program
        // count will be 0 if every reminder is scheduled for a time in the past
      
        for (Reminder reminder : reminders) {
            if(test){
                count += scheduleReminderTestMethod(reminder, trayIcon, c1, count);
            }
            else{
                count += scheduleReminder(reminder, trayIcon, c1, count);
            }
          
        }
        //System.out.println(count);
        checkReminders(trayIcon, c1, count, test);
    }

    /*
     * This method schedules the reminder to be sent
     *  THIS METHOD CAN AND WILL END THE PROGRAM AND SHOULD - WILL ONLY BE CALLED WHEN ALL REMINDERS HAVE BEEN SENT
     */
    public static int scheduleReminder(Reminder reminder, TrayIcon trayIcon, Controller c1, int countOfScheduledReminders) {
        
        ZonedDateTime now = ZonedDateTime.now(); // current time
        long initialDelay = now.until((Temporal)reminder.getReminderDate(), ChronoUnit.HOURS);
        
        //long initialDelay = 5; // print in 12
        if (initialDelay > 0) {
            ScheduledFuture<?> future = scheduler.schedule(() -> {
                //System.out.println("reminder sent");
            
                trayIcon.displayMessage(TrayManger.TITLE, reminder.notifyUser(), TrayIcon.MessageType.INFO);
                c1.getReminderList().remove(reminder); 

            }, initialDelay, TimeUnit.HOURS);//change to hours
        
        }
        else{
           
            return -1;
        }

        return 1;
     
    }
    /*
     * Overloaded test method for scheduling reminders
     * will print to console instead of sending a desktop notification
     */
    public static int scheduleReminderTestMethod(Reminder reminder, TrayIcon trayIcon, Controller c1, int countOfScheduledReminders) {
        
        //ZonedDateTime now = ZonedDateTime.now(); // current time
        //long initialDelay = now.until((Temporal)reminder.getReminderDate(), ChronoUnit.HOURS);

        int count = 2; // just to spread out the reminders a bit
        long initialDelay = 5; // print in 12
        if (initialDelay > 0) {
            ScheduledFuture<?> future = scheduler.schedule(() -> {
                System.out.println(reminder.notifyUser());
                trayIcon.displayMessage(TrayManger.TITLE, reminder.notifyUser(), TrayIcon.MessageType.INFO);
                c1.getReminderList().remove(reminder); 

            }, initialDelay+= (count++), TimeUnit.SECONDS);
        
        }  else{
            return -1;
        }

        return 1;
     
     
    }
    
    /*
     * This method stops the scheduler after all the reminders have been sent
     *  THIS METHOD CAN AND WILL END THE PROGRAM AND SHOULD - WILL ONLY BE CALLED WHEN ALL REMINDERS HAVE BEEN SENT
     */
    private static void stopScheduler(boolean test) {
        try {
            scheduler.shutdown(); 
            Thread.sleep(15000); // Sleep for 30 seconds (adjust as needed)
            if(!test){
             System.exit(0);
            }
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
         
         
    }


    //checks if there are any reminders remaining 
    private static void checkReminders(TrayIcon trayIcon, Controller c1, int countOfScheduledReminders, boolean test){
       scheduler.scheduleAtFixedRate(() -> {
            //System.out.println("Checking if there are any reminders remaining"+ c1.getReminderList().size());// will spam output
            if (c1.getReminderList().isEmpty()|| countOfScheduledReminders == 0) {   
                trayIcon.displayMessage(TrayManger.TITLE, TrayManger.GOODBYE, TrayIcon.MessageType.INFO);
                stopScheduler(test);  
            }
        }, 1, 1, TimeUnit.SECONDS); // Check every second
    }


}
