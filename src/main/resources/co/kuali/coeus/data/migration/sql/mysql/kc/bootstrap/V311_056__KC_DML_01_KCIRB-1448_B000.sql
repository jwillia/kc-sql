DELIMITER /

INSERT INTO PROTOCOL_ACTION_TYPE (PROTOCOL_ACTION_TYPE_CODE, DESCRIPTION, TRIGGER_SUBMISSION, TRIGGER_CORRESPONDENCE, FINAL_ACTION_FOR_BATCH_CORRESP, UPDATE_TIMESTAMP,UPDATE_USER, obj_id) 
VALUES ('903', 'Return to Reviewer', 'N', 'N', 'N', now(), 'admin', UUID())
/

insert into SEQ_NOTIFICATION_TYPE_ID values (NULL)
/
insert into notification_type
(NOTIFICATION_TYPE_ID,MODULE_CODE,ACTION_CODE,DESCRIPTION,SUBJECT,MESSAGE,PROMPT_USER,SEND_NOTIFICATION,SYSTEM_GENERATED,UPDATE_USER,    
 UPDATE_TIMESTAMP,VER_NBR,OBJ_ID ) values(                             
 last_insert_id(),7,903,'Reject Review','{MESSAGE_SUBJECT}','{MESSAGE_BODY}',  'N','Y','Y','admin',NOW(),1,
UUID())
/

insert into SEQ_NOTIFICATION_TYPE_ID values(NULL)
/
insert into NOTIFICATION_TYPE_RECIPIENT(
NOTIFICATION_TYPE_RECIPIENT_ID,NOTIFICATION_TYPE_ID,ROLE_name,ROLE_QUALIFIER,TO_OR_CC,UPDATE_USER,UPDATE_TIMESTAMP,VER_NBR,OBJ_ID)
values(last_insert_id(),(select NOTIFICATION_TYPE_ID from notification_type where DESCRIPTION = 'Reject Review'),'KC-PROTOCOL:IRB Online Reviewer','submissionId', 'T','admin',NOW(),1, 
UUID())
/

insert into SEQ_NOTIFICATION_TYPE_ID values(NULL)
/
insert into NOTIFICATION_TYPE_RECIPIENT(
NOTIFICATION_TYPE_RECIPIENT_ID,NOTIFICATION_TYPE_ID,ROLE_name,ROLE_QUALIFIER,TO_OR_CC,UPDATE_USER,UPDATE_TIMESTAMP,VER_NBR,OBJ_ID)
values(last_insert_id(),(select NOTIFICATION_TYPE_ID from notification_type where DESCRIPTION = 'Reject Review'),'KC-PROTOCOL:IRB Online Reviewer','protocolOnlineReviewId', 'T','admin',NOW(),1, 
UUID())
/

insert into SEQ_NOTIFICATION_TYPE_ID values(NULL)
/
insert into NOTIFICATION_TYPE_RECIPIENT(
NOTIFICATION_TYPE_RECIPIENT_ID,NOTIFICATION_TYPE_ID,ROLE_name,ROLE_QUALIFIER,TO_OR_CC,UPDATE_USER,UPDATE_TIMESTAMP,VER_NBR,OBJ_ID)
values(last_insert_id(),(select NOTIFICATION_TYPE_ID from notification_type where DESCRIPTION = 'Reject Review'),'KC-PROTOCOL:IRB Online Reviewer','protocolLeadUnitNumber', 'T','admin',NOW(),1, 
UUID())
/

DELIMITER ;

