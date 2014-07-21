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
delete from budget_sub_award_period_detail;
delete from budget_sub_award_files;
delete from awd_bgt_det_cal_amts_ext;
delete from budget_details_cal_amts;
delete from budget_rate_and_base;
delete from budget_per_det_rate_and_base;
delete from awd_budget_per_cal_amts_ext;
delete from budget_personnel_cal_amts;
delete from awd_budget_per_det_ext;
delete from budget_personnel_details;
delete from award_budget_details_ext;
delete from budget_details;
delete from budget_sub_awards;
delete from award_approved_subawards;
delete from organization_ynq where ORGANIZATION_ID in (select ORGANIZATION_ID from organization where ORGANIZATION_ID != '000001');
delete from organization_type where ORGANIZATION_ID in (select ORGANIZATION_ID from organization where ORGANIZATION_ID != '000001');
delete from protocol_location where ORGANIZATION_ID in (select ORGANIZATION_ID from organization where ORGANIZATION_ID != '000001');
delete from iacuc_protocol_location where ORGANIZATION_ID in (select ORGANIZATION_ID from organization where ORGANIZATION_ID != '000001');
delete from organization where ORGANIZATION_ID != '000001';

COMMIT;

END //
DELIMITER ;

call RemoveOrganizations();

DROP PROCEDURE IF EXISTS RemoveOrganizations;
