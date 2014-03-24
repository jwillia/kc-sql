Import DDL into database
------------------------

its in `kc_hr_rest/src/main/resources/importstatus.sql`


Authentication
--------------

Authentication uses HTTP basic access authentication [1]. Each request to the HR import endpoints is checked for a valid username and password. Authentication will be based upon three parameters configured in kc-config.xml:

hrimport.authn.username
hrimport.authn.password
hrimport.authn.runas

The username and password values that are submitted with each HTTP request must match the values configured for hrimport.authn.username and hrimport.authn.password, respectively. If these values match the request will continue. All activity performed by the request will be performed under within a user session established for the user identified by hrimport.authn.runas.

[1] http://en.wikipedia.org/wiki/Basic_access_authentication

CURL Testing
------------

1. Import:

  ```
  curl --user [username]:[password] --form file=@[local file name] https://[host]/kc-dev/hr-import/hrimport/import

  response:
    OK:
      status: 200
      content:
    {
      "importId":"331197E0-216A-4777-8184-18C544048E72",
      "status":"PROCESSING",
      "message":"Import is processing",
      "startTime":"1395247538",
      "recordTotal":"50",
      "processedRecords":"0",
      "errorCount":"0"
     }
  ```

2. Abort import:

  ```
  curl --user [username]:[password] -X DELETE https://[host]/kc-dev/hr-import/hrimport/import/[importId]

  response:
    OK:
      status: 200
  ```

3. Import status:

  ```
  curl --user [username]:[password] https://[host]/kc-dev/hr-import/hr-import/hrimport/import/[importId]

    OK:
      status: 200
      content:
    still processing:
    {
      "importId":"331197E0-216A-4777-8184-18C544048E72",
      "status":"PROCESSING",
      "message":"Import is processing",
      "startTime":"1395247538",
      "recordTotal":"50",
      "processedRecords":"15",
      "errorCount":"1",
      "errors": [
          {
            "recordNumber":"3",
            "exception":
              {
                "type":"IllegalArgumentException"
              },
            "message":"Unit number 10000000 does not exist"
          }
       ]
     }
     complete:
     {
       "importId":"331197E0-216A-4777-8184-18C544048E72",
       "status":"COMPLETE",
       "message":"Import completed normally",
       "startTime":"1395247538",
       "endTime":"1395248156",
       "recordTotal":"50",
       "processedRecords":"50",
       "errorCount":"1",
       "errors": [
         {
           "recordNumber":"3",
           "exception":
             {
               "type":"IllegalArgumentException"
             },
           "message":"Unit number 10000000 does not exist"
         }
       ]
     }
     aborted:
     {
       "importId":"331197E0-216A-4777-8184-18C544048E72",
       "status":"ABORTED",
       "message":"Import aborted",
       "startTime":"1395247538",
       "endTime":"1395247553",
       "recordTotal":"50",
       "processedRecords":"22",
       "errorCount":"0"
     }
     abnormal termination:
     {
       "importId":"331197E0-216A-4777-8184-18C544048E72",
       "status":"ABNORMAL_TERMINATION",
       "message":"Import terminated abnormally",
       "startTime":"1395247538",
       "endTime":"1395247553",
       "recordTotal":"50",
       "processedRecords":"22",
       "errorCount":"0"
     }
  ```
