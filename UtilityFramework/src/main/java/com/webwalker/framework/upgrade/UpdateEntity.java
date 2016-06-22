package com.webwalker.framework.upgrade;


/**
 * @author xujian
 * 
 */
public class UpdateEntity {
	public String status;
	public int versionCode;
	public int minVersionCode;
	public String versionName;
	public String versionInfo;
	public String updateTime;
	public String debugUrl;
	public String releaseUrl;
	public boolean isNeedUpdate;
	public boolean isMustUpdate;

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the versionCode
	 */
	public int getVersionCode() {
		return versionCode;
	}

	/**
	 * @param versionCode
	 *            the versionCode to set
	 */
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	/**
	 * @return the minVersionCode
	 */
	public int getMinVersionCode() {
		return minVersionCode;
	}

	/**
	 * @param minVersionCode
	 *            the minVersionCode to set
	 */
	public void setMinVersionCode(int minVersionCode) {
		this.minVersionCode = minVersionCode;
	}

	/**
	 * @return the versionName
	 */
	public String getVersionName() {
		return versionName;
	}

	/**
	 * @param versionName
	 *            the versionName to set
	 */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	/**
	 * @return the versionInfo
	 */
	public String getVersionInfo() {
		return versionInfo;
	}

	/**
	 * @param versionInfo
	 *            the versionInfo to set
	 */
	public void setVersionInfo(String versionInfo) {
		this.versionInfo = versionInfo;
	}

	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the debugUrl
	 */
	public String getDebugUrl() {
		return debugUrl;
	}

	/**
	 * @param debugUrl
	 *            the debugUrl to set
	 */
	public void setDebugUrl(String debugUrl) {
		this.debugUrl = debugUrl;
	}

	/**
	 * @return the releaseUrl
	 */
	public String getReleaseUrl() {
		return releaseUrl;
	}

	/**
	 * @param releaseUrl
	 *            the releaseUrl to set
	 */
	public void setReleaseUrl(String releaseUrl) {
		this.releaseUrl = releaseUrl;
	}

	/**
	 * @return the isNeedUpdate
	 */
	public boolean isNeedUpdate() {
		return isNeedUpdate;
	}

	/**
	 * @param isNeedUpdate
	 *            the isNeedUpdate to set
	 */
	public void setNeedUpdate(boolean isNeedUpdate) {
		this.isNeedUpdate = isNeedUpdate;
	}

	/**
	 * @return the isMustUpdate
	 */
	public boolean isMustUpdate() {
		return isMustUpdate;
	}

	/**
	 * @param isMustUpdate
	 *            the isMustUpdate to set
	 */
	public void setMustUpdate(boolean isMustUpdate) {
		this.isMustUpdate = isMustUpdate;
	}
}
