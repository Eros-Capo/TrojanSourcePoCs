import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class App {

    public static void sayHello() {
        System.out.println("Hello, World!");
    }
    
    public static void sayНello() {
        System.out.println("Goodbye, World!");
    }

    public static void main(String[] args) throws Exception {
        /** PoC_2 01: The function called here will output "Goodbye world" because of the use of homoglyph character **/
        sayНello();

        /** PoC_2 02: We set the accessLevel as user but with the use of LRI, PDI, RLO the user get evaluated as an "admin" **/
        String accessLevel = "user";
        if (accessLevel != "user‮ ⁦// Check if admin⁩ ⁦") {
            System.out.println("You are an admin.");
        }

        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "app.properties";

        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(appConfigPath));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Properties from file:");
        String clientName= appProps.getProperty("client.name");
        //print keys in appProps
        for (String key : appProps.stringPropertyNames()) {
            System.out.println(key);
        }

    //https://native2ascii.net/
    //remote.Host‮⁦//hostname⁩⁦=hidden.host.com
    //remote.Host\u202e\u2066//hostname\u2069\u2066=hidden.host.com

        String remoteHost = appProps.getProperty("remote.host");
        String remoteHost2 = appProps.getProperty("remote.host‮⁦//hostname⁩⁦"); 
        String remoteHost3 = appProps.getProperty("remote.Нost");
          


        System.out.println(clientName);
        System.out.println(remoteHost);
        System.out.println(remoteHost2);
        System.out.println(remoteHost3);

        Properties xmlProps = new Properties();
        
        String xmlConfigPath = rootPath + "app_properties.xml";
        xmlProps.loadFromXML(new FileInputStream(xmlConfigPath));

        System.out.println("Server1: "+ xmlProps.getProperty("Server.Host"));
        System.out.println("Server2: "+ xmlProps.getProperty("Server.Host‮⁦//hostname⁩⁦"));         
        System.out.println("Server3: "+ xmlProps.getProperty("Server.Нost"));
          

    }
}
