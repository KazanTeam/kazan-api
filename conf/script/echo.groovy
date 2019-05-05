import org.d.api.*;
import com.google.gson.reflect.TypeToken;
return gson.fromJson(request.getAttribute("params"),new TypeToken<Map<String,Object>>(){}.getType());