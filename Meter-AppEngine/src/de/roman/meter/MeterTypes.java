package de.roman.meter;

/**
 * Enum the represents the possible types for a meter
 * 
 * @author Roman Schneider
 * 
 */
public enum MeterTypes
{
    STROM
    {
        public String toString()
        {
            return "Strom";
        }
    },

    GAS
    {
        public String toString()
        {
            return "Gas";
        }
    },

    WASSER
    {
        public String toString()
        {
            return "Wasser";
        }
    },

    OEL
    {
        public String toString()
        {
            return "Öl";
        }
    }
}
