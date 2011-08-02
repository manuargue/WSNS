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

/**
 * @brief Represents a node that is part of a network
 * @author Manuel Argüelles
 */
public abstract class Node implements Comparable<Node> {

    protected static int idCount = 0;           // ID counter

    protected String name;                      // node name
    protected boolean isActive;                 // state
    protected Router router;
    protected final int ID;
    /* for Dijkstra algorithm */
    protected int baseNodeDistance;
    protected Node previousNode;

    /**
     * @brief Constructs a node
     * @param name Node name
     * @param net Network to which the node belongs
     */
    public Node(String name, Network net) {
        this.name = name;
        router = new Router(this, net);
        isActive = true;
        baseNodeDistance = -1;
        previousNode = null;
        ID = idCount++;
    }
    
    /**
     * @brief Constructs a node with default name
     * @param net Network to which the node belongs
     */
    public Node(Network net) {
        this("Node", net);
        name += String.valueOf(ID);
    }

    /**
     * @brief Adds a link if possible
     * @param link Link to add
     * @throws NodeFullLinksException 
     */
    public abstract void addLink(Link link) throws NodeFullLinksException;

    /**
     * @brief Removes a connection to the specified node, if it exists
     * @param nodeTo The other node of the link
     */
    public abstract void removeLinkTo(Node nodeTo);

    /**
     * @brief Gets all the links connected to this node 
     * @return All the links connected to this node
     */
    public abstract Link[] getLinks();

    /**
     * @brief Gets the link to the specified node
     * @param nodeTo The other node part of the link
     * @return The link to the specified node, if it exists
     */
    public abstract Link getLinkTo(Node nodeTo);

    /**
     * @brief Removes all the connection of this node
     */
    public abstract void removeAllLinks();

    /**
     * @brief Determines whether this node is connected to a specified node
     * @param nodeTo The other node of the link
     * @return True, if this node is connected to the other node; otherwise false
     */
    public abstract boolean isConnectedTo(Node nodeTo);

    /**
     * @brief Gets all the nodes connected to this node
     * @return All the nodes connected to this node, if they exists
     */
    public abstract Node[] getNodesConnected();

    /**
     * @brief Determines if this node is enable
     * @return True, if this node is enable; otherwise false
     */
    public boolean isActive() { return isActive; }

    /**
     * @brief Enables or disables this node
     * @param isActive A boolean that determines the state of this node
     */
    public void setActive(boolean isActive) { this.isActive = isActive; }

    /**
     * @brief Gets the name of this node
     * @return The name of this node
     */
    public String getName() { return name; }

    /**
     * @brief Sets the name of this node
     * @param name The new name of this node
     */
    public void setName(String name) { this.name = name; }

    /**
     * @brief Gets the identification number of this node
     * @return The identification number of this node
     */
    public int getID() { return ID; }

    /**
     * @brief Gets the distance from this node to the base node, when running
     *        the Dijkstra algorithm
     * @return The distance from this node to the base node, when running the
     *         Dijkstra algorithm
     */
    public int getBaseNodeDistance() { return baseNodeDistance; }

    /**
     * @brief Sets the distance from this node to the base node of the Dijkstra
     *        algorithm
     * @param The distance from this node to the base node
     */
    public void setBaseNodeDistance(int baseNodeDistance) {
        this.baseNodeDistance = baseNodeDistance;
    }

    /**
     * @brief Gets the router instance
     * @return The router instance
     */
    public Router getRouter() { return router; }

    /**
     * @brief Gets the previous node of the path, when running the Dijkstra
     *        algorithm
     * @return The previous node of the path, when running the Dijkstra
     *         algorithm
     */
    public Node getPreviousNode() { return previousNode; }

    
    /**
     * @brief Sets the previous node of the path, when running the Dijkstra
     *        algorithm
     * @param The previous node of the path, when running the Dijkstra
     *         algorithm
     */
    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    /**
     * @brief Compares this with another node, based on the distance to the base
     *        node of the Dijkstra algorithm
     * @param node The other node
     * @return The value 0 if this node is equal to the argument node;
     *         a value less than 0 if this node is less than the argument node;
     *         and a value greater than 0 if this node is greater than the 
     *         argument node (signed comparison).
     */
    @Override
    public int compareTo(Node node) {
        Integer dist = new Integer(baseNodeDistance);
        return dist.compareTo(new Integer(node.getBaseNodeDistance()));
    }

}
