# SmartDb
###Descript
 SmartDb 是一个ORM库，简化对数据库的操作(现在支持对象保存和查看，以后逐步完善)<br>
 SmartDb中 数据库表的创建语句是在编译时自动生成的，放在一个单独的文件中(编译自动生成)<br>
#####Save Object
 ```Java
   User user=new User();
   user.setName("jk");
   user.setPassWord("123");
   Dagger.instance().create(user);
  ```
##### Select
  ```java
   User user=null;
   List<Object> users=Dagger.instance().Select(User.class).where("id",1).query();
   user=(User)users.get(0);
   ```
