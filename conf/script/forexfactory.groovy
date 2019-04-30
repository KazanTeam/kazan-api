import org.d.api.*;

String userId = "";
List<Map<String,String>> userList = Main.utility.qry("select user_id from users where email=? and password=?", [email, password], "default");
if (0 == userList.size()) {
    return ["error_code":"-1","desc":"Wrong Email or Password!!!"];
} else {
    userId = userList.get(0).get("user_id");
}