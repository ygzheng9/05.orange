## 2020/03/01
1. use maven for speed 
    2. kotlin-compiler 
    3. jar: resource 目录下文件不打进 jar，而是 copy 到 config 目录下 
    4. assembly：最终输出 zip，目录结构定义好，对应的被打包文件，依赖度 jar
    
## 2020/03/03
1. 迁移旧程序
    1. mxstats
    2. order fulfilment
    
## 2020/03/15
1. 迁移 Order fulfilment
    1. 订单 - 配货 - 发货
    2. 出入库明细：补货入库，发货出库
    3. 现有库存状态查询：总在库量，已配货占用量，剩余可配货量
    4. 逆向操作；
2. http
    1. post 请求，附加 json 参数；
    2. 通过 rawData -> json parse -> java class 获取 post 来的 json 参数
3. 查询有限考虑返回 Record，除非有明确需求，再转换成 Typed Data Class；
    1. Record 可以和 sql 直接对相应；
    2. 只需要在 前端 做对 Record 做映射；
    3. 当 sql 修改时，只需要改前端，不需要改后端；    

## 2020/04/05
1. 套用 layuimini
2. 实现 db seed
    + 在 non-web 环境下，使用 DB
    + 生成字段值：随机从列表中选取
3. 用户管理(t_zuser) 
    + 无条件的分页查询
    + 顶部查询条件，多条件组合查询，模糊，精确，并且支持分页
    + 新增，修改，删除，批量删除
    + 新增、修改：打开页面 和 post 都是相同的逻辑，通过 id 区分是 新增 还是 修改
    + 批量删除：根据 post 的 array 转成 sql 用的 in string    
4. 通过 gulp 使用 es6

## 2020/04/06
1. enjoy html template 
    + text input
    + dropdown, and select current option
    + radio for single 
    + checkbox for multiple
    + text area
2. form 两种
    + 一行一个输入控件 block
    + 一行多个输入控件 inline    
3. 获取 checkbox 的值，按照 name
    + form 提交时拦截，通过 js 获取
    + layui 自定义模块 metis    
     
## 2020/04/09
1. js 把全局数据保存在 state 中；
2. 菜单项中，可以设置 id，在 js 中访问，附加 url 参数，触发 click
3. html 中 url querystring 附加参数给 后台
4. js 中从 html 的 div 中取出 参数，给后台；
5. 后台把 数据 写入 div 中，供 js 使用；
    
    
## 2020/04/11
1. create new orange from sugar
2. only keep layUI and remove api for stats
3. 添加 -parameters 后需要重新 build 项目，否则 warning 不会消失    
    
## todo 
1. upload
2. action 中获取当前的 actionKey     


