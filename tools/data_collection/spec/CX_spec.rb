require 'spec_helper'
require 'CX'
require 'csv'
require 'pp'

RSpec.describe CX do
  describe '#valid_value' do
    it "tests semantic equality against a set of valid values" do
      expect(CX.valid_value(1, [1, 2, 3])).to eq(true)
      expect(CX.valid_value(2, [1, 2, 3])).to eq(true)
      expect(CX.valid_value(3, [1, 2, 3])).to eq(true)
      expect(CX.valid_value(1, [2, 3])).to eq(false)
      expect(CX.valid_value("1", ["1"])).to eq(true)
      expect(CX.valid_value("1", ["2"])).to eq(false)
      expect(CX.valid_value("1", [1])).to eq(false)
    end

    it "provides a case_sensitive option" do
      expect(CX.valid_value("one", ["ONE"], case_sensitive: false)).to eq(true)
      expect(CX.valid_value("one", ["ONE"], case_sensitive: true)).to eq(false)
      expect(CX.valid_value("one", ["one"], case_sensitive: true)).to eq(true)
      expect(CX.valid_value("one", ["ONE"], case_sensitive: "foo")).to eq(false)
      expect(CX.valid_value("one", ["ONE"])).to eq(false)
    end

    it "allows for a valid_values that is a regular expression" do
      expect(CX.valid_value("word", /^(\w+)$/)).to eq(true)
      expect(CX.valid_value("Z", /^(B|A|Z)?$/)).to eq(true)
      expect(CX.valid_value("", /^(B|A|Z)?$/)).to eq(true)
      expect(CX.valid_value("upper", /^(UPPER)$/i)).to eq(true)

      expect(CX.valid_value("false", /^(true)$/)).to eq(false)
      expect(CX.valid_value("", "^(B|A|Z)+$")).to eq(false)
    end
  end

  describe "#to_bool" do
    it "converts 'Active/Inactive' Strings to Booleans" do
      expect(CX.to_bool("Active")).to eq(true)
      expect(CX.to_bool("Inactive")).to eq(false)
    end

    it "converts 'A/I' Strings to Booleans" do
      expect(CX.to_bool("A")).to eq(true)
      expect(CX.to_bool("i")).to eq(false)
    end

    it "converts 'True/False' Strings to Booleans" do
      expect(CX.to_bool("True")).to eq(true)
      expect(CX.to_bool("False")).to eq(false)
    end

    it "converts 'T/F' Strings to Booleans" do
      expect(CX.to_bool("T")).to eq(true)
      expect(CX.to_bool("F")).to eq(false)
    end

    it "converts 'Yes/No' Strings to Booleans" do
      expect(CX.to_bool("Yes")).to eq(true)
      expect(CX.to_bool("No")).to eq(false)
    end

    it "converts 'Y/N' Strings to Booleans" do
      expect(CX.to_bool("Y")).to eq(true)
      expect(CX.to_bool("N")).to eq(false)
    end

    it "converts '1/0' Strings to Booleans" do
      expect(CX.to_bool("1")).to eq(true)
      expect(CX.to_bool("0")).to eq(false)
    end

    it "handles Booleans in addition to Strings" do
      expect(CX.to_bool(true)).to eq(true)
      expect(CX.to_bool(false)).to eq(false)
    end

    it "converts '' Strings to Booleans" do
      expect(CX.to_bool('')).to eq(false)
    end

    it "converts nil to Booleans" do
      expect(CX.to_bool(nil)).to eq(false)
    end

    it "supports mixed case strings" do
      expect(CX.to_bool("TRUE")).to eq(true)
      expect(CX.to_bool("true")).to eq(true)
      expect(CX.to_bool("True")).to eq(true)
      expect(CX.to_bool("FALSE")).to eq(false)
      expect(CX.to_bool("false")).to eq(false)
      expect(CX.to_bool("False")).to eq(false)
    end

    it "throws an Exception when an invalid string is passed" do
      expect { CX.to_bool("foober") }.to raise_error(ArgumentError)
    end
  end

  describe "#escape_single_quotes" do
    it "Escapes any single quotes in a String with a '\' character" do
      expect(CX.escape_single_quotes("That's it")).to eq("That\\\'s it")
      expect(CX.escape_single_quotes("Thats it")).to eq("Thats it")
      expect(CX.escape_single_quotes("")).to eq("")
      expect(CX.escape_single_quotes(nil)).to eq(nil)
    end
  end

  describe "#parse_string" do
    it "Escapes any single quotes in a String with a '\' character" do
      expect(CX.parse_string("That's it")).to eq("That\\\'s it")
      expect(CX.parse_string("Thats it")).to  eq("Thats it")
    end

    it "Returns empty string if nil or an empty string is passed" do
      expect(CX.parse_string("")).to  eq("")
      expect(CX.parse_string(nil)).to eq("")
    end

    it "Supports a :required option" do
      expect { CX.parse_string("",  required: true)  }.to raise_error(ArgumentError)
      expect { CX.parse_string(nil, required: true)  }.to raise_error(ArgumentError)
      expect { CX.parse_string("",  required: false) }.not_to raise_error
      expect { CX.parse_string(nil, required: false) }.not_to raise_error
    end

    it "Supports a :default option if no String is found" do
      expect(CX.parse_string("",  default: "foo")).to eq("foo")
      expect(CX.parse_string(nil, default: "foo")).to eq("foo")
    end

    it "Ignores the :default option if a String is found" do
      expect(CX.parse_string("bar", default: "foo")).to eq("bar")
    end

    it "Raises an ArgumentError if both :required AND :default options are used" do
      expect { CX.parse_string("foo", required: true, default: "bar") }.to raise_error(ArgumentError)
    end

    it "Supports a :length option which generates a warning by default" do
      # TODO how to test that a warn occurred?
      expect(CX.parse_string("123", length: 1)).to eq("123")
    end

    it "Supports a :strict option which performs a strict :length check" do
      expect { CX.parse_string("123", length: 1, strict: true) }.to raise_error(ArgumentError)
    end
  end

  describe "#parse_integer" do
    it "Converts Strings into Integers" do
      expect(CX.parse_integer("1")).to eq(1)
      expect(CX.parse_integer("0")).to eq(0)
    end

    it "Supports passing Integers for convenience" do
      expect(CX.parse_integer(1)).to eq(1)
      expect(CX.parse_integer(0)).to eq(0)
    end

    it "Raises an ArgumentError if String is nil or empty" do
      expect { CX.parse_integer(nil) }.to raise_error(ArgumentError)
      expect { CX.parse_integer("")  }.to raise_error(ArgumentError)
    end

    it "Supports :default option" do
      expect(CX.parse_integer("", default: "1")).to eq(1)
      expect(CX.parse_integer("", default: 2)).to   eq(2)
    end

    it "Enforces strict length validation to avoid loss of precision" do
      expect { CX.parse_integer("22", length: 1, strict: true) }.to raise_error(ArgumentError)
    end
  end

  describe "#parse_rolodex_id!" do
    it "Modifies the insert_str and values_str based on a CSV::Row match" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['rolodex_id'.to_sym], ['123456'], true)
      CX.parse_rolodex_id!(row, insert_str, values_str)
      expect(insert_str).to eq("ROLODEX_ID,")
      expect(values_str).to eq("'123456',")
    end

    it "Raises an ArgumentError if nil or empty" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['rolodex_id'.to_sym], [nil], true)
      expect { CX.parse_rolodex_id!(row, insert_str, values_str) }.to raise_error(ArgumentError)
      row = CSV::Row.new(['rolodex_id'.to_sym], [''], true)
      expect { CX.parse_rolodex_id!(row, insert_str, values_str) }.to raise_error(ArgumentError)
    end

    it "Raises an ArgumentError if length exceeds 6 characters" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['rolodex_id'.to_sym], ['1234567'], true)
      expect { CX.parse_rolodex_id!(row, insert_str, values_str) }.to raise_error(ArgumentError)
    end
  end

  describe "#parse_country_code!" do
    it "Modifies the insert_str and values_str based on a CSV::Row match" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['country_code'.to_sym], ['USA'], true)
      CX.parse_country_code!(row, insert_str, values_str)
      expect(insert_str).to eq("COUNTRY_CODE,")
      expect(values_str).to eq("'USA',")
    end

    it "Does not raise an ArgumentError if nil or empty" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['country_code'.to_sym], [nil], true)
      expect { CX.parse_country_code!(row, insert_str, values_str) }.not_to raise_error
      row = CSV::Row.new(['country_code'.to_sym], [''], true)
      expect { CX.parse_country_code!(row, insert_str, values_str) }.not_to raise_error
    end

    it "Raises an ArgumentError if length exceeds 4 characters" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['country_code'.to_sym], ['FOUR'], true)
      expect { CX.parse_country_code!(row, insert_str, values_str) }.to raise_error(ArgumentError)
    end
  end

  describe "#parse_state!" do
    it "Modifies the insert_str and values_str based on a CSV::Row match" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['state'.to_sym], ['Arizona'], true)
      CX.parse_state!(row, insert_str, values_str)
      expect(insert_str).to eq("STATE,")
      expect(values_str).to eq("'Arizona',")
    end

    it "Does not raise an ArgumentError if nil or empty" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['state'.to_sym], [nil], true)
      expect { CX.parse_state!(row, insert_str, values_str) }.not_to raise_error
      row = CSV::Row.new(['state'.to_sym], [''], true)
      expect { CX.parse_state!(row, insert_str, values_str) }.not_to raise_error
    end

    it "Raises an ArgumentError if length exceeds 30 characters" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['state'.to_sym], ["x" * 31], true)
      expect { CX.parse_state!(row, insert_str, values_str) }.to raise_error(ArgumentError)
    end
  end

  describe "#parse_sponsor_code!" do
    it "Modifies the insert_str and values_str based on a CSV::Row match" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['sponsor_code'.to_sym], ['000001'], true)
      CX.parse_sponsor_code!(row, insert_str, values_str)
      expect(insert_str).to eq("SPONSOR_CODE,")
      expect(values_str).to eq("'000001',")
    end

    it "Raises an ArgumentError if nil or empty" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['sponsor_code'.to_sym], [nil], true)
      expect { CX.parse_sponsor_code!(row, insert_str, values_str) }.to raise_error(ArgumentError)
      row = CSV::Row.new(['sponsor_code'.to_sym], [""], true)
      expect { CX.parse_sponsor_code!(row, insert_str, values_str) }.to raise_error(ArgumentError)
    end

    it "Raises an ArgumentError if length exceeds 6 characters" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['sponsor_code'.to_sym], ["x" * 7], true)
      expect { CX.parse_sponsor_code!(row, insert_str, values_str) }.to raise_error(ArgumentError)
    end
  end

  describe "#parse_postal_code!" do
    it "Modifies the insert_str and values_str based on a CSV::Row match" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['postal_code'.to_sym], ['12345-7890'], true)
      CX.parse_postal_code!(row, insert_str, values_str)
      expect(insert_str).to eq("POSTAL_CODE,")
      expect(values_str).to eq("'12345-7890',")
    end

    it "Does not raise an ArgumentError if nil or empty" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['postal_code'.to_sym], [nil], true)
      expect { CX.parse_postal_code!(row, insert_str, values_str) }.not_to raise_error
      row = CSV::Row.new(['postal_code'.to_sym], [''], true)
      expect { CX.parse_postal_code!(row, insert_str, values_str) }.not_to raise_error
    end

    it "Raises an ArgumentError if length exceeds 15 characters" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['postal_code'.to_sym], ["x" * 16], true)
      expect { CX.parse_postal_code!(row, insert_str, values_str) }.to raise_error(ArgumentError)
    end
  end

  describe "#parse_owned_by_unit!" do
    it "Modifies the insert_str and values_str based on a CSV::Row match" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['owned_by_unit'.to_sym], ['000001'], true)
      CX.parse_owned_by_unit!(row, insert_str, values_str)
      expect(insert_str).to eq("OWNED_BY_UNIT,")
      expect(values_str).to eq("'000001',")
    end

    it "Raises an ArgumentError if nil or empty" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['owned_by_unit'.to_sym], [nil], true)
      expect { CX.parse_owned_by_unit!(row, insert_str, values_str) }.to raise_error(ArgumentError)
      row = CSV::Row.new(['owned_by_unit'.to_sym], [''], true)
      expect { CX.parse_owned_by_unit!(row, insert_str, values_str) }.to raise_error(ArgumentError)
    end

    it "Raises an ArgumentError if length exceeds 8 characters" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['owned_by_unit'.to_sym], ["x" * 9], true)
      expect { CX.parse_owned_by_unit!(row, insert_str, values_str) }.to raise_error(ArgumentError)
    end
  end

  describe "#parse_actv_ind!" do
    it "Modifies the insert_str and values_str based on a CSV::Row match" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['actv_ind'.to_sym], ['Y'], true)
      CX.parse_actv_ind!(row, insert_str, values_str)
      expect(insert_str).to eq("ACTV_IND,")
      expect(values_str).to eq("'Y',")
    end

    it "allows for lowercase input Strings" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['actv_ind'.to_sym], ['n'], true)
      CX.parse_actv_ind!(row, insert_str, values_str)
      expect(insert_str).to eq("ACTV_IND,")
      expect(values_str).to eq("'N',")
    end

    it "Returns a default value of 'Y' and does not raise an ArgumentError if nil or empty" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['actv_ind'.to_sym], [nil], true)
      expect { CX.parse_actv_ind!(row, insert_str, values_str) }.not_to raise_error
      expect(insert_str).to eq("ACTV_IND,")
      expect(values_str).to eq("'Y',")
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['actv_ind'.to_sym], [''], true)
      expect { CX.parse_actv_ind!(row, insert_str, values_str) }.not_to raise_error
      expect(insert_str).to eq("ACTV_IND,")
      expect(values_str).to eq("'Y',")
    end

    it "Raises an ArgumentError if not a valid 'Y/N' value" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['actv_ind'.to_sym], ["Q"], true)
      expect { CX.parse_actv_ind!(row, insert_str, values_str) }.to raise_error(ArgumentError)
    end

    it "Raises an ArgumentError if length exceeds 1 characters" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['actv_ind'.to_sym], ["x" * 2], true)
      expect { CX.parse_actv_ind!(row, insert_str, values_str) }.to raise_error(ArgumentError)
    end
  end

  describe "#parse_email_address!" do
    it "Modifies the insert_str and values_str based on a CSV::Row match" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['email_address'.to_sym], ['lance@rsmart.com'], true)
      CX.parse_email_address!(row, insert_str, values_str)
      expect(insert_str).to eq("EMAIL_ADDRESS,")
      expect(values_str).to eq("'lance@rsmart.com',")
    end

    it "Does not raise an ArgumentError if nil or empty" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['email_address'.to_sym], [nil], true)
      expect { CX.parse_email_address!(row, insert_str, values_str) }.not_to raise_error
      row = CSV::Row.new(['email_address'.to_sym], [''], true)
      expect { CX.parse_email_address!(row, insert_str, values_str) }.not_to raise_error
    end

    it "Raises an ArgumentError if length exceeds 60 characters" do
      insert_str = ""; values_str = "";
      valid_sixty_one_char_email_address = "abcedefghijksdhfksjfdsdfsdfsdfsdhsjkhdf@abcdesfsdfsdfsdff.com"
      row = CSV::Row.new(['email_address'.to_sym], [valid_sixty_one_char_email_address], true)
      expect { CX.parse_email_address!(row, insert_str, values_str) }.to raise_error(ArgumentError)
    end

    it "Raises an ArgumentError if it does not match the official RFC email address specifications" do
      insert_str = ""; values_str = "";
      row = CSV::Row.new(['email_address'.to_sym], ["foo"], true)
      expect { CX.parse_email_address!(row, insert_str, values_str) }.to raise_error(ArgumentError)
      row = CSV::Row.new(['email_address'.to_sym], ["foo@bar"], true)
      expect { CX.parse_email_address!(row, insert_str, values_str) }.to raise_error(ArgumentError)
      row = CSV::Row.new(['email_address'.to_sym], ["foo@bar."], true)
      expect { CX.parse_email_address!(row, insert_str, values_str) }.to raise_error(ArgumentError)
    end
  end

  describe "#parse_principal_id" do
    it "parses a principal_id from a String" do
      expect(CX.parse_principal_id("ABCD1234")).to eq("ABCD1234")
    end

    it "raises an ArgumentError if the principal_id is nil or empty" do
      expect { CX.parse_principal_id(nil) }.to raise_error(ArgumentError)
      expect { CX.parse_principal_id("") }.to  raise_error(ArgumentError)
    end

    it "raises an ArgumentError if length exceeds 40 characters" do
      expect { CX.parse_principal_id("x" * 41) }.to raise_error(ArgumentError)
    end
  end

  describe "#parse_prncpl_nm" do
    it "parses a principal_nm from a String" do
      expect(CX.parse_prncpl_nm("lspeelmon")).to eq("lspeelmon")
    end

    it "raises an ArgumentError if the principal_nm is nil or empty" do
      expect { CX.parse_prncpl_nm(nil) }.to raise_error(ArgumentError)
      expect { CX.parse_prncpl_nm("") }.to  raise_error(ArgumentError)
    end

    it "raises an ArgumentError if the principal_nm contains illegal characters" do
      expect { CX.parse_prncpl_nm("~!#$%^&*()+=") }.to raise_error(ArgumentError)
      expect { CX.parse_prncpl_nm("LANCE@UPPERCASE.COM") }.to raise_error(ArgumentError)
    end

    it "raises an ArgumentError if length exceeds 100 characters" do
      expect { CX.parse_prncpl_nm("x" * 101) }.to raise_error(ArgumentError)
    end
  end

  describe "#parse_emp_stat_cd" do
    it "parses a emp_stat_cd from a String" do
      # @employee_status_valid_values = ['A', 'D', 'L', 'N', 'P', 'R', 'S', 'T']
      expect(CX.parse_emp_stat_cd("A")).to eq("A")
      expect(CX.parse_emp_stat_cd("D")).to eq("D")
      expect(CX.parse_emp_stat_cd("L")).to eq("L")
      expect(CX.parse_emp_stat_cd("N")).to eq("N")
      expect(CX.parse_emp_stat_cd("P")).to eq("P")
      expect(CX.parse_emp_stat_cd("R")).to eq("R")
      expect(CX.parse_emp_stat_cd("S")).to eq("S")
      expect(CX.parse_emp_stat_cd("T")).to eq("T")
    end

    it "allows for lowercase input Strings" do
      expect(CX.parse_emp_stat_cd("a")).to eq("A")
      expect(CX.parse_emp_stat_cd("d")).to eq("D")
      expect(CX.parse_emp_stat_cd("l")).to eq("L")
      expect(CX.parse_emp_stat_cd("n")).to eq("N")
      expect(CX.parse_emp_stat_cd("p")).to eq("P")
      expect(CX.parse_emp_stat_cd("r")).to eq("R")
      expect(CX.parse_emp_stat_cd("s")).to eq("S")
      expect(CX.parse_emp_stat_cd("t")).to eq("T")
    end

    it "raises an ArgumentError if the emp_typ_cd is not a valid value" do
      expect { CX.parse_emp_stat_cd("Z") }.to raise_error(ArgumentError)
    end

    it "raises an ArgumentError if the emp_stat_cd is nil or empty" do
      expect { CX.parse_emp_stat_cd(nil) }.to raise_error(ArgumentError)
      expect { CX.parse_emp_stat_cd("") }.to  raise_error(ArgumentError)
    end

    it "raises an ArgumentError if length exceeds 40 characters" do
      expect { CX.parse_emp_stat_cd("A" * 41) }.to raise_error(ArgumentError)
    end
  end

  describe "#parse_emp_typ_cd" do
    it "parses a emp_typ_cd from a String" do
      # @emp_typ_cd_valid_values = ['N', 'O', 'P']
      expect(CX.parse_emp_typ_cd("N")).to eq("N")
      expect(CX.parse_emp_typ_cd("O")).to eq("O")
      expect(CX.parse_emp_typ_cd("P")).to eq("P")
    end

    it "allows for lowercase input Strings" do
      expect(CX.parse_emp_typ_cd("n")).to eq("N")
      expect(CX.parse_emp_typ_cd("o")).to eq("O")
      expect(CX.parse_emp_typ_cd("p")).to eq("P")
    end

    it "raises an ArgumentError if the emp_typ_cd is not a valid value" do
      expect { CX.parse_emp_typ_cd("Z") }.to raise_error(ArgumentError)
    end

    it "raises an ArgumentError if the emp_typ_cd is nil or empty" do
      expect { CX.parse_emp_typ_cd(nil) }.to raise_error(ArgumentError)
      expect { CX.parse_emp_typ_cd("") }.to  raise_error(ArgumentError)
    end

    it "raises an ArgumentError if length exceeds 40 characters" do
      expect { CX.parse_emp_typ_cd("N" * 41) }.to raise_error(ArgumentError)
    end
  end

  describe "#parse_address_type_code" do
    it "parses a address_type_code from a String" do
      # <xs:pattern value="HM|OTH|WRK"/>
      # <xs:maxLength value="3"/>
      expect(CX.parse_address_type_code("HM")).to eq("HM")
      expect(CX.parse_address_type_code("OTH")).to eq("OTH")
      expect(CX.parse_address_type_code("WRK")).to eq("WRK")
    end

    it "allows for lowercase input Strings" do
      expect(CX.parse_address_type_code("hm")).to eq("HM")
      expect(CX.parse_address_type_code("oth")).to eq("OTH")
      expect(CX.parse_address_type_code("wrk")).to eq("WRK")
    end

    it "raises an ArgumentError if the address_type_code is not a valid value" do
      expect { CX.parse_address_type_code("Z") }.to raise_error(ArgumentError)
    end

    it "raises an ArgumentError if the address_type_code is nil or empty" do
      expect { CX.parse_address_type_code(nil) }.to raise_error(ArgumentError)
      expect { CX.parse_address_type_code("") }.to  raise_error(ArgumentError)
    end

    it "raises an ArgumentError if length exceeds 3 characters" do
      expect { CX.parse_address_type_code("HOME") }.to raise_error(ArgumentError)
    end
  end

  describe "#parse_name_code" do
    it "parses a name_code from a String" do
      # <xs:pattern value="OTH|PRFR|PRM"/>
      # <xs:maxLength value="4"/>
      expect(CX.parse_name_code("OTH")).to eq("OTH")
      expect(CX.parse_name_code("PRFR")).to eq("PRFR")
      expect(CX.parse_name_code("PRM")).to eq("PRM")
    end

    it "allows for lowercase input Strings" do
      expect(CX.parse_name_code("oth")).to eq("OTH")
      expect(CX.parse_name_code("prfr")).to eq("PRFR")
      expect(CX.parse_name_code("prm")).to eq("PRM")
    end

    it "raises an ArgumentError if the name_code is not a valid value" do
      expect { CX.parse_name_code("Z") }.to raise_error(ArgumentError)
    end

    it "raises an ArgumentError if the name_code is nil or empty" do
      expect { CX.parse_name_code(nil) }.to raise_error(ArgumentError)
      expect { CX.parse_name_code("") }.to  raise_error(ArgumentError)
    end

    it "raises an ArgumentError if length exceeds 4 characters" do
      expect { CX.parse_name_code("OTHER") }.to raise_error(ArgumentError)
    end
  end

  describe "#parse_prefix" do
    it "parses a prefix from a String" do
      # <xs:pattern value="(Ms|Mrs|Mr|Dr)?"/>
      # <xs:maxLength value="3"/>
      expect(CX.parse_prefix("Ms")).to  eq("Ms")
      expect(CX.parse_prefix("Mrs")).to eq("Mrs")
      expect(CX.parse_prefix("Mr")).to  eq("Mr")
      expect(CX.parse_prefix("Dr")).to  eq("Dr")
    end

    it "does NOT raise an ArgumentError if the prefix is nil or empty" do
      expect { CX.parse_prefix(nil) }.not_to raise_error
      expect { CX.parse_prefix("") }.not_to  raise_error
      expect(CX.parse_prefix("")).to eq("")
    end

    it "raises an ArgumentError if the prefix is not a valid value" do
      expect { CX.parse_prefix("Z") }.to raise_error(ArgumentError)
    end

    it "raises an ArgumentError if length exceeds 3 characters" do
      expect { CX.parse_prefix("Miss") }.to raise_error(ArgumentError)
    end
  end

  describe "#parse_suffix" do
    it "parses a suffix from a String" do
      # <xs:maxLength value="2"/>
      # <xs:pattern value="(Jr|Sr|Mr|Md)?"/>
      expect(CX.parse_suffix("Jr")).to eq("Jr")
      expect(CX.parse_suffix("Sr")).to eq("Sr")
      expect(CX.parse_suffix("Mr")).to eq("Mr")
      expect(CX.parse_suffix("Md")).to eq("Md")
    end

    it "does NOT raise an ArgumentError if the suffix is nil or empty" do
      expect { CX.parse_suffix(nil) }.not_to raise_error
      expect { CX.parse_suffix("") }.not_to  raise_error
      expect(CX.parse_suffix("")).to eq("")
    end

    it "raises an ArgumentError if the suffix is not a valid value" do
      expect { CX.parse_suffix("Z") }.to raise_error(ArgumentError)
    end

    it "raises an ArgumentError if length exceeds 2 characters" do
      expect { CX.parse_suffix("Jrr") }.to raise_error(ArgumentError)
    end
  end

  describe "#parse_phone_type" do
    it "parses a phone_type from a String" do
      # <xs:maxLength value="3"/>
      # <xs:pattern value="FAX|HM|MBL|OTH|WRK"/>
      expect(CX.parse_phone_type("FAX")).to eq("FAX")
      expect(CX.parse_phone_type("HM")).to eq("HM")
      expect(CX.parse_phone_type("MBL")).to eq("MBL")
      expect(CX.parse_phone_type("OTH")).to eq("OTH")
      expect(CX.parse_phone_type("WRK")).to eq("WRK")
    end

    it "allows for lowercase input Strings" do
      expect(CX.parse_phone_type("fax")).to eq("FAX")
      expect(CX.parse_phone_type("hm")).to eq("HM")
      expect(CX.parse_phone_type("mbl")).to eq("MBL")
      expect(CX.parse_phone_type("oth")).to eq("OTH")
      expect(CX.parse_phone_type("wrk")).to eq("WRK")
    end

    it "raises an ArgumentError if the phone_type is not a valid value" do
      expect { CX.parse_phone_type("Z") }.to raise_error(ArgumentError)
    end

    it "raises an ArgumentError if the phone_type is nil or empty" do
      expect { CX.parse_phone_type(nil) }.to raise_error(ArgumentError)
      expect { CX.parse_phone_type("") }.to  raise_error(ArgumentError)
    end

    it "raises an ArgumentError if length exceeds 3 characters" do
      expect { CX.parse_phone_type("HOME") }.to raise_error(ArgumentError)
    end
  end

  describe "#parse_phone_number" do
    it "parses a phone_number from a String" do
      # <xs:pattern value="\d{3}-\d{3}-\d{4}"/>
      expect(CX.parse_phone_number("800-555-1212")).to eq("800-555-1212")
      expect(CX.parse_phone_number("480-123-4567")).to eq("480-123-4567")
    end

    it "raises an ArgumentError if the phone_number is not a valid value" do
      expect { CX.parse_phone_number("80-555-1212") }.to raise_error(ArgumentError)
      expect { CX.parse_phone_number("800-55-1212") }.to raise_error(ArgumentError)
      expect { CX.parse_phone_number("800-555-121") }.to raise_error(ArgumentError)
      expect { CX.parse_phone_number("800-555-121") }.to raise_error(ArgumentError)
      expect { CX.parse_phone_number("800") }.to         raise_error(ArgumentError)
      expect { CX.parse_phone_number("555-121") }.to     raise_error(ArgumentError)
      expect { CX.parse_phone_number("Z") }.to raise_error(ArgumentError)
    end

    it "does NOT raise an ArgumentError if the suffix is nil or empty" do
      expect { CX.parse_phone_number(nil) }.not_to raise_error
      expect { CX.parse_phone_number("") }.not_to  raise_error
      expect(CX.parse_phone_number("")).to eq("")
    end

    it "raises an ArgumentError if length exceeds 12 characters" do
      expect { CX.parse_suffix("123-456-78901") }.to raise_error(ArgumentError)
    end
  end

end
