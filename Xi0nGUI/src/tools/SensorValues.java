package tools;

import java.util.ArrayList;

public class SensorValues {
	private int size;
	private ArrayList<Float> values = new ArrayList<>();
	
	public SensorValues ( int size ) {
		this.size = size;
		for ( int i = 0; i < size; i ++ ) {
			values.add((Float)((float)0));
		}
	}
	
	public SensorValues ( int size, float initial_value ) {
		this.size = size;
		for ( int i = 0; i < size; i ++ ) {
			values.add((Float)((float)initial_value));
		}
	}
	
	public void add ( float value ) {
		for ( int i = 0; i < size-1; i++ ) {
			values.set ( i, values.get(i+1) );
		}
		values.set ( size - 1, value );
	}
	
	public int size () {
		return size;
	}
	
	public float get ( int i ) {
		return values.get(i);
	}
	
	public String toString () {
		return ( values.toString());
	}
}