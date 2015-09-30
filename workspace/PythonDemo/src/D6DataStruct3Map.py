'''
Created on 2014年7月1日

@author: junxu.wang
'''
'''
3. 字典

　　　　字典由键值对组成，键必须是唯一的；eg: d = {key1:value1, key2:value2}；空字典用{}表示；字典中的键值对是没有顺序的，如果想要

　　一个特定的顺序，那么使用前需要对它们排序；d[key] = value，如果字典中已有key，则为其赋值为value，否则添加新的键值对key/value；使

　　用del d[key] 可以删除键值对；判断字典中是否有某键，可以使用in 或 not in；
'''
d = {}
d["1"] = "one"
d["2"] = "two"
d["3"] = "three"

del d["3"]

for key, value in d.items():
    print ("%s --> %s" % (key, value))
#1 --> one
#2 --> two

# clear()　　---删除字典中所有元素

d1 = {"1":"one", "2":"two"}
d1.clear()

print (d1) # {}


# copy()　　---返回字典的一个副本(浅复制)

d1 = {"1":"one", "2":"two"}
d2 = d1.copy()

print (d2) #{'1': 'one', '2': 'two'}


# dict.fromkeys(seq,val=None) ---创建并返回一个新字典，以序列seq中元素做字典的键，val为字典所有键对应的初始值(默认为None)
l = [1, 2, 3]
t = (1, 2, 3)

d3 = {}.fromkeys(l)
print (d3) #{1: None, 2: None, 3: None}

d4 = {}.fromkeys(t, "default")
print (d4) #{1: 'default', 2: 'default', 3: 'default'}


# get(key,[default])　　---返回字典dict中键key对应值，如果字典中不存在此键，则返回default 的值(default默认值为None)
d5 = {1:"one", 2:"two", 3:"three"}

print (d5.get(1)) #one
print (d5.get(5)) #None
print (d5.get(5, "test")) #test

'''
# has_key(key)　　---判断字典中是否有键key
d6 = {1:"one", 2:"two", 3:"three"}

print (d6.has_key(1))  #True
print (d6.has_key(5))  #False'''

# items()　　---返回一个包含字典中(键, 值)对元组的列表
d7 = {1:"one", 2:"two", 3:"three"}

for item in d7.items():
    print (item)
#(1, 'one')
#(2, 'two')
#(3, 'three')

for key, value in d7.items():
    print ("%s -- %s" % (key, value))
#1 -- one
#2 -- two
#3 -- three

# keys()　　---返回一个包含字典中所有键的列表
d8 = {1:"one", 2:"two", 3:"three"}

for key in d8.keys():
    print (key)
#1
#2
#3

# values()　　---返回一个包含字典中所有值的列表
d8 = {1:"one", 2:"two", 3:"three"}

for value in d8.values():
    print (value)
#one
#two
#three

# pop(key, [default])　　---若字典中key键存在，删除并返回dict[key]，若不存在，且未给出default值，引发KeyError异常
d9 = {1:"one", 2:"two", 3:"three"}
print (d9.pop(1)) #one
print (d9) #{2: 'two', 3: 'three'}
print (d9.pop(5, None)) #None
try:
    d9.pop(5)  # raise KeyError
except KeyError as ke:
    print  ("KeyError:", ke) #KeyError:5
    
#     popitem()　　---删除任意键值对，并返回该键值对，如果字典为空，则产生异常KeyError
d10 = {1:"one", 2:"two", 3:"three"}

print (d10.popitem())  #(1, 'one')
print (d10)  #{2: 'two', 3: 'three'}

# setdefault(key,[default])　　---若字典中有key，则返回vlaue值，若没有key，则加上该key，值为default，默认None
d = {1:"one", 2:"two", 3:"three"}

print (d.setdefault(1))  #one
print (d.setdefault(5))  #None
print (d)  #{1: 'one', 2: 'two', 3: 'three', 5: None}
print (d.setdefault(6, "six") )#six
print (d)  #{1: 'one', 2: 'two', 3: 'three', 5: None, 6: 'six'}

# update(dict2)　　---把dict2的元素加入到dict中去，键字重复时会覆盖dict中的键值
d = {1:"one", 2:"two", 3:"three"}
d2 = {1:"first", 4:"forth"}

d.update(d2)
print (d)  #{1: 'first', 2: 'two', 3: 'three', 4: 'forth'}

'''
viewitems()　　---返回一个view对象，（key, value）pair的列表，类似于视图。优点是，如果字典发生变化，view会同步发生变化。在

　　迭代过程中，字典不允许改变，否则会报异常
'''
'''
d = {1:"one", 2:"two", 3:"three"}
for key, value in d.viewitems():
    print ("%s - %s" % (key, value))
#1 - one
#2 - two
#3 - three
'''
'''
# viewkeys()　　---返回一个view对象，key的列表
d = {1:"one", 2:"two", 3:"three"}
for key in d.viewkeys():
    print (key)
   
#1
#2
#3 

# viewvalues()　　---返回一个view对象，value的列表
d = {1:"one", 2:"two", 3:"three"}
for value in d.viewvalues():
    print (value)
#one
#two
#three'''
