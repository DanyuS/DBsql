# DBsql
项目需求
1.对某个用户进行套餐的查询（包括历史记录）、订购、退订（考虑立即生效和次月生效）操作
2.某个用户在通话情况下的资费生成
3.某个用户在使用流量情况下的资费生成
4.某个用户月账单的生成

数据库说明
 
数据库名phonephone

设计说明
共设计了四个实体类：
userinfo：用户
packageservice：套餐
usageinfo：套餐订阅及退订使用情况
consumptiondetail：实时记录消费类型及长度

系统构建
Java+mysql
运行环境：windows
运行Main.java后根据提示输入即可得到想要的操作

相应操作
用户注册（操作如图）

 

 

登录

 

查询历史订购套餐

 

订购套餐

 

退订
（PS：退订的时候才会总计缴费，否则查询月账单只会显示基础缴费）

 

通话情况资费生成
（具体实现：首先寻找可以免费使用通话的套餐，免费额度使用完后再去使用付费，如果没有套餐就直接减去用户账户余额）
（PS：这里通话情况控制台不会返回资费详细信息，详细信息应该去数据库Usageinfo和从consumptiondetail中去看）
（又PS：短信和流量类似操作）

 

 

 

使用流量情况下的资费生成

 

 

优化思路：
退订可以添加索引
月账单可以添加非套餐情况
