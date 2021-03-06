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
4. layui 
    1. table 工具栏事件绑定
    2. form 提交事件绑定，提交后刷新 table
    3. miniPage.renderPageTitle 去掉当前菜单项中的 querystring
    
## 2020/04/26
1. jFinal 运行机制
2. 读取 resources 目录下的文件
    1. properties 文件
    2. yaml 文件
3. jUnit5 的单元测试
4. 尽量不使用 data class，而直接使用 POPJ
    1. 无参构造函数
5. MD5 加密与验证        
    
##  2020/05/01
1. form中，lable + input 所占比例可灵活调整；
2. form save 变成共用函数；
3. 从 yaml 中加载 mock data; 
    
    
##  2020/05/02
1. CRUD: company, department
2. 日期选择
3. dropdown table 选择
4. form 内部元素选择：formId + inputClass  
5. activeRecord 底层是 map，效果是动态属性：即使没有声明属性字段，只要 sql 中有字段，就可以直接拿来用  
6. 必须使用 MixedJson
    
##  2020/05/03
1. js 中动态填充 select 的选项；
2. 一行：一个 label，多个 select 并列；
3. dropdown
    1. 点击触发
    2. 可以响应 dropdown 选项的事件；
4. y-admin: https://github.com/C-GY/Y-Admin      


##  2020/05/04
1. 富文本编辑器：wangEditor
    1. 内容可以按照 html 格式读取，即可保存到 db 中；
    2. 从 db 中读取后，也可以设置初始值；
2. upload
    1. layui
    2. zyUpload
    3. jfinal upload 
        1. getFile 文件已经从客户端上载到服务器，并且保存在服务器的指定目录下；
        2. 文件名和上载文件名相同；    

##  2020/05/05
1. js 端增加权限控制
    1. 逻辑和后端一致：资源有需要的权限，当前用户有权限列表，判断两个列表是否有交集
    2. js 获取当前用户信息，在 loginInterceptor 中设置，在 index.js 中可读取，并保存在全局变量中；
    3. 主体逻辑在 metis.js 中，菜单渲染在 miniMenu.js 中，只修改了 each 函数；
2. 把菜单和 resource 合并成一张表
    1. 通过表来配置菜单项；
    2. 递归生成菜单项；
    3. java 中使用 object 来表达 js 中所需的 json
    4. 递归object 转 string 需要用 FastJson，而不是 MixedJson      
    
    
##  2020/05/10
1. 问卷调查 survey：问卷代码，提交人，属性（选项值）
2. 指标填报：表单，指标代码，业务时间，公司，属性（指标值，说明备注，提交人，提交日期）
3. 单选框判断是否选中值 survey_open
4. 从指定文件读取：yaml，excel
5. 匹配：问卷读取的信息 + db 保存的值，fillAnswer
6. selectOption 的可选值：指标填报的公司列表
7. 按照kv 中 key 的后缀，拆分并匹配 指标值 和 说明备注   
    
## todo 
    


