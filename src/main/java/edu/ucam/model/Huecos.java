package edu.ucam.model;

import java.time.LocalDateTime;

public class Huecos {

	private int id;
	private int spaceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    
	public Huecos() {
	}


	public Huecos(int id, int spaceId, LocalDateTime startTime, LocalDateTime endTime) {
		this.id = id;
		this.spaceId = spaceId;
		this.startTime = startTime;
		this.endTime = endTime;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getSpaceId() {
		return spaceId;
	}


	public void setSpaceId(int spaceId) {
		this.spaceId = spaceId;
	}


	public LocalDateTime getStartTime() {
		return startTime;
	}


	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}


	public LocalDateTime getEndTime() {
		return endTime;
	}


	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
}
