package reminder.Interfaces;
import java.time.format.DateTimeFormatter;

/*
 * This interface will hold constants shared across the program
 * @author Kevin Portillo self-proclaimed coding wizard 🧙
 */
public interface ConstantsInterface {
    //print constants
    static final int PRINTOG = 0;//print original EVENTS
    static final int PRINTREMINDER = 1;//print reminder EVENTS
    static final int PRINTALL = 2;//prints all EVENTS
    static final String suffixStr = "\n------------------------------------------\n";
    static final boolean testRun = true;
    static final boolean actualRun = false;

    //formatting constants for printing VERY IMPORTANT DIFFERENT PATTERNS COULD APPEAR
    //THESE PATTERNS ARE FOR A SPECIFIC WEBSITES .ICS OUTPUT FORMAT
    static final DateTimeFormatter INPUTFORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX");// this is how the date is formatted in the .ics file
    
    
    
    static final String TESTPATH = "./app/src/test/resources/icalexport (2).ics";
    static final String TESTOUTPATH = "./app/src/main/resources/output";


    //JUNIT TEST CONSTANTS
    static final String PRINTOGRESULT = "-----EVENT #0-----\n" + //
            "Class: STT3850102-12022202340 (FALL 2023)\n" + //
            "Title: Test 2 (Part 1) opens\n" + //
            "Due Date : Nov 20, 2023, 8:00:00 AM\n" + //
            "-----------------------\n" + //
            "-----EVENT #1-----\n" + //
            "Class: STT3850102-12022202340 (FALL 2023)\n" + //
            "Title: Test 2 (Part 1) closes\n" + //
            "Due Date : Nov 20, 2023, 2:00:00 PM\n" + //
            "-----------------------\n" + //
            "-----EVENT #2-----\n" + //
            "Class: STT3850102-12022202340 (FALL 2023)\n" + //
            "Title: Test 2 is due\n" + //
            "Due Date : Nov 20, 2023, 10:00:00 PM\n" + //
            "-----------------------\n" + //
            "-----EVENT #3-----\n" + //
            "Class: C S3482101-10159202340 (FALL 2023)\n" + //
            "Title: Quiz 15 closes\n" + //
            "Due Date : Nov 21, 2023, 9:00:00 AM\n" + //
            "-----------------------\n" + //
            "-----EVENT #4-----\n" + //
            "Class: C S3482101-10159202340 (FALL 2023)\n" + //
            "Title: Quiz 0 closes\n" + //
            "Due Date : Nov 24, 2023, 9:00:00 AM\n" + //
            "-----------------------\n" + //
            "-----EVENT #5-----\n" + //
            "Class: STT3850102-12022202340 (FALL 2023)\n" + //
            "Title: HW 12 is due\n" + //
            "Due Date : Nov 24, 2023, 8:00:00 PM\n" + //
            "-----------------------\n";
        // Yikes 
    static final String PRINTREMINDERRESULT1 = "-----EVENT #0-----\n" + //
                    "Title: HW 12 is due Reminder\n" + //
                    "Due Date : Nov 21, 2023, 1:00:00 PM\n" + //
                    "-----------------------\n";

        static final String PRINTREMINDERRESULT2 = "-----EVENT #1-----\n" + //
                        "Title: HW 12 is due Reminder\n" + //
                        "Due Date : Nov 22, 2023, 1:00:00 PM\n" + //
                        "-----------------------\n"; //  

    static final String PRINTALLFROMLISTRESULT = "Original EVENT # 0\n" + //
            "Class: STT3850102-12022202340 (FALL 2023)\n" + //
            "Title: Test 2 (Part 1) opens\n" + //
            "Due Date : Nov 20, 2023, 8:00:00 AM\n" + //
            "Original EVENT # 1\n" + //
            "Class: STT3850102-12022202340 (FALL 2023)\n" + //
            "Title: Test 2 (Part 1) closes\n" + //
            "Due Date : Nov 20, 2023, 2:00:00 PM\n" + //
            "Original EVENT # 2\n" + //
            "Class: STT3850102-12022202340 (FALL 2023)\n" + //
            "Title: Test 2 is due\n" + //
            "Due Date : Nov 20, 2023, 10:00:00 PM\n" + //
            "Original EVENT # 3\n" + //
            "Class: C S3482101-10159202340 (FALL 2023)\n" + //
            "Title: Quiz 15 closes\n" + //
            "Due Date : Nov 21, 2023, 9:00:00 AM\n" + //
            "Original EVENT # 4\n" + //
            "Class: C S3482101-10159202340 (FALL 2023)\n" + //
            "Title: Quiz 0 closes\n" + //
            "Due Date : Nov 24, 2023, 9:00:00 AM\n" + //
            "Original EVENT # 5\n" + //
            "Class: STT3850102-12022202340 (FALL 2023)\n" + //
            "Title: HW 12 is due\n" + //
            "Due Date : Nov 24, 2023, 8:00:00 PM\n" + //
            "Reminder EVENT # 0\n" + //
            "Title: HW 12 is due Reminder\n" + //
            "Due Date : Nov 21, 2023, 1:00:00 PM\n"+//
            "Reminder EVENT # 1\n" + //
            "Title: HW 12 is due Reminder\n" + //
            "Due Date : Nov 22, 2023, 1:00:00 PM\n";
            

    static final String TESTNOTIFICATIONS_STRING= "This is a reminder that: HW 12 is due for the Class:\n" + //
                    "STT3850102-12022202340 (FALL 2023)\n" + //
                    "This is a reminder that: HW 12 is due for the Class:\n" + //
                    "STT3850102-12022202340 (FALL 2023)\n";

        
    
}

