package com.metis.common.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {
	
	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("session", "id", Session.class);
		arp.addMapping("t_z_client", "id", ZClient.class);
		arp.addMapping("t_z_client_call", "id", ZClientCall.class);
		arp.addMapping("t_z_client_contact", "id", ZClientContact.class);
		arp.addMapping("t_z_client_opp", "id", ZClientOpp.class);
		arp.addMapping("t_z_client_potential", "id", ZClientPotential.class);
		arp.addMapping("t_z_company", "id", ZCompany.class);
		arp.addMapping("t_z_department", "id", ZDepartment.class);
		arp.addMapping("t_z_lead_info", "id", ZLeadInfo.class);
		arp.addMapping("t_z_resource", "id", ZResource.class);
		arp.addMapping("t_z_user", "id", ZUser.class);
		arp.addMapping("z_t_survey", "id", ZTSurvey.class);
		arp.addMapping("z_t_survey_item", "id", ZTSurveyItem.class);
		arp.addMapping("z_t_survey_matrix", "id", ZTSurveyMatrix.class);
	}
}

