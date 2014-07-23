#!/usr/bin/env ruby

require 'csv'
require 'optparse'
require 'ostruct'
require 'pp'
require './rsmart_common_data_load.rb'

csv_filename = nil
options = OpenStruct.new
optparse = OptionParser.new do |opts|
  opts.banner = "Usage: #{File.basename($0)} [options] csv_file"

  opts.on('--output [sql_file_output]', 'The file the SQL data will be writen to... (defaults to <csv_file>.sql)') do |f|
    options.sql_filename = f
  end

  opts.on( '-h', '--help', 'Display this screen' ) do
    puts opts
    exit
  end

  csv_filename = ARGV[0]
  if csv_filename.nil? || csv_filename.empty?
    puts opts
    exit
  end
end
optparse.parse!

if options.sql_filename.nil?
  options.sql_filename = "#{File.basename(csv_filename, File.extname(csv_filename))}.sql"
end
sql_filename = options.sql_filename

options = { headers: :first_row,
            header_converters: :symbol,
            skip_blanks: true,
            }

File.open(sql_filename, "w") do |sql|
  sql.write "
-- Depends on running RemoveAllTransactionalData() beforehand.

DROP PROCEDURE IF EXISTS LoadUnits;

DELIMITER //
CREATE PROCEDURE LoadUnits()
BEGIN

DECLARE exit handler for sqlexception
BEGIN
-- ERROR
SHOW ERRORS;
ROLLBACK;
END;

START TRANSACTION;

delete from protocol_units;
delete from unit_administrator;
delete from award_report_tracking;
delete from award_pers_unit_cred_splits;
delete from award_person_units;
delete from award_amount_info;
delete from award_attachment;
delete from award_budget_limit;
delete from award_closeout;
delete from award_comment;
delete from award_cost_share;
delete from award_funding_proposals;
delete from award_person_credit_splits;
delete from award_persons;
delete from award_rep_terms_recnt;
delete from award_report_terms;
delete from award_special_review;
delete from award_payment_schedule;
delete from award_idc_rate;
delete from award_transferring_sponsor;
delete from award;
delete from unit where UNIT_NUMBER != '000001';

"
  CSV.open(csv_filename, "r", options) do |csv|
    csv.find_all do |row| # begin processing csv rows
      # pp row
      unit_number = parse_string row[:unit_number]
      if unit_number.nil? || unit_number.empty? || unit_number.length > 8
        raise ArgumentError, "unit_number.length > 8: #{unit_number}"
      end
      unit_name = parse_string row[:unit_name]
      if unit_name.length > 60
        warn "unit_name.length > 60: #{unit_name}"
      end
      parent_unit_number = parse_string row[:parent_unit_number]

      if unit_number.eql? '000001'
        update_str = "update unit set "
        update_str += "UNIT_NAME = '#{unit_name}', "
        update_str += "UPDATE_TIMESTAMP = NOW() "
        update_str += "where UNIT_NUMBER = '000001';"
        sql.write "#{update_str}\n"
      else
        if parent_unit_number.nil? || parent_unit_number.empty?
          raise ArgumentError, "parent_unit_number nil or empty for unit_number: #{unit_number} on line #{$INPUT_LINE_NUMBER}!"
        end
        insert_str = "insert into unit ("
        values_str = "values ("

        insert_str += "UNIT_NUMBER,"
        values_str += "'#{unit_number}',"
        insert_str += "UNIT_NAME,"
        values_str += "'#{unit_name}',"
        insert_str += "ORGANIZATION_ID,"
        values_str += "'000001',"
        insert_str += "PARENT_UNIT_NUMBER,"
        values_str += "'#{parent_unit_number}',"
        insert_str += "ACTIVE_FLAG,"
        values_str += "'Y',"

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
      end # if root unit

    end # row

  end # csv

  sql.write "
COMMIT;

END //
DELIMITER ;

call LoadUnits();
"

end # sql
