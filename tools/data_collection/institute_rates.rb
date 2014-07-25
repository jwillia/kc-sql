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
      insert_str += "#{key},"
      values_str += "'#{hash[key]}',"
    end

    # add synthetic data to metadata columns
    insert_str += "UPDATE_TIMESTAMP,"
    values_str += "now(),"
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
    puts "#{insert_str} #{values_str}"
  end
end
