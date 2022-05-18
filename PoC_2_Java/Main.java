import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	    Scanner scanner = new Scanner(System.in);
	    String username, password;
	    /** Print of user credentials **/
        System.out.println("Username: user , Password: user");

        /** The user insert the credentials **/
        System.out.println("Insert your username...");
        username=scanner.nextLine();
        System.out.println("Insert your password...");
        password=scanner.nextLine();
        /** PoC_2 01: The password will be the one set by the attackers with NewLineAttack instead of the one inserted by the user **/
        //\u000d password="admin";

        /** Check credentials to give a role **/
        if (password.equals("user")){
            System.out.println("Logged in as a user!");
        }else if(password.equals("admin")){
            System.out.println("Hi "+username+", you logged in as admin!");
        }

    }
}
