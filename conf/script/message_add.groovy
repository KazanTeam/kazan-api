import org.d.api.*;

String userId = "";
List<Map<String,String>> userList = Main.utility.qry("select user_id from users where email=? and password=?", [email, password], "default");
if (0 == userList.size()) {
    return ["error_code":"-1","desc":"Wrong Email or Password!!!"];
} else {
    userId = userList.get(0).get("user_id");
}

List<String> groupIds = new ArrayList<String>();
for (String groupAlias: groupAliases) {
    List<Map<String,String>> getGroupId = Main.utility.qry("select group_id from user_group_role where role_id=2 user_id=? and group_alias=?", [userId, groupAlias], "default");
    if (0 == getGroupId.size()) {
        continue;
    }
    String groupId = getGroupId.get(0).get("group_id");
    String roleId = getGroupId.get(0).get("role_id");
    
    if ("2" == roleId) {
        
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
    
    List<Map<String,String>> getUsername = Main.utility.qry("select username from users where user_id=?", [userId], "default");
    String username = "";
    if (getUsername.size() > 0) {
        username = getUsername.get(0).get("username");
    } else {
        return ["error_code":"-1","desc":"Cannot find username from userId!"];
    }
    String content = username + "-" + symbol + "_" + period;

    List<Map<String,String>> sendList = Main.utility.qry("select max(gr.group_alert_bot) group_alert_bot, max(gr.group_name) group_name, us.telegram_id, count(*) count_send from groups gr join user_group_role ugr on gr.group_id = ugr.group_id join users us on ugr.user_id = us.user_id where gr.group_id in " + groupIdStr + " and ugr.role_id<=? and us.user_id=?", 
        [mode, userId], "default");

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
        if(binding.hasVariable("imageUrl") && null != imageUrl && "" != imageUrl) {
            sendedContent+= System.lineSeparator() + System.lineSeparator() + imageUrl;
        }
        Main.get(Main.props.getString("telegram_url") + telegramTokenBot + "/sendMessage?chat_id=" + telegramId + "&text="+ URLEncoder.encode(sendedContent));
        
/*        TelegramBot bot = new TelegramBot(telegramTokenBot);

        def sourceData = image;

        // tokenize the data
        def parts = sourceData.tokenize(",");
        def imageString = parts[1];

        // create a buffered image
        BufferedImage image1 = null;
        byte[] imageByte;

        BASE64Decoder decoder = new BASE64Decoder();
        imageByte = decoder.decodeBuffer(imageString);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
        image1 = ImageIO.read(bis);
        bis.close();

        // write the image to a file
        String fileName = System.nanoTime() + image_name;
        File outputfile = new File(fileName);
        ImageIO.write(image1, "jpg", outputfile);

        SendResponse response = bot.execute(new SendPhoto(telegramId, new File(fileName)).caption(sendedContent));*/
    }
}