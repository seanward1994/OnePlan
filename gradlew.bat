@ECHO OFF
SET DIR=%~dp0
SET APP_HOME=%DIR%
SET CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar
SET JAVA_EXE=java
"%JAVA_EXE%" -Xms64m -Xmx512m -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
