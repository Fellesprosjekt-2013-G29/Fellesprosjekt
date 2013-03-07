SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `fellesprosjekt` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;
USE `fellesprosjekt` ;

-- -----------------------------------------------------
-- Table `User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `User` ;

CREATE  TABLE IF NOT EXISTS `User` (
  `id` INT NOT NULL ,
  `email` VARCHAR(255) NOT NULL ,
  `name` VARCHAR(45) NULL ,
  `password` CHAR(224) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Room`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Room` ;

CREATE  TABLE IF NOT EXISTS `Room` (
  `id` INT NOT NULL ,
  `roomnr` VARCHAR(8) NOT NULL ,
  `location` VARCHAR(20) NULL DEFAULT NULL ,
  `size` INT NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) );


-- -----------------------------------------------------
-- Table `Appointment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Appointment` ;

CREATE  TABLE IF NOT EXISTS `Appointment` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(100) NOT NULL ,
  `start` DATETIME NOT NULL ,
  `end` DATETIME NOT NULL ,
  `description` TEXT NULL DEFAULT NULL ,
  `roomid` INT NULL ,
  `owner` INT NOT NULL ,
  INDEX `userid_idx` (`owner` ASC) ,
  INDEX `room_idx` (`roomid` ASC) ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_user`
    FOREIGN KEY (`owner` )
    REFERENCES `User` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_room`
    FOREIGN KEY (`roomid` )
    REFERENCES `Room` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `Alarm`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Alarm` ;

CREATE  TABLE IF NOT EXISTS `Alarm` (
  `apointmentid` INT NOT NULL ,
  `userid` INT NOT NULL ,
  `alarm_time` DATETIME NOT NULL ,
  PRIMARY KEY (`apointmentid`, `userid`) ,
  INDEX `appointment_idx` (`apointmentid` ASC) ,
  INDEX `user_idx` (`userid` ASC) ,
  CONSTRAINT `fk_appointment`
    FOREIGN KEY (`apointmentid` )
    REFERENCES `Appointment` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_userr`
    FOREIGN KEY (`userid` )
    REFERENCES `User` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `Group_tbl`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Group_tbl` ;

CREATE  TABLE IF NOT EXISTS `Group_tbl` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`id`) );


-- -----------------------------------------------------
-- Table `Rel_user_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Rel_user_group` ;

CREATE  TABLE IF NOT EXISTS `Rel_user_group` (
  `userid` INT NOT NULL ,
  `groupid` INT NOT NULL ,
  PRIMARY KEY (`userid`, `groupid`) ,
  INDEX `group_idx` (`groupid` ASC) ,
  INDEX `user_idx` (`userid` ASC) ,
  CONSTRAINT `fk_group`
    FOREIGN KEY (`groupid` )
    REFERENCES `Group_tbl` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_rel`
    FOREIGN KEY (`userid` )
    REFERENCES `User` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `Rel_group_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Rel_group_group` ;

CREATE  TABLE IF NOT EXISTS `Rel_group_group` (
  `childid` INT NOT NULL ,
  `parentid` INT NOT NULL ,
  PRIMARY KEY (`childid`, `parentid`) ,
  INDEX `child_idx` (`childid` ASC) ,
  INDEX `parent_idx` (`parentid` ASC) ,
  CONSTRAINT `fk_child`
    FOREIGN KEY (`childid` )
    REFERENCES `Group_tbl` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_parent`
    FOREIGN KEY (`parentid` )
    REFERENCES `Group_tbl` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

USE `fellesprosjekt` ;
USE `fellesprosjekt`;

DELIMITER $$

USE `fellesprosjekt`$$
DROP TRIGGER IF EXISTS `Rel_group_group_BINS` $$
USE `fellesprosjekt`$$


CREATE TRIGGER `Rel_group_group_BINS` BEFORE INSERT ON Rel_group_group FOR EACH ROW
-- Edit trigger body code below this line. Do not edit lines above this one
BEGIN
	IF(NEW.childid = NEW.parentid) THEN
		SET NEW.childid = 0;
	END IF;
END
$$


DELIMITER ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
