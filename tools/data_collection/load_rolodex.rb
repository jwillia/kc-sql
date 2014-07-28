#!/usr/bin/env ruby

require 'rubygems'
require 'bundler/setup'

require 'csv'
require 'optparse'
require 'ostruct'
require 'pp'
require './lib/CX.rb'

opt = CX.parse_csv_command_line_options (File.basename $0), ARGF.argv

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

delete from rolodex where ROLODEX_ID != '1';

CREATE table temporary_rolodex_table AS SELECT * FROM rolodex WHERE ROLODEX_ID='1';
UPDATE temporary_rolodex_table SET ROLODEX_ID='999999';
INSERT INTO rolodex SELECT * FROM temporary_rolodex_table;
DROP TABLE temporary_rolodex_table;

update sponsor set ROLODEX_ID='999999';
delete from rolodex where ROLODEX_ID='1';

"
  CSV.open(opt[:csv_filename], "r", opt[:csv_options]) do |csv|
    csv.find_all do |row| # begin processing csv rows
      begin
      insert_str = "insert into rolodex ("
      values_str = "values ("

      #   `ROLODEX_ID` decimal(6,0) NOT NULL DEFAULT '0',
      CX.parse_rolodex_id! row, insert_str, values_str

      #   `LAST_NAME` varchar(20) COLLATE utf8_bin DEFAULT NULL,
      last_name = CX.parse_string row[:last_name], name: "last_name", length: 20
      insert_str += "LAST_NAME,"
      values_str += "'#{last_name}',"

      #   `FIRST_NAME` varchar(20) COLLATE utf8_bin DEFAULT NULL,
      first_name = CX.parse_string row[:first_name], name: "first_name", length: 20
      insert_str += "FIRST_NAME,"
      values_str += "'#{first_name}',"

      #   `MIDDLE_NAME` varchar(20) COLLATE utf8_bin DEFAULT NULL,
      middle_name = CX.parse_string row[:middle_name], name: "middle_name", length: 20
      insert_str += "MIDDLE_NAME,"
      values_str += "'#{middle_name}',"

      #   `SUFFIX` varchar(10) COLLATE utf8_bin DEFAULT NULL,
      suffix = CX.parse_string row[:suffix], name: "suffix", length: 10
      insert_str += "SUFFIX,"
      values_str += "'#{suffix}',"

      #   `PREFIX` varchar(10) COLLATE utf8_bin DEFAULT NULL,
      prefix = CX.parse_string row[:prefix], name: "prefix", length: 10
      insert_str += "PREFIX,"
      values_str += "'#{prefix}',"

      #   `TITLE` varchar(35) COLLATE utf8_bin DEFAULT NULL,
      title = CX.parse_string row[:title], name: "title", length: 35
      insert_str += "TITLE,"
      values_str += "'#{title}',"

      #   `ORGANIZATION` varchar(200) COLLATE utf8_bin DEFAULT NULL,
      organization = CX.parse_string row[:organization], name: "organization", length: 200
      insert_str += "ORGANIZATION,"
      values_str += "'#{organization}',"

      #   `ADDRESS_LINE_1` varchar(80) COLLATE utf8_bin DEFAULT NULL,
      address_line_1 = CX.parse_string row[:address_line_1], name: "address_line_1", length: 80
      insert_str += "ADDRESS_LINE_1,"
      values_str += "'#{address_line_1}',"

      #   `ADDRESS_LINE_2` varchar(80) COLLATE utf8_bin DEFAULT NULL,
      address_line_2 = CX.parse_string row[:address_line_2], name: "address_line_2", length: 80
      insert_str += "ADDRESS_LINE_2,"
      values_str += "'#{address_line_2}',"

      #   `ADDRESS_LINE_3` varchar(80) COLLATE utf8_bin DEFAULT NULL,
      address_line_3 = CX.parse_string row[:address_line_3], name: "address_line_3", length: 80
      insert_str += "ADDRESS_LINE_3,"
      values_str += "'#{address_line_3}',"

      #   `FAX_NUMBER` varchar(20) COLLATE utf8_bin DEFAULT NULL,
      fax_number = CX.parse_string row[:fax_number], name: "fax_number", length: 20
      insert_str += "FAX_NUMBER,"
      values_str += "'#{fax_number}',"

      #   `EMAIL_ADDRESS` varchar(60) COLLATE utf8_bin DEFAULT NULL,
      CX.parse_email_address! row, insert_str, values_str

      #   `CITY` varchar(30) COLLATE utf8_bin DEFAULT NULL,
      city = CX.parse_string row[:city], name: "city", length: 30
      insert_str += "CITY,"
      values_str += "'#{city}',"

      #   `COUNTY` varchar(30) COLLATE utf8_bin DEFAULT NULL,
      county = CX.parse_string row[:county], name: "county", length: 30
      insert_str += "COUNTY,"
      values_str += "'#{county}',"

      #   `STATE` varchar(30) COLLATE utf8_bin DEFAULT NULL,
      CX.parse_state! row, insert_str, values_str

      #   `POSTAL_CODE` varchar(15) COLLATE utf8_bin DEFAULT NULL,
      CX.parse_postal_code! row, insert_str, values_str

      #   `COMMENTS` varchar(300) COLLATE utf8_bin DEFAULT NULL,
      comments = CX.parse_string row[:comments], name: "comments", length: 300
      insert_str += "COMMENTS,"
      values_str += "'#{comments}',"

      #   `PHONE_NUMBER` varchar(20) COLLATE utf8_bin DEFAULT NULL,
      phone_number = CX.parse_string row[:phone_number], name: "phone_number", length: 20
      insert_str += "PHONE_NUMBER,"
      values_str += "'#{phone_number}',"

      #   `COUNTRY_CODE` char(3) COLLATE utf8_bin DEFAULT NULL,
      CX.parse_country_code! row, insert_str, values_str

      #   `SPONSOR_CODE` char(6) COLLATE utf8_bin DEFAULT NULL,
      CX.parse_sponsor_code! row, insert_str, values_str

      #   `OWNED_BY_UNIT` varchar(8) COLLATE utf8_bin NOT NULL,
      CX.parse_owned_by_unit! row, insert_str, values_str

      #   `SPONSOR_ADDRESS_FLAG` char(1) COLLATE utf8_bin NOT NULL,
      sponsor_address_flag = CX.parse_flag row[:sponsor_address_flag],
        name: "sponsor_address_flag", default: "N",
        valid_values: @y_n_valid_values
      insert_str += "SPONSOR_ADDRESS_FLAG,"
      values_str += "'#{sponsor_address_flag}',"

      #   `ACTV_IND` varchar(1) COLLATE utf8_bin DEFAULT 'Y',
      CX.parse_actv_ind! row, insert_str, values_str

      insert_str += "CREATE_USER,"
      values_str += "'admin',"
      insert_str += "UPDATE_TIMESTAMP,"
      values_str += "NOW(),"
      insert_str += "UPDATE_USER,"
      values_str += "'admin',"
      insert_str += "VER_NBR,"
      values_str += "'1',"
      insert_str += "OBJ_ID,"
      values_str += "UUID(),"

      insert_str.chomp!(",")
      values_str.chomp!(",")
      insert_str += ")"
      values_str += ");"
      sql.write "#{insert_str} #{values_str}\n"

      rescue TextParseError => e
        puts e.message
      end
    end # row

  end # csv

  sql.write "
update sponsor set ROLODEX_ID='1';
delete from rolodex where ROLODEX_ID='999999';

COMMIT;

END //
DELIMITER ;

call LoadRolodex();
"

end # sql
