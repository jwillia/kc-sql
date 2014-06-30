-- find users that should be deactivated (may vary for your case, double check!)

select ENTITY_ID from krim_prncpl_t where (PRNCPL_ID like '1%') OR (PRNCPL_ID = '2') OR (PRNCPL_ID = 'guest') OR (PRNCPL_ID = 'notsys');

-- deactivate them!

update krim_entity_emp_info_t set ACTV_IND='N' where ENTITY_ID IN (select ENTITY_ID from krim_prncpl_t where (PRNCPL_ID like '1%') OR (PRNCPL_ID = '2') OR (PRNCPL_ID = 'guest') OR (PRNCPL_ID = 'notsys'));
update krim_entity_afltn_t set ACTV_IND='N' where ENTITY_ID IN (select ENTITY_ID from krim_prncpl_t where (PRNCPL_ID like '1%') OR (PRNCPL_ID = '2') OR (PRNCPL_ID = 'guest') OR (PRNCPL_ID = 'notsys'));
update krim_entity_t set ACTV_IND='N' where ENTITY_ID IN (select ENTITY_ID from krim_prncpl_t where (PRNCPL_ID like '1%') OR (PRNCPL_ID = '2') OR (PRNCPL_ID = 'guest') OR (PRNCPL_ID = 'notsys'));
update krim_prncpl_t set ACTV_IND='N' where (PRNCPL_ID like '1%') OR (PRNCPL_ID = '2') OR (PRNCPL_ID = 'guest') OR (PRNCPL_ID = 'notsys');
