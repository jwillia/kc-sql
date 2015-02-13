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
DROP PROCEDURE IF EXISTS LoadSponsorTerm;

DELIMITER //
CREATE PROCEDURE LoadSponsorTerm()
BEGIN

DECLARE exit handler for sqlexception
BEGIN
-- ERROR
SHOW ERRORS;
ROLLBACK;
END;

START TRANSACTION;

delete from award_template_terms;
delete from award_sponsor_term;
delete from sponsor_term;

"
  # | sponsor_term | CREATE TABLE `sponsor_term` (
  #   `VER_NBR` decimal(8,0) NOT NULL DEFAULT '1',
  #   `SPONSOR_TERM_ID` decimal(12,0) NOT NULL DEFAULT '0',
  #   `SPONSOR_TERM_CODE` varchar(3) COLLATE utf8_bin NOT NULL,
  #   `SPONSOR_TERM_TYPE_CODE` varchar(3) COLLATE utf8_bin NOT NULL,
  #   `DESCRIPTION` varchar(200) COLLATE utf8_bin NOT NULL,
  #   `UPDATE_TIMESTAMP` datetime NOT NULL,
  #   `UPDATE_USER` varchar(60) COLLATE utf8_bin NOT NULL,
  #   `OBJ_ID` varchar(36) COLLATE utf8_bin NOT NULL,
  #   PRIMARY KEY (`SPONSOR_TERM_ID`),
  #   KEY `U_SPONSOR_TERM` (`SPONSOR_TERM_CODE`,`SPONSOR_TERM_TYPE_CODE`),
  #   KEY `FK1_SPONSOR_TERM` (`SPONSOR_TERM_TYPE_CODE`),
  #   CONSTRAINT `FK1_SPONSOR_TERM` FOREIGN KEY (`SPONSOR_TERM_TYPE_CODE`) REFERENCES `sponsor_term_type` (`SPONSOR_TERM_TYPE_CODE`)
  # )

  CSV.open(opt[:csv_filename], "r", opt[:csv_options]) do |csv|
    csv.find_all do |row| # begin processing csv rows
      begin
        # | sponsor_term | CREATE TABLE `sponsor_term` (
        insert_str = "insert into sponsor_term ("
        values_str = "values ("

        #   `SPONSOR_TERM_ID` decimal(12,0) NOT NULL DEFAULT '0',
        ETL.parse_integer! row, insert_str, values_str,
          name: "SPONSOR_TERM_ID", required: true, length: 12

        #   `SPONSOR_TERM_CODE` varchar(3) COLLATE utf8_bin NOT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "SPONSOR_TERM_CODE", required: true, length: 3

        #   `SPONSOR_TERM_TYPE_CODE` varchar(3) COLLATE utf8_bin NOT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "SPONSOR_TERM_TYPE_CODE", required: true, length: 3

        #   `DESCRIPTION` varchar(200) COLLATE utf8_bin NOT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "DESCRIPTION", required: true, length: 200

        # `UPDATE_TIMESTAMP` datetime NOT NULL,
        insert_str.concat "UPDATE_TIMESTAMP,"
        values_str.concat "NOW(),"

        # `UPDATE_USER` varchar(60) COLLATE utf8_bin NOT NULL,
        insert_str.concat "UPDATE_USER,"
        values_str.concat "'admin',"

        # `VER_NBR` decimal(8,0) NOT NULL DEFAULT '1',
        insert_str.concat "VER_NBR,"
        values_str.concat "'1',"

        # `OBJ_ID` varchar(36) COLLATE utf8_bin NOT NULL,
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

call LoadSponsorTerm();
"

end # sql

puts "\nSQL written to #{opt[:sql_filename]}\n\n"
