import org.d.api.*;

String userId = "";
String telegramId = "";
List<Map<String,String>> userList = Main.utility.qry("select user_id, telegram_id from users where email=? and password=?", [email, password], "default");
if (0 == userList.size()) {
    return ["error_code":"-1","desc":"Wrong Email or Password!!!"];
} else {
    userId = userList.get(0).get("user_id");
    telegramId = userList.get(0).get("telegram_id");
}
if ("" == telegramId || telegramId.trim().length() <1) {
    return ["error_code":"-1","desc":"This user does not have Telegram Id!!!"];
}

String sendedContent = "ALERT: ";
if (null != note)
    sendedContent += note + System.lineSeparator();
sendedContent += symbol + "-" + period
if(binding.hasVariable("image") && null != image && "" != image) {
    sendedContent+= System.lineSeparator() + image;
}
Main.get(Main.props.getString("telegram_url") + "582605967:AAFB9Aq2tsA2GbmXejsfWH2RfPpV6BUUJOU/sendMessage?chat_id=" + telegramId + "&text="+ URLEncoder.encode(sendedContent));        
return ["error_code":"1","desc":"Alerted!"];