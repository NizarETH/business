require 'fileutils'
require 'colorize'
require "nokogiri"

class Create

    def initialize
        @group="android-libraries"
        @host="git@git.paperpad.fr"

    end


    def choose_business
        directory_name="./app/src/BusinessApps"
        exist = File.exists?(directory_name)

        if(exist)
            puts" Clonning Business script...".blue
            directory="./business_script"
            if File.exists?(directory)
                puts" removing existing Business script...".green
                FileUtils.remove_dir(directory)
                puts" Done.".green

            end
            cmd="git clone git@git.paperpad.fr:android-scripts/business_script.git"
            system(cmd)
            directory_name="business_script"

            file_path = "./business_script/generator.rb"
            puts" Copying generator ...".blue
            destination_folder = File.expand_path(".", Dir.pwd)
            puts"#{destination_folder}  ".green
            if File.exists?(file_path)
                FileUtils.mv(file_path, destination_folder)
            end
            puts"Done.".red
            ################
             directory_name = "./web_home_content"

         if !File.exists?(directory_name)

           Dir.mkdir(directory_name) unless File.exists?(directory_name)

          end
         puts"mybox_content Done.".red

        file_path = "./business_script/updater.rb"
        puts" Copying Updater_Business ...".blue
        destination_folder = "./web_home_content"
        puts"#{destination_folder}  ".green
        if File.exists?(file_path)
            FileUtils.mv(file_path, destination_folder)
        end
        puts"Updater_Business Done.".red
            ######################
          end
            ###############
            directory_name="./app/src/MyBoxApps"
            exist1 = File.exists?(directory_name)

            if(exist1)
                puts" Clonning MyBoxApps script...".blue
                directory="./mybox_script"
                if File.exists?(directory)
                    puts" removing existing MyBox script...".green
                    FileUtils.remove_dir(directory)
                    puts" Done.".green

                end
                cmd="git clone git@git.paperpad.fr:android-scripts/mybox_script.git"
                system(cmd)
                directory_name="mybox_script"

                file_path = "./mybox_script/generator.rb"
                puts" Copying generator ...".blue
                destination_folder = File.expand_path(".", Dir.pwd)
                puts"#{destination_folder}  ".green
                if File.exists?(file_path)
                    FileUtils.mv(file_path, destination_folder)
                end
                puts"Done.".red
                ##############
                    directory_name = "./mybox_content"

                     if !File.exists?(directory_name)

                       Dir.mkdir(directory_name) unless File.exists?(directory_name)

                      end
                     puts"mybox_content Done.".red

                    file_path = "./mybox_script/Updater_MyBox.rb"
                    puts" Copying Updater_MyBox ...".blue
                    destination_folder = "./mybox_content"
                    puts"#{destination_folder}  ".green
                    if File.exists?(file_path)
                        FileUtils.mv(file_path, destination_folder)
                    end
                    puts"Updater_MyBox Done.".red

            end
            ###############
             if(!exist1 && !exist)
            puts" Clonning Custom script...".blue

            directory="./custom_script"
            if File.exists?(directory)
                puts" removing existing Custom script...".green
                FileUtils.remove_dir(directory)
                puts" Done.".green

            end
            cmd="git clone git@git.paperpad.fr:android-scripts/custom_script.git"
            system(cmd)
            file_path = "./custom_script/generator.rb"
            puts" Copying generator ...".blue
            destination_folder = File.expand_path(".", Dir.pwd)
            puts"#{destination_folder}  ".green
            if File.exists?(file_path)
                FileUtils.mv(file_path, destination_folder)
            end
            puts"Done.".red
        end
    end

    def getLib
        puts "Clonning libraries from git.paperpad.fr.".blue

        File.open('./out.txt', 'w') do |f|
            File.foreach('./settings.gradle') do |line|
                directory_name = line[/':([^']+)'/, 1]
                f.puts directory_name

                if(!File.exist?(directory_name) && directory_name!="app" )
                    Dir.mkdir(directory_name)
                    directory_name.downcase!
                    directory_name.gsub!(' ','-')
                    command1 = "git clone #{@host}:#{@group}/#{directory_name}.git "
                    system(command1)
                    else
                    puts " La librairie #{directory_name} existe deja !".red


                    puts directory_name
                end
            end

        end

    end
end
c=Create.new
c.getLib
c.choose_business