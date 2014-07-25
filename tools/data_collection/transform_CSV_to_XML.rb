#!/usr/bin/env ruby

require 'tempfile'
require 'builder'
require 'csv'
require 'optparse'
require 'ostruct'
require 'pp'
require 'time'
require 'nokogiri'

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

temp_path = preprocess(csv_filename)

CSV.open(temp_path, csv_options) do |csv|
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
          # pp row
          principal_id = row[:principalid].to_s.strip
          principal_name = row[:principalname].to_s.strip

          xml.record principalId: principal_id, principalName: principal_name do |record|
            record.affiliations do |affiliations|
              employee_status = row[:employeestatus].to_s.strip
              employee_type = row[:employeetype].to_s.strip
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
            citizenship_type = row[:citizenshiptype].to_s.strip
            record.kcExtendedAttributes visaType: row[:visatype].to_s.strip, officeLocation: row[:officelocation].to_s.strip,
              secondaryOfficeLocation: row[:secondaryofficelocation].to_s.strip, onSabbatical: on_sabbatical,
              citizenshipType: citizenship_type
            record.appointments do |appointments|
              unless row[:unitnumber].to_s.strip.empty?
                appointments.appointment unitNumber: row[:unitnumber].to_s.strip, jobCode: row[:jobcode].to_s.strip, jobTitle: row[:jobtitle].to_s.strip,
                  preferedJobTitle: row[:preferedjobtitle].to_s.strip
              end
            end # appointments
          end # record
        end # row
      end # record
    end # hrmanifest
  end # file
end # csv

# validate the resulting XML file against the XSD schema
xsd = Nokogiri::XML::Schema(File.read("/Develop/k2/rsmart/ce-tech-docs/hrmanifest.xsd"))
doc = Nokogiri::XML(File.read(options.xml_filename))
xsd.validate(doc).each do |error|
  puts error.message
end
