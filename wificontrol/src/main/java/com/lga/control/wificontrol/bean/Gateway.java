package com.lga.control.wificontrol.bean;

import java.text.DecimalFormat;

public class Gateway {

	private int id;
	private String ip;
	private String mac;
	private String moduleID;
	private DecimalFormat format = new DecimalFormat("00");

	public Gateway() {}

	public Gateway(int id, String ip, String mac, String moduleID) {
		super();
		this.id = id;
		this.ip = ip;
		this.mac = mac;
		this.moduleID = moduleID;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the mac
	 */
	public String getMac() {
		return mac;
	}
	/**
	 * @param mac the mac to set
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}
	/**
	 * @return the moduleID
	 */
	public String getModuleID() {
		return moduleID;
	}
	/**
	 * @param moduleID the moduleID to set
	 */
	public void setModuleID(String moduleID) {
		this.moduleID = moduleID;
	}

	@Override
	public String toString() {
		return String.format("%s. %s  %s  %s", format.format(id), ip, mac, moduleID == null ? "" : moduleID);
	}
}
