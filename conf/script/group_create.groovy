import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.interfaces.DecodedJWT
import org.d.api.*

DecodedJWT jwt = JWT.decode(firebase_token)
Date now = new Date()
if (jwt.getExpiresAt() < now)
    return ["http_code":"404","error":"Invalid FireBase token - Exprired!!!"]
if (jwt.getIssuedAt() > now)
    return ["http_code":"404","error":"Invalid FireBase token - Issue date at the future!!!"]
if (jwt.getAudience().get(0) != "kazan-trading")
    return ["http_code":"404","error":"Invalid FireBase token - Wrong Audience!!!"]
if (jwt.getIssuer() != "https://securetoken.google.com/kazan-trading")
    return ["http_code":"404","error":"Invalid FireBase token - Wrong Issuer!!!"]    
if (jwt.getSubject() == null || jwt.getSubject().trim() == "")
    return ["http_code":"404","error":"Invalid FireBase token - Without Subject!!!"]    
if (jwt.getNotBefore() > now)
    return ["http_code":"404","error":"Invalid FireBase token - Not Before at the future!!!"]

String decoded_email = jwt.getClaim("email").asString()
if (null == decoded_email || decoded_email.trim() == "")
    return ["http_code":"404","error":"Invalid FireBase token - No email!!!"]

String groupId = "";
List<Map<String,String>> groupSequence = Main.utility.qry("SELECT auto_increment FROM information_schema.tables WHERE table_name='groups' ", 
    [], "default");
if (0 == groupSequence.size()) {
    return ["http_code":"404","error":"Cannot create new sequence for new group!!!"]
} else {
    groupId = groupSequence.get(0).get("auto_increment");
}

String result = Main.utility.upd("""
                insert into groups(group_id,group_name,group_image,group_alert_bot,creater,update_at,active) values(?,?,?,?,?,?,?)
                """, 
                [groupId,group_name,group_image,group_alert_bot,creater,update_at,active], "default");    

result = Main.utility.upd("""
        insert into user_group_role(user_id,group_id,role_id,group_alias) values(?,?,1,?)
        """, 
        [creater,groupId,group_name], "default");    

List<Map<String,String>> groupReturn = Main.utility.qry("SELECT * FROM groups", [], "default");
if (0 == groupSequence.size()) {
    return ["http_code":"404","error":"Cannot retrieve new group!!!"]
}  

return groupReturn