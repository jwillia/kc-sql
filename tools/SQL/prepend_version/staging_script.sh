PREPEND_VERSION='/Users/blackcathacker/git/cx_data_migration/tools/SQL/prepend_version/prepend_version.js'
DEST_DIR='/Users/blackcathacker/git/cx_data_migration/src/main/resources/co/kuali/coeus/data/migration/sql/mysql'
SRC_DIR='/Users/blackcathacker/git/kc/coeus-db/coeus-db-sql/src/main/resources/org/kuali/coeus/coeus-sql/'

find $DEST_DIR | grep '\.sql$' | grep '/staging/' | xargs rm -f
#find $DEST_DIR | grep '\.sql$' | node updateRecentVersions.js

CUR_SRC="$SRC_DIR/current_mysql/4.0/dml"
find $CUR_SRC | grep '00S0\|0TS0' | grep 'KC_DML\|KRC_DML' | node $PREPEND_VERSION 521 / $DEST_DIR/kc/staging
find $CUR_SRC | grep '00S0\|0TS0' | grep 'KR_DML' | node $PREPEND_VERSION 521 / $DEST_DIR/rice/staging

CUR_SRC="$SRC_DIR/current_mysql/5.0/dml"
find $CUR_SRC | grep '00S0\|0TS0' | grep 'KC_DML\|KRC_DML' | node $PREPEND_VERSION 521 / $DEST_DIR/kc/staging
find $CUR_SRC | grep '00S0\|0TS0' | grep 'KR_DML' | node $PREPEND_VERSION 521 / $DEST_DIR/rice/staging

CUR_SRC="$SRC_DIR/current_mysql/5.1.0/dml"
find $CUR_SRC | grep '00S0\|0TS0' | grep 'KC_DML\|KRC_DML' | node $PREPEND_VERSION 521 / $DEST_DIR/kc/staging
find $CUR_SRC | grep '00S0\|0TS0' | grep 'KR_DML' | node $PREPEND_VERSION 521 / $DEST_DIR/rice/staging

CUR_SRC="$SRC_DIR/current_mysql/5.1.1/dml"
find $CUR_SRC | grep '00S0\|0TS0' | grep 'KC_DML\|KRC_DML' | node $PREPEND_VERSION 521 / $DEST_DIR/kc/staging
find $CUR_SRC | grep '00S0\|0TS0' | grep 'KR_DML' | node $PREPEND_VERSION 521 / $DEST_DIR/rice/staging

CUR_SRC="$SRC_DIR/current_mysql/5.2.1/dml"
find $CUR_SRC | grep '00S0\|0TS0' | grep 'KC_DML\|KRC_DML' | node $PREPEND_VERSION 521 / $DEST_DIR/kc/staging
find $CUR_SRC | grep '00S0\|0TS0' | grep 'KR_DML' | node $PREPEND_VERSION 521 / $DEST_DIR/rice/staging

CUR_SRC="$SRC_DIR/current_mysql/6.0.0/dml"
find $CUR_SRC | grep '00S0\|0TS0' | grep 'KC_DML\|KRC_DML' | node $PREPEND_VERSION 600 / $DEST_DIR/kc/staging
find $CUR_SRC | grep '00S0\|0TS0' | grep 'KR_DML' | node $PREPEND_VERSION 600 / $DEST_DIR/rice/staging

find $DEST_DIR -name "*.sql" -print | xargs sed -i 's/\_BS\_S/_S/g'
#need to double check for _BS_S and replace with _S to remove bootstrap sequences



