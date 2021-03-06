package com.metis.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseZClientContact<M extends BaseZClientContact<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}

	public void setClientId(java.lang.Integer clientId) {
		set("clientId", clientId);
	}
	
	public java.lang.Integer getClientId() {
		return getInt("clientId");
	}

	public void setPersonName(java.lang.String personName) {
		set("personName", personName);
	}
	
	public java.lang.String getPersonName() {
		return getStr("personName");
	}

	public void setDepartment(java.lang.String department) {
		set("department", department);
	}
	
	public java.lang.String getDepartment() {
		return getStr("department");
	}

	public void setTitle(java.lang.String title) {
		set("title", title);
	}
	
	public java.lang.String getTitle() {
		return getStr("title");
	}

	public void setEmail(java.lang.String email) {
		set("email", email);
	}
	
	public java.lang.String getEmail() {
		return getStr("email");
	}

	public void setPhone(java.lang.String phone) {
		set("phone", phone);
	}
	
	public java.lang.String getPhone() {
		return getStr("phone");
	}

	public void setCurrentVote(java.lang.String currentVote) {
		set("currentVote", currentVote);
	}
	
	public java.lang.String getCurrentVote() {
		return getStr("currentVote");
	}

	public void setTargetVote(java.lang.String targetVote) {
		set("targetVote", targetVote);
	}
	
	public java.lang.String getTargetVote() {
		return getStr("targetVote");
	}

	public void setCoveredBy(java.lang.String coveredBy) {
		set("coveredBy", coveredBy);
	}
	
	public java.lang.String getCoveredBy() {
		return getStr("coveredBy");
	}

	public void setFocus(java.lang.String focus) {
		set("focus", focus);
	}
	
	public java.lang.String getFocus() {
		return getStr("focus");
	}

	public void setRemark(java.lang.String remark) {
		set("remark", remark);
	}
	
	public java.lang.String getRemark() {
		return getStr("remark");
	}

}
