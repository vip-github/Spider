package zzyq.bean;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value="index", noClassnameStored=true)
public class Index
{
	@Id
	private String id;
	
	private long timestamp;
	
	public Index() {
	}

	public Index(String id, long timestamp) {
		this.id = id;
		this.timestamp = timestamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
