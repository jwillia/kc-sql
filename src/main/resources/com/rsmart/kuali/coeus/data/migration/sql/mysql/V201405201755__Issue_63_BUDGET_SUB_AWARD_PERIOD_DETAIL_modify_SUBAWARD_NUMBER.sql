--  -----------------------------------------------------------------------
--  BUDGET_SUB_AWARD_PERIOD_DETAIL
--  Cannot add or update a child row: a foreign key constraint fails (`coeus`.`budget_sub_award_period_detail`, CONSTRAINT `FK1_BUDGET_SUBAWARD_PER_DET` FOREIGN KEY (`SUBAWARD_NUMBER`, `BUDGET_ID`) REFERENCES `budget_sub_awards` (`SUB_AWARD_NUMBER`, `BUDGET_ID`))
--
-- https://github.com/rSmart/issues/issues/63
-- https://jira.kuali.org/browse/KRACOEUS-6777
-- -----------------------------------------------------------------------
delimiter /

alter table BUDGET_SUB_AWARD_PERIOD_DETAIL modify SUBAWARD_NUMBER decimal(3,0) not null
/

delimiter ;
