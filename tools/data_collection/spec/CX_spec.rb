require 'CX'

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
    end
  end

end
