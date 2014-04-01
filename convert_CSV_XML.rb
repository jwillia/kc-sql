#!/usr/bin/env ruby

require 'builder'
require 'csv'
require 'pp'
require 'time'

csv_filename = 'CMU-HRID_03312014-short.csv'
xml_filename = "hrimport.xml"

options = { headers: :first_row,
            header_converters: :symbol,
            col_sep: '|',
            }

def do_it(record_count)
  xml = Builder::XmlMarkup.new(:target => @output, :indent => 2)
  xml.instruct! :xml, encoding: "UTF-8"
  xml.hrmanifest "xmlns:xsi" => "http://www.w3.org/2001/XMLSchema-instance",
    "xsi:schemaLocation" => "https://github.com/rSmart/ce-tech-docs/tree/master/v1_0 https://raw.github.com/rSmart/ce-tech-docs/master/v1_0/hrmanifest.xsd",
    xmlns: "https://github.com/rSmart/ce-tech-docs/tree/master/v1_0",
    schemaVersion: "1.0",
    reportDate: Time.now.iso8601,
  recordCount: record_count do |hrmanifest|
    hrmanifest.records do |record|
      #
    end # record
  end # hrmanifest
end

@records_text = ""
CSV.open(csv_filename, "r", options) do |csv|
  record_count = csv.readlines.count
  csv.rewind # go back to first row

  File.open(xml_filename, 'w') do |xml_file|
    xml = Builder::XmlMarkup.new(target: xml_file, indent: 2)
    xml.instruct! :xml, encoding: "UTF-8"
    xml.hrmanifest "xmlns:xsi" => "http://www.w3.org/2001/XMLSchema-instance",
      "xsi:schemaLocation" => "https://github.com/rSmart/ce-tech-docs/tree/master/v1_0 https://raw.github.com/rSmart/ce-tech-docs/master/v1_0/hrmanifest.xsd",
      xmlns: "https://github.com/rSmart/ce-tech-docs/tree/master/v1_0",
      schemaVersion: "1.0",
      reportDate: Time.now.iso8601,
    recordCount: record_count do |hrmanifest|
      hrmanifest.records do |record|
        csv.find_all do |row|
          xml.record principalId: row[:principal_id], principalName: row[:principal_name] do |record|
            record.affiliations do |affiliations|
              # TODO figure out affiliationType mapping
              affiliations.affiliation affiliationType: row[:affiliation_type],
              campus: row[:campus], default: row[:default_affiliation_indicator] == "Y", active: true do |affiliation|
                affiliation.employment employeeStatus: row[:employee_status], employeeType: row[:employee_type],
                  baseSalaryAmount: row[:base_salary_amount], primaryDepartment: row[:primary_dept], employeeId: row[:employee_id],
                  primaryEmployment: row[:primary_employment_indicator] == "Y"
              end
            end # affiliations
            record.names do |names|
              names.name nameCode: row[:name_code], firstName: row[:first_name], lastName: row[:last_name], suffix: row[:suffix],
                default: row[:name_default_indicator] == "Y", active: row[:name_active_indicator] == "Y"
            end # names
            record.phones do |phones|
              phones.phone phoneType: row[:phone_type], phoneNumber: row[:phone_number],
                default: row[:phone_default_indicator] == "Y", active: row[:phone_active_indicator] == "Y"
            end # phones
            record.emails do |emails|
              emails.email emailType: row[:email_type], emailAddress: row[:email_address],
                default: row[:email_default_indicator] == "Y", active: row[:email_active_indicator] == "Y"
            end # emails
            record.kcExtendedAttributes visaType: row[:visa_type], officeLocation: row[:office_location],
              secondaryOfficeLocation: row[:secondary_office_location], onSabbatical: row[:is_on_sabbatical]
            record.appointments do |appointments|
              appointments.appointment unitNumber: row[:unit], jobCode: row[:job_code], jobTitle: row[:job_title],
                preferedJobTitle: row[:preferred_job_title]
            end # appointments
          end # record
        end # row
      end # record
    end # hrmanifest
  end # file
end # csv
puts @records_text
