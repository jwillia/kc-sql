#!/usr/bin/env ruby

require 'rubygems'
require 'bundler/setup'

require 'csv'
require 'pp'

csv_filename = 'institute_rates.csv'

options = { :headers =>  :first_row,
          }

CSV.open(csv_filename, "r", options) do |csv|
  matches = csv.find_all do |row|
    insert_str = "insert into institute_rates ("
    values_str = "values ("
    hash = row.to_hash
    hash.keys.each do |key|
      insert_str.concat "#{key},"
      values_str.concat "'#{hash[key]}',"
    end

    # add synthetic data to metadata columns
    insert_str.concat "UPDATE_TIMESTAMP,"
    values_str.concat "now(),"
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
    puts "#{insert_str} #{values_str}"
  end
end
