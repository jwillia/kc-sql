-- Find all GSU entities
select ENTITY_ID from KRIM_ENTITY_EMAIL_T where LOWER(EMAIL_ADDR) LIKE '%@georgiasouthern.edu';

-- Activate all GSU users
update KRIM_ENTITY_EMP_INFO_T set ACTV_IND='Y' where ENTITY_ID IN (select ENTITY_ID from KRIM_ENTITY_EMAIL_T where LOWER(EMAIL_ADDR) LIKE '%@georgiasouthern.edu');
update KRIM_ENTITY_AFLTN_T set ACTV_IND='Y' where ENTITY_ID IN (select ENTITY_ID from KRIM_ENTITY_EMAIL_T where LOWER(EMAIL_ADDR) LIKE '%@georgiasouthern.edu');
update KRIM_ENTITY_T set ACTV_IND='Y' where ENTITY_ID IN (select ENTITY_ID from KRIM_ENTITY_EMAIL_T where LOWER(EMAIL_ADDR) LIKE '%@georgiasouthern.edu');
update KRIM_PRNCPL_T set ACTV_IND='Y' where ENTITY_ID IN (select ENTITY_ID from KRIM_ENTITY_EMAIL_T where LOWER(EMAIL_ADDR) LIKE '%@georgiasouthern.edu');
