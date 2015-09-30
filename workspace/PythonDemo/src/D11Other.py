'''
Created on 2014年7月2日

@author: junxu.wang
'''

'''
1. 一些特殊的方法


名称

说明

__init__(self,...) 这个方法在新建对象恰好要被返回使用之前被调用。 
__del__(self) 恰好在对象要被删除之前调用。 
__str__(self) 在我们对对象使用print语句或是使用str()的时候调用。 
__lt__(self,other) 当使用 小于 运算符（<）的时候调用。类似地，对于所有的运算符（+，>等等）都有特殊的方法。 
__getitem__(self,key) 使用x[key]索引操作符的时候调用。 
__len__(self) 对序列对象使用内建的len()函数的时候调用。 
'''
class Array:
    __list = []

    def __init__(self):
        print ("constructor")

    def __del__(self):
        print ("destructor")

    def __str__(self):
        return ("this self-defined array class")

    def __getitem__(self, key):
        return self.__list[key]

    def __len__(self):
        return len(self.__list)

    def Add(self, value):
        self.__list.append(value)

    def Remove(self, index):
        del self.__list[index]

    def DisplayItems(self):
        print ("show all items----")
        for item in self.__list:
            print (item)

arr = Array()   #constructor
print (arr)    #this self-defined array class
print (len(arr))   #0
arr.Add(1)
arr.Add(2)
arr.Add(3)
print (len(arr))   #3
print (arr[0])   #1
arr.DisplayItems()
#show all items----
#1
#2
#3
arr.Remove(1)
arr.DisplayItems()
#show all items----
#1
#3
del arr
#destructor

'''2. 综合列表

　　　　通过列表综合，可以从一个已有的列表导出一个新的列表。
'''
list1 = [1, 2, 3, 4, 5]
list2 = [i*2 for i in list1 if i > 3]

print (list1)  #[1, 2, 3, 4, 5]
print (list2)  #[8, 10]

'''list1 = [1, 2, 3, 4, 5]
list2 = [i*2 for i in list1 if i > 3]

print list1  #[1, 2, 3, 4, 5]
print list2  #[8, 10]
'''
def powersum(power, *args):
    total = 0
    for i in args:
        total += pow(i, power)
    return total

print (powersum(2, 1, 2, 3))   #14 

def displaydic(**args):
    for key,value in args.items():
        print ("key:%s;value:%s" % (key, value))


displaydic(a="one", b="two", c="three")
#key:a;value:one
#key:c;value:three
#key:b;value:two

'''
4. lambda

　　　　lambda语句被用来创建新的函数对象，并在运行时返回它们。lambda需要一个参数，后面仅跟单个表达式作为函数体，而表达式的值被这个

　　新建的函数返回。 注意，即便是print语句也不能用在lambda形式中，只能使用表达式。

'''
func = lambda s: s * 3
print (func("peter "))  #peter peter peter

func2 = lambda a, b: a * b
print (func2(2, 3))  #6

'''
6. assert

　　　　assert语句用来断言某个条件是真的，并且在它非真的时候引发一个错误--AssertionError。
'''
flag = True

assert flag == True

try:
    assert flag == False
except AssertionError as err:
    print ("failed")
else:
    print ("pass")
    
'''7. repr函数

　　　　repr函数用来取得对象的规范字符串表示。反引号（也称转换符）可以完成相同的功能。

　　　　注意，在大多数时候有eval(repr(object)) == object。

　　　　可以通过定义类的__repr__方法来控制对象在被repr函数调用的时候返回的内容。
'''
arr = [1, 2, 3]
# print (`arr`)    #[1, 2, 3]
print (repr(arr))    #[1, 2, 3]