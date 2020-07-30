# Serverless enablement workshop Demo

* 该项目采用java语言实现lambda的编写，同时使用[sparkjava](http://sparkjava.com/)
* 实现 web应用的开发，使用Cognito的用户池实现用户注册与管理，通过身份池实现AWS资源访问权限控制
* 数据持久化到DyanmoDB中，同时使用DynamoDBMapper实现数据持久化操作。 

## 系统架构图
![](/images/architecture.jpg)
## 项目构建与部署
使用maven构建项目
```bash
$ cd serverless-workshop-service
$ mvn clean package
```

也可以使用 [AWS SAM Local](https://github.com/awslabs/aws-sam-local) 进行项目的管理和构建.

首先安装SAM:

```bash
$ npm install -g aws-sam-local
```
同时也可以使用sam进行本地测试，如下启动本地环境来调试
```bash
$ sam local start-api --template sam.yaml

...
Mounting com.amazonaws.serverless.archetypes.StreamLambdaHandler::handleRequest (java8) at http://127.0.0.1:3000/{proxy+} [OPTIONS GET HEAD POST PUT DELETE PATCH]
...
```

通过curl命令测试

```bash
$ curl -s http://127.0.0.1:3000/ping | python -m json.tool

{
    "pong": "Hello, World!"
}
``` 