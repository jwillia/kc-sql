#!/usr/bin/env node

var fs = require('fs');
var dateformat = require('dateformat');

var dir = process.argv[2];
var versions = {};
var allVersions = {};
var moves = '';
fs.readdirSync(process.argv[2]).forEach(function (file) {
	var version = /V([0-9_]+)__/.exec(file);
	if (version != null) {
		var pieces = version[1].split('_');
		var datePiece = pieces[0];
		addToVersion(datePiece, file, version[1]);
	} else {
		var stat = fs.statSync(dir + "/" + file);
		var newVersion = dateformat(stat.mtime, "yyyymmddHHMM");
		addToVersion(newVersion, file);
	}
});
for (var date in versions) {
	var i = 0;
	versions[date].sort(function(a, b) {
		return a.priority - b.priority;
	})
	for ( ; i < versions[date].length; i++) {
		var fileObj = versions[date][i];
		if (!fileObj['currentVersion']) {
			var ext = ("00" + (i+1)).substr(((i+1)+'').length);
			var newVersion = 'V' + date + '_' + ext;
			var newFileName =  newVersion + '__' + fileObj['fileName'];
			fileObj['currentVersion'] = newVersion;
			confirmVersion(newVersion, fileObj['fileName']);
			moves += 'mv ' + dir + '/' + fileObj['fileName'] + ' ' + dir + '/' + newFileName + '\n';
			fs.renameSync(dir + '/' + fileObj['fileName'], dir + '/' + newFileName);
		} else {
			confirmVersion(fileObj['currentVersion'], fileObj['fileName']);
		}
	}
}
console.log(moves);

function confirmVersion(newVersion, fileName) {
	if (newVersion in allVersions) {
		console.log('ERROR : ' + newVersion + ' already exists for ' + allVersions[newVersion] + ' but matches ' + fileName);
	}
}

function addToVersion(date, file, currentVersion) {
	if (!(date in versions)) {
		versions[date] = new Array();
	}
	var priority = 9999;
	if (file.match(/KC_SEQ/)) {
		priority = 1;
	} else if (file.match(/KC_TBL/)) {
		priority = 2;
	} else if (file.match(/KR_RICE/) || file.match(/KRC_RICE/)) {
		priority = file.split('_')[2]+2;
	} else if (file.match(/KC_DML/) || file.match(/KR_DML/)) {
		priority = file.split('_')[2]+1000;
	}
	versions[date].push({'fileName' : file, 'currentVersion' : currentVersion, 'priority' : priority});
}
