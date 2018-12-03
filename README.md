## 这是一个不太规范的文档介绍

### ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
### 各个包中的内容如下
#### data 包中包含的是转换后的数据
#### history 包下包含的是不同特征模板下的程序运行结果
featuresx.txt 表示提取出来特征文件， weightsx.txt表示训练出来的权重，testx.txt表示测试文件的特征， resultx
为最终的结果
##### 各个包中使用的特征模板如下：
try1: [wordID, beforeWordID*3]

try2: [wordID, word's characteristics, beforeWordID* 3]

try3: [wordID, word's characteristics, (beforeWordID, beforeWordLabel)* 2 ]

try4: [wordID, word's characteristics, (beforeWordID, beforeWordLabel)* 3 ]

try5: [wordID， word’s characteristics， beforeWordID* 2, afterWordID*1 ]


其中 wordID表示当前词的编号，beforeWordID是当前词之前的词语编号，afterWordID是当前词之后的
词语编号，word's characteristics是当前词具有的一些特征

#### main包下的main函数是主要的运行程序，需要提前抽取出特征
#### model包下是程序中用到的一些类的定义，MaxEnt类是主要的类
#### utils包下的两个类是对程序中用的数据进行处理
#### features6 是第六组特征，特征模板为[wordID， word’s characteristics， beforeWord* 2， afterword* 2]
###！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
### 如何运行其中的程序
######Main类中的main函数可以直接运行
######如果需要提取新的特征进行训练，需根据自己要使用的特征模板修改utils包下的类中的部分代码（TODO部分）
######修改完这部分代码之后，需要运行两个类中的main函数
######之后将Main类中的main方法中的文件路径修改即可运行