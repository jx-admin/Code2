'''
Created on 2014年7月1日

@author: junxu.wang
'''
'''
六、数据结构

　　python有三种内建的数据结构：列表、元组和字典。

　　1. 列表

　　　　list是处理一组有序项目的数据结构，列表是可变的数据结构。列表的项目包含在方括号[]中，eg: [1, 2, 3]， 空列表[]。判断列表中是否包含某项可以使用in， 比如 l = [1, 2, 3]; print 1 in l; #True；支持索引和切片操作；索引时若超出范围，则IndexError；使用函数len()查看长度；使用del可以删除列表中的项，eg: del l[0] # 如果超出范围，则IndexError

　　　　list函数如下：

　　　　append（value）　　---向列表尾添加项value
'''
l = [1, 2, 2]
print (l)
l.append(3) #[1, 2, 2, 3]
print(l)

#count(value)　　---返回列表中值为value的项的个数
# l = [1, 2, 2]
print (l.count(2)) # 2

# extend(list2)　　---向列表尾添加列表list2
# l = [1, 2, 2]
l1 = [10, 20]
l.extend(l1)
print (l)   #[1, 2, 2, 10, 20]

# index(value, [start, [stop]])　　---返回列表中第一个出现的值为value的索引，如果没有，则异常 ValueError
l = [1, 2, 2]
a = 4
try:
    print (l.index(a))
except ValueError as ve:
    print ("there is no %d in list" % a)
    
# insert(i, value)　　---向列表i位置插入项vlaue，如果没有i，则添加到列表尾部
l = [1, 2, 2]

l.insert(1, 100)
print (l) #[1, 100, 2, 2]

l.insert(100, 1000)
print (l) #[1, 100, 2, 2, 1000]

# pop([i])　　---返回i位置项，并从列表中删除；如果不提供参数，则删除最后一个项；如果提供，但是i超出索引范围，则异常IndexError
l = [0, 1, 2, 3, 4, 5]

print (l.pop()) # 5
print (l) #[0, 1, 2, 3, 4]

print (l.pop(1)) #1
print (l) #[0, 2, 3, 4]

try:
    l.pop(100)
except IndexError as ie:
    print ("index out of range")
    
#     remove(value)　　---删除列表中第一次出现的value，如果列表中没有vlaue，则异常ValueError
l = [1, 2, 3, 1, 2, 3]

l.remove(2)
print (l) #[1, 3, 1, 2, 3]

try:
    l.remove(10)
except ValueError as ve:
    print ("there is no 10 in list")
    
#     reverse()　　---列表反转
l = [1, 2, 3]
l.reverse()
print (l) #[3, 2, 1]

'''
sort(cmp=None, key=None, reverse=False)　　---列表排序

【Python Library Reference】
cmp:cmp specifies a custom comparison function of two arguments (iterable elements) which should return a negative, zero or positive number depending on whether the first argument is considered smaller than, equal to, or larger than the second argument: 
"cmp=lambda x,y: cmp(x.lower(), y.lower())" 
key:key specifies a function of one argument that is used to extract a comparison key from each list element: "key=str.lower"
reverse:reverse is a boolean value. If set to True, then the list elements are sorted as if each comparison were reversed.In general, the key and reverse conversion processes are much faster than specifying an 
equivalent cmp function. This is because cmp is called multiple times for each list element while key and reverse touch each element only once.
'''
l5 = [10, 5, 20, 1, 30]
l5.sort()
print (l5) #[1, 5, 10, 20, 30]

l6 = ["bcd", "abc", "cde", "bbb"]
# l6.sort(cmp = lambda s1, s2: cmp(s1[0],s2[1]))
print (l6) #['abc', 'bbb', 'bcd', 'cde']

l7 = ["bcd", "abc", "cde", "bbb", "faf"]
l7.sort(key = lambda s: s[1])
print (l7) #['faf', 'abc', 'bbb', 'bcd', 'cde']
l7.sort(key = lambda s: s[0])
print (l7) #['abc', 'bbb', 'bcd', 'cde', 'faf']