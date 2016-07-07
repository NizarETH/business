
require 'colorize'
require 'rubygems'
require 'fileutils'
require 'json'
require 'net/http'
require 'open-uri'


class Look
def up
	  directory_name="./importor.rb"
          exist = File.exists?(directory_name)

        if(exist)
	 FileUtils.rm_rf(directory_name)
	end

        cmd="git clone git@git.paperpad.fr:android-scripts/importor.git"
        system(cmd)
        file_path = "./importor/importor.rb"
        puts" Copying importor ...".blue
        destination_folder = File.expand_path(".", Dir.pwd)
        puts"#{destination_folder}  ".green
        if File.exists?(file_path)
            FileUtils.mv(file_path, destination_folder)
        end
        puts" Done.".red

     	directory_name="./importor"
          exist = File.exists?(directory_name)

        if(exist)
	 FileUtils.rm_rf(directory_name)
	end



end
end

look = Look.new
look.up
