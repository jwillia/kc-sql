#!/usr/bin/env node

var fs = require('fs');
var path = require('path');
var sculpt = require('sculpt');
var recentVersions = require('./recentVersions.json');

var version = process.argv[2];
var srcDir = process.argv[3];
var destDir = process.argv[4];
var updateVersions = process.argv[5] === undefined || process.argv[5] === 'true';
var files = [];
var lastVersion = recentVersions[version];
if (!lastVersion) { lastVersion = 1; }
process.stdin.pipe(require('split')()).on('data', function(line) {
	if (line.match(/\.sql$/)) {
		files.push(line);
	}
}).on('end', processFiles);

function processFiles() {
	for (var i = 0; i < files.length; i++) {
		var line = files[i];
		var file = path.basename(line);

		var newFileName = "V" + version + "_" + ("000" + (lastVersion+1)).substr(((lastVersion+1)+'').length) + "__" + file;
		console.log("copy " + file + " -> " + destDir + '/' + newFileName);
		fs.createReadStream(srcDir + "/" + line).pipe(sculpt.replace(/\_BS\_S/ig, '_S')).pipe(fs.createWriteStream(destDir + "/" + newFileName));
		//fs.writeFileSync(, fs.readFileSync().pipe());
		lastVersion++;
		if (lastVersion >= 1000) {
			console.log("ERROR : lastVersion too large");
		}
	};
	recentVersions[version] = lastVersion;
	if (updateVersions) {
		fs.writeFileSync("recentVersions.json", JSON.stringify(recentVersions));
	}

}
