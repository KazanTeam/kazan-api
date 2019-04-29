import com.google.gson.reflect.TypeToken;
import org.d.api.*;

String t = email+"|"+password+"|"+symbol+"|"+mode+"|"+period+"|"+accountName+"|"+accountNumber+"|"+accountServer+"|"+note;

String userId = "";
List<Map<String,String>> userList = Main.utility.qry("select user_id from users where email=? and password=?", [email, password], "default");
if (0 == userList.size()) {
    return ["error_code":"-1","desc":"Wrong Email or Password!!!"];
} else {
    userId = userList.get(0).get("user_id");
}

List<String> groupIds = new ArrayList<String>();
for (String groupAlias: groupAliases) {
    // int groupId = ugrRepository.getGroupIdByGroupAlias(userId, groupAliase);
    List<Map<String,String>> getGroupId = Main.utility.qry("select group_id from user_group_role where user_id=? and group_alias=?", [userId, groupAlias], "default");
    if (0 == getGroupId.size()) {
        continue;
    }
    String groupId = getGroupId.get(0).get("group_id");
    
    // int roleId = ugrRepository.getByGroupIdUserIdSymbol(userId, groupId, symbol); 
    List<Map<String,String>> getRoleId = Main.utility.qry("select role_id, symbol_master, coalesce(expiry_date, subdate(sysdate(), 1)) > sysdate() in_use from user_group_role where user_id=? and group_id=?", [userId, groupId], "default");
    if (0 == getRoleId.size()) {
        continue;
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
    
    if (2 == roleId || (3 == roleId && 3 == mode) ) {
        Main.utility.upd("delete from object where user_id=? and group_id=? and mode_id=? and symbol=?", [userId, groupId, mode, symbol], "default");
        if (null != objects && objects.size() > 0) {
            for (Map<String,String> obj: objects) {
                Main.utility.upd("""
                    insert into object(user_id, group_id, mode_id, symbol, updated_date, 
                        objprop_angle,
                        objprop_back,
                        objprop_bgcolor,
                        objprop_border_color,
                        objprop_border_type,
                        objprop_color,
                        objprop_corner,
                        objprop_deviation,
                        objprop_direction,
                        objprop_ellipse,
                        objprop_fibolevels,
                        objprop_font,
                        objprop_fontsize,
                        objprop_levelcolor,
                        objprop_levels,
                        objprop_levelstyle,
                        objprop_leveltext_1,
                        objprop_leveltext_10,
                        objprop_leveltext_2,
                        objprop_leveltext_3,
                        objprop_leveltext_4,
                        objprop_leveltext_5,
                        objprop_leveltext_6,
                        objprop_leveltext_7,
                        objprop_leveltext_8,
                        objprop_leveltext_9,
                        objprop_levelvalue_1,
                        objprop_levelvalue_10,
                        objprop_levelvalue_2,
                        objprop_levelvalue_3,
                        objprop_levelvalue_4,
                        objprop_levelvalue_5,
                        objprop_levelvalue_6,
                        objprop_levelvalue_7,
                        objprop_levelvalue_8,
                        objprop_levelvalue_9,
                        objprop_levelwidth,
                        objprop_name,
                        objprop_price1,
                        objprop_price2,
                        objprop_price3,
                        objprop_ray,
                        objprop_ray_right,
                        objprop_scale,
                        objprop_style,
                        objprop_text,
                        objprop_time1,
                        objprop_time2,
                        objprop_time3,
                        objprop_timeframes,
                        objprop_type,
                        objprop_width) 
                    values(?,?,?,?,sysdate(),?,?,?,?,? ,?,?,?,?,? ,?,?,?,?,? ,?,?,?,?,? ,?,?,?,?,? ,?,?,?,?,? ,?,?,?,?,? ,?,?,?,?,? ,?,?,?,?,? ,?,FROM_UNIXTIME(?),FROM_UNIXTIME(?),FROM_UNIXTIME(?),? ,?,?)
                    """, 
                    [userId, groupId, mode, symbol,
                        obj.get("objprop_angle"),
                        obj.get("objprop_back"),
                        obj.get("objprop_bgcolor"),
                        obj.get("objprop_border_color"),
                        obj.get("objprop_border_type"),
                        obj.get("objprop_color"),
                        obj.get("objprop_corner"),
                        obj.get("objprop_deviation"),
                        obj.get("objprop_direction"),
                        obj.get("objprop_ellipse"),
                        obj.get("objprop_fibolevels"),
                        obj.get("objprop_font"),
                        obj.get("objprop_fontsize"),
                        obj.get("objprop_levelcolor"),
                        obj.get("objprop_levels"),
                        obj.get("objprop_levelstyle"),
                        obj.get("objprop_leveltext_1"),
                        obj.get("objprop_leveltext_10"),
                        obj.get("objprop_leveltext_2"),
                        obj.get("objprop_leveltext_3"),
                        obj.get("objprop_leveltext_4"),
                        obj.get("objprop_leveltext_5"),
                        obj.get("objprop_leveltext_6"),
                        obj.get("objprop_leveltext_7"),
                        obj.get("objprop_leveltext_8"),
                        obj.get("objprop_leveltext_9"),
                        obj.get("objprop_levelvalue_1"),
                        obj.get("objprop_levelvalue_10"),
                        obj.get("objprop_levelvalue_2"),
                        obj.get("objprop_levelvalue_3"),
                        obj.get("objprop_levelvalue_4"),
                        obj.get("objprop_levelvalue_5"),
                        obj.get("objprop_levelvalue_6"),
                        obj.get("objprop_levelvalue_7"),
                        obj.get("objprop_levelvalue_8"),
                        obj.get("objprop_levelvalue_9"),
                        obj.get("objprop_levelwidth"),
                        obj.get("objprop_name"),
                        obj.get("objprop_price1"),
                        obj.get("objprop_price2"),
                        obj.get("objprop_price3"),
                        obj.get("objprop_ray"),
                        obj.get("objprop_ray_right"),
                        obj.get("objprop_scale"),
                        obj.get("objprop_style"),
                        obj.get("objprop_text"),
                        obj.get("objprop_time1"),
                        obj.get("objprop_time2"),
                        obj.get("objprop_time3"),
                        obj.get("objprop_timeframes"),
                        obj.get("objprop_type"),
                        obj.get("objprop_width")], "default");
            }
        }
        groupIds.add(groupId);
    }
}
		
if (groupIds.isEmpty()) {
    return ["error_code":"-1","desc":"No available group!"];
} 
String groupIdStr = "(";
for (String groupId: groupIds) {
    groupIdStr += "'" + groupId + "',";
}
groupIdStr = groupIdStr.substring(0, groupIdStr.length() - 1);
groupIdStr += ")";


if (null != objects && objects.size() > 0) {
    int telegramBotType = 0;
    if ("3" == mode) {
        telegramBotType = 1;
    } else {
        telegramBotType = 2;
    }
    List<Map<String,String>> getUsername = Main.utility.qry("select username from users where user_id=?", [userId], "default");
    String username = "";
    if (getUsername.size() > 0) {
        username = getUsername.get(0).get("username");
    } else {
        return ["error_code":"-1","desc":"Cannot find username from userId!"];
    }
    String content = username + "-" + symbol + "_" + period;

    List<Map<String,String>> sendList = Main.utility.qry("select max(gr.group_alert_bot) group_alert_bot, max(gr.group_notify_bot) group_notify_bot, max(gr.group_name) group_name, us.telegram_id, count(*) count_send from groups gr join user_group_role ugr on gr.group_id = ugr.group_id join users us on ugr.user_id = us.user_id where gr.group_id in " + groupIdStr + " and ugr.role_id<=? and us.user_id=?", 
        [mode, userId], "default");

    for (Map<String,String> sL: sendList) {
        String groupAlertBot = sL.get("group_alert_bot");
        String groupNotifyBot = sL.get("group_notify_bot");
        String groupName = sL.get("group_name");
        String telegramTokenBot = (1 == telegramBotType) ? (null != groupNotifyBot ? groupNotifyBot : groupAlertBot) : (null != groupAlertBot ? groupAlertBot : groupNotifyBot);
        String telegramId = sL.get("telegram_id");

        String sendedContent = "";		
        sendedContent = groupName.toUpperCase();
        if("0" != sL.get("count_send") && "1" != sL.get("count_send")) {
            sendedContent += " AND " + sL.get("count_send") + " MORE";
        }
        if ("2" == mode) {
            sendedContent += " MASTER: ";
        } else if ("3" == mode) {
            sendedContent+= " : ";
        } else if("4" == mode || "5" == mode) {
            sendedContent+= " ALERT : ";
        }
        if (null != note)
            sendedContent += note;
        sendedContent += System.lineSeparator() + content;
        if(binding.hasVariable("imageUrl") && null != imageUrl && "" != imageUrl) {
            sendedContent+= System.lineSeparator() + System.lineSeparator() + imageUrl;
        }
        Main.get(Main.props.getString("telegram_url") + telegramTokenBot + "/sendMessage?chat_id=" + telegramId + "&text="+ URLEncoder.encode(sendedContent));
    }
}
        
return ["error_code":"1","desc":"Object list synchronized!"];