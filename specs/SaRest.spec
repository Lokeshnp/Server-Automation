#User should be able to create & excute Plan with multiple fixlets/tasks/baselines/clients/sites.

verify automation plan with external site
-----------------------------------------
tags:external
* When User sends SA-REST Get Api Request for the existing external site automation plan
//* Then in response user should get http 200 status code
//* And response body as plan definition xml template
//* And user should validate the plan defination xml template

Verify SA-REST Get API works for custom site automation plan
---------------------------------------------------------------------------------------
tags:custom site
* User creates a custom site plan
* And checks whether sa rest GET api works for it
////* And gets response as http 200 status code
////* And response body as plan defintion xml template
////* And user validate whether plan definition xml template is proper or not