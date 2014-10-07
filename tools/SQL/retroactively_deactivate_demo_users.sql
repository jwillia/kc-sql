update KRIM_PRNCPL_T set ACTV_IND='N' where ENTITY_ID in (
  select ENTITY_ID FROM KRIM_ENTITY_EMAIL_T WHERE ((
    LOWER(EMAIL_ADDR) LIKE '%yourschool.edu' OR
    LOWER(EMAIL_ADDR) LIKE '%myschool.edu' OR
    LOWER(EMAIL_ADDR) LIKE '%email.edu' OR
    LOWER(EMAIL_ADDR) LIKE '%rsmartu.edu') AND (
    ENTITY_ID NOT IN ('1', '1100', '1131', '1132'))
  )
);

delete from eps_prop_person_ext where PERSON_ID in (
  select ENTITY_ID FROM KRIM_ENTITY_EMAIL_T WHERE ((
    LOWER(EMAIL_ADDR) LIKE '%yourschool.edu' OR
    LOWER(EMAIL_ADDR) LIKE '%myschool.edu' OR
    LOWER(EMAIL_ADDR) LIKE '%email.edu' OR
    LOWER(EMAIL_ADDR) LIKE '%rsmartu.edu') AND (
    ENTITY_ID NOT IN ('1', '1100', '1131', '1132'))
  )
);

delete from person_ext_t where PERSON_ID in (
  select ENTITY_ID FROM KRIM_ENTITY_EMAIL_T WHERE ((
    LOWER(EMAIL_ADDR) LIKE '%yourschool.edu' OR
    LOWER(EMAIL_ADDR) LIKE '%myschool.edu' OR
    LOWER(EMAIL_ADDR) LIKE '%email.edu' OR
    LOWER(EMAIL_ADDR) LIKE '%rsmartu.edu') AND (
    ENTITY_ID NOT IN ('1', '1100', '1131', '1132'))
  )
);

update KRIM_ROLE_MBR_T SET ACTV_TO_DT = NOW(), LAST_UPDT_DT = NOW() WHERE MBR_TYP_CD = 'P' and MBR_ID IN (
  select PRNCPL_ID FROM KRIM_PRNCPL_T WHERE ENTITY_ID IN (
    select ENTITY_ID FROM KRIM_ENTITY_EMAIL_T WHERE ((
      LOWER(EMAIL_ADDR) LIKE '%yourschool.edu' OR
      LOWER(EMAIL_ADDR) LIKE '%myschool.edu' OR
      LOWER(EMAIL_ADDR) LIKE '%email.edu' OR
      LOWER(EMAIL_ADDR) LIKE '%rsmartu.edu') AND (
      ENTITY_ID NOT IN ('1', '1100', '1131', '1132'))
    )
  )
);
update KRIM_GRP_MBR_T  SET ACTV_TO_DT = NOW(), LAST_UPDT_DT = NOW() WHERE MBR_TYP_CD = 'P' and MBR_ID IN (
  select PRNCPL_ID FROM KRIM_PRNCPL_T WHERE ENTITY_ID IN (
    select ENTITY_ID FROM KRIM_ENTITY_EMAIL_T WHERE ((
      LOWER(EMAIL_ADDR) LIKE '%yourschool.edu' OR
      LOWER(EMAIL_ADDR) LIKE '%myschool.edu' OR
      LOWER(EMAIL_ADDR) LIKE '%email.edu' OR
      LOWER(EMAIL_ADDR) LIKE '%rsmartu.edu') AND (
      ENTITY_ID NOT IN ('1', '1100', '1131', '1132'))
    )
  )
);

