import org.d.api.*;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.response.SendResponse;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.request.SendMessage;
import sun.misc.BASE64Decoder;

String userId = "";
String telegramId = "";
List<Map<String,String>> userList = Main.utility.qry("select user_id, telegram_id from users where email=? and password=?", [email, password], "default");
if (0 == userList.size()) {
    return "Wrong Email or Password!!!"
} else {
    userId = userList.get(0).get("user_id");
    telegramId = userList.get(0).get("telegram_id");
}
if ("" == telegramId || telegramId.trim().length() <1) {
    return "This user does not have Telegram Id!!!"
}

String sendedContent = "ALERT: ";
if (null != note)
    sendedContent += note + System.lineSeparator();
sendedContent += symbol + "-" + period

if (binding.hasVariable("image_name") && binding.hasVariable("image_data")) {
    byte[] imageByte = new BASE64Decoder().decodeBuffer(image_data);

    new TelegramBot(Main.props.getString("telegram_bot_default")).execute(new SendPhoto(telegramId, imageByte).caption(sendedContent));
} else {
    new TelegramBot(Main.props.getString("telegram_bot_default")).execute(new SendMessage(telegramId, sendedContent));
}

return 0;