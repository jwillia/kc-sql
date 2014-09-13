DELIMITER /

CREATE TABLE COMM_SCHEDULE_ATTACHMENT_ID (
  id bigint(19) not null auto_increment, primary key (id)
) ENGINE MyISAM
/

ALTER TABLE COMM_SCHEDULE_ATTACHMENT_ID auto_increment = 1
/

DELIMITER ;
