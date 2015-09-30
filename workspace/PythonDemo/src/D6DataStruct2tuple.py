'''
Created on 2014年7月1日

@author: junxu.wang
'''


'''
2. 元组

　　　　tuple和list十分相似，但是tuple是不可变的，即不能修改tuple，元组通过圆括号中用逗号分割的项定义；支持索引和切片操作；可以使用 in

　　查看一个元素是否在tuple中。空元组()；只含有一个元素的元组("a",) #需要加个逗号

　　　　优点：tuple比list速度快；对不需要修改的数据进行‘写保护’，可以是代码更安全

　　　　tuple与list可以相互转换，使用内置的函数list()和tuple()。
'''
l = [1, 2, 3]
print (l) # [1, 2, 3]

t = tuple(l)
print (t) # (1, 2, 3)

l1 = list(t)
print (l1) #[1, 2, 3]
# 元组最通常的用法是用在打印语句，如下例：
name = "Peter Zhang"
age = 25
print ("Name: %s; Age: %d" % (name, age))
# Name: Peter Zhang; Age: 25


#count(value)　　---返回元组中值为value的元素的个数
t = (1, 2, 3, 1, 2, 3)

print (t.count(2)) # 2

# index(value, [start, [stop]])　　---返回列表中第一个出现的值为value的索引，如果没有，则异常 ValueError
t = (1, 2, 3, 1, 2, 3)

print (t.index(3)) # 2
try:
    print (t.index(4))
except ValueError as ve:
    print ("there is no 4 in tuple")  # there is no 4 in tuple