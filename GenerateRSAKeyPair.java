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
 
