# AgileFrameModelForAndroid
一个提供敏捷开发基础服务的android开源组件（An android open source component that provides the basic services of agile development.）
### 以上为该敏捷开发框架的基础功能，开发者可以直接下载Demo，然后将Common model导入到项目中，然后仿照Demo中app model的业务逻辑实现相应的功能。
# 具体说明如下 ：
## 基础服务组件说明 ##
* 该项目总体架构如下所述，但现在处于逐渐补充的阶段，在本项目未基本完成前，若遇到相应问题，可直接联系qq：564045867
* 该组件使支持组件化开发，除了主Model(app)和基础服务Model(common)外，其他模块应当由开发者按照业务逻辑进行组件划分
* 该组件为mvp设计模式，mvp概念可见[MVP模式简介](https://baike.baidu.com/item/MVP%E6%A8%A1%E5%BC%8F/10961746?fr=aladdin)
* 网络请求采用Retrofit + RxJava + OkHttp（当前主流的网络及异步处理框架）
* 数据库采用GreenDao（开发者也可以自行集成litPal等轻量级数据库sdk）
* 发布/订阅事件总线采用EventBus；
* 路由中间件采用ARouter；
* 下拉刷新采用SmartRefreshLayout；

## 基础服务组件（Common）功能介绍： ##
* 第三方依赖及其封装(Rxjava、EventBus、GreenDao、Glide等)
* Activity、Fragment、Adapter的基类(异步请求，通知，转场动画诸多功能均已实现)
* 网络请求和错误处理封装//TODO
* 图片加载封装
* 日志封装
* GreenDao简单封装
* MVP封装
* 权限请求封装
* 有数据、无数据、加载中、加载失败、网络异常、网络不佳页面切换管理器//TODO
* 通用的接口、常量类、工具类和公共组件

## app 包结构介绍： ##
*   activity：
    *   MainActivity：主界面 （带有eventBus 接收消息的逻辑）
    *   MVPTestActivity：mvp 模式演示类的view
    *   PermissionAndCameraActivity：权限请求及相机相册调用的演示类
    *   EventBusPostMessageActivity：EventBus发送请求的演示类
    *   GreenDaoTestActivity：GreenDao的简单演示类（辅助工具在common Model 的greenDao包下）
*   api：模块的接口管理
*   contract：mvp契约接口管理类
*   entry：实体类
*   model：mvp数据处理类Model
*   presenter：mvp主持者Prsenter 用于实现view 和 model的交互
    
## Common 包结构介绍： ##

## 组件引用的第三方依赖 ##
* [Retrofit](https://github.com/square/retrofit)
* [Okhttp3](https://github.com/square/okhttp)
* [Rxjava2](https://github.com/ReactiveX/RxJava)
* [Glide](https://github.com/bumptech/glide)
* [EventBus](https://github.com/greenrobot/EventBus)
* [GreenDao](https://github.com/greenrobot/greenDAO)
* [Gson](https://github.com/google/gson)
* [ARouter](https://github.com/alibaba/arouter)
* [BGABanner](https://github.com/bingoogolapple/BGABanner-Android)
* [FlycoTabLayout](https://github.com/H07000223/FlycoTabLayout)
* [SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)



