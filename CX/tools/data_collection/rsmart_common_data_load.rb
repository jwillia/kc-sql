# Common function for the data loading scripts

def self.escape_single_quotes(str)
  if str.nil?
    return nil
  end
  return str.to_s.gsub("'", "\\\\'")
end

def self.parse_string(str)
  if str.nil?
    return nil
  end
  return str.to_s.strip.gsub("'", "\\\\'").encode("UTF-8", :invalid => :replace, :undef =>
                                                  :replace, :replace => "?")
end

def self.parse_integer(str)
  if str.nil?
    return nil
  end
  if str.to_s.strip.empty?
    raise ArgumentError, "Cannot parse integer from an empty string: '#{str}'"
  end
  return str.to_i
end

# TODO refactor to be more generic and take an array of valid values instead of hard coding
def self.parse_boolean_y_n(str, default_value)
  if !default_value.nil? && (str.nil? || str.empty?)
    return default_value
  end
  unless str.casecmp("Y") || str.casecmp("N")
    raise ArgumentError, "Illegal Y/N flag found: '#{str}'"
  end
  return str.upcase
end
