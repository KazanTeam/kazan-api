import org.d.api.*;

List<Map<String,String>> thisUser = Main.utility.qry("select user_id,email,telegram_id,phone,first_name,last_name,user_image,username,create_at,update_at,active from users where user_id=? and email is null", [user_id], "default");
if (0 == thisUser.size()) {
    return ["error":"No user with this user_id or user has registered!!!"]
} else {
    return thisUser.get(0)
}