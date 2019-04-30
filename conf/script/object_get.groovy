import org.d.api.*;

String userId = "";
List<Map<String,String>> userList = Main.utility.qry("select user_id from users where email=? and password=?", [email, password], "default");
if (0 == userList.size()) {
    return ["error_code":"-1","desc":"Wrong Email or Password!!!"];
} else {
    userId = userList.get(0).get("user_id");
}

String groupAlias = groupAliases.get(0);
List<Map<String,String>> getGroupId = Main.utility.qry("select group_id from user_group_role where role_id in(2,3,4) and user_id=? and group_alias=?", [userId, groupAlias], "default");
if (0 == getGroupId.size()) {
    return ["error_code":"-1","desc":"Group not found!"];
}
String groupId = getGroupId.get(0).get("group_id");

List<Map<String,String>> getRoleId = Main.utility.qry("select role_id, symbol_master, coalesce(expiry_date, subdate(sysdate(), 1)) > sysdate() in_use from user_group_role where  role_id in(2,3,4) and user_id=? and group_id=?", [userId, groupId], "default");
if (0 == getRoleId.size()) {
    return ["error_code":"-1","desc":"RoleId not found!"];
}
int roleId = Integer.parseInt(getRoleId.get(0).get("role_id"));
String symbolStr = getRoleId.get(0).get("symbol_master");
if (3 == roleId && null != symbolStr) {
    String[] listSymbolMasters = symbolStr.split(",");
    for(String symbolMaster: listSymbolMasters) {
        if(symbol.equalsIgnoreCase(symbolMaster)) roleId = 2;
    }
} else if (4 == roleId && ("0" == getRoleId.get(0).get("in_use"))) {
    roleId = 5;
}

if (mode > 1 && roleId <= mode) {
    String user_id = "";            
    if(!"".equals(getFromUser)) {
        List<Map<String,String>> getUserId = Main.utility.qry("select user_id from users where username=?", [getFromUser], "default");
        user_id = getUserId.get(0).get("user_id");
    }

    String mode_id = mode;
    if("".equals(user_id)) {
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
        mode_id = userUpdate.get(0).get("mode_id");
        user_id = userUpdate.get(0).get("user_id");
    }

    if ("2" == mode_id || "3" == mode_id || "4" == mode_id) {
        List<Map<String,String>> objectList = Main.utility.qry("select * from object where symbol=? and user_id=? and group_id=? and mode_id=?", [symbol,user_id,groupId,mode_id], "default")
        for (Map<String,String> oL: objectList) {
            while (oL.values().remove(""));
        }
        return ["error_code":"1","objects":gson.toJson(objectList)];
    } else {
        return ["error_code":"-1","desc":"Invalid mode!!!"];
    }
} else {
    return ["error_code":"-1","desc":"Error getting object!"];
}