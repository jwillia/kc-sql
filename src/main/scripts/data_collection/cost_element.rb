#!/usr/bin/env ruby

require 'rubygems'
require 'bundler/setup'

require 'csv'
require 'pp'

csv_filename = 'sample_data.csv'

options = { :headers =>  :first_row,
          }

CSV.open(csv_filename, "r", options) do |csv|
  matches = csv.find_all do |row|
    insert_str = "Insert into COST_ELEMENT ("
    values_str = "values ("
    hash = row.to_hash
    hash.keys.each do |key|
      insert_str.concat "#{key},"
      # TODO FIXME only wrap values with quotes if certain data type
      values_str.concat "'#{hash[key]}',"
    end
    insert_str.chomp!(",")
    values_str.chomp!(",")
    insert_str.concat ")"
    values_str.concat ");"
    puts "#{insert_str} #{values_str}"
  end
end
