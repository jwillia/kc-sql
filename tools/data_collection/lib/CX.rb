# Common behaviors for the CX data loading scripts
class CX

  @y_n_valid_values = ["Y", "y", "N", "n"]
  @employee_status_valid_values = ['A', 'D', 'L', 'N', 'P', 'R', 'S', 'T']
  @emp_typ_cd_valid_values = ['N', 'O', 'P']

  # Test to see if subject is a member of valid_values Array
  def self.valid_value(subject, valid_values, opt={})
    raise ArgumentError, "valid_values must be an Array!" unless valid_values.kind_of? Array
    raise ArgumentError, "valid_values must have at least one element!" unless valid_values.length > 0
    retval = false
    valid_values.each do |valid_value|
      if opt[:case_sensitive] == false
        # perform case insensitive comparison
        raise ArgumentError, "case_sensitive only supported for Strings!" unless subject.kind_of?(String) && valid_value.kind_of?(String)
        retval = true if subject.casecmp valid_value
      else
        # default to eql? method
        retval = true if subject.eql? valid_value
      end
    end
    return retval
  end

  def self.to_bool(str)
    return true  if str == true  || str.to_s =~ /^(active|a|true|t|yes|y|1)$/i
    return false if str == false || str.nil? || str.empty? || str =~ /^(inactive|i|false|f|no|n|0)$/i
    raise ArgumentError, "invalid value for Boolean: '#{str}'"
  end

  def self.escape_single_quotes(str)
    if str.nil?
      return nil
    end
    return str.to_s.gsub("'", "\\\\'")
  end

  def self.parse_string(str, opt={})
    if opt[:required] && opt[:default]
      raise ArgumentError, "opt[:required] && opt[:default] are mutually exclusive!"
    end
    unless opt[:encoding]
      opt[:encoding] = "UTF-8"
    end
    retval = str.to_s.strip.encode(opt[:encoding], :invalid => :replace, :undef =>
                                   :replace, :replace => "")
    if opt[:default] && retval.empty?
      return opt[:default]
    end
    if opt[:required] && retval.empty?
      msg = "ERROR: Line #{$INPUT_LINE_NUMBER}: Required data element not found: "
      msg += "#{opt[:name]}: " if opt[:name]
      msg += "'#{str}'"
      raise ArgumentError, msg
    end
    if opt[:length] && retval.length > opt[:length].to_i
      if opt[:strict]
        msg = "ERROR: Line #{$INPUT_LINE_NUMBER}: Data exceeds maximum field length: "
        msg += "#{opt[:name]}." if opt[:name]
        msg += "length > #{opt[:length]}: '#{str}'"
        raise ArgumentError, msg
      end
      msg = "WARN:  Line #{$INPUT_LINE_NUMBER}: Data truncation warning: "
      msg += "#{opt[:name]}." if opt[:name]
      msg += "length > #{opt[:length]}: '#{str}'"
      warn msg
    end
    return escape_single_quotes retval
  end

  def self.parse_integer(str, opt={})
    i = parse_string str, opt
    if i.to_s.empty?
      msg = "ERROR: Line #{$INPUT_LINE_NUMBER}: Cannot convert empty string to Integer: "
      msg += "#{opt[:name]}: " if opt[:name]
      msg += "'#{str}'"
      raise ArgumentError, msg
    end
    return i.to_i
  end

  def self.parse_flag(str, opt={})
    # TODO make case insensitive by default to be more forgiving for consultants
    flag = parse_string str, opt # already includes :default behavior :)
    unless valid_value flag, opt[:valid_values], opt
      msg = "ERROR: Line #{$INPUT_LINE_NUMBER}: Illegal flag found: "
      msg += "#{opt[:name]}: " if opt[:name]
      msg += "'#{str}' not found in #{opt[:valid_values]}"
      raise ArgumentError, msg
    end
    return flag.upcase
  end

  def self.parse_rolodex_id!(row, insert_str, values_str)
    #   `ROLODEX_ID` decimal(6,0) NOT NULL DEFAULT '0',
    rolodex_id = parse_integer row[:rolodex_id],
      name: "rolodex_id", length: 6, required: true, strict: true
    insert_str.concat "ROLODEX_ID,"
    values_str.concat "'#{rolodex_id}',"
  end

  def self.parse_country_code!(row, insert_str, values_str)
    #   `COUNTRY_CODE` char(3) COLLATE utf8_bin DEFAULT NULL,
    country_code = parse_string row[:country_code], name: "country_code", length: 3, strict: true
    insert_str.concat "COUNTRY_CODE,"
    values_str.concat "'#{country_code}',"
  end

  def self.parse_state!(row, insert_str, values_str)
    #   `STATE` varchar(30) COLLATE utf8_bin DEFAULT NULL,
    state = parse_string row[:state], name: "state", length: 30, strict: true
    insert_str.concat "STATE,"
    values_str.concat "'#{state}',"
  end

  def self.parse_sponsor_code!(row, insert_str, values_str)
    #   `SPONSOR_CODE` char(6) COLLATE utf8_bin NOT NULL DEFAULT '',
    sponsor_code = parse_string row[:sponsor_code], name: "sponsor_code", length: 6, required: true
    insert_str += "SPONSOR_CODE,"
    values_str += "'#{sponsor_code}',"
  end

  def self.parse_postal_code!(row, insert_str, values_str)
    #   `POSTAL_CODE` varchar(15) COLLATE utf8_bin DEFAULT NULL,
    postal_code = parse_string row[:postal_code], name: "postal_code", length: 15
    insert_str += "POSTAL_CODE,"
    values_str += "'#{postal_code}',"
  end

  def self.parse_owned_by_unit!(row, insert_str, values_str)
    #   `OWNED_BY_UNIT` varchar(8) COLLATE utf8_bin NOT NULL,
    owned_by_unit = parse_string row[:owned_by_unit],
      name: "owned_by_unit", length: 8, required: true, strict: true
    insert_str += "OWNED_BY_UNIT,"
    values_str += "'#{owned_by_unit}',"
  end

  def self.parse_actv_ind!(row, insert_str, values_str)
    #   `ACTV_IND` varchar(1) COLLATE utf8_bin DEFAULT 'Y',
    actv_ind = parse_flag row[:actv_ind], name: "actv_ind", default: "Y",
      valid_values: @y_n_valid_values
    insert_str += "ACTV_IND,"
    values_str += "'#{actv_ind}',"
  end

  def self.parse_email_address!(row, insert_str, values_str)
    #   `EMAIL_ADDRESS` varchar(60) COLLATE utf8_bin DEFAULT NULL,
    email_address = parse_string row[:email_address], name: "email_address", length: 60, strict: true
    unless email_address.empty? || email_address =~ /([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)/
      warn "WARN:  Line #{$INPUT_LINE_NUMBER}: Illegal email_address pattern: '#{email_address}'"
    end
    insert_str += "EMAIL_ADDRESS,"
    values_str += "'#{email_address}',"
  end

  def self.parse_principal_id(str, opt={})
    #   `PRNCPL_ID` varchar(40) COLLATE utf8_bin NOT NULL DEFAULT '',
    opt[:length]   = 40   if opt[:length].nil?
    opt[:required] = true if opt[:required].nil?
    opt[:strict]   = true if opt[:strict].nil?
    parse_string str, opt
  end

  def self.parse_prncpl_nm(str, opt={})
    #   `PRNCPL_NM` varchar(100) COLLATE utf8_bin NOT NULL,
    opt[:length]   = 100  if opt[:length].nil?
    opt[:required] = true if opt[:required].nil?
    opt[:strict]   = true if opt[:strict].nil?
    parse_string str, opt
  end

  def self.parse_emp_stat_cd(str, opt={})
    #   `EMP_STAT_CD` varchar(40) COLLATE utf8_bin DEFAULT NULL,
    opt[:length]       = 40 if opt[:length].nil?
    opt[:valid_values] = @employee_status_valid_values if opt[:valid_values].nil?
    parse_flag str, opt
  end

  def self.parse_emp_typ_cd(str, opt={})
    #   `EMP_TYP_CD` varchar(40) COLLATE utf8_bin DEFAULT NULL,
    opt[:length]       = 40 if opt[:length].nil?
    opt[:valid_values] = @emp_typ_cd_valid_values if opt[:valid_values].nil?
    parse_flag str, opt
  end

  # Parse common command line options for CSV --> SQL transformations.
  def self.parse_csv_command_line_options(
      executable, args, opt={ csv_options: { headers: :first_row,
                                             header_converters: :symbol,
                                             skip_blanks: true,
                                             col_sep: ",", # comma by default
                                             quote_char: '"', # double quote by default
                                             }
                              } )
    optparse = OptionParser.new do |opts|
      opts.banner = "Usage: #{executable} [options] csv_file"
      opts.on( '-o [sql_file_output]' ,'--output [sql_file_output]', 'The file the SQL data will be writen to... (defaults to <csv_file>.sql)') do |f|
        opt[:sql_filename] = f
      end
      opts.on( '-s [separator_character]' ,'--separator [separator_character]', 'The character that separates each column of the CSV file.') do |s|
        opt[:col_sep] = s
      end
      opts.on( '-q [quote_character]' ,'--quote [quote_character]', 'The character used to quote fields.') do |q|
        opt[:quote_char] = q
      end
      opts.on( '-h', '--help', 'Display this screen' ) do
        puts opts
        exit
      end

      opt[:csv_filename] = args[0] unless opt[:csv_filename]
      if opt[:csv_filename].nil? || opt[:csv_filename].empty?
        puts opts
        exit
      end
    end
    optparse.parse!

    # construct a sensible default ouptput filename
    unless opt[:sql_filename]
      file_extension = File.extname opt[:csv_filename]
      dir_name = File.dirname opt[:csv_filename]
      base_name = File.basename opt[:csv_filename], file_extension
      opt[:sql_filename] = "#{dir_name}/#{base_name}.sql"
    end

    return opt
  end

end
