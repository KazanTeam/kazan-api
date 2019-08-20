import org.d.api.*;

List<Map<String,String>> thisUser = Main.utility.qry("select * from users where user_id=? and email is null", [user_id], "default");
if (0 == thisUser.size()) {
    return "No user with this user_id or user has registered!!!"
} else {
    return thisUser
}