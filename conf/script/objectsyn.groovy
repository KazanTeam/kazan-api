import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.regex.Matcher
import java.util.regex.Pattern
import com.google.gson.reflect.TypeToken;
import org.d.api.*;



String t = email+"|"+password+"|"+symbol+"|"+mode+"|"+period+"|"+accountName+"|"+accountNumber+"|"+accountServer+"|"+note;

System.out.println("tttt..................." +t);
Map<String,Object> body = gson.fromJson(request.getAttribute("params"),new TypeToken<Map<String,Object>>(){}.getType());
System.out.println("groupAliases..................." +body.get("groupAliases")[0]);
System.out.println("objects..................." +body.get("objects")[0].get("objprop_name"));

return ["error_code":"-1","result":email,"Desc":"Service not found only package_list"];