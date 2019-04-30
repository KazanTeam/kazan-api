import org.d.api.*;
import com.google.gson.reflect.TypeToken;
Map<String,Object> body = gson.fromJson(request.getAttribute("params"),new TypeToken<Map<String,Object>>(){}.getType());
println body;
return ["body":body]