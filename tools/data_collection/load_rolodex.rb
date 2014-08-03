#!/usr/bin/env ruby

require 'rubygems'
require 'bundler/setup'
require 'rsmart_toolbox/etl/grm'

ETL = RsmartToolbox::ETL
GRM = RsmartToolbox::ETL::GRM

opt = ETL.parse_csv_command_line_options (File.basename $0), ARGF.argv

File.open(opt[:sql_filename], "w") do |sql|
  sql.write "
DROP PROCEDURE IF EXISTS LoadRolodex;

DELIMITER //
CREATE PROCEDURE LoadRolodex()
BEGIN

DECLARE exit handler for sqlexception
BEGIN
-- ERROR
SHOW ERRORS;
ROLLBACK;
END;

START TRANSACTION;

update sponsor set ROLODEX_ID=1;
update subaward_contact set ROLODEX_ID=1;
update award_template_contact set ROLODEX_ID=1;
delete from rolodex where ROLODEX_ID != 1;

CREATE table temporary_rolodex_table AS SELECT * FROM rolodex WHERE ROLODEX_ID=1;
UPDATE temporary_rolodex_table SET ROLODEX_ID=0 WHERE ROLODEX_ID=1;
INSERT INTO rolodex SELECT * FROM temporary_rolodex_table WHERE ROLODEX_ID=0;
DROP TABLE temporary_rolodex_table;

update sponsor set ROLODEX_ID=0;
update subaward_contact set ROLODEX_ID=0;
update award_template_contact set ROLODEX_ID=0;
delete from rolodex where ROLODEX_ID=1;

"
  CSV.open(opt[:csv_filename], "r", opt[:csv_options]) do |csv|
    csv.find_all do |row| # begin processing csv rows
      begin
        insert_str = "insert into rolodex ("
        values_str = "values ("

        #   `ROLODEX_ID` decimal(6,0) NOT NULL DEFAULT '0',
        # TODO add validation to ensure a row exists where ROLODEX_ID=1
        GRM.parse_rolodex_id! row, insert_str, values_str

        #   `LAST_NAME` varchar(20) COLLATE utf8_bin DEFAULT NULL,
        last_name = ETL.parse_string row[:last_name], name: "last_name", length: 20
        insert_str.concat "LAST_NAME,"
        values_str.concat "'#{last_name}',"

        #   `FIRST_NAME` varchar(20) COLLATE utf8_bin DEFAULT NULL,
        first_name = ETL.parse_string row[:first_name], name: "first_name", length: 20
        insert_str.concat "FIRST_NAME,"
        values_str.concat "'#{first_name}',"

        #   `MIDDLE_NAME` varchar(20) COLLATE utf8_bin DEFAULT NULL,
        middle_name = ETL.parse_string row[:middle_name], name: "middle_name", length: 20
        insert_str.concat "MIDDLE_NAME,"
        values_str.concat "'#{middle_name}',"

        #   `SUFFIX` varchar(10) COLLATE utf8_bin DEFAULT NULL,
        suffix = ETL.parse_string row[:suffix], name: "suffix", length: 10
        insert_str.concat "SUFFIX,"
        values_str.concat "'#{suffix}',"

        #   `PREFIX` varchar(10) COLLATE utf8_bin DEFAULT NULL,
        prefix = ETL.parse_string row[:prefix], name: "prefix", length: 10
        insert_str.concat "PREFIX,"
        values_str.concat "'#{prefix}',"

        #   `TITLE` varchar(35) COLLATE utf8_bin DEFAULT NULL,
        title = ETL.parse_string row[:title], name: "title", length: 35
        insert_str.concat "TITLE,"
        values_str.concat "'#{title}',"

        #   `ORGANIZATION` varchar(200) COLLATE utf8_bin DEFAULT NULL,
        organization = ETL.parse_string row[:organization], name: "organization", length: 200
        insert_str.concat "ORGANIZATION,"
        values_str.concat "'#{organization}',"

        #   `ADDRESS_LINE_1` varchar(80) COLLATE utf8_bin DEFAULT NULL,
        address_line_1 = ETL.parse_string row[:address_line_1], name: "address_line_1", length: 80
        insert_str.concat "ADDRESS_LINE_1,"
        values_str.concat "'#{address_line_1}',"

        #   `ADDRESS_LINE_2` varchar(80) COLLATE utf8_bin DEFAULT NULL,
        address_line_2 = ETL.parse_string row[:address_line_2], name: "address_line_2", length: 80
        insert_str.concat "ADDRESS_LINE_2,"
        values_str.concat "'#{address_line_2}',"

        #   `ADDRESS_LINE_3` varchar(80) COLLATE utf8_bin DEFAULT NULL,
        address_line_3 = ETL.parse_string row[:address_line_3], name: "address_line_3", length: 80
        insert_str.concat "ADDRESS_LINE_3,"
        values_str.concat "'#{address_line_3}',"

        #   `FAX_NUMBER` varchar(20) COLLATE utf8_bin DEFAULT NULL,
        fax_number = ETL.parse_string row[:fax_number], name: "fax_number", length: 20
        insert_str.concat "FAX_NUMBER,"
        values_str.concat "'#{fax_number}',"

        #   `EMAIL_ADDRESS` varchar(60) COLLATE utf8_bin DEFAULT NULL,
        GRM.parse_email_address! row, insert_str, values_str

        #   `CITY` varchar(30) COLLATE utf8_bin DEFAULT NULL,
        city = ETL.parse_string row[:city], name: "city", length: 30
        insert_str.concat "CITY,"
        values_str.concat "'#{city}',"

        #   `COUNTY` varchar(30) COLLATE utf8_bin DEFAULT NULL,
        county = ETL.parse_string row[:county], name: "county", length: 30
        insert_str.concat "COUNTY,"
        values_str.concat "'#{county}',"

        #   `STATE` varchar(30) COLLATE utf8_bin DEFAULT NULL,
        GRM.parse_state! row, insert_str, values_str

        #   `POSTAL_CODE` varchar(15) COLLATE utf8_bin DEFAULT NULL,
        GRM.parse_postal_code! row, insert_str, values_str

        #   `COMMENTS` varchar(300) COLLATE utf8_bin DEFAULT NULL,
        comments = ETL.parse_string row[:comments], name: "comments", length: 300
        insert_str.concat "COMMENTS,"
        values_str.concat "'#{comments}',"

        #   `PHONE_NUMBER` varchar(20) COLLATE utf8_bin DEFAULT NULL,
        phone_number = ETL.parse_string row[:phone_number], name: "phone_number", length: 20
        insert_str.concat "PHONE_NUMBER,"
        values_str.concat "'#{phone_number}',"

        #   `COUNTRY_CODE` char(3) COLLATE utf8_bin DEFAULT NULL,
        GRM.parse_country_code! row, insert_str, values_str

        #   `SPONSOR_CODE` char(6) COLLATE utf8_bin DEFAULT NULL,
        GRM.parse_sponsor_code! row, insert_str, values_str

        #   `OWNED_BY_UNIT` varchar(8) COLLATE utf8_bin NOT NULL,
        GRM.parse_owned_by_unit! row, insert_str, values_str

        #   `SPONSOR_ADDRESS_FLAG` char(1) COLLATE utf8_bin NOT NULL,
        sponsor_address_flag = ETL.parse_flag row[:sponsor_address_flag],
          name: "sponsor_address_flag", default: "N",
          valid_values: @y_n_valid_values
        insert_str.concat "SPONSOR_ADDRESS_FLAG,"
        values_str.concat "'#{sponsor_address_flag}',"

        #   `ACTV_IND` varchar(1) COLLATE utf8_bin DEFAULT 'Y',
        ETL.parse_actv_ind! row, insert_str, values_str

        insert_str.concat "CREATE_USER,"
        values_str.concat "'admin',"
        insert_str.concat "UPDATE_TIMESTAMP,"
        values_str.concat "NOW(),"
        insert_str.concat "UPDATE_USER,"
        values_str.concat "'admin',"
        insert_str.concat "VER_NBR,"
        values_str.concat "'1',"
        insert_str.concat "OBJ_ID,"
        values_str.concat "UUID(),"

        insert_str.chomp!(",")
        values_str.chomp!(",")
        insert_str.concat ")"
        values_str.concat ");"
        sql.write "#{insert_str} #{values_str}\n"

      rescue TextParseError => e
        puts e.message
      end
    end # row

  end # csv

  sql.write "
update sponsor set ROLODEX_ID=1;
update subaward_contact set ROLODEX_ID=1;
update award_template_contact set ROLODEX_ID=1;
delete from rolodex where ROLODEX_ID=0;

COMMIT;

END //
DELIMITER ;

call LoadRolodex();
"

end # sql
