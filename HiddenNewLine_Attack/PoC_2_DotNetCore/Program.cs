using System;

namespace PoC_2_DotNetCore
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Hello World!");
            string passaword = "123456";
             /** PoC_2 01: The password will be the one set by the attackers with NewLineAttack instead of the one inserted by the user **/
             //\u000d password="111111";
             Console.WriteLine("The password is : "+passaword);
        }
    }
}
