# dbtool
###db cmd tool  in java
##Build 
mvn install

##Usage: 
  java -jar dbtool.jar -u "jdbc:mysql://mysqlserver/mydatabase" -un myusername -pa mypassword -s "show databases;"

JDK 1.8 Required

Features
2015年12月4日13:35:29  can set sqlstr from file in dir "./sql" by -sid 
##Usage
 java -jar dbtool.jar -u "jdbc:mysql://mysqlserver/mydatabase" -un myusername -pa mypassword -sid sbs
 
 
 *.xml in './sql' dir
 File Formate:
```
 <xml>
 <sql>
 <sqlName>sid</sqlName>
 <sqlValue>ture sqlstr</sqlValue>
 </sql>
 <sql>
 </sql>
 ....
 </xml>
  code snippet
```
 
 Questions : 
 
 what's wrong with both -s and -sid?
 
 if -s is before -sid ,-sid will reset sqlstr with the value get from sqlutil. 
 
 if -s is after -sid ,the -s works.
 
 more detail see Main.java parseArgs method..
 

