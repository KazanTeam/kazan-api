import org.d.api.*;
import java.nio.file.*;

String forexFactoryFile = Main.get("https://cdn-nfs.faireconomy.media/ff_calendar_thisweek.xml");
Path path = Paths.get("ff_calendar_thisweek.xml");
byte[] strToBytes = forexFactoryFile.getBytes();
Files.write(path, strToBytes);
return 0;