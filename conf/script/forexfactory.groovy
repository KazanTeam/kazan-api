import org.d.api.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;

// String userId = "";
// List<Map<String,String>> userList = Main.utility.qry("select user_id from users where email=? and password=?", [email, password], "default");
// if (0 == userList.size()) {
//     return "Wrong Email or Password!!!"
// } else {
//     userId = userList.get(0).get("user_id");
// }    

Path path = Paths.get("ff_calendar_thisweek.xml");       
byte[] fileBytes = Files.readAllBytes(path);
String returnString = new String(fileBytes);
returnString = returnString.replaceAll("\\t","");

String DATE_PREFIX = "<date><![CDATA[";
int DATE_PREFIX_LENGTH = 15;
int DATE_LENGTH = 10;
int DATE_END = DATE_PREFIX_LENGTH + DATE_LENGTH;
int TOTAL_LENGTH = returnString.length();

String format = "MM-dd-yyyy";
SimpleDateFormat df = new SimpleDateFormat(format);

Calendar currentDate = Calendar.getInstance();
currentDate.setTime(new Date());
int CURRENT_DAY_OF_WEEK = currentDate.get(Calendar.DAY_OF_WEEK);
int CURRENT_WEEK = currentDate.get(Calendar.WEEK_OF_YEAR);

int i=0;
String tempString = returnString;
Calendar cal = Calendar.getInstance();
boolean needNewFile = false;

while ( (i = tempString.indexOf(DATE_PREFIX)) > 0 ) {
    String dateString = tempString.substring(i + DATE_PREFIX_LENGTH, i + DATE_END)
    Date checkDate = df.parse(dateString);
    cal.setTime(checkDate);
    int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
    int week = cal.get(Calendar.WEEK_OF_YEAR);
    if (1 == CURRENT_DAY_OF_WEEK) {
        if (1 == CURRENT_WEEK) {
            if (week != 52 && week !=53)
                needNewFile = true;
                break;
        } else {
            if (week != CURRENT_WEEK-1)
                needNewFile = true;
                break;
        }

    } else if (6 == CURRENT_DAY_OF_WEEK || 7 == CURRENT_DAY_OF_WEEK) {
        if (week != CURRENT_WEEK) {
            needNewFile = true;
            break;
        }            
    }

    tempString = tempString.substring(i + DATE_END, tempString.length());
}

if (needNewFile) {
    String forexFactoryFile = Main.get("https://cdn-nfs.faireconomy.media/ff_calendar_thisweek.xml");
    Path forexFactoryPath = Paths.get("ff_calendar_thisweek.xml");
    byte[] strToBytes = forexFactoryFile.getBytes();
    Files.write(forexFactoryPath, strToBytes);

    fileBytes = Files.readAllBytes(path);
    returnString = new String(fileBytes);
    returnString = returnString.replaceAll("\\t","");
}

return needNewFile;