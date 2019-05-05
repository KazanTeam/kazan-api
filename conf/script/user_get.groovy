import org.d.api.*;

String userId = "";
List<Map<String,String>> userList = Main.utility.qry("select user_id from users where email=? and password=?", [email, password], "default");
if (0 == userList.size()) {
    return "Wrong Email or Password!!!"
} else {
    userId = userList.get(0).get("user_id");
}

String groupAlias = groupAliases.get(0);
List<Map<String,String>> getGroupId = Main.utility.qry("select group_id from user_group_role where role_id in(2,3) and user_id=? and group_alias=?", [userId, groupAlias], "default");
if (0 == getGroupId.size()) {
    return "Group not found!"
}
String groupId = getGroupId.get(0).get("group_id");

List<Map<String,String>> userUpdate = Main.utility.qry("""
    SELECT u.email, u.username, TIMESTAMPDIFF(MICROSECOND,'1970-01-01',o.updated_date) updated_date
    FROM object o JOIN users u on o.user_id = u.user_id
    WHERE o.group_id = ? and o.symbol = ?
    GROUP BY o.updated_date, o.user_id, u.username, u.email
    ORDER BY o.updated_date desc
    """, [groupId, symbol], "default");            
if (0 == userUpdate.size()) {
    return "Found no update!!!"
}
String [][] userArray = new String [userUpdate.size()][3];
for (int i=0; i<userUpdate.size(); i++) {
    userArray[i][0] = userUpdate.get(i).get("email");
    userArray[i][1] = userUpdate.get(i).get("username");
    userArray[i][2] = userUpdate.get(i).get("updated_date");
}
String returnString = userArray.toString();
return returnString.substring(1, returnString.length()-1);