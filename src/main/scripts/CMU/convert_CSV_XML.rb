#!/usr/bin/env ruby

require 'tempfile'
require 'builder'
require 'csv'
require 'optparse'
require 'ostruct'
require 'pp'
require 'time'

csv_filename = nil
options = OpenStruct.new
options.email_recipients = "lspeelmon@rsmart.com"
options.xml_filename = "hrimport.xml"
optparse = OptionParser.new do |opts|
  opts.banner = "Usage: #{File.basename($0)} [options] csv_file"

  opts.on('--email [email_recipients]', 'Email recipient list that will receive job report...') do |e|
    options.email_recipients = e
  end

  opts.on('--output [xml_file_output]', 'The file the XML data will be writen to... (defaults to hrimport.xml)') do |f|
    options.xml_filename = f
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

csv_options = { headers: :first_row,
                header_converters: :symbol,
                skip_blanks: true,
                col_sep: '|',
                }

def preprocess(csvfile)
  Tempfile.open("convert_CSV_XML.temp.#{Process.pid}") do |tempfile|
    csvout = CSV.new(tempfile, { col_sep: '|' })
    
    CSV.open(csvfile, { col_sep: '|' }) do |csv|
      csv.find_all do |row| # begin processing csv rows
        if row.length > 2
          csvout << row
        end
      end # find_all
    end # csv
    return tempfile.path
  end
end

temppath = preprocess(csv_filename)

CSV.open(temppath, csv_options) do |csv|
  record_count = csv.readlines.count
  csv.rewind # go back to first row

  File.open(options.xml_filename, 'w') do |xml_file|
    xml = Builder::XmlMarkup.new(target: xml_file, indent: 2)
    xml.instruct! :xml, encoding: "UTF-8"
    xml.hrmanifest "xmlns:xsi" => "http://www.w3.org/2001/XMLSchema-instance",
      "xsi:schemaLocation" => "https://github.com/rSmart/ce-tech-docs/tree/master/v1_0 https://raw.github.com/rSmart/ce-tech-docs/master/v1_0/hrmanifest.xsd",
      xmlns: "https://github.com/rSmart/ce-tech-docs/tree/master/v1_0",
      schemaVersion: "1.0",
      statusEmailRecipient: options.email_recipients,
      reportDate: Time.now.iso8601,
    recordCount: record_count do |hrmanifest|
      hrmanifest.records do |record|
        csv.find_all do |row| # begin processing csv rows
          xml.record principalId: row[:principal_id], principalName: row[:principal_name] do |record|
            record.affiliations do |affiliations|
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
              email_address = row[:email_address]
              if ( email_address =~ /^([a-zA-Z0-9_\-\.]+)@$/ ) # fix bad 'user@' email addresses
                email_address = "#{email_address}andrew.cmu.edu"
              end
              emails.email emailType: row[:email_type], emailAddress: email_address,
                default: row[:email_default_indicator] == "Y", active: row[:email_active_indicator] == "Y"
            end # emails
            record.kcExtendedAttributes visaType: row[:visa_type], officeLocation: row[:office_location],
              secondaryOfficeLocation: row[:secondary_office_location], onSabbatical: row[:is_on_sabbatical] == "Y",
              citizenshipType: "1" # make everyone a US CITIZEN OR NONCITIZEN NATIONAL
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
