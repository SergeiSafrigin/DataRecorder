package kcg.datarecorder.location;

import java.io.Serializable;
import java.util.Comparator;

public class Point3d implements Serializable {
	private static final long serialVersionUID = 3981020914000750152L;
	
	public double x;
	public double y;
	public double z;

	public Point3d(double x, double y) {
		this.x = x;
		this.y = y;
		z = 0;
	}

	public Point3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point3d(Point3d point3d) {
		x = point3d.getLat();
		y = point3d.getLon();
		z = point3d.getAlt();
	}
	
	
	public static Comparator<Point3d> getComparatorByX() {
		return new Comparator<Point3d>() {
			@Override
			public int compare(Point3d p1, Point3d p2) {
				if (p1.x < p2.x)
					return -1;
				if (p1.x > p2.x)
					return 1;
				return 0;
			}		
		};
	}
	
	public boolean equals2d(Point3d point3d) {
		return x == point3d.x && y == point3d.y;
	}

	public double getAlt() {
		return z;
	}

	public double getLat() {
		return x;
	}

	public double getLon() {
		return y;
	}

	public void setAlt(double d) {
		z = d;
	}

	public void setLat(double d) {
		x = d;
	}

	public void setLon(double d) {
		y = d;
	}

	public String toString() {
		return "("+x+","+y+","+z+")";
	}
}
