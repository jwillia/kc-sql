# Fix_Classpaths

Fix classpaths is designed to help us on KC repair classpaths that have been stored in a database. This tool is far from end-user facing and designed more to assist our development as we may decide to repackage classes as part of our development effort. In this case the tool was written to assist after the move from org.kuali.kra to org.kuali.coeus for sys, common and propdev. Since we frequently store classpaths in both KC and Rice we need a way to identify and fix renamed classpaths. This tool is designed to help, but currently can't handle class renames only moves. If a class has been renamed it may require significantly more intervention or modifications to this process.

# Using

This tool is again not user facing and requires some finess to use. For me typical usage is as follows.

First step is to list out all the files in the coeus-impl sub-project in KC.

`find . | grep coeus-impl | grep -v target > kc_files.txt`

Then using a hopefully production sized database run the following

`mysqldump -c kc_database_name | grep '^INSERT INTO ' | node fix_classpaths.js kc_files.txt | sort -u > generated_sql_file.sql`

And thats it...hopefully. After that you should review the changes, separate them by KC or Rice and add them to the kc-sql project for running against databases.

Good luck!

