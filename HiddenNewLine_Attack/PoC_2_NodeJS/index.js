var username, password;
const { Console } = require("console");
const readline = require("readline");
const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});


//read username from console   
rl.question("Username: ", (answer) => {
    username = answer;
    rl.question("Password: ", (answer) => {
        password = answer;
        rl.close();
    });
});

rl.on("close", () => {
    /** PoC_2 01: The password will be the one set by the attackers with NewLineAttack instead of the one inserted by the user **/
    //\u000d password="admin";

    /** Check credentials to give a role **/
    if (password ==="user"){
        console.log("Logged in as a user!");
    }else if(password ==="admin"){
        console.log("Hi "+username+", you logged in as admin!");
    }
});