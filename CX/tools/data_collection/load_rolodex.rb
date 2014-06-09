#!/usr/bin/env ruby

require 'csv'

csv_filename = 'PDX_ADDRESSBOOK_LOAD.csv'
sql_output = 'PDX_ADDRESSBOOK_LOAD.sql'

options = { headers: :first_row,
            header_converters: :symbol,
            skip_blanks: true,
            }

File.open(sql_output, "w") do |sql|
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
  CSV.open(csv_filename, "r", options) do |csv|
    csv.find_all do |row| # begin processing csv rows
      sponsor_address_flag = row[:sponsor_address_flag]
      unless sponsor_address_flag =~ /^(Y|N)$/
        raise "Illegal SPONSOR_ADDRESS_FLAG value: '#{sponsor_address_flag}'. Must be Y or N."
      end
      insert_str = "insert into rolodex ("
      values_str = "values ("

      insert_str += "ROLODEX_ID,"
      values_str += "'#{row[:rolodex_id]}',"
      insert_str += "LAST_NAME,"
      values_str += "'#{row[:last_name]}',"
      insert_str += "FIRST_NAME,"
      values_str += "'#{row[:first_name]}',"
      insert_str += "MIDDLE_NAME,"
      values_str += "'#{row[:middle_name]}',"
      insert_str += "ADDRESS_LINE_1,"
      values_str += "'#{row[:address_line_1]}',"
      insert_str += "ADDRESS_LINE_2,"
      values_str += "'#{row[:address_line_2]}',"
      insert_str += "FAX_NUMBER,"
      values_str += "'#{row[:fax_number]}',"
      insert_str += "EMAIL_ADDRESS,"
      values_str += "'#{row[:email_address]}',"
      insert_str += "CITY,"
      values_str += "'#{row[:city]}',"
      insert_str += "STATE,"
      values_str += "'#{row[:state]}',"
      insert_str += "POSTAL_CODE,"
      values_str += "'#{row[:postal_code]}',"
      insert_str += "PHONE_NUMBER,"
      values_str += "'#{row[:phone_number]}',"
      insert_str += "COUNTRY_CODE,"
      values_str += "'#{row[:country_code]}',"
      insert_str += "SPONSOR_ADDRESS_FLAG,"
      values_str += "'#{sponsor_address_flag}',"
      insert_str += "OWNED_BY_UNIT,"
      values_str += "'#{row[:owned_by_unit]}',"
      # insert_str += "ACTV_IND,"
      # values_str += "'#{row[:actv_ind]}',"

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
