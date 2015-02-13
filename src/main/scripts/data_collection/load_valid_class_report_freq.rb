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

DROP PROCEDURE IF EXISTS LoadValidClassReportReq;

DELIMITER //
CREATE PROCEDURE LoadValidClassReportReq()
BEGIN

DECLARE exit handler for sqlexception
BEGIN
-- ERROR
SHOW ERRORS;
ROLLBACK;
END;

START TRANSACTION;

delete from valid_class_report_freq;

"
  CSV.open(opt[:csv_filename], "r", opt[:csv_options]) do |csv|
    csv.find_all do |row| # begin processing csv rows
      begin
        # | CREATE TABLE `valid_class_report_freq` (
        insert_str = "insert into valid_class_report_freq ("
        values_str = "values ("

        #   `VALID_CLASS_REPORT_FREQ_ID` decimal(12,0) NOT NULL DEFAULT '0',
        ETL.parse_integer! row, insert_str, values_str,
          name: "VALID_CLASS_REPORT_FREQ_ID", required: true, length: 12

        #   `REPORT_CLASS_CODE` varchar(3) COLLATE utf8_bin NOT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "REPORT_CLASS_CODE", required: true, length: 3

        #   `REPORT_CODE` varchar(3) COLLATE utf8_bin NOT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "REPORT_CODE", required: true, length: 3

        #   `FREQUENCY_CODE` varchar(3) COLLATE utf8_bin NOT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "FREQUENCY_CODE", required: true, length: 3

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

call LoadValidClassReportReq();
"

end # sql

puts "\nSQL written to #{opt[:sql_filename]}\n\n"
