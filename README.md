# OpenChrom - KNIME Connector

The tools provided in this repository will help you to create KNIME nodes from OpenChrom Plug-ins. 

## Setting up an OpenChrom - KNIME Connector Development Environment

+ Clone this project: `git clone git@github.com:OpenChrom/knimeconnector.git`

+ Check-out the `develop` branch:  `git checkout develop`

+ Start Eclipse, preferably with a fresh workspace.

+ Start the import wizard and select "Existing Projects into Workspace"

![alt text](images/import-1.png  "alt text")

Select all projects and press "Finish".

![alt text](images/import-2.png  "alt text")

A minimum setup would include just the `knimeconnector.targetplatform.knime` project.

+ Open The `KNIME-AP-OpenChrom.target` and click on *Set as active Target Platoform*.

![alt text](images/select-tp.png  "alt text")

After the target platform has been resolved (this can take some time), there should be no errors left in the workspace and you are ready to go! 

![alt text](images/loaded-tp.png  "alt text")


