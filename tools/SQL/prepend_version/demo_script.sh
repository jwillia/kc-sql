PREPEND_VERSION='/Users/blackcathacker/git/cx_data_migration/tools/SQL/prepend_version/prepend_version.js'
DEST_DIR='/Users/blackcathacker/git/cx_data_migration/src/main/resources/co/kuali/coeus/data/migration/sql/mysql'
SRC_DIR='/Users/blackcathacker/git/kc/coeus-db/coeus-db-sql/src/main/resources/org/kuali/coeus/coeus-sql/RELEASE-SCRIPTS'

find $DEST_DIR | grep '\.sql$' | grep '/demo/' | xargs rm -f
#find $DEST_DIR | grep '\.sql$' | node updateRecentVersions.js

CUR_SRC="$SRC_DIR/KC-RELEASE-3_1-SCRIPT"
cat $CUR_SRC/KRC-RELEASE-3_1-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 521 $CUR_SRC $DEST_DIR/kc/demo
cat $CUR_SRC/KC-RELEASE-3_1-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 521 $CUR_SRC $DEST_DIR/kc/demo
cat $CUR_SRC/KR-RELEASE-3_1-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 521 $CUR_SRC $DEST_DIR/rice/demo

CUR_SRC="$SRC_DIR/KC-RELEASE-3_1_1-SCRIPT"
cat $CUR_SRC/KC-RELEASE-3_1_1-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 521 $CUR_SRC $DEST_DIR/kc/demo

CUR_SRC="$SRC_DIR/KC-RELEASE-3_2-SCRIPT"
cat $CUR_SRC/KC-RELEASE-3_2-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 521 $CUR_SRC $DEST_DIR/kc/demo
cat $CUR_SRC/KR-RELEASE-3_2-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 521 $CUR_SRC $DEST_DIR/rice/demo

CUR_SRC="$SRC_DIR/KC-RELEASE-4_0-SCRIPT"
cat $CUR_SRC/KC-RELEASE-4_0-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 521 $CUR_SRC $DEST_DIR/kc/demo
cat $CUR_SRC/KR-RELEASE-4_0-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 521 $CUR_SRC $DEST_DIR/rice/demo

CUR_SRC="$SRC_DIR/KC-RELEASE-5_0-SCRIPT"
cat $CUR_SRC/KC-RELEASE-5_0-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 521 $CUR_SRC $DEST_DIR/kc/demo
cat $CUR_SRC/KR-RELEASE-5_0-Demo-MYSQL.sql| grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 521 $CUR_SRC $DEST_DIR/rice/demo

CUR_SRC="$SRC_DIR/KC-RELEASE-5_0_1-SCRIPT"
cat $CUR_SRC/KC-RELEASE-5_0_1-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 521 $CUR_SRC $DEST_DIR/kc/demo
cat $CUR_SRC/KR-RELEASE-5_0_1-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 521 $CUR_SRC $DEST_DIR/rice/demo

CUR_SRC="$SRC_DIR/KC-RELEASE-5_1_0-SCRIPT"
cat $CUR_SRC/KC-RELEASE-5_1_0-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 521 $CUR_SRC $DEST_DIR/kc/demo
cat $CUR_SRC/KR-RELEASE-5_1_0-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 521 $CUR_SRC $DEST_DIR/rice/demo

CUR_SRC="$SRC_DIR/KC-RELEASE-5_2_0-SCRIPT"
cat $CUR_SRC/KR-RELEASE-5_2_0-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 521 $CUR_SRC $DEST_DIR/rice/demo

CUR_SRC="$SRC_DIR/KC-RELEASE-6_0_0-SCRIPT"
cat $CUR_SRC/KC-RELEASE-6_0_0-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/kc/demo

find $DEST_DIR -name "*.sql" -print | xargs sed -i 's/\_BS\_S/_S/g'
#need to double check for _BS_S and replace with _S to remove bootstrap sequences



