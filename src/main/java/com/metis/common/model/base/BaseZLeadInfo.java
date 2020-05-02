package com.metis.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseZLeadInfo<M extends BaseZLeadInfo<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}

	public void setLeadName(java.lang.String leadName) {
		set("leadName", leadName);
	}
	
	public java.lang.String getLeadName() {
		return getStr("leadName");
	}

	public void setClientName(java.lang.String clientName) {
		set("clientName", clientName);
	}
	
	public java.lang.String getClientName() {
		return getStr("clientName");
	}

	public void setLeadNum(java.lang.String leadNum) {
		set("leadNum", leadNum);
	}
	
	public java.lang.String getLeadNum() {
		return getStr("leadNum");
	}

	public void setLeadLocation(java.lang.String leadLocation) {
		set("leadLocation", leadLocation);
	}
	
	public java.lang.String getLeadLocation() {
		return getStr("leadLocation");
	}

	public void setLeadDesc(java.lang.String leadDesc) {
		set("leadDesc", leadDesc);
	}
	
	public java.lang.String getLeadDesc() {
		return getStr("leadDesc");
	}

	public void setLeadOwner(java.lang.String leadOwner) {
		set("leadOwner", leadOwner);
	}
	
	public java.lang.String getLeadOwner() {
		return getStr("leadOwner");
	}

	public void setLeadPhase(java.lang.String leadPhase) {
		set("leadPhase", leadPhase);
	}
	
	public java.lang.String getLeadPhase() {
		return getStr("leadPhase");
	}

	public void setLeadAmt(java.math.BigDecimal leadAmt) {
		set("leadAmt", leadAmt);
	}
	
	public java.math.BigDecimal getLeadAmt() {
		return get("leadAmt");
	}

	public void setWinRate(java.math.BigDecimal winRate) {
		set("winRate", winRate);
	}
	
	public java.math.BigDecimal getWinRate() {
		return get("winRate");
	}

	public void setTargetDate(java.lang.String targetDate) {
		set("targetDate", targetDate);
	}
	
	public java.lang.String getTargetDate() {
		return getStr("targetDate");
	}

}
