# User should be able to create & excute Plan with multiple fixlets/tasks/baselines/clients. In order to execute, Plan engine should be up & running




verify default settings functionality for the newly added step before executing the plan 
-----------------------------------------------------------------------------------------
tags:skip
* Login to Bigfix console
* Navigate to Automation Plan Dashboard
* And click on create button
* Then give plan name in details tab
* Go to steps tab 
* And click on add step button
* Then add a fixlet to the plan
* Click on default settings icon
* verify user is able to add computers in selected targets tab
* Go to execution tab
* verify target details is displaying in execution tab
* Click on save button
* Select the created plan & click on take action button
* Verify added target computers is displaying under selected Targets tab

Create & execute automation plan with multiple fixlets
-------------------------------------------------------------------------
tags:multipleFixlets
* Login to Bigfix console 
* Navigate to Automation Plan Dashboard
* Create automation plan with multiple fixlets on following OS
|OS |
|---|
|123|
|456|
|789|
//* Verify automation plan got created with multiple fixlets in Plan Dashboard
//* Then execute automation plan with multiple fixlets on following OS
//|OS Name |
//|--------|
//|windows |
//|Linux   |
//* And wait untill it got executed sucessfully
//* And verify the automation plan status
//* And delete the Plan after status verification

Create & execute automation plan with multiple Tasks
-------------------------------------------------------------------------
tags:skip
* Login to Bigfix console 
* Navigate to Automation Plan Dashboard
* Create automation plan with multiple tasks on following OS
|OS Name | 
|--------|
|windows | 
|Linux   |
* Verify automation plan got created with multiple tasks in Plan Dashboard
* Then execute automation plan with multiple tasks on following OS
|OS Name | 
|--------|
|windows | 
|Linux   |
* And wait untill it got executed sucessfully
* And verify the automation plan status
* And delete the Plan after status verification

Create & execute automation plan with multiple fixlets And tasks
-------------------------------------------------------------------------
tags:skip
* Login to Bigfix console 
* Navigate to Automation Plan Dashboard
* Create automation plan with multiple fixlets and tasks on following OS
|OS Name | 
|--------|
|windows | 
|Linux   |
* Verify automation plan got created with multiple fixlets and tasks in Plan Dashboard
* Then execute automation plan with multiple fixlets and tasks on following OS
|OS Name | 
|--------|
|windows | 
|Linux   |
* And wait untill it got executed sucessfully
* And verify the automation plan status
* And delete the Plan after status verification 

Create & execute automation plan with baseline having multiple fixlets [Re-check]
------------------------------------------------------------------------------------------------------
tags:skip
* Login to Bigfix console 
* Navigate to Automation Plan Dashboard
* Create automation plan with baseline having multiple fixlets on following OS [Re-check]
|OS Name | 
|--------|
|windows | 
|Linux   |
* Verify automation plan got created with baseline having muliplte fixlets in Plan Dashboard[Re-check]
* Then execute automation plan with baseline having muliplte fixlets on following OS
|OS Name | 
|--------|
|windows | 
|Linux   |
* And wait untill it got executed sucessfully
* And verify the automation plan status

Create & execute automation plan with baseline having multiple tasks
--------------------------------------------------------------------------------------------------
tags:skip
* Login to Bigfix console 
* Navigate to Automation Plan Dashboard
* Create automation plan with baseline having multiple tasks on following OS
|OS Name | 
|--------|
|windows | 
|Linux   |
* Verify automation plan got created with baseline having multiple tasks in Plan Dashboard
* Then execute automation plan with baseline having multiple tasks on following OS
|OS Name | 
|--------|
|windows | 
|Linux   |
* And wait untill it got executed sucessfully
* And verify the automation plan status
* And delete the Plan after status verification 

Create & execute automation plan with baseline having multiple fixlets and tasks
----------------------------------------------------------------------------------------------------------
tags:skip
* Login to Bigfix console 
* Create automation plan with baseline having multiple fixlets and  tasks on following OS
|OS Name | 
|--------|
|windows | 
|Linux   |
* Verify automation plan got created with baseline having fixlets and tasks in Plan Dashboard
* Then execute automation plan with baseline having fixlets and tasks on following OS
|OS Name | 
|--------|
|windows | 
|Linux   |
* And wait untill it got executed sucessfully
* And verify the automation plan status
* And delete the Plan after status verification
 
Create & execute automation plan with a combination of  baselines,fixlets and tasks
----------------------------------------------------------------------------
tags:skip
* Login to Bigfix console 
* Create automation plan with a combination of baselines, fixlets and tasks on following OS
|OS Name | 
|--------|
|windows | 
|Linux   |
* Verify automation plan got created with a combination of baselines, fixlets, tasks in Plan Dashboard
* Then execute automation plan with with a combination of baselines, fixlets, tasks on following OS
|OS Name | 
|--------|
|windows | 
|Linux   |
* And wait untill it got executed sucessfully
* And verify the automation plan status
* And delete the Plan after status verification

Create & execute automation plan with baseline having multiple groups
--------------------------------------------------------------------------------------------------
tags:skip
* Login to Bigfix console 
* Create automation plan with baseline having multiple groups on following OS 
|OS Name | 
|--------|
|windows | 
|Linux   |
* Verify automation plan got created with baseline having multiple groups in Plan Dashboard
* Then execute automation plan with baseline having multiple groups on following OS
|OS Name | 
|--------|
|windows | 
|Linux   |
* And wait untill it got executed sucessfully
* And verify the automation plan status
* And delete the Plan after status verification

Create & execute automation plan with baseline having fixlets of custom site
--------------------------------------------------------------------------------------------------
tags:skip
* Login to Bigfix console 
* Create automation plan with baseline having fixlets of custom site on following OS
|OS Name | 
|--------|
|windows | 
|Linux   |
* Verify automation plan got created with with baseline having fixlets of custom site in Plan Dashboard
* Then execute automation plan with baseline having fixlets of custom site on following OS
|OS Name | 
|--------|
|windows | 
|Linux   |
* And wait untill it got executed sucessfully
* And verify the automation plan status
* And delete the Plan after status verification

Create & execute parallel automation plan
--------------------------------------------------------
tags:skip
* Login to Bigfix console 
* Create parallel automation plan on following OS
|OS Name | 
|--------|
|windows | 
|Linux   |
* Verify it got created in Plan Dashboard
* Then execute parallel automation plan on following OS
|OS Name | 
|--------|
|windows | 
|Linux   |
* And wait untill it got executed sucessfully
* And verify the automation plan status
* And delete the Plan after status verification

Create & execute automation plan with multiple clients
--------------------------------------------------------
tags:skip
* Login to Bigfix console 
* Create automation plan by adding multiple clients using default settings on following OS 
|OS Name | 
|--------|
|windows | 
|Linux   |
* Verify plan got created with multiple clients in Plan Dashboard
* Then execute automation plan on following OS & below clients
|OS Name | Client Name          |
|--------|----------------------|
|windows | Client 1, Client 2   |
|Linux   | Client 3, Client 4   |
* And wait untill it got executed sucessfully
* And verify the automation plan status
* And delete the Plan after status verification

Uninstallation of plan engine
--------------------------------------------------------------
tags:uninstall
* User should be able to execute Uninstall Automation Plan Engine Fixlet on the server Machine
//* Plan engine should be uninstatlled on the target machine
//* And plan engine folder must be deleted