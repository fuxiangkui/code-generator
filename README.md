# code-generator


## 简述
    代码自动生成
     
## 代码结构
    
``` lua
jar-dataworks 
├── dataworks-api           -- 对外API接口，只做接口定义和基本的参数校验
├── dataworks-business      -- 核心业务逻辑
├── dataworks-contract      -- 服务接口定义，内部RPC服务接口包
├── dataworks-dao           -- 数据持久化包，mybatics定义包
├── dataworks-service       -- 服务接口实现，内部RPC服务的实现
├── dataworks-web-router    -- 服务静态页面，通过nodejs打包，nginx部署
```

## 依赖

### 后端
- JDK：1.8（支持1.6+）
- 数据库：Mysql
- 项目构建工具：Maven 3.3.3
- API文档：baofooYapi
- MVC框架：SpringMVC 4.2.1.RELEASE
- 核心框架：Spring 4.2.1.RELEASE
- ORM框架：MyBatis 3.3.0
- 分布式协调服务：Zookeeper 3.4.7
- 分布式RPC服务：Dubbo 2.5.3（默认Hessian 4.0.38）
- 分布式缓存服务：Redis 2.8.12
- 分布式消息服务：rabitMQ 5.13.3
- JSON工具：Fastjson 1.2.29
- 数据库连接池：Druid 1.0.15
- 日志管理：SLF4J 1.7.21、Logback 1.1.7

### 前端
- 基础代码库：Nodejs、webpack、VueJS、ElementUI
- 前端模板： 暂未定
 
### 部署打包
- 前端：npm run build
- API后端：maven clean install 

## 后续
加速推进社会文明和人类文明的进化
