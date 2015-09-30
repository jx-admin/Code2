在文件 PackageManagerService.java 中，第 6066 行到第6078行加入了屏蔽代码。
PackageManagerService.java 在Android源代码中的路径是：
Android\frameworks\base\services\java\com\android\server
它调用的删除方法：deletePackage，该方法是在PackageManagerService中定义的。
adb shell 中的Install与Uninstall命令调用的是:
Z:\Android\frameworks\base\cmds\pm\src\com\android\commands\pm\Pm.java
中的 runInstall与runUninstall 命令
以runUninstall 为例：
调用流程如下：
Pm.java(runUninstall)->IPackageManager.java(deletePackage)->PackageManagerService.java(deletePackage)->deletePackageX->deletePackageLI
这个操作到这里，还没有结束，后面还有进一步的判断，判断是否System的App，是否只是删除数据等。
该调研需要系统权限：
 dpm.packageHasActiveAdmins(packageName)
runInstall 过程与 runUninstall过程调用大致相同，
但是Install在packageManagerService.java中用到的是消息的机制。
只需要在进入PackageManagerService.java的installPackage处做屏蔽，或者解开屏蔽就可以了。
两个最重要的文件：
Android\frameworks\base\services\java\com\android\server\ PackageManagerService.java
\Android\out\target\common\obj\JAVA_LIBRARIES\android_stubs_current_intermediates\src\android\content\pm\PackageManager.java 

