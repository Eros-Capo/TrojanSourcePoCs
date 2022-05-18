using System;

namespace PoC_2_DotNetCore
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Hello World!");

            //read from config file
            string configFile = "config.json";
            string configJson = System.IO.File.ReadAllText(configFile);
            dynamic config = Newtonsoft.Json.JsonConvert.DeserializeObject(configJson);
            Console.WriteLine(config.Server.Нost);
            
        }
    }
}
