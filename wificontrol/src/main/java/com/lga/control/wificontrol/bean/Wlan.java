package com.lga.control.wificontrol.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.text.DecimalFormat;

public class Wlan {

	private int id;
	private String ssid;
	private String mac;
	private String encryption;
	private int rssi;
	private DecimalFormat format = new DecimalFormat("00");

    public Wlan() {}

    /**
     * @param id id
     * @param ssid ssid
     * @param mac mac
     * @param encryption encryption
     * @param rssi rssi
     */
    public Wlan(int id, String ssid, String mac, String encryption, int rssi) {
        this();
        this.id = id;
        this.ssid = ssid;
        this.mac = mac;
        this.encryption = encryption;
        this.rssi = rssi;
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
	 * @return the ssid
	 */
	public String getSsid() {
		return ssid;
	}
	/**
	 * @param ssid the ssid to set
	 */
	public void setSsid(String ssid) {
		this.ssid = ssid;
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
	 * @return the encryption
	 */
	public String getEncryption() {
		return encryption;
	}
    /**
     * @param encryption the encryption to set
     */
    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }
    /**
     * @param rssi the encryption to set
     */
    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
    /**
     * @return the rssi
     */
    public int getRssi() {
        return rssi;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || obj.getClass() != getClass()) {
            return false;
        }
        if(obj == this) {
            return true;
        }

        Wlan wlan = (Wlan) obj;

        return new EqualsBuilder()
//                .appendSuper(super.equals(obj))
                .append(mac, wlan.mac)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(mac).toHashCode();
    }

    @Override
	public String toString() {
		return String.format("%s. %s  %s  %s  %s", format.format(id), ssid, mac, encryption, rssi);
	}
}
