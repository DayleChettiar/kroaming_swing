create database UserUsage;
use UserUsage;
grant all on UserUsage.* to 'root'@'localhost' identified by 'test'; 

CREATE TABLE `User` (
 `user_id` int(10) NOT NULL AUTO_INCREMENT,
 `user_name` varchar(40) NOT NULL,
 `email` varchar(100) NOT NULL,
 `country` varchar(40) NOT NULL,
 `phoneNumber` varchar(16) NOT NULL,
 `usg_type` varchar(6) NOT NULL,
 `time_stamp` date NOT NULL,
 PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=100000009;