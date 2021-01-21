package de.cimeyclust.util;

import de.cimeyclust.CimeyCraft;

public class UsefulMethods
{
    private CimeyCraft plugin;

    public UsefulMethods(CimeyCraft plugin) {
        this.plugin = plugin;
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
