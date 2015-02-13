PREPEND_VERSION='/Users/blackcathacker/git/cx_data_migration/tools/SQL/prepend_version/prepend_version.js'
DEST_DIR='/Users/blackcathacker/git/cx_data_migration/src/main/resources/co/kuali/coeus/data/migration/sql/mysql'
SRC_DIR='/Users/blackcathacker/git/kc/coeus-db/coeus-db-sql/src/main/resources/org/kuali/coeus/coeus-sql/RELEASE-SCRIPTS'

rm recentVersions.json; echo "{ }" > recentVersions.json
find $DEST_DIR | grep '\.sql$' | xargs rm -f
find $DEST_DIR | grep '\.sqlr$' | xargs rm -f
find $DEST_DIR | grep '\.sqlrs$' | xargs rm -f

CUR_SRC="$SRC_DIR/KC-RELEASE-0_0_0-SCRIPT"
cat $CUR_SRC/KR-RELEASE-0_0_0-Upgrade-MYSQL.sql | awk '{print $2}' | node $PREPEND_VERSION 001 $CUR_SRC $DEST_DIR/rice/bootstrap
cat $CUR_SRC/KC-RELEASE-0_0_0-Upgrade-MYSQL.sql | awk '{print $2}' | node $PREPEND_VERSION 001 $CUR_SRC $DEST_DIR/kc/bootstrap


CUR_SRC="$SRC_DIR/KC-RELEASE-3_0-CLEAN/mysql"
cat $CUR_SRC/mysql_server_rice.sql | grep -v 'select ' | awk '{print $2}' |  node $PREPEND_VERSION 300 $CUR_SRC $DEST_DIR/rice_server/bootstrap
cat $CUR_SRC/mysql_server.sql | grep -v 'select ' | awk '{print $2}' |  node $PREPEND_VERSION 300 $CUR_SRC $DEST_DIR/rice_server/bootstrap
cat $CUR_SRC/mysql_server_SR.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 300 $CUR_SRC $DEST_DIR/rice_data_only/bootstrap
cat $CUR_SRC/mysql_client.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 300 $CUR_SRC $DEST_DIR/kc/bootstrap


CUR_SRC="$SRC_DIR/KC-RELEASE-3_0_1-SCRIPT"
cat $CUR_SRC/KR-Release-3_0-3_0_1-Upgrade-Mysql-Install.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 301 $CUR_SRC $DEST_DIR/rice_server/bootstrap
cat $CUR_SRC/KR-Release-3_0-3_0_1-SR-Mysql-Install.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 301 $CUR_SRC $DEST_DIR/rice_data_only/bootstrap
cat $CUR_SRC/KC-Release-3_0-3_0_1-Upgrade-Mysql-Install.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 301 $CUR_SRC $DEST_DIR/kc/bootstrap


CUR_SRC="$SRC_DIR/KC-RELEASE-3_1_SP1-SCRIPT"
cat $CUR_SRC/KR-Server-Release-1_0_3-1_0_3_1-Upgrade-Mysql-Install.sql |  grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 310_1 $CUR_SRC $DEST_DIR/rice_server/bootstrap
cat $CUR_SRC/KR-Release-3_0_1-3_1_S1-Upgrade-Mysql-Install.sql |  grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 310_1 $CUR_SRC $DEST_DIR/rice_server/bootstrap
cat $CUR_SRC/KR-Release-3_0_1-3_1_S1-SR-Mysql-Install.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 310_1 $CUR_SRC $DEST_DIR/rice_data_only/bootstrap
cat $CUR_SRC/KC-Release-3_0_1-3_1_S1-Upgrade-Mysql-Install.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 310_1 $CUR_SRC $DEST_DIR/kc/bootstrap

CUR_SRC="$SRC_DIR/KC-RELEASE-3_1_SP2-SCRIPT"
cat $CUR_SRC/KR-RELEASE-3_1_SP2-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 310_2 $CUR_SRC $DEST_DIR/rice_server/bootstrap
cat $CUR_SRC/KR-RELEASE-3_1_SP2-SR-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 310_2 $CUR_SRC $DEST_DIR/rice_data_only/bootstrap
cat $CUR_SRC/KRC-RELEASE-3_1_SP2-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 310 $CUR_SRC $DEST_DIR/kc/bootstrap
cat $CUR_SRC/KC-RELEASE-3_1_SP2-Upgrade-MYSQL.sql| grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 310_2 $CUR_SRC $DEST_DIR/kc/bootstrap


CUR_SRC="$SRC_DIR/KC-RELEASE-3_1_SP3-SCRIPT"
cat $CUR_SRC/KR-RELEASE-3_1_SP3-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 310_3 $CUR_SRC $DEST_DIR/rice_server/bootstrap
cat $CUR_SRC/KR-RELEASE-3_1_SP3-SR-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 310_3 $CUR_SRC $DEST_DIR/rice_data_only/bootstrap
cat $CUR_SRC/KRC-RELEASE-3_1_SP3-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 310 $CUR_SRC $DEST_DIR/kc/bootstrap
cat $CUR_SRC/KC-RELEASE-3_1_SP3-Upgrade-MYSQL.sql| grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 310_3 $CUR_SRC $DEST_DIR/kc/bootstrap

CUR_SRC="$SRC_DIR/KC-RELEASE-3_1_SP4-SCRIPT"
cat $CUR_SRC/RICE-1_0_3_1-1_0_3_2-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 310_4 $CUR_SRC $DEST_DIR/rice_server/bootstrap
cat $CUR_SRC/KR-RELEASE-3_1_SP4-Upgrade-MYSQL.sql |  grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 310_4 $CUR_SRC $DEST_DIR/rice_server/bootstrap
cat $CUR_SRC/KR-RELEASE-3_1_SP4-SR-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 310_4 $CUR_SRC $DEST_DIR/rice_data_only/bootstrap
cat $CUR_SRC/KRC-RELEASE-3_1_SP4-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 310 $CUR_SRC $DEST_DIR/kc/bootstrap
cat $CUR_SRC/KC-RELEASE-3_1_SP4-Upgrade-MYSQL.sql| grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 310_4 $CUR_SRC $DEST_DIR/kc/bootstrap

CUR_SRC="$SRC_DIR/KC-RELEASE-3_1_1-SCRIPT"
cat $CUR_SRC/KR-RELEASE-3_1_1-Upgrade-MYSQL.sql |  grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 311 $CUR_SRC $DEST_DIR/rice_server/bootstrap
cat $CUR_SRC/KR-RELEASE-3_1_1-SR-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 311 $CUR_SRC $DEST_DIR/rice_data_only/bootstrap
cat $CUR_SRC/KC-RELEASE-3_1_1-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 311 $CUR_SRC $DEST_DIR/kc/bootstrap

CUR_SRC="$SRC_DIR/KC-RELEASE-3_2-SCRIPT"
cat $CUR_SRC/KR-RELEASE-3_2-Upgrade-MYSQL.sql |  grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 320 $CUR_SRC $DEST_DIR/rice_server/bootstrap
cat $CUR_SRC/KR-RELEASE-3_2-SR-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 320 $CUR_SRC $DEST_DIR/rice_data_only/bootstrap
cat $CUR_SRC/KC-RELEASE-3_2-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 320 $CUR_SRC $DEST_DIR/kc/bootstrap

CUR_SRC="$SRC_DIR/KC-RELEASE-4_0-SCRIPT"
cat $CUR_SRC/KR_RICE-RELEASE-4_0-Upgrade-MYSQL.sql |  grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 400 $CUR_SRC $DEST_DIR/rice_server/bootstrap
cat $CUR_SRC/KR-RELEASE-4_0-Upgrade-MYSQL.sql |  grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 400 $CUR_SRC $DEST_DIR/rice_server/bootstrap
cat $CUR_SRC/KR-RELEASE-4_0-SR-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 400 $CUR_SRC $DEST_DIR/rice_data_only/bootstrap
cat $CUR_SRC/KRC_RICE-RELEASE-4_0-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 400 $CUR_SRC $DEST_DIR/kc/embedded_client_scripts
cat $CUR_SRC/KC_RICE-RELEASE-4_0-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 400 $CUR_SRC $DEST_DIR/kc/bootstrap
cat $CUR_SRC/KC-RELEASE-4_0-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 400 $CUR_SRC $DEST_DIR/kc/bootstrap

CUR_SRC="$SRC_DIR/KC-RELEASE-5_0-SCRIPT"
cat $CUR_SRC/KR_RICE-RELEASE-5_0-Upgrade-MYSQL.sql |  grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 500 $CUR_SRC $DEST_DIR/rice_server/bootstrap
cat $CUR_SRC/KR-RELEASE-5_0-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 500 $CUR_SRC $DEST_DIR/rice/bootstrap
cat $CUR_SRC/KRC_RICE-RELEASE-5_0-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 500 $CUR_SRC $DEST_DIR/kc/embedded_client_scripts
cat $CUR_SRC/KC-RELEASE-5_0-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 500 $CUR_SRC $DEST_DIR/kc/bootstrap

CUR_SRC="$SRC_DIR/KC-RELEASE-5_0_1-SCRIPT"
cat $CUR_SRC/KR-RELEASE-5_0_1-Upgrade-MYSQL.sql |  grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 501 $CUR_SRC $DEST_DIR/rice/bootstrap
cat $CUR_SRC/KC-RELEASE-5_0_1-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 501 $CUR_SRC $DEST_DIR/kc/bootstrap

CUR_SRC="$SRC_DIR/KC-RELEASE-5_1_0-SCRIPT"
cat $CUR_SRC/KR_RICE-RELEASE-5_1_0-Upgrade-MYSQL.sql |  grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 510 $CUR_SRC $DEST_DIR/rice_server/bootstrap
cat $CUR_SRC/KR-RELEASE-5_1_0-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 510 $CUR_SRC $DEST_DIR/rice/bootstrap
cat $CUR_SRC/KRC_RICE-RELEASE-5_1_0-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 510 $CUR_SRC $DEST_DIR/kc/embedded_client_scripts
cat $CUR_SRC/KC-RELEASE-5_1_0-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 510 $CUR_SRC $DEST_DIR/kc/bootstrap

CUR_SRC="$SRC_DIR/KC-RELEASE-5_1_1-SCRIPT"
cat $CUR_SRC/KR_RICE-RELEASE-5_1_1-Upgrade-MYSQL.sql |  grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 511 $CUR_SRC $DEST_DIR/rice_server/bootstrap
cat $CUR_SRC/KR-RELEASE-5_1_1-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 511 $CUR_SRC $DEST_DIR/rice/bootstrap
cat $CUR_SRC/KRC_RICE-RELEASE-5_1_1-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 511 $CUR_SRC $DEST_DIR/kc/embedded_client_scripts
cat $CUR_SRC/KC-RELEASE-5_1_1-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 511 $CUR_SRC $DEST_DIR/kc/bootstrap

CUR_SRC="$SRC_DIR/KC-RELEASE-5_2_0-SCRIPT"
cat $CUR_SRC/KR_RICE-RELEASE-5_2_0-Upgrade-MYSQL.sql |  grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 520 $CUR_SRC $DEST_DIR/rice_server/bootstrap
cat $CUR_SRC/KR-RELEASE-5_2_0-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 520 $CUR_SRC $DEST_DIR/rice/bootstrap
cat $CUR_SRC/KRC_RICE-RELEASE-5_2_0-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 520 $CUR_SRC $DEST_DIR/kc/embedded_client_scripts
cat $CUR_SRC/KC-RELEASE-5_2_0-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 520 $CUR_SRC $DEST_DIR/kc/bootstrap

CUR_SRC="$SRC_DIR/KC-RELEASE-5_2_1-SCRIPT"
cat $CUR_SRC/KR-RELEASE-5_2_1-Upgrade-MYSQL.sql |  grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 521 $CUR_SRC $DEST_DIR/rice/bootstrap
cat $CUR_SRC/KC-RELEASE-5_2_1-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 521 $CUR_SRC $DEST_DIR/kc/bootstrap

CUR_SRC="$SRC_DIR/KC-RELEASE-6_0_0-SCRIPT"
cat $CUR_SRC/KR_RICE-RELEASE-6_0_0-Upgrade-MYSQL.sql |  grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/rice_server/bootstrap
cat $CUR_SRC/KR-RELEASE-6_0_0-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/rice/bootstrap
cat $CUR_SRC/KRC_RICE-RELEASE-6_0_0-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/kc/embedded_client_scripts
cat $CUR_SRC/KC-RELEASE-6_0_0-Upgrade-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/kc/bootstrap

CUR_SRC="$SRC_DIR/KC-RELEASE-3_1-SCRIPT"
cat $CUR_SRC/KRC-RELEASE-3_1-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/kc/demo
cat $CUR_SRC/KC-RELEASE-3_1-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/kc/demo
cat $CUR_SRC/KR-RELEASE-3_1-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/rice/demo

CUR_SRC="$SRC_DIR/KC-RELEASE-3_1_1-SCRIPT"
cat $CUR_SRC/KC-RELEASE-3_1_1-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/kc/demo

CUR_SRC="$SRC_DIR/KC-RELEASE-3_2-SCRIPT"
cat $CUR_SRC/KC-RELEASE-3_2-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/kc/demo
cat $CUR_SRC/KR-RELEASE-3_2-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/rice/demo

CUR_SRC="$SRC_DIR/KC-RELEASE-4_0-SCRIPT"
cat $CUR_SRC/KC-RELEASE-4_0-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/kc/demo
cat $CUR_SRC/KR-RELEASE-4_0-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/rice/demo

CUR_SRC="$SRC_DIR/KC-RELEASE-5_0-SCRIPT"
cat $CUR_SRC/KC-RELEASE-5_0-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/kc/demo
cat $CUR_SRC/KR-RELEASE-5_0-Demo-MYSQL.sql| grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/rice/demo

CUR_SRC="$SRC_DIR/KC-RELEASE-5_0_1-SCRIPT"
cat $CUR_SRC/KC-RELEASE-5_0_1-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/kc/demo
cat $CUR_SRC/KR-RELEASE-5_0_1-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/rice/demo

CUR_SRC="$SRC_DIR/KC-RELEASE-5_1_0-SCRIPT"
cat $CUR_SRC/KC-RELEASE-5_1_0-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/kc/demo
cat $CUR_SRC/KR-RELEASE-5_1_0-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/rice/demo

CUR_SRC="$SRC_DIR/KC-RELEASE-5_2_0-SCRIPT"
cat $CUR_SRC/KR-RELEASE-5_2_0-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/rice/demo

CUR_SRC="$SRC_DIR/KC-RELEASE-6_0_0-SCRIPT"
cat $CUR_SRC/KC-RELEASE-6_0_0-Demo-MYSQL.sql | grep -v 'select ' | awk '{print $2}' | node $PREPEND_VERSION 600 $CUR_SRC $DEST_DIR/kc/demo

find $DEST_DIR -name "*.sql" -print | xargs sed -i 's/\_BS\_S/_S/g'
#need to double check for _BS_S and replace with _S to remove bootstrap sequences



