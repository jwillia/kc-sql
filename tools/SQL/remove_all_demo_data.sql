DELIMITER //
DROP PROCEDURE IF EXISTS RemoveAllDemoData //
CREATE PROCEDURE RemoveAllDemoData()
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

-- select * from krim_entity_t;
-- disable all entities except kr, admin, notsys, kc
update krim_entity_emp_info_t set ACTV_IND='N' where ENTITY_ID NOT IN ('1', '1100', '1131', '1132');
update krim_entity_afltn_t set ACTV_IND='N' where ENTITY_ID NOT IN ('1', '1100', '1131', '1132');
update krim_entity_t set ACTV_IND='N' where ENTITY_ID NOT IN ('1', '1100', '1131', '1132');
update krim_prncpl_t set ACTV_IND='N' where ENTITY_ID NOT IN ('1', '1100', '1131', '1132');
delete from eps_prop_person_ext;
delete from person_ext_t;
-- delete all entities except admin
-- delete from krim_entity_emp_info_t where ENTITY_AFLTN_ID in (select ENTITY_AFLTN_ID from krim_entity_afltn_t where ENTITY_ID != '1100');
-- delete from krim_entity_afltn_t where ENTITY_ID != '1100';
-- delete from krim_entity_t where ENTITY_ID != '1100';
-- delete from krim_prncpl_t where ENTITY_ID != '1100';

-- deactivate demo users in KRIM_ROLE_MBR_T and KRIM_GRP_MBR_T
update KRIM_ROLE_MBR_T SET ACTV_TO_DT = NOW(), LAST_UPDT_DT = NOW() WHERE MBR_TYP_CD = 'P' and MBR_ID IN (
  select PRNCPL_ID FROM KRIM_PRNCPL_T WHERE ENTITY_ID IN (
    select ENTITY_ID FROM KRIM_ENTITY_EMAIL_T WHERE ((
      EMAIL_ADDR LIKE '%yourschool.edu' OR
      EMAIL_ADDR LIKE '%myschool.edu' OR
      EMAIL_ADDR LIKE '%email.edu' OR
      EMAIL_ADDR LIKE '%rsmartu.edu') AND (
      ENTITY_ID NOT IN ('1', '1100', '1131', '1132'))
    )
  )
);
update KRIM_GRP_MBR_T  SET ACTV_TO_DT = NOW(), LAST_UPDT_DT = NOW() WHERE MBR_TYP_CD = 'P' and MBR_ID IN (
  select PRNCPL_ID FROM KRIM_PRNCPL_T WHERE ENTITY_ID IN (
    select ENTITY_ID FROM KRIM_ENTITY_EMAIL_T WHERE ((
      EMAIL_ADDR LIKE '%yourschool.edu' OR
      EMAIL_ADDR LIKE '%myschool.edu' OR
      EMAIL_ADDR LIKE '%email.edu' OR
      EMAIL_ADDR LIKE '%rsmartu.edu') AND (
      ENTITY_ID NOT IN ('1', '1100', '1131', '1132'))
    )
  )
);

-- select * from committee;
delete from comm_member_expertise;
delete from comm_member_roles;
delete from comm_memberships;
delete from comm_research_areas;
delete from comm_schedule_act_items;
delete from comm_schedule_attendance;
delete from comm_schedule_minute_doc;
delete from schedule_agenda;
delete from comm_schedule;
delete from committee;

delete from KRNS_DOC_HDR_T where DOC_HDR_ID IN (select DOCUMENT_NUMBER from committee_document);
delete from krew_actn_itm_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from committee_document);
delete from krew_doc_hdr_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from committee_document);
delete from committee_document;

-- TODO include remove_transactional_data_only.sql

COMMIT;

END //
DELIMITER ;

call RemoveAllDemoData();

DROP PROCEDURE IF EXISTS RemoveAllDemoData;
