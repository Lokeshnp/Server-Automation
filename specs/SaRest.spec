# User should be able to excute & view the plan status through SA-REST API's. In order to execute Plan engine & SA-REST API Service should be up & running

Verify SA-REST Get API works for master site automation plan having external site fixlets/tasks
-------------------------------------------------------------------------------------------------
tags:
* User creates a master site plan having external site fixlets/tasks
|OS Name |
|--------|
|windows |
|Linux   |
* And checks whether sa rest GET api works for external site
* And gets response as http 200 status code for external site
* And response body as plan defintion xml template of external site


Verify SA-REST Get API works for existing external site automation plan
---------------------------------------------------------------------------------------

tags:
* When User sends SA-REST Get Api Request for the existing external site automation plan
* And checks whether sa rest GET api works for existing external site
* Then in response user should get http 200 status code for existing external site
* And response body as plan defintion xml template of existing external site

Verify SA-REST Plan Action API without authentication parameters
-----------------------------------------------------------------
tags:

* When User sends SA-REST Plan Action Api Request without authentication parameters
* And checks whether sa rest GET api works for without authentication
* Then in response user should get http 401 status code for without authentication
* And user should validate the error message in response body of without authentication


Verify SA-REST execute Plan API without authentication parameters
-----------------------------------------------------------------
tags:

* When User sends SA-REST execute Plan Api Request without authentication parameters
* And checks whether sa rest POST api works for execute plan
* Then in response user should get http 401 status code for execute plan
* And user should validate the error message in response body of execute plan

Verify SA-REST Get API without authentication parameters
-------------------------------------------------------------------------------------
tags:
* When User sends SA-REST Get Api Request without authentication parameters
* Then in response user should get http 401 status code
* And user should validate the error message in response body


Verify SA-REST Get API with invalid authentication parameters
----------------------------------------------------------------------------------------
tags:
* When User sends SA-REST Get Api Request with invalid authentication parameters
* Then in response user should get http 401 status code for invalid authentication
*  And checks whether sa rest GET api works for invalid authentication
* And user should validate the error message in response body of invalid authentication

Verify SA-REST Plan Action API works for existing plan action id
-----------------------------------------------------------------
tags:
  * Create automation plan with multiple fixlet on following OS
  |OS Name |
  |--------|
  |windows |
  |Linux   |
  * Then execute automation plan with multiple fixlet on following OS
   |OS Name |
   |--------|
   |windows |
   |Linux   |
* And checks whether sa rest GET api works for existing plan
* Then in response user should get http 200 status code for existing plan
* And response body as plan defintion xml template of existing plan

Verify SA-REST Plan Action API with invalid authentication parameters
-----------------------------------------------------------------------
tags:
* When User sends SA-REST Plan Action Api Request with invalid authentication parameter
* And checks whether sa rest GET api works for invalid authentication parameter
* Then in response user should get http 401 status code for invalid authentication parameter
* And user should validate the error message in response body of invalid authentication parameter


Verify SA-REST Get API works for master site automation plan having os deployment site fixlets/tasks
-------------------------------------------------------------------------------------------------
tags:
*User creates a automation plan having os deployment and bare metal imagning fixlets/tasks
  |--------|
  |windows |
  |Linux   |
* And checks whether sa rest GET api works for os deployment site
* And gets response as http 200 status code for os deployment site
* And response body as plan defintion xml template of os deployment site


Verify SA-REST Get API works for custom site automation plan
---------------------------------------------------------------------------------------
tags:
* User creates a custom site plan
* And checks whether sa rest GET api works for custom site
* Then in response user should get http 200 status code for custom site
* And response body as plan defintion xml template of custom site
