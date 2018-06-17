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
import com.google.zxing.common.GlobalHistogramBinarizer;


import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.encoder.QRCode;


public class TestZxing {
    public static void main( String[] args ) {
        System.out.println("test beginning...");
        String testDir = "/home/svtter/Documents/Dataset/red/processed/";
//        String testDir = "/home/svtter/Documents/Dataset/blue/processed/";

        System.out.println("Enter your dir: [default: " + testDir + "]");

        Scanner sc = new Scanner(System.in);
        String nextDir = sc.nextLine();
        if (nextDir.equals("")) {
            nextDir = testDir;
        }
        System.out.println("Test dir: " + nextDir);

        ZxingReader test = new ZxingReader();
        try {
            test.testFolder(nextDir);
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
        int wholeFiles = 0;
        int acc = 0;


        if (file.exists()) {
            LinkedList<File> list = new LinkedList<File>();
            File[] files = file.listFiles();
            for (File file2 : files) {
                if (file2.isDirectory()) {
                    System.out.println("文件夹:" + file2.getAbsolutePath());
                } else {
                    String filePath = file2.getAbsolutePath();
                    System.out.println("文件:" + filePath);
                    if (this.test(filePath)) {
                        acc ++;
                        System.out.println("acc num: " + acc);
                    }
                    wholeFiles++;
                }
            }
        }

        System.out.println("ACC: " + acc);
        System.out.println("WholeFiles: " + wholeFiles);
        System.out.println("Acc rate: " + (double)acc / wholeFiles);
    }


    // mutiple test
    public boolean test(String path) {
        try {

            File imageFile = new File(path);
            BufferedImage image = null;
            image = ImageIO.read(imageFile);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer Hbinarizer = new HybridBinarizer(source);
            Binarizer Gbinarizer = new GlobalHistogramBinarizer(source);


            boolean res1, res2;
            System.out.println("H:");
            res1 = testor(Hbinarizer);
            System.out.println("G:");
            res2 = testor(Gbinarizer);
            System.out.println("Result: " + res1 + " " + res2);
            return res1 || res2;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }



    public boolean testor(Binarizer binarizer){
        //二维码图片路径
        try {
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
            return true;
        } catch (NotFoundException e) {
            System.out.println("There is no QR code in the image");
        } catch (Exception e) {
            System.out.println("Other exception.");
            e.printStackTrace();
        }

        return false;
    }
}


class ResultRecorder {

}
