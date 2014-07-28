#!/usr/bin/env ruby

require 'rubygems'
require 'bundler/setup'

require 'builder'
require 'csv'
require 'net/http'
require 'nokogiri'
require 'optparse'
require 'ostruct'
require 'pp'
require 'tempfile'
require 'time'
require './lib/CX.rb'

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

CSV.open(csv_filename, csv_options) do |csv|
  record_count = csv.readlines.count
  csv.rewind # go back to first row

  File.open(options.xml_filename, 'w') do |xml_file|
    xml = Builder::XmlMarkup.new target: xml_file, indent: 2
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
          begin
            # pp row
            xml.record principalId: CX.parse_principal_id( row[:principalid] ),
            principalName: CX.parse_principal_name( row[:principalname] ) do |record|
              record.affiliations do |affiliations|
                employee_type = CX.parse_emp_typ_cd( row[:employeetype] )
                affiliations.affiliation affiliationType: CX.parse_string( row[:affiliationtype] ),
                  campus: CX.parse_string( row[:campus] ),
                default: true, active: true do |affiliation|
                  affiliation.employment employeeStatus: CX.parse_emp_stat_cd( row[:employeestatus] ),
                    employeeType:      CX.parse_emp_typ_cd( row[:employeetype] ),
                    baseSalaryAmount:  CX.parse_float( row[:basesalaryamount] ),
                    primaryDepartment: CX.parse_string( row[:primarydepartment] ),
                    employeeId:        CX.parse_string( row[:employeeid] ),
                    primaryEmployment: true
                end
              end # affiliations
              record.names do |names|
                names.name nameCode: CX.parse_name_code( row[:namecode] ),
                  prefix:     CX.parse_prefix( row[:prefix] ),
                  firstName:  CX.parse_string( row[:firstname] ),
                  middleName: CX.parse_string( row[:middlename] ),
                  lastName:   CX.parse_string( row[:lastname] ),
                  suffix:     CX.parse_suffix( row[:suffix]),
                  title:      CX.parse_suffix( row[:title]),
                  default:    true,
                  active:     true
              end # names
              record.phones do |phones|
                unless row[:phonetype].to_s.strip.empty?
                  phones.phone phoneType: CX.parse_phone_type( row[:phonetype] ),
                    phoneNumber: CX.parse_phone_number( row[:phonenumber] ),
                    extension:   CX.parse_string( row[:extension] ),
                    country:     CX.parse_string( row[:country] ),
                    default:     true,
                    active:      true
                end
              end # phones
              record.emails do |emails|
                emails.email emailType: CX.parse_email_type( row[:emailtype] ),
                  emailAddress: CX.parse_email_address( row[:emailaddress] ),
                  default: true, active: true
              end # emails
              record.kcExtendedAttributes visaType: CX.parse_string( row[:visatype] ),
                county:                   CX.parse_string(  row[:county] ),
                # ageByFiscalYear:          CX.parse_integer( row[:agebyfiscalyear] ),
                race:                     CX.parse_string(  row[:race] ),
                educationLevel:           CX.parse_string(  row[:educationlevel] ),
                degree:                   CX.parse_string(  row[:degree] ),
                major:                    CX.parse_string(  row[:major] ),
                handicapped:              CX.parse_boolean( row[:handicapped], default: false ),
                handicapType:             CX.parse_string(  row[:handicaptype] ),
                veteran:                  CX.parse_boolean( row[:veteran], default: false ),
                veteranType:              CX.parse_string(  row[:veterantype] ),
                visa:                     CX.parse_boolean( row[:visa], default: false ),
                visaCode:                 CX.parse_string(  row[:visacode] ),
                # visaRenewalDate:          CX.parse_string(  row[:visarenewaldate] ),
                officeLocation:           CX.parse_string(  row[:officelocation] ),
                secondaryOfficeLocation:  CX.parse_string(  row[:secondaryofficelocation] ),
                school:                   CX.parse_string(  row[:school] ),
                # yearGraduated:            CX.parse_year(    row[:yeargraduated] ),
                directoryDepartment:      CX.parse_string(  row[:directorydepartment] ),
                directoryTitle:           CX.parse_string(  row[:directorytitle] ),
                primaryTitle:             CX.parse_string(  row[:primarytitle] ),
                vacationAccrual:          CX.parse_boolean( row[:vacationaccrual], default: false ),
                onSabbatical:             CX.parse_boolean( row[:onsabbatical], default: false ),
                idProvided:               CX.parse_string(  row[:idprovided] ),
                idVerified:               CX.parse_string(  row[:idverified] ),
                citizenshipType:          CX.parse_citizenship_type( row[:citizenshiptype] ),
                multiCampusPrincipalId:   CX.parse_string(  row[:multicampusprincipalid] ),
                multiCampusPrincipalName: CX.parse_string(  row[:multicampusprincipalname] )
              # salaryAnniversaryDate:    CX.parse_string(  row[:salaryanniversarydate] )
              record.appointments do |appointments|
                unit_number = CX.parse_string( row[:unitnumber] )
                unless unit_number.empty?
                  appointments.appointment unitNumber: unit_number,
                    appointmentType:  CX.parse_string( row[:appointmenttype], required: true ),
                    jobCode:          CX.parse_string( row[:jobcode] ),
                    salary:           CX.parse_float(  row[:salary] ),
                    startDate:        CX.parse_string( row[:startdate] ),
                    endDate:          CX.parse_string( row[:enddate] ),
                    jobTitle:         CX.parse_string( row[:jobtitle] ),
                    preferedJobTitle: CX.parse_string( row[:preferedjobtitle] )
                end
              end # appointments
            end # record

          rescue TextParseError => e
            puts e.message
          end
        end # row
      end # record
    end # hrmanifest
  end # file
end # csv

# validate the resulting XML file against the official XSD schema
uri = URI 'https://raw.githubusercontent.com/rSmart/ce-tech-docs/master/hrmanifest.xsd'
Net::HTTP.start(uri.host, uri.port, use_ssl: true) do |http|
  Tempfile.open "hrmanifest.xsd" do |file|
    request = Net::HTTP::Get.new uri
    http.request request do |response|
      response.read_body do |segment|
        file.write(segment)
      end
    end
    file.rewind
    xsd = Nokogiri::XML::Schema file
    doc = Nokogiri::XML File.read options.xml_filename
    xml_errors = xsd.validate doc
    if xml_errors.empty?
      puts "Congratulations! The XML file passes XSD schema validation! w00t!"
    else
      xml_errors.each do |error|
        puts error.message
      end
    end
  end # file
end
