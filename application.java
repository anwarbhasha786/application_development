Step 1: Generate an RSA Key Pair
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class GenerateRSAKeyPair {
    public static void main(String[] args) {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(2048);

            KeyPair pair = keyPairGen.generateKeyPair();
            PublicKey publicKey = pair.getPublic();
            PrivateKey privateKey = pair.getPrivate();

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("publicKey"))) {
                oos.writeObject(publicKey);
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("privateKey"))) {
                oos.writeObject(privateKey);
            }

            System.out.println("Keys generated and saved to files.");
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }
}
 
Step 2: Generate SHA-256 Hash of the File
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

Step 3: Encrypt and Decrypt the File Using RSA Key Pair
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