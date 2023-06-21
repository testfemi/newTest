@ECHO off
:: Encryption tool for helping with encrypting passwords/plain-text values
ECHO Loading Encryption Tool...
ECHO ----------------------------------------------
ECHO ********** Encryption Tool: Welcome **********
ECHO ----------------------------------------------
ECHO;
SET /p plainText=Please enter the value to encrypt: 
ECHO Value to encrypt is: %plainText% & :: This is the plain-text value (provided by the user) to encrypt
ECHO Encrypting Value...
IF EXIST password_encryption.jar (
	GOTO :success
) ELSE (
	GOTO :failure
)

:success
:: Call the JAR file (includes project compiled with its dependencies and with entry point configured as utility.AESUtil) and save result of call to cipherText variable
FOR /f "delims=" %%a IN ('CALL java -jar password_encryption.jar %plainText%') DO SET cipherText=%%a
ECHO Encryption Completed Successfully...
ECHO;
ECHO ----------------------------------------------
ECHO Encrypted Value: %cipherText% & :: Print the cipher-text produced from calling the JAR above
ECHO ----------------------------------------------
GOTO :end

:failure
ECHO;
ECHO;
ECHO Cannot find the 'password_encryption.jar' file.  Please check that this file exists within the current directory.
GOTO :end

:end
:: DO NOT automatically close the window after the above sub-routines are completed, allow time for the user to view the output
ECHO;
ECHO;
ECHO Shutting Down Encryption Tool...
ECHO ------------------------
ECHO Encryption Tool Shutdown
ECHO ------------------------
<nul set /p "=Press any key to close this window..."
PAUSE > nul