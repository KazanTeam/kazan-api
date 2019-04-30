import java.awt.image.BufferedImage;
import sun.misc.BASE64Decoder;
import javax.imageio.ImageIO;
import org.d.api.*;
import org.apache.commons.io.FilenameUtils;


def sourceData = image_data;

// tokenize the data
//def parts = sourceData.tokenize(",");
def imageString = sourceData;

// create a buffered image
BufferedImage bufferedImage = null;
byte[] imageByte;

BASE64Decoder decoder = new BASE64Decoder();
imageByte = decoder.decodeBuffer(imageString);
ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
bufferedImage = ImageIO.read(bis);
bis.close();

// write the image to a file
String fileExtension = FilenameUtils.getExtension(image_name);
String fileName = UUID.randomUUID().toString() + "." + fileExtension;
File outputfile = new File(fileName);
ImageIO.write(bufferedImage, fileExtension, outputfile);

return ["error_code":"1","desc":"Got the file!","filename":fileName];