
var path = require('path');
var fs = require('fs');

var kcFiles = process.argv[2];
var files = {};
fs.createReadStream(kcFiles).pipe(require('split')()).on('data', function(line) {
	files[path.basename(line)] = line;
}).on('end', function() {
process.stdin.pipe(require('split')()).on('data', function(line) {
	var statementPieces = /^INSERT INTO `([^`]+)` \(([^\)]+)\) VALUES (.*);/.exec(line);
	if (statementPieces != null) {
		var tableName = statementPieces[1];
		var columns = statementPieces[2].split(/`,? ?`?/).filter(function(item) { return item; });
		var rows = statementPieces[3].split(/\),\(/).map(function(item) {return item.replace(/^\(/,'').replace(/\'/g,'').replace(/\($/)});
		rows.forEach(function(row) {
			var values = row.split(',');
			for (var i = 0; i < values.length; i++) {
				if (values[i].indexOf('org.kuali.kra') >= 0 || values[i].indexOf('org.kuali.coeus') >= 0) {
					var classReg = new RegExp('org\\.kuali\\.[\\w\\.]+', 'g');
					var resultArr = [];
					var updatedStr = values[i];
					while ((resultArr = classReg.exec(values[i])) != null) {
						var className = resultArr[0];
						var fileName = path.basename(className.replace(new RegExp('\\.', 'g'), '/') + '.java');
						var newClassName = files[fileName].replace('./coeus-impl/src/main/java/', '').replace(new RegExp('/', 'g'), '.').replace('.java', '');
						if (className != newClassName) {
							while (updatedStr.indexOf(className) >= 0) {
								updatedStr = updatedStr.replace(className, newClassName);
							}
						}
					}
					if (updatedStr !== values[i]) {
						console.log('update ' + tableName + ' set ' + columns[i] + ' = \'' + updatedStr + '\' where ' + columns[i] + ' = \'' + values[i] + '\';');
					}
				}
			}
		});
		//console.log(rows);

	} else {
		console.log("Line didn't match -- " + line);
	}
});
});