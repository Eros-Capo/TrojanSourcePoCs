import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class JavaPropertiesPoC {
    public static void main(String[] args) throws Exception {

        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        Properties xmlProps = new Properties();
        
        String xmlConfigPath = rootPath + "app_properties.xml";
        xmlProps.loadFromXML(new FileInputStream(xmlConfigPath));
        //PoC01
        String serverHost = xmlProps.getProperty("Server.Нost");
        //PoC02
        //String serverHost= xmlProps.getProperty("Server.Host‮⁦//hostname⁩⁦"));         

        //PoC03 
        String key = xmlProps.getProperty("encryption.kｅy");
        System.out.println("key: " + key);
        String cipherTextBase64 = encrypt("some plainText message to be encrypted", key);
        System.out.println("cipherText: " + cipherTextBase64);
        
        System.out.println("ServerHost: "+ serverHost);

        String[] data= {"userID=250", "sessionID=123456","cipherText="+cipherTextBase64};

        SendDataToServer(serverHost,data);
    }

//encrypt
    public static String encrypt(String plainText, String key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException {
        
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        KeySpec spec = new PBEKeySpec(key.toCharArray(), salt, 65536, 256); // AES-256
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] byteArray_key = f.generateSecret(spec).getEncoded();
        SecretKeySpec keySpec = new SecretKeySpec(byteArray_key, "AES");

        byte[] ivBytes = new byte[16];
        random.nextBytes(ivBytes);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
    
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

/***
 * Send telemetry data to the server
 * @param serverHost
 * @throws IOException
 */
private static void SendDataToServer(String serverHost , String[] data) throws IOException {
 
    URL url = new URL("http://"+serverHost+"/api/telemetry");
 
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("POST");
    connection.setRequestProperty("User-Agent", "Mozilla/5.0");
    connection.setDoOutput(true);
 
    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

    //TODO: inviare Json
    String urlPostParameters= String.join("&", data);

    outputStream.writeBytes(urlPostParameters);
    outputStream.flush();
    outputStream.close();
 
    System.out.println("Send 'HTTP POST' request to : " + url);
 
    int responseCode = connection.getResponseCode();
    System.out.println("Response Code : " + responseCode);
 
    if (responseCode == HttpURLConnection.HTTP_OK) {
        BufferedReader inputReader = new BufferedReader(
            new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
 
        while ((inputLine = inputReader.readLine()) != null) {
            response.append(inputLine);
        }
        inputReader.close();
 
        System.out.println(response.toString());
    }

}
}
