## 一. 入口

测试的入口，test 包中 RegisterServer1，RegisterServer2，RegisterServer3 和 RpcClient1，RpcClient2

RegisterServer4 当新节点启动时会自动加入稳定的集群中。测试代码中会手动调用 addPeer()

## 二. 组件

raft 相关实体类：

server 端：

**RegisterStateMachine**： 处理应用 Node#apply(task) 提交的日志，比如注册信息等数据，并存储

**RegisterStorage**:  存储和获取注册信息，默认实现类使用 guava Cache 来提供注册信息定时过期能力

**RegisterServer**： 持有 node ，通过 node 提供 raft 能力。
也持有 CliClientService(Client CommandLine Service)，提供操作 peer 的能力

**RegisterServerConfig**： 封装 raft 相关的服务端配置项

client端：

**RegisterService**： 提供供客户端调用的方法，目前有两个，注册 和 获取注册信息

**RegisterClient**： 持有 RegisterService 对象，对外提供注册中心 client 的相关功能

**RegisterClientConfig**： 封装 raft 相关客的户端配置项

## 三. 重要 api
server： RegisterStateMachine#onApply(Iterator)： 应用 task。如客户端注册的信息和服务间共享的数据等，存储到 storage 中

client： RegisterClientServiceImpl#addAndGetRegister(Map<String, String>): 持有 CliClientServiceImpl 获取 rpc 能力，向 server
发送注册和获取信息请求

server： RegisterRequestProcessor#handleRequest(RpcContext,RegisterRequest): netty 的请求处理 processor。处理用户的注册请求，持有 RegisterService，封装 task 发送到状态机，交给状态机处理

client： RenewScheduleTask#run(): 客户端调用 addAndGetRegister() 后，自动开启一个定时续约线程，进行续约操作



## 四. 展望
注册中心采用 push 模式， pull 模式需要客户端和服务端保持双工通信，暂未实现

注册中心也可当作配置中心使用，存储的信息不设置过期时间，交由 raft 存储，raft 提供了宕机从日志恢复数据等功能

通过 raft 提供的一致性状态机，解决了复制、修复、节点管理等问题，开发者仅仅需要关心业务相关操作

使用 raft 可以构建很多应用，比如 高可靠元信息管理、分布式锁、分布式存储 等


双工通信需要服务端在进行 processor 的时候，保存客户端的地址，开启线程，定时发送拉取注册信息接口给客户端。

## 更新：
2021-08-11 

增加服务端拉取客户端注册信息，即拉模式，入口在 duplex 包下。

2021-08-12

调整包结构，增加 springboot starter 注入客户端方式