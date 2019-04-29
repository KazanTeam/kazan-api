import org.d.api.*;

String userId = "";
List<Map<String,String>> userList = Main.utility.qry("select user_id from users where email=? and password=?", [email, password], "default");
if (0 == userList.size()) {
    return ["error_code":"-1","desc":"Wrong Email or Password!!!"];
} else {
    userId = userList.get(0).get("user_id");
}

List<String> groupAliases = alertWrapper.getGroupAliases();
List<Integer> groupIds = new ArrayList<Integer>();
int groupId;
for(String groupAliase:groupAliases) {
    groupId = checkMessagePermission(groupAliase, userId) ;
    if(-1!=groupId) groupIds.add(groupId);
}
int TelegramBotType=2;
telegramSenderGroups.sendMessage(groupIds, alertWrapper.getMode(), TelegramBotType, alertWrapper.getContent(), alertWrapper.getNote(), alertWrapper.getImage());
return new ResponseEntity<String>("Alert added successfully!", HttpStatus.ACCEPTED);