# User should be able to create & excute Plan with multiple fixlets/tasks/baselines/clients. In order to execute, Plan engine should be up & running

Create & execute automation plan with multiple fixlets
---------------------------------------------------------
tags:multipleFixlets
* Create automation plan with multiple fixlets on following OS
|OS Name |
|--------|
|windows |
|Linux   |
* Then execute automation plan with multiple fixlets on following OS
|OS Name |
|--------|
|windows |
|Linux   |
* Verify action status and wait untill it got executed sucessfully
* And delete the Plan after status verification

Create & execute automation plan with baseline having multiple fixlets
-----------------------------------------------------------------------
tags:multipleBaselines
* Create automation plan with baseline having multiple fixlets on following OS
|OS Name |
|--------|
|windows |
|Linux   |
* Then execute automation plan with baseline having multiple fixlets on following OS
|OS Name |
|--------|
|windows |
|Linux   |
* Verify action status and wait untill it got executed sucessfully
* And delete the Plan after status verification

Create & execute automation plan with multiple Tasks
------------------------------------------------------
tags:multipleTasks
* Create automation plan with multiple tasks on following OS
|OS Name |
|--------|
|windows |
|Linux   |
* Then execute automation plan with multiple tasks on following OS
|OS Name |
|--------|
|windows |
|Linux   |
* Verify action status and wait untill it got executed sucessfully
* And delete the Plan after status verification

Create & execute automation plan with baseline having multiple tasks
--------------------------------------------------------------------------
tags:BaselineHavingMultipleTasks
* Create automation plan with baseline having multiple tasks on following OS
|OS Name |
|--------|
|windows |
|Linux   |
* Then execute automation plan with baseline having multiple tasks on following OS
|OS Name |
|--------|
|windows |
|Linux   |
* Verify action status and wait untill it got executed sucessfully
* And delete the Plan after status verification

Create & execute automation plan with multiple fixlets And tasks
-----------------------------------------------------------------------
tags:multipleFixletsAndTasks
* Create automation plan with multiple fixlets and tasks on following OS
|OS Name |
|--------|
|windows |
|Linux   |
* Then execute automation plan with multiple fixlets and tasks on following OS
|OS Name |
|--------|
|windows |
|Linux   |
* Verify action status and wait untill it got executed sucessfully
* And delete the Plan after status verification

Create & execute automation plan with baseline having multiple fixlets and tasks
--------------------------------------------------------------------------------------
tags:baselineHavingFixletsAndTasks
* Create automation plan with baseline having multiple fixlets and tasks on following OS
|OS Name |
|--------|
|windows |
|Linux   |
* Then execute automation plan with baseline having fixlets and tasks on following OS
|OS Name |
|--------|
|windows |
|Linux   |
* Verify action status and wait untill it got executed sucessfully
* And delete the Plan after status verification

Create & execute automation plan with a combination of baseline, fixlets and tasks
---------------------------------------------------------------------------------
tags:combinationOfBaselineFixletsTasks
* Create automation plan with a combination of baseline, fixlets and tasks on following OS
|OS Name |
|--------|
|windows |
|Linux   |
* Then execute automation plan with a combination of baselines, fixlets and tasks on following OS
|OS Name |
|--------|
|windows |
|Linux   |
* Verify action status and wait untill it got executed sucessfully
* And delete the Plan after status verification

Create & execute parallel automation plan
--------------------------------------------------------
tags: parallel
* Create parallel automation plan on following OS
|OS Name |
|--------|
|windows |
|Linux   |
* Then execute parallel automation plan on following OS
|OS Name |
|--------|
|windows |
|Linux   |
* Verify action status and wait untill it got executed sucessfully
* And delete the Plan after status verification

verify default settings functionality for the newly added step before executing the plan
-----------------------------------------------------------------------------------------
tags: default
* User click on create button
* Then give plan name in details tab
* And click on add step button
* Then add a fixlet to the plan
* Click on default settings icon
* verify user is able to add computers in selected targets tab
* Click on save button
* Select the created plan & click on take action button
* Verify added target computers is displaying under selected Targets tab

Uninstallation of plan engine
-------------------------------------------------------------
tags:uninstall
* User should be able to execute Uninstall Automation Plan Engine Fixlet on the server Machine