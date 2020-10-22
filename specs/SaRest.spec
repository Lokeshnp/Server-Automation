# User should be able to execute & view the plan status through SA-REST API's. In order to execute Plan engine & SA-REST API Service should be up & running

//Installation of SA-REST API Service
//-------------------------------------------------------------------------------
//*tags:
//* Given sa rest service is not installed on the target machine
//* And User install it on the target machine
//* Then sa rest folder should be created in default location
//* And sa rest service should be up and running

Verify SA-REST Get API works for existing external site automation plan
---------------------------------------------------------------------------------------

tags: external
* When User sends SA-REST Get Api Request for the existing external site automation plan
* And response body as plan defintion xml template of existing external site

Verify SA-REST Get API works for custom site automation plan
---------------------------------------------------------------------------------------
tags: custom
* User creates a custom site plan
* And checks whether sa rest GET api works for custom site
* Then in response user should get http 200 status code for custom site
* And response body as plan defintion xml template of custom site

//Verify SA-REST Get API works for operator site automation plan
//---------------------------------------------------------------------------------------
//*tags:
//* User creates a operator site plan
//* And checks whether sa rest GET api works for it
//* And gets response as http 200 status code
//* And response body as plan defintion xml template
//* And user validate whether plan definition xml template is proper or not

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

Verify SA-REST Get API works for master site automation plan having os deployment site fixlets/tasks
-------------------------------------------------------------------------------------------------
tags: os
*User creates a automation plan having os deployment and bare metal imagning fixlets/tasks
  |--------|
  |windows |
  |Linux   |
* And checks whether sa rest GET api works for os deployment site
* And gets response as http 200 status code for os deployment site
* And response body as plan defintion xml template of os deployment site

Verify SA-REST Get API with invalid authentication parameters
----------------------------------------------------------------------------------------
tags:
* When User sends SA-REST Get Api Request with invalid authentication parameters
* Then in response user should get http 401 status code for invalid authentication
* And user should validate the error message in response body of invalid authentication

Verify SA-REST Get API without authentication parameters
-------------------------------------------------------------------------------------
tags:
* When User sends SA-REST Get Api Request without authentication parameters
* Then in response user should get http 401 status code
* And user should validate the error message in response body

Verify SA-REST execute Plan API without authentication parameters
-----------------------------------------------------------------
tags:
* User creates a master site plan
* When User sends SA-REST execute Plan Api Request without authentication parameters
* Then in response user should get http 401 status code for execute plan
* And user should validate the error message in response body of execute plan

//Execute Plan API by adding multiple target computer ids and names in target set tag of plan definition XML template
//-------------------------------------------------------------------------------------------------------------------------
//*tags:
//* User creates master site plan
//* And execute sa rest get api by adding the plan id in path parameters
//* Verify API is returning HTTP 200 status code & Plan definition XML template
//* And add multiple target computer names & ids in target-set tag of plan definition XML template
//* Then execute the plan by adding xml template as request body in sa-rest post api
//* Verify API is returning HTTP 200 status code & Plan Action id
//* And wait untill it got executed sucessfully
//* And verify the plan action id status
//* And delete the Plan action id after status verification

//Execute Plan API by adding multiple target computer names in target set tag of plan definition XML template
//-------------------------------------------------------------------------------------------------------------
//*tags:
//* User creates master site plan
//* And execute sa rest get api by adding the plan id in path parameters
//* Verify API is returning HTTP 200 status code & Plan definition XML template
//* And add multiple target computer names in target-set tag of plan definition XML template
//* Then execute the plan by adding xml template as request body in sa-rest post api
//* Verify API is returning HTTP 200 status code & Plan Action id
//* And wait untill it got executed sucessfully
//* And verify the plan action id status
//* And delete the Plan action id after status verification

//Execute Plan API by adding multiple target computer groups in target set tag of plan definition XML template
//-------------------------------------------------------------------------------------------------------------
//*tags:
//* User creates master site plan
//* And execute sa rest get api by adding the plan id in path parameters
//* Verify API is returning HTTP 200 status code & Plan definition XML template
//* And add multiple target computer groups in target-set tag of plan definition XML template
//* Then execute the plan by adding xml template as request body in sa-rest post api
//* Verify API is returning HTTP 200 status code & Plan Action id
//* And wait untill it got executed sucessfully
//* And verify the plan action id status
//* And delete the Plan action id after status verification

Execute Plan API by adding regular parameter, secure parameter in parameter-set tag of plan definition XML template
---------------------------------------------------------------------------------------------------------------------------
tags: secure
* User creates master site plan by using parameterised fixlet
* And execute sa rest get api by adding the plan id in path parameters
* Verify API is returning HTTP 200 status code & Plan definition XML template
* And add regular parameter, secure parameter in parameter-set tag of plan definition XML
* Then execute the plan by adding xml template as request body in sa-rest post api
* Verify API is returning HTTP 200 status code
* Verify plan action status and wait untill it got executed sucessfully
* And delete the Plan after status verification

Verify SA-REST Plan Action API works for existing plan action id
-----------------------------------------------------------------
tags: action
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
* And checks whether SA-REST Plan Action api works for existing plan
* Then in response user should get http 200 status code for existing plan
* And response body as plan defintion xml template of existing plan

Verify SA-REST Plan Action API without authentication parameters
-----------------------------------------------------------------
tags:

* When User sends SA-REST Plan Action Api Request without authentication parameters
* And gets response as http 401 status code for without authentication
* And user should validate the error message in response body of without authentication

Verify SA-REST Plan Action API with invalid authentication parameters
-----------------------------------------------------------------------
tags:
* When User sends SA-REST Plan Action Api Request with invalid authentication parameter
* Then in response user should get http 401 status code for invalid authentication parameter
* And user should validate the error message in response body of invalid authentication parameter

//Verify SA-REST Help API works for GET request
//--------------------------------------------------------------------
//*tags:
//* When User sends SA-REST Help Api with GET Request
//* Then in response user should get http 200 status code
//* And response body as help text document
//* And user should validate the help text document

//Verify SA-REST.xsd API works for GET request
//--------------------------------------------------------------------
//*tags:
//* When User sends SA-REST.xsd Api with GET Request
//* Then in response user should get http 200 status code
//* And response body as SA-REST.xsd document
//* And user should validate the SA-REST.xsd document

//Uninstallation of SA-REST API Service
//-------------------------------------------------------------------------------
//*tags:
//* Given sa rest service is installed on the target machine
//* And User uninstall it on the target machine
//* Then sa rest folder should be deleted in default location
//* And sa rest service should be deleted
