package footstep.footstep.configs.security;

import java.io.File;
import java.io.FileWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class KeyConfig {
  private static final String PRIVATE_KEY_PATH = "src/main/resources/app.key";
  private static final String PUBLIC_KEY_PATH = "src/main/resources/app.pub";

  @PostConstruct
  public void generateKeysIfNotExists() throws Exception {
    File privateKeyFile = new File(PRIVATE_KEY_PATH);
    File publicKeyFile = new File(PUBLIC_KEY_PATH);

    if (!privateKeyFile.exists() || !publicKeyFile.exists()) {
      KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");

      keygen.initialize(2048);

      KeyPair pair = keygen.generateKeyPair();

      PrivateKey privateKey = pair.getPrivate();
      PublicKey publicKey = pair.getPublic();

      String privateKeyPem = "-----BEGIN PRIVATE KEY-----\n" +
       Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(privateKey.getEncoded()) +
       "\n-----END PRIVATE KEY-----\n";

       String publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
       Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(publicKey.getEncoded()) +
       "\n-----END PUBLIC KEY-----\n";

       try (FileWriter fileWriter = new FileWriter(privateKeyFile)) {
        fileWriter.write(privateKeyPem);
       }

       try (FileWriter fileWriter = new FileWriter(publicKeyFile)) {
        fileWriter.write(publicKeyPem);
       }
    }
  }
}
