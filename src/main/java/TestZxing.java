import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;


//import org.junit.Test;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.encoder.QRCode;


public class TestZxing {
  public static void main( String[] args ) {
    System.out.println("test beginning...");
    System.out.println("Enter your dir: ");

//    Scanner sc = new Scanner(System.in);
//    String testDir = sc.nextLine();
    ZxingReader test = new ZxingReader();
    String testDir = "/Users/xiuhao/Pictures/qrcode";
    try {
      test.testFolder(testDir);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

class ZxingReader {
  private static String decodeQRCode(File qrCodeimage) throws IOException {
    BufferedImage bufferedImage = ImageIO.read(qrCodeimage);
    LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

    try {
      Result result = new MultiFormatReader().decode(bitmap);
      return result.getText();
    } catch (NotFoundException e) {
      System.out.println("There is no QR code in the image");
      return null;
    }
  }


  public void testFolder(String path) {
    File file = new File(path);
    if (file.exists()) {
      LinkedList<File> list = new LinkedList<File>();
      File[] files = file.listFiles();
      for (File file2 : files) {
        if (file2.isDirectory()) {
          System.out.println("文件夹:" + file2.getAbsolutePath());
        } else {
          String filePath = file2.getAbsolutePath();
          System.out.println("文件:" + filePath);
          this.test(filePath);
        }
      }
    }
  }



  public void test(String path){
    //二维码图片路径
//    String path = "/Users/xiuhao/Pictures/qrcode_gray/2018-3-21-15-46-41.bmp";
    File imageFile = new File(path);
    BufferedImage image = null;
    try {
      image = ImageIO.read(imageFile);
      LuminanceSource source = new BufferedImageLuminanceSource(image);
      Binarizer binarizer = new HybridBinarizer(source);
      BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

      List<BarcodeFormat> allFormats = new ArrayList<>();
      allFormats.add(BarcodeFormat.QR_CODE);

      Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
      hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
      hints.put(DecodeHintType.TRY_HARDER, BarcodeFormat.QR_CODE);
      hints.put(DecodeHintType.POSSIBLE_FORMATS, allFormats);

      //解码获取二维码中信息

      QRCodeReader qrcoder = new QRCodeReader();
      Result result = qrcoder.decode(binaryBitmap, hints);
//      Result result = qrcoder.decode(binaryBitmap);
      System.out.println(result.getText());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NotFoundException e) {
      System.out.println("There is no QR code in the image");
    } catch (Exception e) {
      System.out.println("Other exception.");
      e.printStackTrace();
    }
  }
}
