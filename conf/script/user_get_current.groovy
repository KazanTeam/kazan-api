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

List<Map<String,String>> thisUser = Main.utility.qry("select user_id,email,telegram_id,phone,first_name,last_name,user_image,username,create_at,update_at,active from users where email=?", [decoded_email], "default");
if (0 == thisUser.size()) {
    return ["http_code":"404","error":"No user with this email!!!"]
} else {
    return thisUser.get(0)
}