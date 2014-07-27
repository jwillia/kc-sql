#spec_helper.rb
require 'simplecov'

RSpec.configure do |config|
  # capture original references for later
  original_stderr = $stderr
  original_stdout = $stdout

  config.before(:all) do
    # Redirect stderr and stdout to /dev/null
    $stderr = File.new '/dev/null', 'w'
    $stdout = File.new '/dev/null', 'w'
  end

  config.after(:all) do
    # restore original references
    $stderr = original_stderr
    $stdout = original_stdout
  end
end

SimpleCov.start
