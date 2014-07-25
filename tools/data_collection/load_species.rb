#!/usr/bin/env ruby

require 'rubygems'
require 'bundler/setup'

require 'csv'
require 'optparse'
require 'ostruct'

def self.escapeSingleQuotes(str)
  return str.gsub("'", "\\\\'")
end

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

DROP PROCEDURE IF EXISTS LoadSpecies;

DELIMITER //
CREATE PROCEDURE LoadSpecies()
BEGIN

DECLARE exit handler for sqlexception
BEGIN
-- ERROR
SHOW ERRORS;
ROLLBACK;
END;

START TRANSACTION;

delete from iacuc_protocol_exceptions;
delete from iacuc_species;

"
  CSV.open(csv_filename, "r", options) do |csv|
    csv.find_all do |row| # begin processing csv rows

      # Diane's spreadsheet does not have species_code; making one up!
      # Set species_code to CSV line number
      species_code = "#{$. - 1}"
      if "#{species_code}".length > 4
        raise ArgumentError, "species_code.length > 4: #{species_code}"
      end

      species_name = row[:species_name]
      if species_name.length > 2000
        warn "species_name.length > 2000: #{species_name}"
      end
      species_name = escapeSingleQuotes species_name

      insert_str = "insert into iacuc_species ("
      values_str = "values ("

      insert_str += "SPECIES_CODE,"
      values_str += "'#{species_code}',"

      insert_str += "SPECIES_NAME,"
      values_str += "'#{species_name}',"

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

call LoadSpecies();
"

end # sql
