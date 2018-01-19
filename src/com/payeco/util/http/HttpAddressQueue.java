package com.payeco.util.http;

import java.util.List;

public class HttpAddressQueue {
	public HttpAddressQueue(List<HttpAddress> addressList){
		this.addressList = addressList;
	}
	private List<HttpAddress> addressList;
	private long lastUseTime = System.currentTimeMillis();
	private int totalWeight = 0;
	private int weight = 0;
	
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public synchronized void weightOneReduce(){
		if(this.weight > 0)
			this.weight--;
	}
	public synchronized void weightReset(){
		if(this.weight <= 0){
			this.weight = this.totalWeight;
		}
	}
	public int getTotalWeight() {
		return totalWeight;
	}
	public synchronized void setTotalWeight(int totalWeight) {
		this.totalWeight = totalWeight;
		this.weight = totalWeight;
	}
	public List<HttpAddress> getAddressList() {
		return addressList;
	}
	public void setAddressList(List<HttpAddress> addressList) {
		this.addressList = addressList;
	}
	public long getLastUseTime() {
		return lastUseTime;
	}
	public void setLastUseTime(long lastUseTime) {
		this.lastUseTime = lastUseTime;
	}
	
}
