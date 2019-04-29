import org.d.api.*;

List<Map<String,String>> sendList = Main.utility.qry("select max(gr.group_alert_bot) group_alert_bot, max(gr.group_notify_bot) group_notify_bot, max(gr.group_name) group_name, us.telegram_id, count(*) count_send from groups gr join user_group_role ugr on gr.group_id = ugr.group_id join users us on ugr.user_id = us.user_id", 
        [], "default");
return ["sendList":sendList.get(0).get("count_send")];