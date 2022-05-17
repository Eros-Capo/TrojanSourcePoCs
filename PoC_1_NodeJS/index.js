// read property from an external file
var fs = require('fs');
var data = fs.readFileSync('data.json');
var json = JSON.parse(data);
console.log(json.Server.Нost);
