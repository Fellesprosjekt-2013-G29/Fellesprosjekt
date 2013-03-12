SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `fellesprosjekt` ;
CREATE SCHEMA IF NOT EXISTS `fellesprosjekt` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;
USE `fellesprosjekt` ;

-- -----------------------------------------------------
-- Table `fellesprosjekt`.`User`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `fellesprosjekt`.`User` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `email` VARCHAR(255) NOT NULL ,
  `name` VARCHAR(45) NULL DEFAULT NULL ,
  `password` BLOB NOT NULL ,
  `pw_hash` BLOB NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `fellesprosjekt`.`Room`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `fellesprosjekt`.`Room` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `roomnr` VARCHAR(8) NOT NULL ,
  `location` VARCHAR(20) NULL DEFAULT NULL ,
  `size` INT NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) );


-- -----------------------------------------------------
-- Table `fellesprosjekt`.`Appointment`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `fellesprosjekt`.`Appointment` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(100) NOT NULL ,
  `start` DATETIME NOT NULL ,
  `end` DATETIME NOT NULL ,
  `description` TEXT NULL DEFAULT NULL ,
  `roomid` INT NULL DEFAULT NULL ,
  `owner` INT NOT NULL ,
  `deleted` TINYINT(1) NOT NULL DEFAULT 0 ,
  INDEX `userid_idx` (`owner` ASC) ,
  INDEX `room_idx` (`roomid` ASC) ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_user`
    FOREIGN KEY (`owner` )
    REFERENCES `fellesprosjekt`.`User` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_room`
    FOREIGN KEY (`roomid` )
    REFERENCES `fellesprosjekt`.`Room` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `fellesprosjekt`.`Alarm`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `fellesprosjekt`.`Alarm` (
  `apointmentid` INT NOT NULL ,
  `userid` INT NOT NULL ,
  `alarm_time` DATETIME NOT NULL ,
  PRIMARY KEY (`apointmentid`, `userid`) ,
  INDEX `appointment_idx` (`apointmentid` ASC) ,
  INDEX `user_idx` (`userid` ASC) ,
  CONSTRAINT `fk_appointment`
    FOREIGN KEY (`apointmentid` )
    REFERENCES `fellesprosjekt`.`Appointment` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_userr`
    FOREIGN KEY (`userid` )
    REFERENCES `fellesprosjekt`.`User` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `fellesprosjekt`.`CanceledAppointment`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `fellesprosjekt`.`CanceledAppointment` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `cancelled_on` TIMESTAMP NOT NULL ,
  `appointment_id` INT NOT NULL ,
  `user_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_ca_aptmnt`
    FOREIGN KEY (`id` )
    REFERENCES `fellesprosjekt`.`Appointment` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ca_user`
    FOREIGN KEY (`id` )
    REFERENCES `fellesprosjekt`.`User` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `fellesprosjekt`.`Invitation`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `fellesprosjekt`.`Invitation` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `created` TIMESTAMP NOT NULL ,
  `status` ENUM('YES', 'NO', 'NA') NOT NULL DEFAULT 'NA' ,
  `alarm` DATETIME NULL ,
  `appointment_id` INT NOT NULL ,
  `user_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_inv_user`
    FOREIGN KEY (`id` )
    REFERENCES `fellesprosjekt`.`User` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_aptm`
    FOREIGN KEY (`id` )
    REFERENCES `fellesprosjekt`.`Appointment` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `fellesprosjekt` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
