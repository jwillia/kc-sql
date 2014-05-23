select * from krim_entity_t;

delete from krim_entity_emp_info_t where ENTITY_AFLTN_ID in (select ENTITY_AFLTN_ID from krim_entity_afltn_t where ENTITY_ID!='1100');
delete from krim_entity_afltn_t where ENTITY_ID!='1100';
delete from krim_entity_t where ENTITY_ID!='1100';
