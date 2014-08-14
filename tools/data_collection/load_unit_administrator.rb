#!/usr/bin/env ruby

require 'rubygems'
require 'bundler/setup'
require 'rsmart_toolbox/etl/grm'

ETL = Rsmart::ETL
GRM = Rsmart::ETL::GRM
TextParseError = Rsmart::ETL::TextParseError

opt = ETL.parse_csv_command_line_options (File.basename $0), ARGF.argv

File.open(opt[:sql_filename], "w") do |sql|
  sql.write "
DROP PROCEDURE IF EXISTS LoadUnitAdministrator;

DELIMITER //
CREATE PROCEDURE LoadUnitAdministrator()
BEGIN

DECLARE exit handler for sqlexception
BEGIN
-- ERROR
SHOW ERRORS;
ROLLBACK;
END;

START TRANSACTION;

delete from unit_administrator;

"
  CSV.open(opt[:csv_filename], "r", opt[:csv_options]) do |csv|
    csv.find_all do |row| # begin processing csv rows
      begin
        # | CREATE TABLE `valid_class_report_freq` (
        insert_str = "insert into unit_administrator ("
        values_str = "values ("

        #   `UNIT_NUMBER` varchar(8) COLLATE utf8_bin NOT NULL DEFAULT '',
        ETL.parse_string! row, insert_str, values_str,
          name: "UNIT_NUMBER", required: true, length: 8

        #   `PERSON_ID` varchar(40) COLLATE utf8_bin NOT NULL DEFAULT '',
        ETL.parse_string! row, insert_str, values_str,
          name: "PERSON_ID", required: true, length: 40

        #   `UNIT_ADMINISTRATOR_TYPE_CODE` varchar(3) COLLATE utf8_bin NOT NULL DEFAULT '',
        ETL.parse_string! row, insert_str, values_str,
          name: "UNIT_ADMINISTRATOR_TYPE_CODE", required: true, length: 3

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

call LoadUnitAdministrator();
"

end # sql

puts "\nSQL written to #{opt[:sql_filename]}\n\n"
