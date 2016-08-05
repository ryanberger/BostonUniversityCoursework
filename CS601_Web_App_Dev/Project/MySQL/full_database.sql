-- phpMyAdmin SQL Dump
-- version 3.2.0.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Dec 06, 2009 at 09:12 PM
-- Server version: 5.1.37
-- PHP Version: 5.3.0

CREATE DATABASE IF NOT EXISTS project;

USE project;

CREATE USER 'admin'@'localhost' IDENTIFIED BY  'ebsco';

GRANT SELECT, INSERT, UPDATE, DELETE,
FILE ON * . * TO  'admin'@'localhost' IDENTIFIED BY  'ebsco' WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0 ;

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `project`
--

-- --------------------------------------------------------

--
-- Table structure for table `feeds`
--

CREATE TABLE IF NOT EXISTS `feeds` (
  `feed_id` int(11) NOT NULL AUTO_INCREMENT,
  `vendor_id` int(11) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `delivery_type` varchar(20) DEFAULT NULL,
  `delivery_freq` varchar(15) DEFAULT NULL,
  `build_freq` varchar(15) DEFAULT NULL,
  `approx_size` int(11) DEFAULT NULL,
  `filename_pattern` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`feed_id`,`vendor_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `feeds`
--

INSERT INTO `feeds` (`feed_id`, `vendor_id`, `description`, `delivery_type`, `delivery_freq`, `build_freq`, `approx_size`, `filename_pattern`) VALUES
(1, 1, 'Indiana', 'FTP', 'Monthly', 'Monthly', 6200000, '*.out'),
(3, 2, 'Melbourne catalog', 'FTP', 'Weekly', 'Weekly', 1200000, '*.mar'),
(4, 2, 'More Melbourne data', 'FTP', 'Weekly', 'Weekly', 100, '*.txt'),
(6, 6, 'Cinahl Plus', 'FTP', 'Daily', 'Weekly', 4200000, '*.xml');

-- --------------------------------------------------------

--
-- Table structure for table `feed_prod`
--

CREATE TABLE IF NOT EXISTS `feed_prod` (
  `feed_id` int(11) NOT NULL,
  `prodcode` varchar(10) NOT NULL,
  PRIMARY KEY (`feed_id`,`prodcode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `feed_prod`
--

INSERT INTO `feed_prod` (`feed_id`, `prodcode`) VALUES
(1, 'custcat'),
(1, 'inun'),
(3, 'custcat'),
(3, 'melb'),
(4, 'custcat'),
(4, 'melb');

-- --------------------------------------------------------

--
-- Table structure for table `files`
--

CREATE TABLE IF NOT EXISTS `files` (
  `feed_id` int(11) NOT NULL,
  `file_path` varchar(200) NOT NULL,
  `file_name` varchar(50) NOT NULL,
  `file_size` varchar(30) DEFAULT NULL,
  `date_posted` date DEFAULT NULL,
  PRIMARY KEY (`feed_id`,`file_path`,`file_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `files`
--

INSERT INTO `files` (`feed_id`, `file_path`, `file_name`, `file_size`, `date_posted`) VALUES
(1, '\\\\server\\FTP\\127.68.42.1\\custcat\\inun', 'file1.out', '23000', '2009-12-06'),
(1, '\\\\server\\FTP\\127.68.42.1\\custcat\\inun', 'file2.out', '20000', '2009-12-06'),
(1, '\\\\server\\FTP\\127.68.42.1\\custcat\\inun', 'file4.out', '1', '2009-12-07'),
(1, '\\\\server\\FTP\\127.68.42.1\\custcat\\inun\\extra', 'file5.out', '200', '2009-12-07'),
(1, '\\\\server\\FTP\\127.68.42.1\\custcat\\inun\\sub', 'file3.out', '100', '2009-12-07'),
(3, '\\\\server\\FTP\\127.68.42.1\\custcat\\melb', 'file1.out', '10000', '2009-10-23');

-- --------------------------------------------------------

--
-- Table structure for table `ftp_info`
--

CREATE TABLE IF NOT EXISTS `ftp_info` (
  `feed_id` int(11) NOT NULL,
  `host` varchar(50) DEFAULT NULL,
  `username` varchar(25) DEFAULT NULL,
  `password` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`feed_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ftp_info`
--


-- --------------------------------------------------------

--
-- Table structure for table `prod_info`
--

CREATE TABLE IF NOT EXISTS `prod_info` (
  `prodcode` varchar(10) NOT NULL,
  `prodname` varchar(50) DEFAULT NULL,
  `date_range` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`prodcode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `prod_info`
--

INSERT INTO `prod_info` (`prodcode`, `prodname`, `date_range`) VALUES
('custcat', 'Customer Catalogs', '1800 - present'),
('gua', 'Georgia Catalog', '1890 - present'),
('inun', 'Indiana University', '1900 - present'),
('melb', 'Melbourne Catalog', '1900-Present'),
('metu', 'METU Catalog', '1900 - present');

-- --------------------------------------------------------

--
-- Table structure for table `user_info`
--

CREATE TABLE IF NOT EXISTS `user_info` (
  `username` varchar(25) NOT NULL,
  `password` varchar(25) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_info`
--

INSERT INTO `user_info` (`username`, `password`) VALUES
('test', 'test'),
('test2', 'password');

-- --------------------------------------------------------

--
-- Table structure for table `vendor_info`
--

CREATE TABLE IF NOT EXISTS `vendor_info` (
  `vendor_id` int(11) NOT NULL AUTO_INCREMENT,
  `vendor_name` varchar(50) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `email` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`vendor_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `vendor_info`
--

INSERT INTO `vendor_info` (`vendor_id`, `vendor_name`, `address`, `phone`, `email`) VALUES
(1, 'Indiana University', '107 S. Indiana Ave., Bloomington, IN', '(812) 855-4848', 'info@iu.edu'),
(2, 'University of Melbourne', 'Australia', '555-555-5555', 'library@melbourne.edu'),
(3, 'University of Georgia', 'Athens, GA', '(706) 542-3000', 'infodesk@uga.edu'),
(4, 'University of Liverpool', 'Liverpool L69 3BX, England, United Kingdom', '+44 (0)151 794 2000', 'info@liv.ac.uk'),
(5, 'Middle Eastern Technical University', '06531 Ankara/Turkey', '+90 312 210 20 00', 'kddbmetu.edu.tr'),
(6, 'CINAHL', '1509 Wilson Terr., Glendale, CA 91206', '(800) 959-7167', 'info@cinahl.com'),
(7, 'SCHIN', 'Newcastle upon Tyne, NE1 2ES, United Kingdom', '+44 (0)191 243 6100', '');

-- --------------------------------------------------------

--
-- Table structure for table `vendor_prod`
--

CREATE TABLE IF NOT EXISTS `vendor_prod` (
  `vendor_id` int(11) NOT NULL,
  `prodcode` varchar(10) NOT NULL,
  PRIMARY KEY (`vendor_id`,`prodcode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `vendor_prod`
--

INSERT INTO `vendor_prod` (`vendor_id`, `prodcode`) VALUES
(1, 'custcat'),
(1, 'inun'),
(2, 'custcat'),
(2, 'melb'),
(3, 'custcat'),
(4, 'custcat'),
(5, 'custcat'),
(5, 'metu');
