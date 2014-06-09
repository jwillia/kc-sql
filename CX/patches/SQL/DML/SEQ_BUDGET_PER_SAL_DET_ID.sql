# -----------------------------------------------------------------------
# SEQ_BUDGET_PER_SAL_DET_ID
# KC Community release is missing this table completely! Fudge!
# https://jira.kuali.org/browse/KRAFDBCK-10201
# -----------------------------------------------------------------------
delimiter /

drop table if exists SEQ_BUDGET_PER_SAL_DET_ID
/

CREATE TABLE SEQ_BUDGET_PER_SAL_DET_ID
(
    id bigint(19) not null auto_increment, primary key (id)
) ENGINE MyISAM
/
ALTER TABLE SEQ_BUDGET_PER_SAL_DET_ID auto_increment = 9
/

delimiter ;
