import org.d.api.*;

String userId = "";
List<Map<String,String>> userList = Main.utility.qry("select user_id from users where email=? and password=?", [email, password], "default");
if (0 == userList.size()) {
    return "Wrong Email or Password!!!"
} else {
    userId = userList.get(0).get("user_id");
}

String groupAlias = groupAliases.get(0);
List<Map<String,String>> getGroupId = Main.utility.qry("select group_id from user_group_role where role_id in(2,3,4) and user_id=? and group_alias=?", [userId, groupAlias], "default");
if (0 == getGroupId.size()) {
    return "Group not found!"
}
String groupId = getGroupId.get(0).get("group_id");

List<Map<String,String>> getRoleId = Main.utility.qry("select role_id, coalesce(expiry_date, subdate(sysdate(), 1)) > sysdate() in_use from user_group_role where  role_id in(2,3,4) and user_id=? and group_id=?", [userId, groupId], "default");
if (0 == getRoleId.size()) {
    return "RoleId not found!"
}

String user_id = "";            
if(!"".equals(getFromUser)) {
	List<Map<String,String>> getUserId = Main.utility.qry("select user_id from users where email=?", [getFromUser], "default");
	if (getUserId.size() > 0) {
		user_id = getUserId.get(0).get("user_id");
	}
}

if("".equals(user_id)) {
	 List<Map<String,String>> userUpdate = Main.utility.qry("SELECT user_id FROM object WHERE group_id=? and symbol=? ORDER BY updated_date desc", [groupId, symbol], "default");            
	if (0 == userUpdate.size()) {
		return "Found no update!!!"
	}
	user_id = userUpdate.get(0).get("user_id");
}

List<Map<String,String>> objectList = Main.utility.qry("select * from object where symbol=? and user_id=? and group_id=?", [symbol,user_id,groupId], "default")
for (Map<String,String> oL: objectList) {
	while (oL.values().remove(""));
}
return objectList;