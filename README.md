# SSO 解决方案（鉴权+SESSION信息）

## 一、设计背景

单点登录(SingleSignOn，SSO)，就是通过用户的一次性鉴别登录。当用户在[身份认证服务器](https://baike.baidu.com/item/身份认证服务器/10881881)上登录一次以后，即可获得访问单点登录系统中其他关联系统和应用软件的权限，**意味着在多个应用系统中，用户只需一次登录就可以访问所有相互信任的应用系统**。

但是，鉴权和 session信息应该由谁去处理，如果是后端处理，那么每个系统	只要是**对外暴露**，或者**涉及隐私** 或者 **必须要求登录情况**下操作的接口都需要后端处理，这样做有以下几个缺点：

1）后台服务增加压力，并且nginx到service 请求响应时间会增加

2）增加代码复杂度，因为每个对外暴露的系统都需设计 统一或者 各个组件需要的鉴权方案，容易造成信息不一致性。

3）过多引入 第三方SSO框架会让我们的异常错误不在自己可控制的范围内。同时也不会为我们带来新的单点登录的问题解决方案，不利于提高项目的健壮性及可持续发展。

这不是设计此方案的初衷，**我们更希望所有的请求从开始到结束成功或者失败 应该在我们自己的可控范围内**。

所以该方案，不借助于第三方框架，并期望请求都先在反向代理做校验， 这样减少了后端压力及代码复杂度，同时方便将组件统一化管理，并可以满足 跨语言系统的实现。

它更适合于 跨语言的系统之间调度，或者 JAVA 老项目（servlet session获取信息），或者希望优化代码的项目中。

## 二、主要解决的问题

1）请求鉴权，每个请求的访问都有严格的权限控制，公开和非公开

2）全局session管理，各个系统之间仅凭 用户本地cookie就可以拿到用户信息，方便用户操作

3）组件之间鉴权，一旦跨服务，跨系统之间调度，通过组件鉴权提高 系统安全性。



## 三、技术选型



|             模块介绍              |        采用组件        |
| :-------------------------------: | :--------------------: |
|             反向代理              |       openresty        |
|              SSO服务              | spring boot (java实现) |
| 常规服务 （商品，订单，用户服务） | spring boot(java实现)  |
|            浏览器页面             |           JS           |



## 四、设计方案-流程

### 1.系统架构

![sso-系统结构.drawio](sso-%E7%B3%BB%E7%BB%9F%E7%BB%93%E6%9E%84.drawio.png)

### 2.鉴权流程

![sso-鉴权流程.drawio](sso-%E9%89%B4%E6%9D%83%E6%B5%81%E7%A8%8B.drawio.png)

### 3.获取session信息流程

![sso-获取用户信息.drawio](sso-%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E4%BF%A1%E6%81%AF.drawio.png)

## 五、核心功能介绍：

	1. sso登录

对外暴露的接口  /api/sso/login：

```nginx

    #用户登录 帮助跳到指定页或登录页
    location  = /api/sso/login {  
        lua_code_cache off;
        rewrite_by_lua_file  lua/sso_login.lua;
        root /usr/local/Cellar/openresty/1.19.9.1_2/nginx/html;
        charset utf-8;

        #默认post请求不支持跳转页面
        error_page 405 =200 $uri;
    }
```

​	用户在前端页面，发起登录，nginx收到请求开始处理

- nginx先获取post请求体中的用户名和密码,如果没有获取到用户信息，nginx重新跳到登录页
- nginx发起内部的sso服务登录http请求(api/sso/ssoLogin)，将用户信息传入，等待服务返回结果
- 服务如果没有正确返回结果，或者http状态码不为200，nginx重新跳到登录页面
- 如果返回结果，但是登录失败，则跳到登录失败页面
- 如果用户登录成功，lua脚本将信息加密后存入到cookie(**ut为用户登录token，uit为用户信息** )，并设置过期时间为30分钟后
- 跳到主页

2.获取用户session信息

对外暴露的接口 /api/sso/getUserInfo：

```nginx
    location  = /api/sso/getUserInfo {  
        lua_code_cache off;
        content_by_lua_file  lua/sso_user_info.lua;        
        charset utf-8;
    }
```

所有子系统前端页面，通过axios发起获取用户session请求

- nginx先获取请求头中的 cookie的加密数据(ut,uit信息)，一旦不存在， nginx返回json'{"code":10001,"msg":"用户校验失败，请重新登录"}'
- 获取完毕后，解密uit用户信息，取出其中的userId,将用户登录token和userId发起sso服务的鉴权请求(/api/sso/checkLogin)
- 服务如果没有正确返回结果，或者http状态码不为200，nginx 返回json '{"code":10001,"msg":"用户校，验失败，请重新登录"}'
- 如果返回结果，但是登录失败，则nginx 返回json '{"code":10001,"msg":"用户校验失败，请重新登录"}'
- 如果返回结果显示校验成功，返回json'{"code":10000,"msg":"用户校验成功","data": ' .. **decryptData** .. '}'，其中data里的**decryptData**就为 第二步**解密uit用户信息的数据，拿过来直接用**



3.访问公开页面（类似于浏览商品，查看商品分类等等）

对外暴露的接口 /api/XXX

````nginx
    location  /api/product{
        proxy_pass  http://127.0.0.1:10082;
    }
````

这里不做阐述，直接放行



4.访问私密页面（类似于用户订单，用户收藏，用户好友列表等等）

对外暴露的接口 /api/XXX 

```nginx
    location  /api/order{
        lua_code_cache off;
        access_by_lua_file lua/sso_filter.lua;
        proxy_pass  http://127.0.0.1:10081;

    }
```

浏览器在访问这种的请求时，lua的策略是做权限控制，如果校验通过放行，下发到后面的服务，如果校验不通过，直接让nginx返回 401 500等页面

**这里注意：正则表达式可以拦截所有 以/api开头的请求，统一判断，如果请求里包含了"order" "user" "password"等关键字的请求，可以做校验，不然则放行，看个人需要。**

- nginx先获取请求头中的 cookie的加密数据(ut,uit信息)，一旦不存在， nginx直接跳转到400页面
- 获取完毕后，解密uit用户信息，取出其中的userId,将用户登录token和userId发起sso服务的鉴权请求(/api/sso/checkLogin)
- 服务如果没有正确返回结果，或者http状态码不为200，nginx 跳转到500页面
- 如果返回结果，但是登录失败，则nginx跳转到401页面
- **如果返回结果显示校验成功，直接放行**







## 六、后台服务接口文档

### 1.sso接口

#### (1)用户登录

【功能定义】

用户登录，并授予用户登录token及session信息（nginx校验用）
接口地址：http://<ip>:<port>/sso/ssoLogin

输入：

| 参数名        | 参数类型 | 是否必填 | 备注               |
| ------------- | -------- | -------- | ------------------ |
| user_name     | String   | 是       | 用户名             |
| user_password | String   | 是       | 用户密码           |
| target_url    | String   | 否       | 用户将要访问的资源 |
| host_ip       | String   | 否       | 用户登录地址       |

输出：

| 参数 | 参数类型   | 备注                                                         |
| ---- | ---------- | ------------------------------------------------------------ |
| code | int        | 10000 校验成功，能够获取用户信息，<br/>10001 校验失败，用户名或密码错误  <br/>10002 用户服务异常   <br/>10003 未知异常 <br/>10004 字段校验错误 |
| msg  | string     | 中文描述                                                     |
| Data | JSONObject | 结果详情  用户session信息，用户token，用户id                 |





#### (2)获取用户session信息

【功能定义】

用于所有系统获取用户已登录后的session信息（nginx校验用）
接口地址：http://<ip>:<port>/sso/getUserInfo

输入：

| 参数名     | 参数类型 | 是否必填 | 备注                             |
| ---------- | -------- | -------- | -------------------------------- |
| user_token | String   | 是       | 用户登录的token                  |
| user_id    | String   | 是       | 用户id，和token一起校验          |
| host_ip    | String   | 否       | 用户的登录ip，可判断是否异常登录 |
| target_url | String   | 否       | 用户将要访问的资源               |

输出：

| 参数 | 参数类型 | 备注                                                         |
| ---- | -------- | ------------------------------------------------------------ |
| code | int      | 10000 校验成功，能够获取用户信息，<br/>10001 校验失败，用户名或密码错误或者token过期  <br/>10002 用户服务异常   <br/>10003 未知异常 <br/>10004 字段校验错误 |
| msg  | string   | 中文描述                                                     |

#### (3)校验用户是否登录

【功能定义】

用于校验用户是否处于登录状态(nginx校验使用）
接口地址：http://<ip>:<port>/sso/checkToken

输入：

| 参数名     | 参数类型 | 是否必填 | 备注                             |
| ---------- | -------- | -------- | -------------------------------- |
| user_token | String   | 是       | 用户登录的token                  |
| host_ip    | String   | 否       | 用户的登录ip，可判断是否异常登录 |
| target_url | String   | 否       | 用户将要访问的资源               |

输出：

| 参数 | 参数类型   | 备注                                                         |
| ---- | ---------- | ------------------------------------------------------------ |
| code | int        | 10000 校验成功，能够获取用户信息，<br/>10001 校验失败，用户名或密码错误  <br/>10002 用户服务异常   <br/>10003 未知异常 <br/>10004 字段校验错误 |
| msg  | string     | 中文描述                                                     |
| Data | JSONObject | 结果详情  json格式 默认为null                                |







### 3.商品，订单服务接口

没有特殊定义，作为普通服务正常调用接口即可。

注意的是：此次案例以 订单服务作为用户隐私的服务，需要校验登录权限





## 七、案例



后台所有服务在 sso_all_project下

用户服务：sso-client-user

商品服务：sso-client-product

订单服务：sso-client-user

sso服务：sso-server



nginx和lua配置在sso_nginx_lua_conf下，具体存放位置要基于openresty安装的版本做调整，这里只是列出具体所需文件





