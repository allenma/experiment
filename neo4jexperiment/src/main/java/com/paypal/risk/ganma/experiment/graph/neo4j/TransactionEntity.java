package com.paypal.risk.ganma.experiment.graph.neo4j;


public class TransactionEntity {
	private long transactionID;
	private long senderID;
	private long receiverID;
	private long timestamp;
	private int ammount;

	public TransactionEntity() {
	}

	public long getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(long transactionID) {
		this.transactionID = transactionID;
	}

	public long getSenderID() {
		return senderID;
	}

	public void setSenderID(long senderID) {
		this.senderID = senderID;
	}

	public long getReceiverID() {
		return receiverID;
	}

	public void setReceiverID(long receiverID) {
		this.receiverID = receiverID;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getAmmount() {
		return ammount;
	}

	public void setAmmount(int ammount) {
		this.ammount = ammount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (transactionID ^ (transactionID >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransactionEntity other = (TransactionEntity) obj;
		if (transactionID != other.transactionID)
			return false;
		return true;
	}

}
