package fr.hydroxios.hxareas;


public class Clipboard {
	
	private Vector3 pos1, pos2;
	
	public Clipboard() {}
	
	public void setPos1(Vector3 location) {
		this.pos1 = location;
	}
	
	public void setPos2(Vector3 location) {
		this.pos2 = location;
	}
	
	public Vector3 getPos1() {
		return this.pos1;
	}
	
	public Vector3 getPos2() {
		return this.pos2;
	}

}
