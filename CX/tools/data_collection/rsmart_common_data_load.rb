# Common behaviors for the CX data loading scripts

@y_n_valid_values = ["Y", "y", "N", "n"]

# Test to see if subject is a member of valid_values Array
def self.valid_value(subject, valid_values)
  raise ArgumentError, "valid_values must be an Array!" unless valid_values.kind_of? Array
  raise ArgumentError, "valid_values must have at least one element!" unless valid_values.length > 0
  retval = false
  valid_values.each do |valid_value|
    retval = true if subject.eql? valid_value
  end
  return retval
end

def self.to_bool(str)
  return true  if str == true  || str =~ /(active|a|true|t|yes|y|1)$/i
  return false if str == false || str.nil? || str.empty? || str =~ /(inactive|i|false|f|no|n|0)$/i
  raise ArgumentError, "invalid value for Boolean: '#{str}'"
end

def self.escape_single_quotes(str)
  if str.nil?
    return nil
  end
  return str.to_s.gsub("'", "\\\\'")
end

def self.parse_string(str, opt={})
  unless opt[:encoding]
    opt[:encoding] = "UTF-8"
  end
  retval = str.to_s.strip.encode(opt[:encoding], :invalid => :replace, :undef =>
                                 :replace, :replace => "")
  if opt[:required] && retval.empty?
    msg = "ERROR: "
    msg += "Line #{opt[:line]}: " if opt[:line]
    msg += "Required data element not found: "
    msg += "#{opt[:name]}: " if opt[:name]
    msg += "'#{str}'"
    raise ArgumentError, msg
  end
  if opt[:length] && retval.length > opt[:length].to_i
    if opt[:strict]
      msg = "ERROR:  "
      msg += "Line #{opt[:line]}: " if opt[:line]
      msg += "Data exceeds maximum field length: "
      msg += "#{opt[:name]}." if opt[:name]
      msg += "length > #{opt[:length]}: '#{str}'"
      raise ArgumentError, msg
    end
    msg = "WARN:  "
    msg += "Line #{opt[:line]}: " if opt[:line]
    msg += "Data truncation warning: "
    msg += "#{opt[:name]}." if opt[:name]
    msg += "length > #{opt[:length]}: '#{str}'"
    warn msg
  end
  return escape_single_quotes retval
end

def self.parse_integer(str, opt)
  if str.nil?
    return nil
  end
  i = parse_string str, opt
  if i.empty?
    msg = "ERROR: "
    msg += "Line #{opt[:line]}: " if opt[:line]
    msg += "Cannot convert empty string to Integer: "
    msg += "#{opt[:name]}: " if opt[:name]
    msg += "'#{str}'"
    raise ArgumentError, msg
  end
  return i.to_i
end

def self.parse_flag(str, opt={})
  flag = parse_string str, opt
  if opt[:default] && flag.empty?
    return opt[:default]
  end
  unless valid_value str, opt[:valid_values]
    msg = "ERROR: "
    msg += "Line #{opt[:line]}: " if opt[:line]
    msg += "Illegal flag found: "
    msg += "#{opt[:name]}: " if opt[:name]
    msg += "'#{str}' not found in #{opt[:valid_values]}"
    raise ArgumentError, msg
  end
  return str.upcase
end

def self.parse_rolodex_id!(row, insert_str, values_str)
  #   `ROLODEX_ID` decimal(6,0) NOT NULL DEFAULT '0',
  rolodex_id = parse_integer row[:rolodex_id],
    name: "rolodex_id", length: 6, required: true, strict: true, line: $INPUT_LINE_NUMBER
  insert_str += "ROLODEX_ID,"
  values_str += "'#{rolodex_id}',"
end

def self.parse_country_code!(row, insert_str, values_str)
  #   `COUNTRY_CODE` char(3) COLLATE utf8_bin DEFAULT NULL,
  country_code = parse_string row[:country_code], name: "country_code", length: 3, strict: true, line: $INPUT_LINE_NUMBER
  insert_str += "COUNTRY_CODE,"
  values_str += "'#{country_code}',"
end

def self.parse_state!(row, insert_str, values_str)
  #   `STATE` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  state = parse_string row[:state], name: "state", length: 30, line: $INPUT_LINE_NUMBER
  insert_str += "STATE,"
  values_str += "'#{state}',"
end

def self.parse_sponsor_code!(row, insert_str, values_str)
  #   `SPONSOR_CODE` char(6) COLLATE utf8_bin NOT NULL DEFAULT '',
  sponsor_code = parse_string row[:sponsor_code], name: "sponsor_code", length: 6, required: true, line: $INPUT_LINE_NUMBER
  insert_str += "SPONSOR_CODE,"
  values_str += "'#{sponsor_code}',"
end

def self.parse_postal_code!(row, insert_str, values_str)
  #   `POSTAL_CODE` varchar(15) COLLATE utf8_bin DEFAULT NULL,
  postal_code = parse_string row[:postal_code], name: "postal_code", length: 15, line: $INPUT_LINE_NUMBER
  insert_str += "POSTAL_CODE,"
  values_str += "'#{postal_code}',"
end

def self.parse_owned_by_unit!(row, insert_str, values_str)
  #   `OWNED_BY_UNIT` varchar(8) COLLATE utf8_bin NOT NULL,
  owned_by_unit = parse_string row[:owned_by_unit],
    name: "owned_by_unit", length: 8, required: true, strict: true, line: $INPUT_LINE_NUMBER
  insert_str += "OWNED_BY_UNIT,"
  values_str += "'#{owned_by_unit}',"
end

def self.parse_actv_ind!(row, insert_str, values_str)
  #   `ACTV_IND` varchar(1) COLLATE utf8_bin DEFAULT 'Y',
  actv_ind = parse_flag( row[:actv_ind], {name: "actv_ind", default: "Y",
                                          valid_values: @y_n_valid_values, line: $INPUT_LINE_NUMBER} )
  insert_str += "ACTV_IND,"
  values_str += "'#{actv_ind}',"
end
