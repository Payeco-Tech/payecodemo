package com.payeco.util.http;

/**
 *  代理地址
	目标地址
	权重
	连接超时
	读取等待超时
	最大等待超时次数(超过后会暂停N个周期)
	超过等待最大次数暂停周期
 * @author luoyunqiu
 * @time 2016-09-12 14:31
 *
 */
public class HttpAddress {
	private String address;
	private String targetAddress;
	private int distributeWeight;
	private int weight;
	private int connectTimeout;
	private int readTimeout;
	private int maxerrortimes;
	private int errorTimes;
	private long errorCycle;
	private long startErrorTime;
	private long stopCycle;
	private int stopTimes;
	private long startStopTime;
	private long lastTryTime;
	private String sslVersion = "SSL"; //TSL OR SSL
	
	public synchronized boolean isAvailable() {
		if(stopTimes > 0){
			if(startStopTime == 0){
				startStopTime = System.currentTimeMillis();
			}
			long nexttime = this.startStopTime + stopTimes*stopCycle;
			if(System.currentTimeMillis()>nexttime){
				//can retry one time
				startStopTime = System.currentTimeMillis();
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
	
 	public synchronized void weightOneReduce(){
 		if(this.weight > 0)
			this.weight--;
	}
	public synchronized void weightReset(){
		if(this.weight <= 0){
			this.weight = this.distributeWeight;
		}
	}
	
	public synchronized void active(){
		stopTimes = 0;
		errorTimes = 0;
		startStopTime = 0;
		startErrorTime = 0;
	}
	public synchronized void stop(){
		if(stopTimes == 0){
			startStopTime = System.currentTimeMillis();
		}
		stopTimes++;
	}
	
	public synchronized void errorOne(){
		if(stopTimes > 0){
			stopTimes++;
		} else {
			if(startErrorTime == 0){
				startErrorTime = System.currentTimeMillis();
			}
			long nextResetErrorTime = startErrorTime + this.errorCycle;
			if(nextResetErrorTime >= System.currentTimeMillis()){
				errorTimes++;
			} else {
				errorTimes=0;
				startErrorTime = System.currentTimeMillis();
			}
			if(errorTimes > this.maxerrortimes){
				stopTimes++;
			}
		}
	}
	
	public long getErrorCycle() {
		return errorCycle;
	}
	public void setErrorCycle(long errorCycle) {
		this.errorCycle = errorCycle;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTargetAddress() {
		return targetAddress;
	}
	public void setTargetAddress(String targetAddress) {
		this.targetAddress = targetAddress;
	}
	public int getDistributeWeight() {
		return distributeWeight;
	}
	public void setDistributeWeight(int distributeWeight) {
		this.distributeWeight = distributeWeight;
		this.weight = distributeWeight;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getConnectTimeout() {
		return connectTimeout;
	}
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	public int getReadTimeout() {
		return readTimeout;
	}
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	public int getMaxerrortimes() {
		return maxerrortimes;
	}
	public void setMaxerrortimes(int maxerrortimes) {
		this.maxerrortimes = maxerrortimes;
	}
	public int getErrorTimes() {
		return errorTimes;
	}
	public void setErrorTimes(int errorTimes) {
		this.errorTimes = errorTimes;
	}
	public long getStopCycle() {
		return stopCycle;
	}
	public void setStopCycle(long stopCycle) {
		this.stopCycle = stopCycle;
	}
	public int getStopTimes() {
		return stopTimes;
	}
	public void setStopTimes(int stopTimes) {
		this.stopTimes = stopTimes;
	}
	public long getLastTryTime() {
		return lastTryTime;
	}
	public void setLastTryTime(long lastTryTime) {
		this.lastTryTime = lastTryTime;
	}
	public String getSslVersion() {
		return sslVersion;
	}
	public void setSslVersion(String sslVersion) {
		this.sslVersion = sslVersion;
	}
	
}
