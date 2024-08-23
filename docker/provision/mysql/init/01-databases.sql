#Create the databases
CREATE DATABASE IF NOT EXISTS `step1`;
CREATE DATABASE IF NOT EXISTS `step2`;
CREATE DATABASE IF NOT EXISTS `step3`;
CREATE DATABASE IF NOT EXISTS `microstamp`;

#Create root user and grant rights
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';