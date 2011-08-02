/**
 * Copyright 2011, Manuel Argüelles
 * 
 * This file is part of WSNS
 * 
 * WSNS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * WSNS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with WSNS.  If not, see <http://www.gnu.org/licenses/>.
 */

package ar.wsns.sensor;

/**
 * @author Manuel Argüelles
 */
public class WeatherVane extends Sensor {

    public static enum DIRECTION {N, NE, E, SE, S, SO, O, NO};
    private int currentDir;
    
    public WeatherVane() {
        currentDir = (int) (Math.random() * 7);
    }

    @Override
    public String readValue(int hour) {
        if (hour % 2 == 0) {
            currentDir = (int) (Math.random() * 7);
        }
        return String.valueOf(currentDir);
    }

    @Override
    public String getType() {
        return "V";
    }

    @Override
    public String toString() {
        return "Weather vane";
    }

}
