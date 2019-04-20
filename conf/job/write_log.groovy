import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger
import org.d.api.*;
import com.google.gson.reflect.TypeToken;

StringBuffer cdr = null;
while (Main.ccbsRequests.size()>0){
	String[] request = Main.ccbsRequests.poll();
	Main.utility.upd("insert into ccs_log.log_request values (?,?,?,?,?,sysdate)",[request[0],request[1],request[2],request[3],request[4]],"ccbs119");
}
return "OK"