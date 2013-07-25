package de.roman.meter;

/**
 * Enum the represents the possible units for a meter
 * 
 * @author Roman Schneider
 * 
 */
public enum Units
{
    KWH
    {
        public String toString()
        {
            return "kWh";
        }
    },

    LITER
    {
        public String toString()
        {
            return "l";
        }
    },

    CUBIC
    {
        public String toString()
        {
            return "m3";
        }
    }
}
