package de.roman.meter;

public enum Units
{
	KWH {
	    public String toString() {
	        return "kWh";
	    }
	},
	
	LITER {
	    public String toString() {
	        return "l";
	    }
	},
	 
	CUBIC {
	    public String toString() {
	        return "m3";
	    }
	}
}
