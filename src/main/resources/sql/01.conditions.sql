### 界面上多条件查询的拼接

### 用户查询界面上面的查询条件
#sql("findUser")
select *
  from t_z_user
where 1 = 1
#if(userName)
  AND userName like concat('%', #para(userName), '%')
#end
#if(email)
  AND email like concat('%', #para(email), '%')
#end
#if(company)
  AND company like concat('%', #para(company), '%')
#end
#if(department)
  AND department like concat('%', #para(department), '%')
#end
#if(city)
  AND city = #para(city)
#end
order by id asc
#end

### 后面有分页，所以这里结束不能有分号;

#--
; 
--#


### 用户查询界面上面的查询条件
#sql("findClient")
select *
from t_z_client
where 1 = 1
#if(clientName)
AND clientName like concat('%', #para(clientName), '%')
#end
#if(clientCode)
AND clientCode like concat('%', #para(clientCode), '%')
#end
#if(address)
AND address like concat('%', #para(address), '%')
#end
#if(channelType)
AND channelType like concat('%', #para(channelType), '%')
#end
order by id asc
#end

#--
;
--#

### 用户查询界面上面的查询条件
#sql("findCall")
select *
from t_z_client_call
where clientid = #para(0)
order by id asc
#end

#--
;
--#

#sql("findPotentialByClient")
select *
 from t_z_client_potential
where clientid = #para(0)
;
#end

### 客户过往项目和机会
#sql("findHistory")
select a.*
  from t_z_client_opp a
where a.bizType = #para(0)
  and a.clientId = #para(1)
;
#end

### 客户联系人
#sql("findContact")
select a.*
from t_z_client_contact a
where a.clientId = #para(0)
;
#end
