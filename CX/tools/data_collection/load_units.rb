#!/usr/bin/env ruby

require 'csv'
require 'optparse'
require 'ostruct'
require 'pp'
require './rsmart_common_data_load.rb'

@root_unit_number = '000001'

begin

opt = parse_csv_command_line_options (File.basename $0), ARGF.argv

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
      # | unit  | CREATE TABLE `unit`
      update_str = "update unit set "
      insert_str = "insert into unit ("
      values_str = "values ("

      #   `UNIT_NUMBER` varchar(8) COLLATE utf8_bin NOT NULL DEFAULT '',
      unit_number = parse_string row[:unit_number],
        name: "unit_number", required: true, length: 8, strict: true
      insert_str += "UNIT_NUMBER,"
      values_str += "'#{unit_number}',"

      #   `UNIT_NAME` varchar(60) COLLATE utf8_bin DEFAULT NULL,
      unit_name = parse_string row[:unit_name],
        name: "unit_name", required: true, length: 60
      update_str += "UNIT_NAME = '#{unit_name}', "
      insert_str += "UNIT_NAME,"
      values_str += "'#{unit_name}',"

      #   `ORGANIZATION_ID` varchar(8) COLLATE utf8_bin DEFAULT NULL,
      organization_id = parse_string row[:organization_id],
        name: "organization_id", length: 8, default: @root_unit_number
      insert_str += "ORGANIZATION_ID,"
      values_str += "'#{organization_id}',"

      #   `PARENT_UNIT_NUMBER` varchar(8) COLLATE utf8_bin DEFAULT NULL,
      parent_unit_number = parse_string row[:parent_unit_number],
        name: "parent_unit_number", required: true, length: 8, strict: true
      insert_str += "PARENT_UNIT_NUMBER,"
      values_str += "'#{parent_unit_number}',"

      #   `ACTIVE_FLAG` char(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y',
      active_flag = parse_flag row[:active_flag], name: "active_flag", default: "Y",
        valid_values: @y_n_valid_values
      insert_str += "ACTIVE_FLAG,"
      values_str += "'#{active_flag}',"

      # if root node then update the existing row instead of insert
      if @root_unit_number.eql? unit_number
        update_str += "UPDATE_TIMESTAMP = NOW() "
        update_str += "where UNIT_NUMBER = '#{@root_unit_number}';"
        sql.write "#{update_str}\n"
      else # insert
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

rescue ArgumentError => e
  puts e.message
  puts e.backtrace
end
