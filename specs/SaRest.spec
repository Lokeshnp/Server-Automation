# User should be able to excute & view the plan status through SA-REST API's. In order to execute Plan engine & SA-REST API Service should be up & running

Verify SA-REST Get API works for master site automation plan having external site fixlets/tasks
-------------------------------------------------------------------------------------------------
tags:
* User creates a master site plan having external site fixlets/tasks
|OS Name |
|--------|
|windows |
|Linux   |
* And checks whether sa rest GET api works for it
* And gets response as http 200 status code
* And response body as plan defintion xml template


Verify SA-REST Get API works for existing external site automation plan
---------------------------------------------------------------------------------------

tags:
* When User sends SA-REST Get Api Request for the existing external site automation plan
* And checks whether sa rest GET api works for it
* Then in response user should get http 200 status code
* And response body as plan defintion xml template

Verify SA-REST Plan Action API without authentication parameters
-----------------------------------------------------------------
tags:

* When User sends SA-REST Plan Action Api Request without authentication parameters
* And checks whether sa rest GET api works for it
* Then in response user should get http 401 status code
* And user should validate the error message in response body