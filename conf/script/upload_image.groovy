import java.awt.image.BufferedImage;
import sun.misc.BASE64Decoder;
import javax.imageio.ImageIO;
import org.d.api.*;
import com.google.gson.reflect.TypeToken;

return ["error_code":"1"];

/*
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
ImageIO.write(image1, "png", outputfile);*/