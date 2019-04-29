import org.d.api.*;

String userId = "";
List<Map<String,String>> userList = Main.utility.qry("select user_id from users where email=? and password=?", [email, password], "default");
if (0 == userList.size()) {
    return ["error_code":"-1","desc":"Wrong Email or Password!!!"];
} else {
    userId = userList.get(0).get("user_id");
}

String groupAlias = groupAliases.get(0);
List<Map<String,String>> getGroupId = Main.utility.qry("select group_id from user_group_role where user_id=? and group_alias=?", [userId, groupAlias], "default");
if (0 == getGroupId.size()) {
    return ["error_code":"-1","desc":"Group not found!"];
}
String groupId = getGroupId.get(0).get("group_id");

List<Map<String,String>> getRoleId = Main.utility.qry("select role_id, symbol_master, coalesce(expiry_date, subdate(sysdate(), 1)) > sysdate() in_use from user_group_role where user_id=? and group_id=?", [userId, groupId], "default");
if (0 == getRoleId.size()) {
    return ["error_code":"-1","desc":"RoleId not found!"];
}
int roleId = 0;
String roleStr = getRoleId.get(0).get("role_id");
String symbolStr = getRoleId.get(0).get("symbol_master");
if ("1" == roleStr || "2" == roleStr) {
    roleId = 2;
} else if ("3" == roleStr) {
    if (null != symbolStr) {
        String[] listSymbolMasters = symbolStr.split(",");
        for(String symbolMaster: listSymbolMasters) {
            if(symbol.equalsIgnoreCase(symbolMaster)) roleId = 2;
        }
    }
    if (0 == roleId) {
        roleId = 3;
    }        
} else if ("4" == roleStr) {
    if ("1" == getRoleId.get(0).get("in_use")) {
        roleId = 4;
    } else {
        roleId = 5;
    }
}

List<Map<String,String>> userUpdate = Main.utility.qry("""
    SELECT o.mode_id, u.user_id, TIMESTAMPDIFF(MICROSECOND,'1970-01-01',o.updated_date)
    FROM object o JOIN users u on o.user_id = u.user_id
    WHERE o.group_id = ? and o.mode_id >= ? and o.symbol = ?
    GROUP BY o.updated_date, o.user_id, u.username, o.mode_id
    ORDER BY o.updated_date desc
    """, [groupId, mode, symbol], "default");            
if (0 == userUpdate.size()) {
    return ["error_code":"-1","desc":"Found no update!!!"];
}
return ["error_code":"1","updates":userUpdate];