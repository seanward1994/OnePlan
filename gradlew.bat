@ECHO OFF
SET DIR=%~dp0
IF EXIST "%DIR%gradle\wrapper\gradle-wrapper.jar" (
  "%JAVA_HOME%\bin\java" -Xmx64m -cp "%DIR%gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
) ELSE (
  gradle %*
)
