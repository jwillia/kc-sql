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
DROP PROCEDURE IF EXISTS LoadOrganization;

DELIMITER //
CREATE PROCEDURE LoadOrganization()
BEGIN

DECLARE exit handler for sqlexception
BEGIN
-- ERROR
SHOW ERRORS;
ROLLBACK;
END;

START TRANSACTION;

delete from organization_ynq;
delete from organization;

"
  # | organization | CREATE TABLE `organization` (
  #   `ORGANIZATION_ID` varchar(8) COLLATE utf8_bin NOT NULL DEFAULT '',
  #   `ORGANIZATION_NAME` varchar(60) COLLATE utf8_bin NOT NULL,
  #   `CONTACT_ADDRESS_ID` decimal(6,0) NOT NULL,
  #   `ADDRESS` varchar(60) COLLATE utf8_bin DEFAULT NULL,
  #   `CABLE_ADDRESS` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  #   `TELEX_NUMBER` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  #   `COUNTY` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  #   `CONGRESSIONAL_DISTRICT` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  #   `INCORPORATED_IN` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  #   `INCORPORATED_DATE` datetime DEFAULT NULL,
  #   `NUMBER_OF_EMPLOYEES` decimal(6,0) DEFAULT NULL,
  #   `IRS_TAX_EXCEMPTION` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  #   `FEDRAL_EMPLOYER_ID` varchar(15) COLLATE utf8_bin DEFAULT NULL,
  #   `MASS_TAX_EXCEMPT_NUM` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  #   `AGENCY_SYMBOL` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  #   `VENDOR_CODE` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  #   `COM_GOV_ENTITY_CODE` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  #   `MASS_EMPLOYEE_CLAIM` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  #   `DUNS_NUMBER` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  #   `DUNS_PLUS_FOUR_NUMBER` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  #   `DODAC_NUMBER` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  #   `CAGE_NUMBER` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  #   `HUMAN_SUB_ASSURANCE` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  #   `ANIMAL_WELFARE_ASSURANCE` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  #   `SCIENCE_MISCONDUCT_COMPL_DATE` datetime DEFAULT NULL,
  #   `PHS_ACOUNT` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  #   `NSF_INSTITUTIONAL_CODE` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  #   `INDIRECT_COST_RATE_AGREEMENT` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  #   `COGNIZANT_AUDITOR` decimal(6,0) DEFAULT NULL,
  #   `ONR_RESIDENT_REP` decimal(6,0) DEFAULT NULL,
  #   `UPDATE_TIMESTAMP` datetime NOT NULL,
  #   `UPDATE_USER` varchar(60) COLLATE utf8_bin NOT NULL,
  #   `VER_NBR` decimal(8,0) NOT NULL DEFAULT '1',
  #   `OBJ_ID` varchar(36) COLLATE utf8_bin NOT NULL,
  #   PRIMARY KEY (`ORGANIZATION_ID`)

  CSV.open(opt[:csv_filename], "r", opt[:csv_options]) do |csv|
    csv.find_all do |row| # begin processing csv rows
      begin
        # | organization | CREATE TABLE `organization` (
        insert_str = "insert into organization ("
        values_str = "values ("

        #   `ORGANIZATION_ID` varchar(8) COLLATE utf8_bin NOT NULL DEFAULT '',
        ETL.parse_string! row, insert_str, values_str,
          name: "ORGANIZATION_ID", required: true, length: 8

        #   `ORGANIZATION_NAME` varchar(60) COLLATE utf8_bin NOT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "ORGANIZATION_NAME", required: true, length: 60

        #   `CONTACT_ADDRESS_ID` decimal(6,0) NOT NULL,
        ETL.parse_integer! row, insert_str, values_str,
          name: "CONTACT_ADDRESS_ID", required: true, length: 6

        #   `ADDRESS` varchar(60) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "ADDRESS", length: 60

        #   `CABLE_ADDRESS` varchar(20) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "CABLE_ADDRESS", length: 20

        #   `TELEX_NUMBER` varchar(20) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "TELEX_NUMBER", length: 20

        #   `COUNTY` varchar(30) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "COUNTY", length: 30

        #   `CONGRESSIONAL_DISTRICT` varchar(50) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "CONGRESSIONAL_DISTRICT", length: 50

        #   `INCORPORATED_IN` varchar(50) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "INCORPORATED_IN", length: 50

        #   `INCORPORATED_DATE` datetime DEFAULT NULL,
        ETL.parse_datetime! row, insert_str, values_str,
          name: "INCORPORATED_DATE"

        #   `NUMBER_OF_EMPLOYEES` decimal(6,0) DEFAULT NULL,
        ETL.parse_integer! row, insert_str, values_str,
          name: "NUMBER_OF_EMPLOYEES", length: 6

        #   `IRS_TAX_EXCEMPTION` varchar(30) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "IRS_TAX_EXCEMPTION", length: 30

        #   `FEDRAL_EMPLOYER_ID` varchar(15) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "FEDRAL_EMPLOYER_ID", length: 15

        #   `MASS_TAX_EXCEMPT_NUM` varchar(30) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "MASS_TAX_EXCEMPT_NUM", length: 30

        #   `AGENCY_SYMBOL` varchar(30) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "AGENCY_SYMBOL", length: 30

        #   `VENDOR_CODE` varchar(30) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "VENDOR_CODE", length: 30

        #   `COM_GOV_ENTITY_CODE` varchar(30) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "COM_GOV_ENTITY_CODE", length: 30

        #   `MASS_EMPLOYEE_CLAIM` varchar(30) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "MASS_EMPLOYEE_CLAIM", length: 30

        #   `DUNS_NUMBER` varchar(20) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "DUNS_NUMBER", length: 20

        #   `DUNS_PLUS_FOUR_NUMBER` varchar(20) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "DUNS_PLUS_FOUR_NUMBER", length: 20

        #   `DODAC_NUMBER` varchar(20) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "DODAC_NUMBER", length: 20

        #   `CAGE_NUMBER` varchar(20) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "CAGE_NUMBER", length: 20

        #   `HUMAN_SUB_ASSURANCE` varchar(30) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "HUMAN_SUB_ASSURANCE", length: 30

        #   `ANIMAL_WELFARE_ASSURANCE` varchar(20) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "ANIMAL_WELFARE_ASSURANCE", length: 20

        #   `SCIENCE_MISCONDUCT_COMPL_DATE` datetime DEFAULT NULL,
        ETL.parse_datetime! row, insert_str, values_str,
          name: "SCIENCE_MISCONDUCT_COMPL_DATE"

        #   `PHS_ACOUNT` varchar(30) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "PHS_ACOUNT", length: 30

        #   `NSF_INSTITUTIONAL_CODE` varchar(30) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "NSF_INSTITUTIONAL_CODE", length: 30

        #   `INDIRECT_COST_RATE_AGREEMENT` varchar(50) COLLATE utf8_bin DEFAULT NULL,
        ETL.parse_string! row, insert_str, values_str,
          name: "INDIRECT_COST_RATE_AGREEMENT", length: 50

        #   `COGNIZANT_AUDITOR` decimal(6,0) DEFAULT NULL,
        ETL.parse_integer! row, insert_str, values_str,
          name: "COGNIZANT_AUDITOR", length: 6

        #   `ONR_RESIDENT_REP` decimal(6,0) DEFAULT NULL,
        ETL.parse_integer! row, insert_str, values_str,
          name: "ONR_RESIDENT_REP", length: 6


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

call LoadOrganization();
"

end # sql

puts "\nSQL written to #{opt[:sql_filename]}\n\n"
