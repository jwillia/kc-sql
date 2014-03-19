CREATE TABLE import_status (
  importId                VARCHAR(50) NOT NULL PRIMARY KEY,
  status                  VARCHAR(20) NOT NULL DEFAULT 'PROCESSING',
  startTime               BIGINT NOT NULL,
  endTime                 BIGINT DEFAULT -1,
  numRecords              INT DEFAULT 0,
  numProcessed            INT DEFAULT 0,
  detail                  TEXT
);

CREATE TABLE import_errors (
  errorId                 INT PRIMARY KEY AUTO_INCREMENT,
  importId                VARCHAR(50) NOT NULL,
  recordNum               INT,
  exception               TEXT,

  CONSTRAINT FK_IMPORT FOREIGN KEY (importId)
  REFERENCES import_status(importId)
  ON DELETE CASCADE
);

CREATE TABLE import_persons (
  personId                VARCHAR(40) PRIMARY KEY NOT NULL,
  importId                VARCHAR(50) NOT NULL,
  recordStatus            VARCHAR(8) NOT NULL DEFAULT 'ADDED',

  CONSTRAINT FK_IMPORT_STATUS FOREIGN KEY (importId)
  REFERENCES import_status(importId)
  ON DELETE CASCADE
);