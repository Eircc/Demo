# Demo

### 前言

> 时隔一个月再来回顾简单搭建`Dubbo-Zookeeper`

### 一、搭建Dubbo-Admin监控中心



#### 1、下载`Dubbo-Admin`文件

解压在目录`Dubbo-Admin`下的压缩文件夹`dubbo-admin-develop`

> 也可自行下载，下载地址：`https://github.com/apache/dubbo-admin`



#### 2、修改`Dubbo-Admin`配置文件

解压之后需要手动修改`Dubbo-Admin`的配置文件，修改方式如下：

- 配置文件的位置为：

```shell
$ dubbo-admin-server/src/main/resources/application.properties
```

- 主要的配置有：

```properties
admin.registry.address=zookeeper://xxx.xxx.xxx:2181
admin.config-center=zookeeper://xxx.xxx.xxx:2181
admin.metadata-report.address=zookeeper://xxx.xxx.xxx:2181
```

分别为：

- 注册中心的地址
- 配置中心的地址
- 元数据中心的地址
- 指定自己`Zookeeper`服务地址即可



#### 3、部署`Dubbo-Admin`

修改完配置文件之后进行打包部署，以`jar`包的形式运行，在当前位置进入`cmd`命令如下：

```shell
$ cd dubbo-admin

$ mvn clean package

$ cd dubbo-admin-develop\dubbo-admin-distribution\target

$ java -jar dubbo-admin-0.2.0-SNAPSHOT.jar

```

- 出现Spring Boot的启动log即为启动成功
- 启动成功之后将黑窗口挂起即可，不要关闭，在浏览器访问`localhost:8080`即可看见`Dubbo-Admin`的管理界面
- 用户名密码均为`root`

> `Dubbo-Admin`也可进行前后端分离部署，这里不多赘述



### 二、服务提供者

#### 1、工程目录

![](D:\DevelopTools\IDEA\IDEA-workspace\Demo\img\工程目录.png)



#### 2、`pom.xml`依赖

`Dubbo-Interface`公共模块：

```xml-dtd
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.kapcb</groupId>
    <artifactId>Dubbo-Interface</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <!--Dubbo版本与curator版本许一一对应，否则会把错-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>dubbo</artifactId>
            <version>2.6.7</version>
        </dependency>
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>4.3.0</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.12</version>
        </dependency>
    </dependencies>
</project>
```



`Dubbo-Provider`服务提供者模块：

```xml-dtd
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>org.example</groupId>
    <artifactId>Dubbo-Provider</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>com.kapcb</groupId>
            <artifactId>Dubbo-Interface</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>
```



`Dubbo-Consumer`服务消费者模块

```xml-dtd
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>org.example</groupId>
    <artifactId>Dubbo-Consumer</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>com.kapcb</groupId>
            <artifactId>Dubbo-Interface</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>
```



依赖关系为：`Dubbo-Provider`服务提供者与`Dubbo-Consumer`服务消费者均依赖`Dubbo-Interface`



#### 3、在`Dubbo-Interface`中定义接口以及实体Bean

Bean对象`User.java`

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = -464963472153405512L;

    private String username;
    private String password;
    private String email;
    private String phone;
    private Date birth;
}
```



接口`IUserService`：

```java
public interface IUserService {
    /**
     * 根据id获取用户信息
     *
     * @param id String
     * @return User
     */
    List<User> getUserList(String id);
}
```



#### 4、在`Dubbo-Provider`中实现`Dubbo-Interface`中定义的接口

接口的实现`IUserServiceImpl`：

```java
public class IUserServiceImpl implements IUserService {
    private static final String DEMO_ONE = "1";

    public List<User> getUserList(String id) {
        if (DEMO_ONE.equals(id)) {
            User kapcb = new User("kapcb", "123456", "eircccallroot@163.com", "1111222333", new Date());
            User eircc = new User("eircc", "123456", "eircccallroot@yeah.net", "11112222333", new Date());
            return Arrays.asList(kapcb,eircc);
        } else {
            return null;
        }
    }
}
```



#### 5、使用`Spring`配置文件声明暴露提供者服务

在`Dubbo-Provider`的`resources`目录下创建`provider.xml`

```xml-dtd
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-4.3.xsd
       http://dubbo.apache.org/schema/dubbo
       http://dubbo.apache.org/schema/dubbo/dubbo.xsd">

    <!--指定提供者信息，用于计算消费者与提供者之间的依赖关系-->
    <dubbo:application name="Dubbo-Provider"/>

    <!--使用zookeeper注册中心暴露服务地址-->
    <dubbo:registry protocol="zookeeper" address="{your.zookeeper.server.port}:2181"/>

    <!--使用dubbo协议(RPC)在20880端口暴露服务(指定通讯协议，端口号)-->
    <dubbo:protocol name="dubbo" port="20880"/>

    <!--声明需要暴露出去的服务，ref指向本地需要暴服务的bean对象-->
    <dubbo:service interface="com.kapcb.ccc.service.IUserService" ref="IUserServiceImpl"/>

    <!--服务的实现-->
    <bean id="IUserServiceImpl" class="com.kapcb.ccc.service.impl.IUserServiceImpl"/>
</beans>
```



#### 6、加载`Spring`的配置

在`Dubbo-Provider`工程下新建`ProviderMainApplication`

```java
public class ProviderMainApplication {
    private static final String CONFIG_LOCATION = "provider.xml";

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext ioc = new ClassPathXmlApplicationContext(CONFIG_LOCATION);
        // 对外提供服务
        ioc.start();
        // 阻塞
        System.in.read();
    }
}
```



启动完成之后控制台会报`log4j`相关的warn，忽视即可，因为没有引入日志。直接取`Dubbo-Admin`中查看是否服务注册进来即可



### 三、服务消费者

#### 1、通过`Spring`配置引用远程服务

`Dubbo-Consumer`下的`resources`下创建`consumer.xml`

```xml-dtd
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo" xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-4.3.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://dubbo.apache.org/schema/dubbo
       http://dubbo.apache.org/schema/dubbo/dubbo.xsd">

    <!--开启包扫描-->
    <context:component-scan base-package="com.kapcb.ccc.service"/>

    <!--指定服务名称-->
    <dubbo:application name="Dubbo-Consumer" />

    <!--zookeeper注册地址-->
    <dubbo:registry protocol="zookeeper" address="{your.zookeeper.server.port}:2181"/>

    <!--远程调用服务-->
    <dubbo:reference id="IUserService" interface="com.kapcb.ccc.service.IUserService"/>
</beans>
```



#### 2、声明远程调用

在`Dubbo-Consumer`模块下声明`IOrderService`接口及其实现类`IOrderServiceImpl`

```java
public interface IOrderService {
    /**
     * 根据id获取信息
     *
     * @param id String
     */
    void getInfo(String id);
}
```



```java
@Service
@RequiredArgsConstructor
public class IOrderServiceImpl implements IOrderService {

    private Logger logger = Logger.getLogger(String.valueOf(this.getClass()));

    private final IUserService iUserService;

    public void getInfo(String id) {
        logger.info("远程调用开始！");
        List<User> userList = this.iUserService.getUserList(id);
        for (User user : userList) {
            System.out.println(user);
        }
        logger.info("远程调用成功！");
    }
}
```



#### 3、加载`Spring`配置，实现远程服务调用

`Dubbo-Consumer`模块下的创建`ConsumerMainApplication`

```java
public class ConsumerMainApplication {

    private static final String TEST_ID = "1";
    private static final String CONFIG_LOCATION = "consumer.xml";

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext ioc = new ClassPathXmlApplicationContext(CONFIG_LOCATION);
        ioc.start();
        // 获取bean对象
        IOrderService bean = ioc.getBean(IOrderService.class);
        // 实现远程调用
        bean.getInfo(TEST_ID);
        // 阻塞
        System.in.read();
    }
}
```

- 1、先启动服务提供者
- 2、启动服务消费者
- 3、等待

- 4、查看控制台与`Dubbo-Admin`即可知道远程调用是否成功

## 后续部分，一定对您有帮助

### 前言
- 如果您看到了这里，相信您一定也是很爱学习的人，如果觉得对您有帮助，您的`Start`是对我的最大认同。
- 记录自己学习`Dubbo`的<strong>学习笔记</strong>还有`Demo`，笔记均在`Demo`注释部分，后续会整理成博客发布在自己的私人博客上。
- 如果觉得对您有帮助，您的`Start`就是对我最大的帮助。
- 欢迎互相学习交流，如果项目中有问题的部分可以用邮箱联系我`eircccallroot@163.com`。
- 欢迎翻阅我的私人博客`https.www.ccaizx.cn`,记录日常学习中的一些技术干货。



### 其他项目

> 目前在维护的其它学习项目

<table>
    <tr>
        <td><a style="text-decoration: none;" href="https://github.com/Eircc/Java-Kapcb" target="_blank"><strong>Java-Kapcb</strong></a></td>
        <td><a style="text-decoration: none;" href="https://github.com/Eircc/Java-Kapcb" target="_blank"><b>Java基础的学习项目(更新中、含笔记)</b></a></td>
    </tr>
    <tr>
    	<td><a style="text-decoration: none;" href="https://github.com/Eircc/Spring-Kapcb" target="_blank"><strong>Spring-Kapcb</strong></a></td>
        <td><a style="text-decoration: none;" href="https://github.com/Eircc/Spring-Kapcb" target="_blank"><b>Spring、Spring MVC以及MyBatis的经典学习项目以及SSM整合学习项目(更新中、含笔记)</b></a></td>
    </tr>
    <tr>
    	<td><a style="text-decoration: none;" href="https://github.com/Eircc/SpringBoot-Kapcb" target="_blank"><strong>SpringBoot-Kapcb</strong></a></td>
        <td><a style="text-decoration: none;" href="https://github.com/Eircc/SpringBoot-Kapcb" target="_blank"><b>SpringBoot的基本原理以及与各种中间件的整合学习项目(更新中、含笔记)</b></a></td>
    </tr>
    <tr>
    	<td><a style="text-decoration: none;" href="https://github.com/Eircc/SpringCloud-Kapcb" target="_blank"><strong>SpringCloud-Kapcb</strong></a></td>
        <td><a style="text-decoration: none;" href="https://github.com/Eircc/SpringCloud-Kapcb" target="_blank"><b>Spring Cloud Netflix与Spring Cloud Alibaba的生态组件学习项目(更新中、含笔记)</b></a></td>
    </tr>
    <tr>
        <td><a style="text-decoration: none;" href="https://github.com/Eircc/H5-CSS3-Kapcb" target="_blank"><strong>H5-CSS3-Kapcb</strong></a></td>
        <td><a style="text-decoration: none;" href="https://github.com/Eircc/H5-CSS3-Kapcb" target="_blank"><b>Web前端H5与CSS3的基础入门学习项目(更新中、含笔记)</b></a></td>
    </tr>
    <tr>
    	<td><a style="text-decoration: none;" href="https://github.com/Eircc/JavaScript-Kapcb" target="_blank"><strong>JavaScript-Kapcb</strong></a></td>
        <td><a style="text-decoration: none;" href="https://github.com/Eircc/JavaScript-Kapcb" target="_blank"><b>Web前端JavaScript学习项目(更新中、含笔记)</b></a></td>
    </tr>
    <tr>
        <td><a style="text-decoration: none;" href="https://www.ccaizx.cn" target="_blank" ><b>个人博客</b><a</td>
        <td><a style="text-decoration: none;" href="https://www.ccaizx.cn" target="_blank" ><b>https://www.ccaizx.cn</b></a></td>
    </tr>
</table>
