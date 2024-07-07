package fr.hydroxios.hxareas;

import java.util.UUID;

public class Area {

	private UUID uuid;
	private String name;
	private Vector3 corner1, corner2;
	
	
	public Area(String name, Vector3 corner1, Vector3 corner2) {
		this(UUID.randomUUID(), name, corner1, corner2);
	}
	
	public Area(UUID uuid, String name, Vector3 upperCorner, Vector3 bottomCorner) {
		this.uuid = uuid;
		this.name = name;
		this.corner1 = upperCorner;
		this.corner2 = bottomCorner;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Vector3 getCorner1() {
		return this.corner1;
	}
	
	public Vector3 getCorner2() {
		return this.corner2;
	}
	
}
