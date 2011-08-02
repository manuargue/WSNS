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

import ar.wsns.core.exception.NodeFullLinksException;
import ar.wsns.core.exception.NodeFullSensorsException;
import ar.wsns.sensor.Sensor;

/**
 * @brief Represents a weather station that is part of a network
 * @author Manuel Argüelles
 */
public final class Station extends Node {

    private Link[] links;
    private Sensor[] sensors;
    
    /**
     * @brief Constructs a station
     * @param name Station name
     * @param net Network to which this station belongs
     */
    public Station(String name, Network net) {
        super(name, net);
        links = new Link[4];
        sensors = new Sensor[4];
    }

    /**
     * @brief Constructs a station with default name
     * @param net Network to which this station belongs
     */
    public Station(Network net) {
        super(net);
        links = new Link[4];
        sensors = new Sensor[4];
    }

    /**
     * @brief Adds a link if possible
     * @param link Link to add
     * @throws NodeFullLinksException 
     */
    @Override
    public void addLink(Link link) throws NodeFullLinksException {
        int i;
        for (i = 0; i < links.length; i++)
            if (links[i] == null) {
                links[i] = link;
                return;
            }
        if (i == links.length)
            throw new NodeFullLinksException();
     }

    /**
     * @brief Removes a connection to the specified node, if it exists
     * @param nodeTo The other node of the link
     */
    @Override
    public void removeLinkTo(Node nodeTo) {
        for (int i = 0; i < links.length; i++)
            if (links[i] != null && links[i].getNodeTo() == nodeTo) {
                links[i] = null;
                return;
            }
    }

    /**
     * @brief Removes all the connection of this station
     */
    @Override
    public void removeAllLinks() {
        links = new Link[4];
    }
    
    /**
     * @brief Determines whether this station is connected to a specified node
     * @param nodeTo The other node of the link
     * @return True, if this station is connected to the other node; otherwise
     *         false
     */
    @Override
    public boolean isConnectedTo(Node nodeTo) {
        for (Link l: links)
            if (l != null && l.getNodeTo() == nodeTo)
                return true;
        return false;
    }

    /**
     * @brief Gets all the links connected to this station 
     * @return All the links connected to this station
     */
    @Override
    public Link[] getLinks() {
        return links;
    }

    /**
     * @brief Connects a sensor to this station
     * @param sensor Sensor to be connected
     * @throws NodeFullSensorsException 
     */
    public void addSensor(Sensor sensor) throws NodeFullSensorsException {
        int i;
        for (i = 0; i < sensors.length; i++)
            if (sensors[i] == null) {
                sensors[i] = sensor;
                return;
            }
        if (i == sensors.length)
            throw new NodeFullSensorsException(this.getName() + " has four sensors");
    }

    /**
     * @brief Removes a sensor connected to this station
     * @param sensor Sensor to be removed
     */
    public void removeSensor(Sensor sensor) {
        for (int i = 0; i < sensors.length; i++)
            if (sensors[i] == sensor) {
                sensors[i] = null;
                return;
            }
    }

    /**
     * @brief Removes a sensor connected to this station by its index number
     * @param index Index number of the sensor
     */
    public void removeSensor(int index) {
        if (index > -1 && index < 5)
            sensors[index] = null;
    }

    /**
     * @brief Gets all the sensors connected to this station
     * @return All the sensors connected to this station
     */
    public Sensor[] getSensors() {
        return sensors;
    }

    /**
     * @brief Determines the behavior of this station when it receives a packet
     * 
     * If this station is not active, returns null. If this station is not the
     * destination of the packet, forwards it to the destiny station. Otherwise, 
     * returns a new packet containing the sensors values.
     * 
     * @param packet Packet received
     * @return The packet response, if this station is active; otherwise null
     */
    public Packet handlePacket(Packet packet) {
        if (!isActive) {
            /* is not active, no reply */
            return null;
        }
        else if (packet.getNodeTo() == this) {
            /* return a new packet containing sensors values */
            System.out.println(name + " sends response packet to the Base station");
            return createPacket(packet.getTime());
        }
        else {
            /* forward the packet to the destiny station */
            return sendPacket(packet);
        }
    }

    /**
     * @brief Forwards a packet
     * @param packet Packet to send
     * @return The packet responded by another station
     */
    public Packet sendPacket(Packet packet) {
        Station est = (Station) router.getPath(packet.getNodeTo());
        System.out.println(name + " forwards packet to " + est.getName());

        return est.handlePacket(packet);
    }

    /**
     * @brief Creates a packet containing the sensors values
     * 
     * The packet format is:
     *      "Txxxx/Txxxx/ ..."
     * where "T" is a character that represents the sensor type, "xxxx" is the
     * sensor value and "/" is a separator.
     */
    private Packet createPacket(int packetHour) {
        Packet packet = new Packet(this, null, packetHour);
        for (Sensor sensor: sensors) {
            if (sensor != null) {
                packet.writeData(sensor.getType() +
                                 sensor.readValue(packetHour) + "/");
            }
        }
        return packet;
    }

    /**
     * @brief Gets all the nodes connected to this station
     * @return All the nodes connected to this station, if they exists
     */
    @Override
    public Node[] getNodesConnected() {
        Node[] nodes = new Node[4];

        for (int i = 0; i < links.length; i++) {
            if (links[i] != null)
                nodes[i] = links[i].getNodeTo();
        }

        return nodes;
    }

    /**
     * @brief Gets the link to the specified node
     * @param nodeTo The other node part of the link
     * @return The link to the specified node, if it exists
     */
    @Override
    public Link getLinkTo(Node nodeTo) {
        for (int i = 0; i < links.length; i++)
            if (links[i] != null && links[i].getNodeTo() == nodeTo)
                return links[i];
        return null;
    }

}
