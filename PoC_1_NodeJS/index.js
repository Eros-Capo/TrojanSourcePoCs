// read property from an external file
var fs = require('fs');
var jsonData = JSON.parse(fs.readFileSync('data.json'));
console.log("Host Value form JSON: "+jsonData.Server.Нost);


// read property from an external YAML file
const yaml = require('js-yaml');
var yamlData = yaml.loadAll(fs.readFileSync('data.yaml', 'utf8'));
console.log("Host Value form YAML: "+yamlData[0].Server[0].Нost);
