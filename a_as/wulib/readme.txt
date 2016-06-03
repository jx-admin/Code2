可以eclipse环境或AndroidStudio环境。


如果AndroidStudio环境。

集成SDK步骤：

1：打开wu_template。（或者新建自己的项目）

2：导入wu_lib。

    通过import module导入到AS Android工程中。

3：wu_template(或自己的项目）对wu_lib进行Module依赖

   compile project(':wu_lib')


clean 编译 运行