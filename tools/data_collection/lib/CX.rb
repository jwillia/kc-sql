# Common behaviors for the CX data loading scripts
class CX

  # Test to see if subject is a member of valid_values Array
  def self.valid_value(subject, valid_values, opt={})
    raise ArgumentError, "valid_values must not be nil!" if valid_values.nil?
    if valid_values.kind_of? Regexp
      return true if subject =~ valid_values
    end
    if valid_values.kind_of? Array
      raise ArgumentError, "valid_values must have at least one element!" unless valid_values.length > 0
      if opt[:case_sensitive] == false # case insensitive comparison requested
        raise ArgumentError, "case_sensitive only supported for Strings!" unless subject.kind_of?(String)
        valid_values.each do |valid_value|
          return true if valid_value.casecmp(subject) == 0
        end
      end
      return true if valid_values.include? subject # default to == equality
    end
    return false
  end

  def self.to_bool(str)
    return true  if str == true  || str.to_s =~ /^(active|a|true|t|yes|y|1)$/i
    return false if str == false || str.nil? || str.empty? || str =~ /^(inactive|i|false|f|no|n|0)$/i
    raise ArgumentError, "invalid value for Boolean: '#{str}'"
  end

  def self.to_symbol(str)
    raise ArgumentError, "Illegal symbol name: '#{str}'" if str.nil? || str.empty?
    str.downcase.to_sym
  end

  # DRY up some common string manipulation
  def self.mutate_sql_stmt(insert_str, column_name, values_str, value)
    insert_str.concat "#{column_name.upcase},"
    values_str.concat "'#{value}',"
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
    if opt[:valid_values] && ! valid_value(retval, opt[:valid_values], opt)
      msg = "ERROR: Line #{$INPUT_LINE_NUMBER}: Illegal "
      msg += "#{opt[:name]}: " if opt[:name]
      msg = "value found: '#{str}' not found in #{opt[:valid_values]}"
      raise ArgumentError, msg
    end
    return escape_single_quotes retval
  end

  def self.parse_string!(row, insert_str, values_str, opt={})
    raise ArgumentError, "opt[:name] is required!" unless opt[:name]
    str = parse_string( row[ to_symbol( opt[:name] ) ], opt )
    mutate_sql_stmt insert_str, opt[:name], values_str, str
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

  def self.parse_rolodex_id!(row, insert_str, values_str, opt={})
    #   `ROLODEX_ID` decimal(6,0) NOT NULL DEFAULT '0',
    opt[:name]     = "ROLODEX_ID" if opt[:name].nil?
    opt[:required] = true if opt[:required].nil?
    opt[:length]   = 6 if opt[:length].nil?
    opt[:strict]   = true if opt[:strict].nil?
    parse_string! row, insert_str, values_str, opt
  end

  def self.parse_country_code!(row, insert_str, values_str, opt={})
    #   `COUNTRY_CODE` char(3) COLLATE utf8_bin DEFAULT NULL,
    opt[:name]   = "COUNTRY_CODE" if opt[:name].nil?
    opt[:length] = 3 if opt[:length].nil?
    opt[:strict] = true if opt[:strict].nil?
    parse_string! row, insert_str, values_str, opt
  end

  def self.parse_state!(row, insert_str, values_str, opt={})
    #   `STATE` varchar(30) COLLATE utf8_bin DEFAULT NULL,
    opt[:name]   = "STATE" if opt[:name].nil?
    opt[:length] = 30 if opt[:length].nil?
    opt[:strict] = true if opt[:strict].nil?
    parse_string! row, insert_str, values_str, opt
  end

  def self.parse_sponsor_code!(row, insert_str, values_str, opt={})
    #   `SPONSOR_CODE` char(6) COLLATE utf8_bin NOT NULL DEFAULT '',
    opt[:name]     = "SPONSOR_CODE" if opt[:name].nil?
    opt[:required] = true if opt[:required].nil?
    opt[:length]   = 6 if opt[:length].nil?
    opt[:strict]   = true if opt[:strict].nil?
    parse_string! row, insert_str, values_str, opt
  end

  def self.parse_postal_code!(row, insert_str, values_str, opt={})
    #   `POSTAL_CODE` varchar(15) COLLATE utf8_bin DEFAULT NULL,
    opt[:name]   = "POSTAL_CODE" if opt[:name].nil?
    opt[:length] = 15 if opt[:length].nil?
    opt[:strict] = true if opt[:strict].nil?
    parse_string! row, insert_str, values_str, opt
  end

  def self.parse_owned_by_unit!(row, insert_str, values_str, opt={})
    #   `OWNED_BY_UNIT` varchar(8) COLLATE utf8_bin NOT NULL,
    opt[:name]     = "OWNED_BY_UNIT" if opt[:name].nil?
    opt[:required] = true if opt[:required].nil?
    opt[:length]   = 8 if opt[:length].nil?
    opt[:strict]   = true if opt[:strict].nil?
    parse_string! row, insert_str, values_str, opt
  end

  # Useful for parsing "flag" like values. Always returns upcase for consistency.
  # Assumes :strict :length of 1 by default.
  def self.parse_flag(str, opt={})
    opt[:length] = 1 if opt[:length].nil?
    opt[:strict] = true if opt[:strict].nil?
    opt[:upcase] = true if opt[:upcase].nil?
    retval = parse_string str, opt
    retval = retval.upcase if opt[:upcase] == true
    return retval
  end

  # Designed specifically for actv_ind, but could be used on *any*
  # fields that matches /^(Y|N)$/i.
  def self.parse_actv_ind(str, opt={})
    #   `ACTV_IND` varchar(1) COLLATE utf8_bin DEFAULT 'Y',
    opt[:name]         = "actv_ind" if opt[:name].nil?
    opt[:default]      = "Y" if opt[:default].nil?
    opt[:valid_values] = /^(Y|N)$/i if opt[:valid_values].nil?
    return parse_flag str, opt
  end

  # Designed specifically for actv_ind, but could be used on *any*
  # fields that matches /^(Y|N)$/i.
  def self.parse_actv_ind!(row, insert_str, values_str, opt={})
    #   `ACTV_IND` varchar(1) COLLATE utf8_bin DEFAULT 'Y',
    opt[:name] = "actv_ind" if opt[:name].nil?
    actv_ind = parse_actv_ind row[ to_symbol( opt[:name] ) ]
    mutate_sql_stmt insert_str, opt[:name], values_str, actv_ind
  end

  def self.parse_email_address!(row, insert_str, values_str)
    #   `EMAIL_ADDRESS` varchar(60) COLLATE utf8_bin DEFAULT NULL,
    email_address = parse_string row[:email_address], name: "email_address", length: 60, strict: true
    unless email_address.empty? || email_address =~ /^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/
      raise ArgumentError, "ERROR: Line #{$INPUT_LINE_NUMBER}: Illegal email_address pattern: '#{email_address}'"
    end
    mutate_sql_stmt insert_str, "EMAIL_ADDRESS", values_str, email_address
  end

  def self.parse_principal_id(str, opt={})
    #   `PRNCPL_ID` varchar(40) COLLATE utf8_bin NOT NULL DEFAULT '',
    opt[:required] = true if opt[:required].nil?
    opt[:length]   = 40   if opt[:length].nil?
    opt[:strict]   = true if opt[:strict].nil?
    parse_string str, opt
  end

  def self.parse_prncpl_nm(str, opt={})
    #   `PRNCPL_NM` varchar(100) COLLATE utf8_bin NOT NULL,
    opt[:length]   = 100  if opt[:length].nil?
    opt[:required] = true if opt[:required].nil?
    opt[:strict]   = true if opt[:strict].nil?
    prncpl_nm = parse_string str, opt
    unless prncpl_nm =~ /^([a-z0-9\@\.\_\-]+)$/
      raise ArgumentError, "ERROR: Line #{$INPUT_LINE_NUMBER}: Illegal prncpl_nm: '#{prncpl_nm}'"
    end
    return prncpl_nm
  end

  def self.parse_emp_stat_cd(str, opt={})
    #   `EMP_STAT_CD` varchar(40) COLLATE utf8_bin DEFAULT NULL,
    opt[:name]         = "EMP_STAT_CD" if opt[:name].nil?
    opt[:valid_values] = /^(A|D|L|N|P|R|S|T)$/i if opt[:valid_values].nil?
    return parse_flag str, opt
  end

  def self.parse_emp_typ_cd(str, opt={})
    #   `EMP_TYP_CD` varchar(40) COLLATE utf8_bin DEFAULT NULL,
    opt[:name]         = "EMP_TYP_CD" if opt[:name].nil?
    opt[:valid_values] = /^(N|O|P)$/i if opt[:valid_values].nil?
    return parse_flag str, opt
  end

  def self.parse_address_type_code(str, opt={})
    # TODO find real column name
    opt[:name]         = "TODO_address_type_code" if opt[:name].nil?
    opt[:length]       = 3 if opt[:length].nil?
    opt[:valid_values] = /^(HM|OTH|WRK)$/i if opt[:valid_values].nil?
    return parse_flag str, opt
  end

  def self.parse_name_code(str, opt={})
    # TODO find real column name
    opt[:name]         = "TODO_name_code" if opt[:name].nil?
    opt[:length]       = 4 if opt[:length].nil?
    opt[:valid_values] = /^(OTH|PRFR|PRM)$/i if opt[:valid_values].nil?
    return parse_flag str, opt
  end

  def self.parse_prefix(str, opt={})
    # TODO find real column name
    opt[:name]         = "TODO_prefix" if opt[:name].nil?
    opt[:length]       = 3 if opt[:length].nil?
    opt[:valid_values] = /^(Ms|Mrs|Mr|Dr)?$/ if opt[:valid_values].nil?
    opt[:upcase]       = false if opt[:upcase].nil?
    return parse_flag str, opt
  end

  def self.parse_suffix(str, opt={})
    # TODO find real column name
    opt[:name]         = "TODO_suffix" if opt[:name].nil?
    opt[:length]       = 3 if opt[:length].nil?
    opt[:valid_values] = /^(Jr|Sr|Mr|Md)?$/ if opt[:valid_values].nil?
    opt[:upcase]       = false if opt[:upcase].nil?
    return parse_flag str, opt
  end

  def self.parse_phone_type(str, opt={})
    # TODO find real column name
    opt[:name]         = "TODO_phone_type" if opt[:name].nil?
    opt[:length]       = 3 if opt[:length].nil?
    opt[:valid_values] = /^(FAX|HM|MBL|OTH|WRK)$/i if opt[:valid_values].nil?
    return parse_flag str, opt
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
