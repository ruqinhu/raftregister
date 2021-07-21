功能 list：
客户端：
   1. 向服务端注册接口  register2server()
   2. 提供对外暴露的注册状态接口   registerStatus()
   3. 提供修改 服务端 地址信息的接口 updateServerConfig()
   
服务端:
   1. 提供注册接口给客户端，注册  addAndGetRegister()
   2. 拉取已注册客户端的健康状态，并续约  getRegister()
   3. 选举成功后，更新客户端的 config 信息， updateClientConfig()   raft 代做

服务端之间的共识，使用 jraft 。客户端保存 raft leader 信息，服务端选举后，通知 客户端最新的 leader 信息。
判断数据是否过期和续约，使用 guava Cache，服务端拉取客户端状态时，把状态信息写入 cache 中，没有写入说明没有获取到。


raft 相关实体类：
   RegisterStateMachine： raft 成员之间注册信息的保存和获取
   RegisterService： 代理 server，提供 request 的处理方法
   RegisterServer： 持有 raft 的 node 节点，通过 node 提供 raft 能力


服务端之间共享数据，不需要关心，raft 自动共享

客户端通过 RegisterClient 进行注册和续约，需要和服务端一直保持连接，服务端会定时拉取客户端传递过来的数据。

客户端传给服务端的数据，可以做成过期的和不过期的，不过期的就是配置数据，过期需要续约的就是注册信息。
