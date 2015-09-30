'''
Created on 2014

@author: junxu.wang
'''
'''
3. python 控制台输出使用print
'''


print ("abc")    #打印abc并换行
print ("abc%s" % "d")    #打印abcd
print ("abc%sef%s" % ("d", "g"))    #打印abcdefg


'''
三、控制流

　　1. if 语句
'''
i = 10
n =1# int(input("enter a number:"))

if n == i:
    print ("equal")
elif n < i:
    print ("lower")
else:
    print ("higher")
    
'''
2. while语句
'''
i=0
while i<4:
    print ('whiled%d' %i)
    i=i+1
else:
    print ('while else %d' % (i))
    
'''
3. for 循环 for..in
'''
for i in range(0, 5):
    print (i)
else:
    print ("pass%d" %i)
    pass
# 打印0到4

'''
注：当for循环结束后执行else语句；

　　　　range(a, b)返回一个序列，从a开始到b为止，但不包括b，range默认步长为1，可以指定步长，range(0,10,2)；

　　4. break语句

　　　　终止循环语句，如果从for或while中终止，任何对应循环的else将不执行。

　　5. continue语句

　　　　continue语句用来调过当前循环的剩余语句，然后继续下一轮循环。
'''

'''
四、函数

　　函数通过def定义。def关键字后跟函数的标识符名称，然后跟一对圆括号，括号之内可以包含一些变量名，该行以冒号结尾；接下来是一块语句，即函数体。
'''

def sumOf(a, b):
    return a + b

'''
　　1. 函数形参

　　　　函数中的参数名称为‘形参’，调用函数时传递的值为‘实参’

　　2. 局部变量

　　　　在函数内定义的变量与函数外具有相同名称的其他变量没有任何关系，即变量名称对于函数来说是局部的。这称为变量的作用域。

　　　　global语句， 为定义在函数外的变量赋值时使用global语句。

'''
def func():
    global x
    print ("x is ", x)
    x = 1

x = 3
func()
print (x)

#3
#1 

'''
3. 默认参数

　　　　通过使用默认参数可以使函数的一些参数是‘可选的’。
'''
def say(msg, times =  1):
    print (msg * times)

say("peter")
say("peter", 3)
'''
　　　　注意：只有在形参表末尾的那些参数可以有默认参数值，即不能在声明函数形参的时候，先声明有默认值的形参而后声明没有默认值的形参，只是因为赋给形参的值是根据位置而赋值的。
'''

'''
4. 关键参数

　　　　如果某个函数有很多参数，而现在只想指定其中的部分，那么可以通过命名为这些参数赋值（称为‘关键参数’）。

　　　　优点：不必担心参数的顺序，使函数变的更加简单；假设其他参数都有默认值，可以只给我们想要的那些参数赋值。
'''
def func3(a, b=2, c=3):
    print ("a is %s, b is %s, c is %s" % (a, b, c))

func3(1) #a is 1, b is 2, c is 3
func3(1, 5) #a is 1, b is 5, c is 3
func3(1, c = 10) #a is 1, b is 2, c is 10
func3(c = 20, a = 30) #a is 30, b is 2, c is 20

'''

　　5. return 语句

　　　　return语句用来从一个函数返回，即跳出函数。可从函数返回一个值。

　　　　没有返回值的return语句等价于return None。None表示没有任何东西的特殊类型。
'''
'''
6. DocStrings (文档字符串)
'''
def func2():
    '''This is self-defined function

Do nothing'''
    pass

print (func2.__doc__)

#This is self-defined function
#
#Do nothing


'''
    五、模块

　　模块就是一个包含了所有你定义的函数和变量的文件，模块必须以.py为扩展名。模块可以从其他程序中‘输入’(import)以便利用它的功能。

　　在python程序中导入其他模块使用'import', 所导入的模块必须在sys.path所列的目录中，因为sys.path第一个字符串是空串''即当前目录，所以程序中可导入当前目录的模块。

 　　1. 字节编译的.pyc文件

　　　　导入模块比较费时，python做了优化，以便导入模块更快些。一种方法是创建字节编译的文件，这些文件以.pyc为扩展名。

　　　　pyc是一种二进制文件，是py文件经编译后产生的一种byte code，而且是跨平台的（平台无关）字节码，是有python虚拟机执行的，类似于

　　java或.net虚拟机的概念。pyc的内容，是跟python的版本相关的，不同版本编译后的pyc文件是不同的。

　　2. from .. import

　　　　如果想直接使用其他模块的变量或其他，而不加'模块名+.'前缀，可以使用from .. import。

　　　　例如想直接使用sys的argv，from sys import argv 或 from sys import *

　　3. 模块的__name__

　　　　每个模块都有一个名称，py文件对应模块名默认为py文件名，也可在py文件中为__name__赋值；如果是__name__，说明这个模块被用户

　　单独运行。

　　4. dir()函数

　　　　dir(sys)返回sys模块的名称列表；如果不提供参数，即dir()，则返回当前模块中定义名称列表。

　　　　del -> 删除一个变量/名称，del之后，该变量就不能再使用。
'''