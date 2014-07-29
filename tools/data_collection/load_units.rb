#!/usr/bin/env ruby

require 'rubygems'
require 'bundler/setup'

require 'graph'
require 'pp'
require_relative './lib/CX.rb'

@root_unit_number = '000001'

class UnitNode

  @@csv_filename = nil
  @@opts = nil

  # @id: Unit number from csv.
  # @children: All units from csv with parent unit number of @id.
  # @parent: Parent unit number of @id.
  def initialize(id=@root_unit_number)
    @id = id
    @children = []
    get_children_args(id).each do |child|
      @children << UnitNode.new(child)
    end
    @parent = get_parent_id_args(id)
  end

  # Gives access to the csv input file with the options.
  def self.set_opts(file=csv_filename, opts=options)
    @@csv_filename = file
    @@opts = opts
  end

  # Helper method that returns the name of the given unit number.
  def get_name(unit)
    name = ""
    CSV.open(@@csv_filename, @@opts) do |csv|
      csv.find_all do |row|
        if row[:unit_number].to_s.strip.eql? unit
          name.concat row[:unit_name].to_s.strip
        end
      end
    end
    return name
  end

  # Helper method that returns the parent number of the unit given by the parameter.
  def get_parent_id_args(id)
    parent_id = ""
    CSV.open(@@csv_filename, @@opts) do |csv|
      csv.find_all do |row|
        if row[:unit_number].to_s.strip.eql? id
          parent_id << row[:parent_unit_number].to_s.strip
        end
      end
    end
    return parent_id
  end

  # Helper method that returns an array of all units with the parent number of the unit given by the parameter.
  def get_children_args(id)
    children_array = []
    CSV.open(@@csv_filename, @@opts) do |csv|
      csv.find_all do |row|
        if row[:parent_unit_number].to_s.strip.eql? id
          children_array << row[:unit_number].to_s.strip
        end
      end
    end
    return children_array
  end

  # Reads csv into an array of arrays, finds the names from the unit numbers, and
  # uses GraphViz to generate a graph of the unit hierarchy. The graph is then
  # unflattened using a command line tool provided by GraphViz.
  def generate_graph
    hierarchy = CSV.read(@@csv_filename, @@opts)
    hierarchy.each do |obj|
      if obj[0].eql? @root_unit_number
        obj[0] = ""
        obj[2] = ""
      else
        obj[2]=get_name(obj[2])
        obj[0]=get_name(obj[0])
      end
    end
    digraph do
      hierarchy.each do |obj|
        if obj[0].eql? ""
        else
          edge "#{obj[2]}", "#{obj[0]}"
        end
      end
      save 'Unit_Hierarchy', 'png'
      %x(unflatten -l7 Unit_Hierarchy.dot | dot -Tpng -o Unit_Hierarchy.png)
      %x(rm "Unit_Hierarchy.dot")
      puts "Graphical Hierarchy generated at #{File.expand_path(File.dirname("Unit_Hierarchy.png"))}/Unit_Hierarchy.png"
    end
  end

  # Recursively traverses the n-ary tree created from the hierarchy.
  # Base Case: if at a leaf, return 1.
  # else return the max between the current depth and that of the next subtree.
  # Return the max depth plus one for the last set of leaves.
  def get_depth(from=@root_unit_number)
    childarr = get_children_args(from)
    if get_children_args(from) == []
      return 1
    else
      depth = 0
      childarr.each do |obj|
        depth = [depth, get_depth(obj)].max
      end
    end
    return depth+1
  end

end

opt = CX.parse_csv_command_line_options (File.basename $0), ARGF.argv

# Hierarchy creation, validation, and generation.
UnitNode.set_opts(opt[:csv_filename], opt[:csv_options])
foo = UnitNode.new
if foo.get_depth > 4
  raise ArgumentError, "Unit Hierarchy exceeds 4 levels"
end
foo.generate_graph

File.open(opt[:sql_filename], "w") do |sql|
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
delete from comm_member_expertise;
delete from comm_member_roles;
delete from comm_memberships;
delete from comm_research_areas;
delete from comm_schedule_act_items;
delete from comm_schedule_attendance;
delete from protocol_onln_rvws;
delete from protocol_correspondence;
delete from protocol_actions;
delete from comm_schedule_minutes;
delete from protocol_reviewers;
delete from protocol_expidited_chklst;
delete from protocol_exempt_chklst;
delete from protocol_submission;
delete from comm_schedule_minute_doc;
delete from schedule_agenda;
delete from comm_schedule;
delete from committee;
delete from iacuc_protocol_units;
delete from proposal_log;
delete from unit where UNIT_NUMBER != '#{@root_unit_number}';

"
  CSV.open(opt[:csv_filename], "r", opt[:csv_options]) do |csv|
    csv.find_all do |row| # begin processing csv rows
      begin
        # | unit  | CREATE TABLE `unit`
        update_str = "update unit set "
        insert_str = "insert into unit ("
        values_str = "values ("

        #   `UNIT_NUMBER` varchar(8) COLLATE utf8_bin NOT NULL DEFAULT '',
        unit_number = CX.parse_string row[:unit_number],
          name: "unit_number", required: true, length: 8
        insert_str.concat "UNIT_NUMBER,"
        values_str.concat "'#{unit_number}',"

        #   `UNIT_NAME` varchar(60) COLLATE utf8_bin DEFAULT NULL,
        unit_name = CX.parse_string row[:unit_name],
          name: "unit_name", required: true, length: 60
        update_str.concat "UNIT_NAME = '#{unit_name}', "
        insert_str.concat "UNIT_NAME,"
        values_str.concat "'#{unit_name}',"

        #   `ORGANIZATION_ID` varchar(8) COLLATE utf8_bin DEFAULT NULL,
        organization_id = CX.parse_string row[:organization_id],
          name: "organization_id", length: 8, default: @root_unit_number
        insert_str.concat "ORGANIZATION_ID,"
        values_str.concat "'#{organization_id}',"

        #   `PARENT_UNIT_NUMBER` varchar(8) COLLATE utf8_bin DEFAULT NULL,
        parent_unit_number = CX.parse_string row[:parent_unit_number],
          name: "parent_unit_number", required: true, length: 8
        insert_str.concat "PARENT_UNIT_NUMBER,"
        values_str.concat "'#{parent_unit_number}',"

        #   `ACTIVE_FLAG` char(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y',
        CX.parse_actv_ind! row, insert_str, values_str,
          name: "active_flag", default: "Y"

        # if root node then update the existing row instead of insert
        if @root_unit_number.eql? unit_number
          update_str.concat "UPDATE_TIMESTAMP = NOW() "
          update_str.concat "where UNIT_NUMBER = '#{@root_unit_number}';"
          sql.write "#{update_str}\n"
        else # insert
          insert_str.concat "UPDATE_TIMESTAMP,"
          values_str.concat "NOW(),"
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
          sql.write "#{insert_str} #{values_str}\n"
        end # if root unit

      rescue TextParseError => e
        puts e.message
      end
    end # row

  end # csv

  sql.write "
COMMIT;

END //
DELIMITER ;

call LoadUnits();
"

end # sql
