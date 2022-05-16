using System;

namespace ConsoleApp1
{
    class Program
    {
        static void Main(string[] args)
        {   
	    
	    String username, password;
	    /** Print of user credentials **/
        System.Console.WriteLine("Username: user , Password: user");

        /** The user insert the credentials **/
        System.Console.WriteLine("Insert your username...");
        username = System.Console.ReadLine();
        System.Console.WriteLine("Insert your password...");
        password = System.Console.ReadLine();
            /** PoC_2 01: The password will be the one set by the attackers with NewLineAttack instead of the one inserted by the user **/
            //\u000d password="admin";

            /** Check credentials to give a role **/
        if (password.Equals("user")){
            System.Console.WriteLine("Logged in as a user!");
        }else if(password.Equals("admin")){
            System.Console.WriteLine("Hi " +username+", you logged in as admin!");
        }
        }
    }
}
