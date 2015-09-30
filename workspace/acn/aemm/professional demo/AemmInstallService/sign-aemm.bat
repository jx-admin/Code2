set path=%path%;"c:\Program Files (x86)\Java\jre6\bin";C:\tools\eclipse-SDK-3.6.2-win32\android-sdk_r11-windows\android-sdk-windows\platform-tools;
java -Xmx512m -jar C:\tools\eclipse-SDK-3.6.2-win32\android-sdk_r11-windows\android-sdk-windows\platform-tools\signapk.jar -w C:\workspace\platform.x509.pem C:\workspace\platform.pk8 bin\AemmInstallService.apk bin\AemmInstallService.apk
rem copy signed_AEMM_Express.apk AEMM_Express.apk
rem adb install signed_AEMM_Express.apk
pause