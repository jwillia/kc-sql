DELIMITER //
CREATE PROCEDURE UpdateRateType(IN descript varchar(200), IN rateClassCode varchar(3), IN oldRateTypeCode varchar(3), IN newRateTypeCode varchar(3))
BEGIN

DECLARE exit handler for sqlexception
  BEGIN
    -- ERROR
  ROLLBACK;
END;

DECLARE exit handler for sqlwarning
 BEGIN
    -- WARNING
 ROLLBACK;
END;

START TRANSACTION;
insert into rate_type (RATE_CLASS_CODE, RATE_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER, VER_NBR, OBJ_ID) values (rateClassCode, newRateTypeCode, descript, now(), 'admin', '1', UUID());
update institute_rates set RATE_TYPE_CODE=newRateTypeCode where RATE_CLASS_CODE=rateClassCode and RATE_TYPE_CODE=oldRateTypeCode;
update valid_ce_rate_types set RATE_TYPE_CODE=newRateTypeCode where RATE_CLASS_CODE=rateClassCode and RATE_TYPE_CODE=oldRateTypeCode;
delete from rate_type where RATE_CLASS_CODE=rateClassCode and RATE_TYPE_CODE=oldRateTypeCode;
COMMIT;

END //
DELIMITER ;

call UpdateRateType('TDC', '2', '2', '1');
call UpdateRateType('S&W', '3', '3', '1');
call UpdateRateType('Salaries - Non-Classified: SalNC', '5', '4', '1');
call UpdateRateType('Salaries - Classified: SalClass', '5', '5', '2');
call UpdateRateType('Salaries - Graduate Assistants: SalGA', '5', '6', '3');
call UpdateRateType('Hourly - Employee/Non-Student: Wages', '5', '7', '4');
call UpdateRateType('Hourly - Enrolled Students: WagesSTU', '5', '8', '5');
call UpdateRateType('Salaries - Summer: SalSumAL', '5', '9', '6');
call UpdateRateType('Salaries', '5', '10', '7');
call UpdateRateType('Salaries-Non Student', '7', '10', '1');
call UpdateRateType('Salaries-Student', '7', '3', '2');
call UpdateRateType('Tuition - GA', '7', '11', '3');
call UpdateRateType('Materials and Services', '7', '4', '4');
call UpdateRateType('Equipment', '7', '12', '5');
call UpdateRateType('Domestic Travel', '7', '7', '6');