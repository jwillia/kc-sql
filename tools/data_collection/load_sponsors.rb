#!/usr/bin/env ruby

require 'rubygems'
require 'bundler/setup'
require 'kuali_toolbox/etl/grm'

ETL = KualiCo::ETL
GRM = KualiCo::ETL::GRM
TextParseError = KualiCo::ETL::TextParseError

opt = ETL.parse_csv_command_line_options (File.basename $0), ARGF.argv

File.open(opt[:sql_filename], "w") do |sql|
  sql.write "
-- Depends on running RemoveAllTransactionalData() beforehand.

DROP PROCEDURE IF EXISTS LoadSponsors;

DELIMITER //
CREATE PROCEDURE LoadSponsors()
BEGIN

DECLARE exit handler for sqlexception
BEGIN
-- ERROR
SHOW ERRORS;
ROLLBACK;
END;

START TRANSACTION;

delete from award_template_terms;
delete from award_template_report_terms;
delete from award_template_contact;
delete from award_template;
delete from sponsor_forms;
delete from sponsor;

"
  CSV.open(opt[:csv_filename], "r", opt[:csv_options]) do |csv|
    csv.find_all do |row| # begin processing csv rows
      begin
        # | sponsor | CREATE TABLE `sponsor` (
        insert_str = "insert into sponsor ("
        values_str = "values ("

        #   `SPONSOR_CODE` char(6) COLLATE utf8_bin NOT NULL DEFAULT '',
        GRM.parse_sponsor_code! row, insert_str, values_str

        #   `SPONSOR_NAME` varchar(200) COLLATE utf8_bin DEFAULT NULL,
        sponsor_name = ETL.parse_string row[:sponsor_name], name: "sponsor_name", length: 200
        insert_str.concat "SPONSOR_NAME,"
        values_str.concat "'#{sponsor_name}',"

        #   `ACRONYM` varchar(10) COLLATE utf8_bin DEFAULT NULL,
        acronym = ETL.parse_string row[:acronym], name: "acronym", length: 10
        insert_str.concat "ACRONYM,"
        values_str.concat "'#{acronym}',"

        #   `SPONSOR_TYPE_CODE` varchar(3) COLLATE utf8_bin NOT NULL,
        sponsor_type_code = ETL.parse_string row[:sponsor_type_code],
          name: "sponsor_type_code", length: 3, required: true
        insert_str.concat "SPONSOR_TYPE_CODE,"
        values_str.concat "'#{sponsor_type_code}',"

        #   `DUN_AND_BRADSTREET_NUMBER` varchar(20) COLLATE utf8_bin DEFAULT NULL,
        dun_and_bradstreet_number = ETL.parse_string row[:dun_and_bradstreet_number],
          name: "dun_and_bradstreet_number", length: 20
        insert_str.concat "DUN_AND_BRADSTREET_NUMBER,"
        values_str.concat "'#{dun_and_bradstreet_number}',"

        #   `DUNS_PLUS_FOUR_NUMBER` varchar(20) COLLATE utf8_bin DEFAULT NULL,
        duns_plus_four_number = ETL.parse_string row[:duns_plus_four_number],
          name: "duns_plus_four_number", length: 20
        insert_str.concat "DUNS_PLUS_FOUR_NUMBER,"
        values_str.concat "'#{duns_plus_four_number}',"

        #   `DODAC_NUMBER` varchar(20) COLLATE utf8_bin DEFAULT NULL,
        dodac_number = ETL.parse_string row[:dodac_number], name: "dodac_number", length: 20
        insert_str.concat "DODAC_NUMBER,"
        values_str.concat "'#{dodac_number}',"

        #   `CAGE_NUMBER` varchar(20) COLLATE utf8_bin DEFAULT NULL,
        cage_number = ETL.parse_string row[:cage_number], name: "cage_number", length: 20
        insert_str.concat "CAGE_NUMBER,"
        values_str.concat "'#{cage_number}',"

        #   `POSTAL_CODE` varchar(15) COLLATE utf8_bin DEFAULT NULL,
        GRM.parse_postal_code! row, insert_str, values_str

        #   `STATE` varchar(30) COLLATE utf8_bin DEFAULT NULL,
        GRM.parse_state! row, insert_str, values_str

        #   `COUNTRY_CODE` char(3) COLLATE utf8_bin DEFAULT NULL,
        GRM.parse_country_code! row, insert_str, values_str

        #   `ROLODEX_ID` decimal(6,0) NOT NULL,
        GRM.parse_rolodex_id! row, insert_str, values_str

        #   `AUDIT_REPORT_SENT_FOR_FY` char(4) COLLATE utf8_bin DEFAULT NULL,
        audit_report_sent_for_fy = ETL.parse_string row[:audit_report_sent_for_fy],
          name: "audit_report_sent_for_fy", length: 4
        insert_str.concat "AUDIT_REPORT_SENT_FOR_FY,"
        values_str.concat "'#{audit_report_sent_for_fy}',"

        #   `OWNED_BY_UNIT` varchar(8) COLLATE utf8_bin NOT NULL,
        GRM.parse_owned_by_unit! row, insert_str, values_str

        #   `ACTV_IND` varchar(1) COLLATE utf8_bin DEFAULT 'Y',
        GRM.parse_actv_ind! row, insert_str, values_str

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
COMMIT;

END //
DELIMITER ;

call LoadSponsors();
"

end # sql

puts "\nSQL written to #{opt[:sql_filename]}\n\n"
