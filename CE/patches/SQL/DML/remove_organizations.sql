DELIMITER //
DROP PROCEDURE IF EXISTS RemoveOrganizations //
CREATE PROCEDURE RemoveOrganizations()
BEGIN

DECLARE exit handler for sqlexception
  BEGIN
    SHOW ERRORS;
  ROLLBACK;
END;

DECLARE exit handler for sqlwarning
  BEGIN
    SHOW WARNINGS;
  ROLLBACK;
END;

START TRANSACTION;

-- Assumes this has been run *after* the general cleanup script

-- select * from organization where ORGANIZATION_ID='000001';
delete from budget_sub_awards where ORGANIZATION_ID in (select ORGANIZATION_ID from organization where ORGANIZATION_ID != '000001');
delete from award_approved_subawards where ORGANIZATION_ID in (select ORGANIZATION_ID from organization where ORGANIZATION_ID != '000001');
delete from organization_ynq where ORGANIZATION_ID in (select ORGANIZATION_ID from organization where ORGANIZATION_ID != '000001');
delete from organization_type where ORGANIZATION_ID in (select ORGANIZATION_ID from organization where ORGANIZATION_ID != '000001');
delete from organization where ORGANIZATION_ID != '000001';

COMMIT;

END //
DELIMITER ;

call RemoveOrganizations();

DROP PROCEDURE IF EXISTS RemoveOrganizations;
