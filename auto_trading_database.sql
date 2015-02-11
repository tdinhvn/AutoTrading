-- phpMyAdmin SQL Dump
-- version 4.2.11
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 11, 2015 at 02:46 PM
-- Server version: 5.6.21
-- PHP Version: 5.6.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `auto_trading_database`
--

-- --------------------------------------------------------

--
-- Table structure for table `asset`
--

CREATE TABLE IF NOT EXISTS `asset` (
`asset_id` bigint(11) NOT NULL,
  `name` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `symbol` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `exchange_id` bigint(11) NOT NULL,
  `asset_info` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fluctuation_range` double DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=5740 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `exchange`
--

CREATE TABLE IF NOT EXISTS `exchange` (
`exchange_id` bigint(11) NOT NULL,
  `name` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `fluctuation_range` double DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `order`
--

CREATE TABLE IF NOT EXISTS `order` (
`order_id` bigint(11) NOT NULL,
  `order_type` tinyint(1) NOT NULL,
  `user_id` bigint(11) NOT NULL,
  `date` date NOT NULL,
  `asset_id` bigint(11) NOT NULL,
  `price` double NOT NULL,
  `volume` double NOT NULL,
  `matched` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `portfolio`
--

CREATE TABLE IF NOT EXISTS `portfolio` (
`portfolio_id` bigint(11) NOT NULL,
  `user_id` bigint(11) NOT NULL,
  `asset_id` bigint(11) NOT NULL,
  `price` double NOT NULL,
  `volume` double NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `price`
--

CREATE TABLE IF NOT EXISTS `price` (
`price_id` bigint(11) NOT NULL,
  `asset_id` bigint(11) NOT NULL,
  `date` date NOT NULL,
  `delivery_date` date NOT NULL DEFAULT '0000-01-01',
  `volume` double NOT NULL,
  `open` double NOT NULL,
  `close` double NOT NULL,
  `high` double NOT NULL,
  `low` double NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3495390 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `report`
--

CREATE TABLE IF NOT EXISTS `report` (
`report_id` bigint(11) NOT NULL,
  `asset_id` bigint(11) NOT NULL,
  `quarter` int(11) DEFAULT NULL,
  `year` int(11) NOT NULL,
  `shares_outstanding` bigint(20) DEFAULT NULL,
  `EPS` double DEFAULT NULL,
  `current_asset` double DEFAULT NULL,
  `cash_and_cash_equivalents` double DEFAULT NULL,
  `shortterm_financial_investment` double DEFAULT NULL,
  `shortterm_account_receivables` double DEFAULT NULL,
  `inventory` double DEFAULT NULL,
  `other_current_assets` double DEFAULT NULL,
  `non_current_assets` double DEFAULT NULL,
  `longterm_account_receivable` double DEFAULT NULL,
  `fixed_assets` double DEFAULT NULL,
  `tangible_fixed_assets_accumulated_depreciation` double DEFAULT NULL,
  `leasing_fixed_assets_accumulated_depreciation` double DEFAULT NULL,
  `intangible_fixed_assets_accumulated_depreciation` double DEFAULT NULL,
  `goodwills` double DEFAULT NULL,
  `real_estate_investment` double DEFAULT NULL,
  `longterm_financial_investments` double DEFAULT NULL,
  `other_longterm_assets` double DEFAULT NULL,
  `total_asset` double DEFAULT NULL,
  `liabilities` double DEFAULT NULL,
  `shortterm_liabilities` double DEFAULT NULL,
  `longterm_liabilities` double DEFAULT NULL,
  `provision` double DEFAULT NULL,
  `other_payable` double DEFAULT NULL,
  `owners_equity` double DEFAULT NULL,
  `expenditures_and_other_funds` double DEFAULT NULL,
  `owners_capitals` double DEFAULT NULL,
  `minority_interest` double DEFAULT NULL,
  `total_equity` double DEFAULT NULL,
  `gross_sale_revenues` double DEFAULT NULL,
  `deduction_revenues` double DEFAULT NULL,
  `net_sales` double DEFAULT NULL,
  `cost_of_goods_sold` double DEFAULT NULL,
  `gross_profit` double DEFAULT NULL,
  `financial_activities_revenues` double DEFAULT NULL,
  `financial_expenses` double DEFAULT NULL,
  `selling_expenses` double DEFAULT NULL,
  `managing_expenses` double DEFAULT NULL,
  `net_profit_from_operating_activities` double DEFAULT NULL,
  `other_incomes` double DEFAULT NULL,
  `other_expenses` double DEFAULT NULL,
  `other_profits` double DEFAULT NULL,
  `total_profit_before_tax` double DEFAULT NULL,
  `corporate_income_tax_expenses` double DEFAULT NULL,
  `profit_after_corporate_income_tax` double DEFAULT NULL,
  `benefits_of_minority_shareholders` double DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=15170 DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `total_asset`
--

CREATE TABLE IF NOT EXISTS `total_asset` (
`total_asset_id` bigint(20) NOT NULL,
  `date` date NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `total_asset` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
`user_id` bigint(11) NOT NULL,
  `name` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `cash` double NOT NULL,
  `cash01` double NOT NULL,
  `cash02` double NOT NULL,
  `cash03` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `asset`
--
ALTER TABLE `asset`
 ADD PRIMARY KEY (`asset_id`), ADD UNIQUE KEY `symbol` (`symbol`,`exchange_id`), ADD KEY `exchange_name` (`exchange_id`);

--
-- Indexes for table `exchange`
--
ALTER TABLE `exchange`
 ADD PRIMARY KEY (`exchange_id`), ADD UNIQUE KEY `name_2` (`name`), ADD KEY `name` (`name`);

--
-- Indexes for table `order`
--
ALTER TABLE `order`
 ADD PRIMARY KEY (`order_id`), ADD KEY `user_id` (`user_id`), ADD KEY `asset_id` (`asset_id`);

--
-- Indexes for table `portfolio`
--
ALTER TABLE `portfolio`
 ADD PRIMARY KEY (`portfolio_id`), ADD UNIQUE KEY `user_id_2` (`user_id`,`asset_id`,`date`), ADD KEY `user_id` (`user_id`), ADD KEY `asset_id` (`asset_id`);

--
-- Indexes for table `price`
--
ALTER TABLE `price`
 ADD PRIMARY KEY (`price_id`), ADD UNIQUE KEY `asset_id_2` (`asset_id`,`date`,`delivery_date`), ADD KEY `asset_id` (`asset_id`), ADD KEY `date` (`date`);

--
-- Indexes for table `report`
--
ALTER TABLE `report`
 ADD PRIMARY KEY (`report_id`), ADD UNIQUE KEY `asset_id` (`asset_id`,`quarter`,`year`);

--
-- Indexes for table `total_asset`
--
ALTER TABLE `total_asset`
 ADD PRIMARY KEY (`total_asset_id`), ADD UNIQUE KEY `Date` (`date`,`user_id`), ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
 ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `asset`
--
ALTER TABLE `asset`
MODIFY `asset_id` bigint(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5740;
--
-- AUTO_INCREMENT for table `exchange`
--
ALTER TABLE `exchange`
MODIFY `exchange_id` bigint(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `order`
--
ALTER TABLE `order`
MODIFY `order_id` bigint(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `portfolio`
--
ALTER TABLE `portfolio`
MODIFY `portfolio_id` bigint(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `price`
--
ALTER TABLE `price`
MODIFY `price_id` bigint(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3495390;
--
-- AUTO_INCREMENT for table `report`
--
ALTER TABLE `report`
MODIFY `report_id` bigint(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=15170;
--
-- AUTO_INCREMENT for table `total_asset`
--
ALTER TABLE `total_asset`
MODIFY `total_asset_id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
MODIFY `user_id` bigint(11) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `asset`
--
ALTER TABLE `asset`
ADD CONSTRAINT `asset_ibfk_1` FOREIGN KEY (`exchange_id`) REFERENCES `exchange` (`exchange_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `order`
--
ALTER TABLE `order`
ADD CONSTRAINT `order_ibfk_1` FOREIGN KEY (`asset_id`) REFERENCES `asset` (`asset_id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `order_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `portfolio`
--
ALTER TABLE `portfolio`
ADD CONSTRAINT `portfolio_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `portfolio_ibfk_2` FOREIGN KEY (`asset_id`) REFERENCES `asset` (`asset_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `price`
--
ALTER TABLE `price`
ADD CONSTRAINT `price_ibfk_1` FOREIGN KEY (`asset_id`) REFERENCES `asset` (`asset_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `total_asset`
--
ALTER TABLE `total_asset`
ADD CONSTRAINT `total_asset_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
