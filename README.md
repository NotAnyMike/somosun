# README #

* The main problem you can face it with the different packages, there are 11 (26/06/15)
* There is another problem if the message says "[ERROR] Unable to find 'target/classes...", just got Run As/Run Configurations/Arguments and in "program arguments" delete "target.classes.com.uibinder.index.UiBinderIndex src.com.uibinder.index.UiBinderIndex"
* There is actualy a very hard to find error, this error will end up in either two things, it will make your project to be unavailable (it will run but it will not load the index with a error 503 [jetty]) will throw this error "JettyLogger warn Avertissement: EXCEPTION java.lang.ClassNotFoundException" or the problem will make that every change you do in your source files will have no effect when you run it, this error happens because the output folder (in build paht) is not "...war/WEB-INF/classes".