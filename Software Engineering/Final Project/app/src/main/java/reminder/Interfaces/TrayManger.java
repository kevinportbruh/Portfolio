package reminder.Interfaces;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

/**
 * This class manages the tray icon and its components
 * does not need to be instantiated
 * @author Kevin Portillo self-proclaimed coding wizard🧙
 */
public interface TrayManger {
    static Image image = Toolkit.getDefaultToolkit().getImage("./src/main/resources/styles/tray.gif");
    static TrayIcon trayIcon = new TrayIcon(image, "Assingment Reminder");
    static String TITLE = "Assignemnt Reminder App!";
    static String GOODBYE ="All reminders have been sent!\n Closing the Program, GoodBye!";
    static String HELLO ="The Assignment Reminder App has been Minimized to the system Tray!\n";
    public static void createTray(){
        if (!SystemTray.isSupported()) {
            System.err.println("System tray not supported!");
        } else {
            SystemTray tray = SystemTray.getSystemTray();
           
            
            trayIcon.setImageAutoSize(true);

            // Create a pop-up menu components
            final PopupMenu popup = new PopupMenu();
            MenuItem aboutItem = new MenuItem("About");
            MenuItem exitItem = new MenuItem("Exit");
            //when the user clicks on the about button
            aboutItem.addActionListener(e-> 
            {
                System.out.println("hello world");
            });
            //when the user clicks on the exit button exit the program
            exitItem.addActionListener(e-> 
            {
                System.exit(0);
            });

            //Add components to pop-up menu
            popup.add(aboutItem);
            popup.addSeparator();
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);
           
            
            try {
                tray.add(trayIcon);
                
            } catch (AWTException e) {
                System.out.println("TrayIcon could not be added.");
            }

        }
    }
   
    

}
