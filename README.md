# README #

Something important to do is to change the default charset coding of eclipse to utf-8 to avoid removing all the accents when any file is moved

## General Information about the project
The branch dev is a development branch (it means this version is used as the branch master of the new version) so all features should be created in a different branch a later it should be merged into dev, when the new version is ready, dev should be merge with master but dev must not be deleted

## Some errors that may occur
* The main problem you can face it with the different packages, there are 11 (26/06/15)
* There is another problem if the message says "[ERROR] Unable to find 'target/classes...", just got Run As/Run Configurations/Arguments and in "program arguments" delete "target.classes.com.uibinder.index.UiBinderIndex src.com.uibinder.index.UiBinderIndex"
* There is actualy a very hard to find error, this error will end up in either two things, it will make your project to be unavailable (it will run but it will not load the index with a error 503 [jetty]) will throw this error "JettyLogger warn Avertissement: EXCEPTION java.lang.ClassNotFoundException" or the problem will make that every change you do in your source files will have no effect when you run it, this error happens because the output folder (in build paht) is not "...war/WEB-INF/classes".
* When working on the UI it is better to modify the argument -Xmx512M to arround -Xmx700m in run -> Run-configuration to avoid Out of memory; to increase the amount of memory, use the -Xmx flag at startup (java -Xmx128M ...)
* To avoid (just a work around) the error of that no persistance.jpa file not foud go to Preferences -> JPA -> error/warning  -> des-activate the option with the persistance.jpa 
* Another error (not so common) is that the pom.xml of a module (generally a backend module) will show an error about the lifecycle of some plugin, simple add the part of pluginManagement
