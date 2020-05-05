### 判定用户登录
#sql("checkLogin")
select a.*
from t_z_user a
where a.email = #para(0)
  and a.password = #para(1)
;
#end

### 需要做权限控制的资源
#sql("allResources")
select a.href `key`, max(a.permissions) permissions
from t_z_resource a
group by a.href
order by a.href;
#end

### 获取菜单项
#sql("loadMenu")
select *
  from t_z_resource
where `type` = 'menu'
order by `code`;
#end
