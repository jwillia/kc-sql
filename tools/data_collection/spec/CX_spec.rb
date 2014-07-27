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

  describe "#parse_flag" do
    it "Perform exact valid_values matching" do
      expect(CX.parse_flag("N", valid_values: ['N', 'O', 'P'])).to eq("N")
    end

    it "Perform case insensitive valid_values matching" do
      expect(CX.parse_flag("o", case_sensitive: false, valid_values: ['N', 'O', 'P'])).to eq("O")
    end

    it "Raises an ArgumentError if String is not a valid value" do
      expect { CX.parse_flag("Q", valid_values: ['N', 'O', 'P']) }.to raise_error(ArgumentError)
      expect { CX.parse_flag("",  valid_values: ['N', 'O', 'P']) }.to raise_error(ArgumentError)
      expect { CX.parse_flag(nil, valid_values: ['N', 'O', 'P']) }.to raise_error(ArgumentError)
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
      expect(CX.parse_principal_id("lspeelmon")).to eq("lspeelmon")
    end

    it "raises an ArgumentError if the principal_id is nil or empty" do
      expect { CX.parse_principal_id(nil) }.to raise_error(ArgumentError)
      expect { CX.parse_principal_id("") }.to  raise_error(ArgumentError)
    end

    it "raises an ArgumentError if length exceeds 40 characters" do
      expect { CX.parse_principal_id("x" * 41) }.to raise_error(ArgumentError)
    end

  end

end
