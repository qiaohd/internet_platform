## 本项目用到了git,maven,redis,zookeeper,mysql等

#### 1、代码下载

- git clone git@git.yonyou.com:ip/internet_platform.git
	
#### 2、导入工程时，需要将parent、admin、api、common、service、web 6个工程全部导入

###### parent - 父工程

- admin  - dubbo服务的管理控制台，可以对dubbo的发布和消费者的调用进行管理和控制
- api    - 接口，服务提供者和服务消费者都会使用该工程输出的jar包
- common - 公共jar包，服务提供者和服务消费者都会使用该工程输出的jar包
- service- 服务提供者，后台服务注册到zookeeper上
- web    - 服务消费者，前台，使用zookeeper上的接口服务
	  
#### 3、开发流程

- 0、git pull origin master

> 拉取远程master最新代码

- 1、git checkout -b <分支名称>
  
> 创建自己的本地分支,分支命名规范：邮箱前缀_当前任务。
  
- 2、功能开发完成

- 3、git status

> 查看当前工程文件状态（删除、修改、新增）
  
- 4、git add .
  
> 把开发的文件添加到缓存区
  
- 5、git commit -m "信息"
  
> 把开发的文件添加到本地仓库（head） 
  
- 6、git pull origin master

> 把远程master上最新代码，下载到本地远程origin/master

- *7、处理冲突

> 找到冲突文件，修改，然后重新提交到本地仓库（git add 修改的文件；git commit -m "resolve conflict"）

- 8、git push origin yourBranch

> 推送合并后的分支到远程
  
- 9、申请 merge request

> 登录git.yonyou.com进行申请

- 10、申请 merge request，并删除分支

> merge request审核(勾选删除分支)通过后，切换都本地master上，然后删除本地开发分支（git branch -d 分支名称）

- 11、进行下一步任务时，从第 0 步开始重复执行	  

- 12、建议在每次执行 git add . 或者 git commit -m "修改信息" 前，都执行git status 查看当前本地文件的状态

#### 4、部署方式

- parent项目 - 运行方式 - maven install

> 执行中如果缺少jar包，可从http://central.maven.org/maven2/下载.

> compile 编译 

> clean 清空target目录

> package 打包

> test 运行单元测试

> install 把打出的包装载到本地仓库，这样其他pom.xml就能找到这个依赖了.

- 检查service和web子项目的数据库连接、redis、zookeeper连接，启动mysql,redis和zookeeper
- service子项目，启动test类中的StartServiceServer
- web子项目，启动test类中的QuickStartIPWeb
