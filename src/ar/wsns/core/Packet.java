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

package ar.wsns.core;

/**
 * @brief Represents a packet that contains information and is transmitted between two nodes
 * @author Manuel Argüelles
 */
public class Packet {

    private Node nodeFrom;          // source node
    private Node nodeTo;            // destiny node
    private String data;            // packet information
    private int time;               // transmission time

    /**
     * @brief Constructs a packet
     * @param nodeFrom Source node
     * @param nodeTo Destiny node
     * @param time Transmission time
     * @param data Packet information
     */
    public Packet(Node nodeFrom, Node nodeTo, int time, String data) {
        this.nodeFrom = nodeFrom;
        this.nodeTo = nodeTo;
        this.data = data;
        this.time = time;
    }

    /**
     * @brief Constructs a packet without information
     * @param nodeFrom Source node
     * @param nodeTo Destiny node
     * @param time Transmission time
     */
    public Packet(Node nodeFrom, Node nodeTo, int time) {
        this(nodeFrom, nodeTo, time, "");
    }

    /**
     * @brief Gets the packet data
     * @return The packet data
     */
    public String getData() {
        return data;
    }

    /**
     * @brief Gets the destiny node
     * @return The destiny node
     */
    public Node getNodeFrom() {
        return nodeFrom;
    }

    /**
     * @brief Gets the source node
     * @return The source node
     */
    public Node getNodeTo() {
        return nodeTo;
    }

    /**
     * @brief Appends data at the end of the packet
     * @param newData Data to be appended
     */
    public void writeData(String newData) {
        data += newData;
    }

    /**
     * @brief Gets the transmission time of this packet
     * @return The transmission time of this packet
     */
    public int getTime() {
        return time;
    }
    
}
