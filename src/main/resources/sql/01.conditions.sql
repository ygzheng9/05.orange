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
#if(keyword)
  and ( (userName like concat('%', #para(keyword), '%')) or
        (email like concat('%', #para(keyword), '%')) )
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


### 商机查询部分

#sql("findLead")
select *
from t_z_lead_info
where 1 = 1
#if(clientName)
  AND clientName like concat('%', #para(clientName), '%')
#end
#if(leadName)
AND leadName like concat('%', #para(leadName), '%')
#end
#if(leadOwner)
AND leadOwner like concat('%', #para(leadOwner), '%')
#end
#if(leadPhase)
AND leadPhase like concat('%', #para(leadPhase), '%')
#end
order by id asc
#end

### 后面有分页，所以这里结束不能有分号;

#--
;
--#

### 公司基本信息
#sql("findCompany")
select *
from t_z_company
where 1 = 1
#if(companyName)
  AND companyName like concat('%', #para(companyName), '%')
#end
#if(bizUnit)
AND bizUnit like concat('%', #para(bizUnit), '%')
#end
#if(companyCode)
AND companyCode like concat('%', #para(companyCode), '%')
#end
#if(address)
AND address like concat('%', #para(address), '%')
#end
#if(keyword)
AND ( (companyName like concat('%', #para(keyword), '%')) or
      (companyCode like concat('%', #para(keyword), '%')) )
#end
order by id asc
#end

### 后面有分页，所以这里结束不能有分号;

#--
;
--#


### 部门基本信息
#sql("findDepartmentById")
select dept.*,
       comp.companyName,
       usr.userName
from t_z_department dept
         left join t_z_company comp on comp.id = dept.companyId
         left join t_z_user usr on usr.id = dept.headId
where dept.id = #para(0);
#end

#sql("findDepartment")
select dept.*,
       comp.companyName,
       usr.userName
from t_z_department dept
left join t_z_company comp on comp.id = dept.companyId
left join t_z_user usr on usr.id = dept.headId
where 1 = 1
#if(companyId)
  AND dept.companyId = #para(companyId)
#end
#if(departmentName)
  AND dept.departmentName like concat('%', #para(departmentName), '%')
#end
#if(departmentCode)
  AND dept.departmentCode like concat('%', #para(departmentCode), '%')
#end
#if(headId)
  AND dept.headId = #para(headId)
#end
order by id asc
#end

### 后面有分页，所以这里结束不能有分号;

#--
;
--#
