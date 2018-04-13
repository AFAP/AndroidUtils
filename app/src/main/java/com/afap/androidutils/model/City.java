package com.afap.androidutils.model;

import java.io.Serializable;

/**
 * 城市
 */
public class City implements Serializable {
	private static final long serialVersionUID = -6174936682407995865L;
	private String code;
	private String name;
	private String pinyin;
	private String firstLetter;
	private String provinceCode;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getFirstLetter() {
		return firstLetter;
	}

	public void setFirshLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("code:").append(code).append("\n");
		sb.append("name:").append(name).append("\n");
		return sb.toString();
	}

}