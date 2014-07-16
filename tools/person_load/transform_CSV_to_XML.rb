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
@col_sep = ','
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
                col_sep: @col_sep,
                }

def preprocess(csvfile)
  Tempfile.open("convert_CSV_XML.temp.#{Process.pid}") do |tempfile|
    csvout = CSV.new(tempfile, { col_sep: @col_sep })

    CSV.open(csvfile, { col_sep: @col_sep }) do |csv|
      csv.find_all do |row| # begin processing csv rows
        if row.length > 2
          csvout << row
        end
      end # find_all
    end # csv
    return tempfile.path
  end
end

def self.parse_principal_id(principal_id)
  if principal_id.nil? || principal_id.strip.empty?
    raise "principalId is nil or empty on line #{$INPUT_LINE_NUMBER}!"
  end
  return principal_id.strip
end

def self.parse_principal_name(principal_name)
  if principal_name.nil? || principal_name.strip.empty?
    raise "principal_name is nil or empty on line #{$INPUT_LINE_NUMBER}!"
  end
  return principal_name.strip
end

def self.parse_citizenship_type(citizenship_type)
  if citizenship_type.nil? || citizenship_type.to_i < 1 || citizenship_type.to_i > 4
    raise "Illegal citizenshipType: '#{citizenship_type}' on line #{$INPUT_LINE_NUMBER}!"
  end
  return citizenship_type.to_i
end

def self.parse_employee_status(employee_status)
  unless employee_status =~ /A|D|L|N|P|R|S|T/
    raise "Illegal employeeStatus: '#{employee_status}' on line #{$INPUT_LINE_NUMBER}!"
  end
  return employee_status
end

def self.parse_employee_type(employee_type)
  unless employee_type =~ /N|O|P/
    raise "Illegal employeeType: '#{employee_type}' on line #{$INPUT_LINE_NUMBER}!"
  end
  return employee_type
end

def self.parse_address_type_code(address_type_code)
  unless address_type_code =~ /HM|OTH|WRK/
    raise "Illegal addressTypeCode: '#{address_type_code}' on line #{$INPUT_LINE_NUMBER}!"
  end
  return address_type_code
end

def self.parse_name_code(name_code)
  unless name_code =~ /OTH|PRFR|PRM/
    raise "Illegal nameCode: '#{name_code}' on line #{$INPUT_LINE_NUMBER}!"
  end
  return name_code
end

def self.parse_prefix(prefix)
  unless prefix =~ /|Ms|Mrs|Mr|Dr/
    raise "Illegal prefix: '#{prefix}' on line #{$INPUT_LINE_NUMBER}!"
  end
  return prefix
end

def self.parse_suffix(suffix)
  unless suffix =~ /|Jr|Sr|Mr|Md/
    raise "Illegal suffix: '#{suffix}' on line #{$INPUT_LINE_NUMBER}!"
  end
  return suffix
end

def self.parse_phone_type(phone_type)
  unless phone_type =~ /FAX|HM|MBL|OTH|WRK/
    raise "Illegal phoneType: '#{phone_type}' on line #{$INPUT_LINE_NUMBER}!"
  end
  return phone_type
end

def self.parse_phone_number(phone_number)
  unless phone_number =~ /\d{3}-\d{3}-\d{4}/
    raise "Illegal phoneNumber: '#{phone_number}' on line #{$INPUT_LINE_NUMBER}!"
  end
  return phone_number
end

def self.parse_email_address(email_address)
  unless email_address =~ /\d{3}-\d{3}-\d{4}/
    raise "Illegal emailAddress: '#{email_address}' on line #{$INPUT_LINE_NUMBER}!"
  end
  return email_address
end

def self.parse_email_address(year)
  unless year =~ /\d{4}/
    raise "Illegal year: '#{year}' on line #{$INPUT_LINE_NUMBER}!"
  end
  return year.to_i
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
          pp row
          principal_id = parse_principal_id row[:principalid]
          principal_name = parse_principal_name row[:principalname]

          xml.record principalId: principal_id, principalName: principal_name do |record|
            record.affiliations do |affiliations|
              employee_status = parse_employee_status row[:employeestatus]
              employee_type = parse_employee_type row[:employeetype]
              affiliations.affiliation affiliationType: row[:affiliationtype].to_s.strip,
              campus: row[:campus].to_s.strip, default: true, active: true do |affiliation|
                affiliation.employment employeeStatus: employee_status, employeeType: employee_type,
                  baseSalaryAmount: row[:basesalaryamount].to_s.strip, primaryDepartment: row[:primarydepartment].to_s.strip, employeeId: row[:employeeid].to_s.strip,
                  primaryEmployment: true
              end
            end # affiliations
            record.names do |names|
              names.name nameCode: row[:namecode].to_s.strip, prefix: row[:prefix].to_s.strip, firstName: row[:firstname].to_s.strip, lastName: row[:lastname].to_s.strip, suffix: row[:suffix].to_s.strip,
                default: true, active: true
            end # names
            record.phones do |phones|
              unless row[:phonetype].to_s.strip.empty?
                phones.phone phoneType: row[:phonetype].to_s.strip, phoneNumber: row[:phonenumber].to_s.strip,
                  default: true, active: true
              end
            end # phones
            record.emails do |emails|
              emails.email emailType: row[:emailtype].to_s.strip, emailAddress: row[:emailaddress].to_s.strip,
                default: true, active: true
            end # emails
            on_sabbatical = false
            if !row[:onsabbatical].nil? && row[:onsabbatical].to_s.strip.casecmp("Y")
              on_sabbatical = true
            end
            citizenship_type = parse_citizenship_type row[:citizenshiptype]
            record.kcExtendedAttributes visaType: row[:visatype].to_s.strip, officeLocation: row[:officelocation].to_s.strip,
              secondaryOfficeLocation: row[:secondaryofficelocation].to_s.strip, onSabbatical: on_sabbatical,
              citizenshipType: citizenship_type
            record.appointments do |appointments|
              appointments.appointment unitNumber: row[:unitnumber].to_s.strip, jobCode: row[:jobcode].to_s.strip, jobTitle: row[:jobtitle].to_s.strip,
                preferedJobTitle: row[:preferedjobtitle].to_s.strip
            end # appointments
          end # record
        end # row
      end # record
    end # hrmanifest
  end # file
end # csv
