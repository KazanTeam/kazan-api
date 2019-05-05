import org.d.api.*;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.response.SendResponse;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.request.SendMessage;
import sun.misc.BASE64Decoder;

String userId = "";
List<Map<String,String>> userList = Main.utility.qry("select user_id from users where email=? and password=?", [email, password], "default");
if (0 == userList.size()) {
    return "Wrong Email or Password!!!"
} else {
    userId = userList.get(0).get("user_id");
}

List<String> groupIds = new ArrayList<String>();
for (String groupAlias: groupAliases) {
    List<Map<String,String>> getGroupId = Main.utility.qry("select group_id, role_id from user_group_role where role_id in(2,3) and user_id=? and group_alias=?", [userId, groupAlias], "default");
    if (0 == getGroupId.size()) {
        continue;
    }
    String groupId = getGroupId.get(0).get("group_id");
    
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
		
if (groupIds.isEmpty()) {
    return "No available group!"
} 
String groupIdStr = "(";
for (String groupId: groupIds) {
    groupIdStr += "'" + groupId + "',";
}
groupIdStr = groupIdStr.substring(0, groupIdStr.length() - 1);
groupIdStr += ")";


if (null != objects && objects.size() > 0) {
    
    List<Map<String,String>> getUsername = Main.utility.qry("select username from users where user_id=?", [userId], "default");
    String username = "";
    if (getUsername.size() > 0) {
        username = getUsername.get(0).get("username");
    } else {
        return "Cannot find username from userId!"
    }
    String content = username + "-" + symbol + "_" + period;

    List<Map<String,String>> sendList = Main.utility.qry("select max(gr.group_alert_bot) group_alert_bot, max(gr.group_name) group_name, us.telegram_id, count(*) count_send from groups gr join user_group_role ugr on gr.group_id = ugr.group_id join users us on ugr.user_id = us.user_id where gr.group_id in " + groupIdStr + " and ugr.role_id in (2,3,4) group by us.telegram_id", [], "default");

    for (Map<String,String> sL: sendList) {
        String telegramTokenBot = sL.get("group_alert_bot");
        String groupName = sL.get("group_name");
        String telegramId = sL.get("telegram_id");

        String sendedContent = "";		
        sendedContent = groupName.toUpperCase();
        if("0" != sL.get("count_send") && "1" != sL.get("count_send")) {
            sendedContent += " AND " + sL.get("count_send") + " MORE";
        }
        sendedContent+= " : ";
        
        if (null != note)
            sendedContent += note;
        sendedContent += System.lineSeparator() + content;
        
        if(binding.hasVariable("image_name") && binding.hasVariable("image_data")) {
            byte[] imageByte = new BASE64Decoder().decodeBuffer(image_data);

            new TelegramBot(telegramTokenBot).execute(new SendPhoto(telegramId, imageByte).caption(sendedContent));
        } else {
            new TelegramBot(telegramTokenBot).execute(new SendMessage(telegramId, sendedContent));
        }
    }
}
        
return 0;