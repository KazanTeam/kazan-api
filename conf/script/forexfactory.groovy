import org.d.api.*;
import java.nio.file.*;

String userId = "";
List<Map<String,String>> userList = Main.utility.qry("select user_id from users where email=? and password=?", [email, password], "default");
if (0 == userList.size()) {
    return "Wrong Email or Password!!!"
} else {
    userId = userList.get(0).get("user_id");
}

Path path = Paths.get("ff_calendar_thisweek.xml");       
byte[] fileBytes = Files.readAllBytes(path);

return new String(fileBytes)