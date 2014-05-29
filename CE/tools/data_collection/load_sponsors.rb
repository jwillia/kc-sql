#!/usr/bin/env ruby

require 'csv'

csv_filename = 'PDX_SPONSORS_LOAD.csv'
sql_output = 'PDX_SPONSORS_LOAD.sql'

options = { headers: :first_row,
            header_converters: :symbol,
            skip_blanks: true,
            }

File.open(sql_output, "w") do |sql|
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

delete from sponsor_forms;
delete from sponsor;

"
  CSV.open(csv_filename, "r", options) do |csv|
    csv.find_all do |row| # begin processing csv rows
      acronym = row[:acronym];
      if acronym.length > 10
        warn "ACRONYM length too long: #{acronym}"
      end

      insert_str = "insert into sponsor ("
      values_str = "values ("

      insert_str += "SPONSOR_CODE,"
      values_str += "'#{row[:sponsor_code]}',"
      insert_str += "SPONSOR_NAME,"
      values_str += "'#{row[:sponsor_name].gsub("'", "\\\\'")}',"
      insert_str += "ACRONYM,"
      values_str += "'#{row[:acronym]}',"
      insert_str += "SPONSOR_TYPE_CODE,"
      values_str += "'#{row[:sponsor_type_code]}',"
      insert_str += "ROLODEX_ID,"
      values_str += "'#{row[:rolodex_id]}',"
      insert_str += "OWNED_BY_UNIT,"
      values_str += "'#{row[:owned_by_unit]}',"
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
COMMIT;

END //
DELIMITER ;

call LoadSponsors();
"

end # sql
