 import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GenerateSHA256Hash {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java GenerateSHA256Hash <file path>");
            return;
        }

        try {
            File file = new File(args[0]);
            FileInputStream fis = new FileInputStream(file);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] byteArray = new byte[1024];
            int bytesCount = 0;

            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }

            fis.close();
            byte[] bytes = digest.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }

            System.out.println("SHA-256 hash: " + sb.toString());
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }
}
