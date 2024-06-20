import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Cipher;

public class RSAFileEncryptDecrypt {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java RSAFileEncryptDecrypt <encrypt/decrypt> <input file> <output file>");
            return;
        }

        try {
            String operation = args[0];
            String inputFilePath = args[1];
            String outputFilePath = args[2];

            if (operation.equalsIgnoreCase("encrypt")) {
                PublicKey publicKey = getPublicKey("publicKey");
                processFile(Cipher.ENCRYPT_MODE, publicKey, inputFilePath, outputFilePath);
            } else if (operation.equalsIgnoreCase("decrypt")) {
                PrivateKey privateKey = getPrivateKey("privateKey");
                processFile(Cipher.DECRYPT_MODE, privateKey, inputFilePath, outputFilePath);
            } else {
                System.out.println("Invalid operation. Use 'encrypt' or 'decrypt'.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PublicKey getPublicKey(String filename) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (PublicKey) ois.readObject();
        }
    }

    private static PrivateKey getPrivateKey(String filename) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (PrivateKey) ois.readObject();
        }
    }

    private static void processFile(int cipherMode, java.security.Key key, String inputFile, String outputFile) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(cipherMode, key);

        try (FileInputStream inputStream = new FileInputStream(inputFile);
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {

            byte[] inputBytes = new byte[2048];
            int bytesRead;

            while ((bytesRead = inputStream.read(inputBytes)) != -1) {
                byte[] outputBytes = cipher.doFinal(inputBytes, 0, bytesRead);
                outputStream.write(outputBytes);
            }
        }
    }
}
