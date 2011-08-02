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

import java.util.ArrayList;
import ar.wsns.core.exception.*;

/**
 * @brief Represents a network composed by nodes and links
 * @author Manuel Argüelles
 */
public class Network {

    private ArrayList<Node> nodes;
    /* indicates if the network changed in the last run of the simulator */
    private boolean modified;

    /**
     * @brief Constructs a network
     */
    public Network() {
        nodes = new ArrayList<Node>();
        modified = false;
    }

    /**
     * @return True, if this network changed in the last run of the simulator;
     *         otherwise, false
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * @brief Sets if this network changed in the last run of the simulator
     * @param mod 
     */
    public void setModified(boolean mod) {
        modified = mod;
    }

    /**
     * @brief Adds a node to this network
     * @param node Node to add
     * @throws DuplicatedNodeException 
     */
    public void addNode(Node node) throws DuplicatedNodeException {
        if (nodes.contains(node))
            throw new DuplicatedNodeException();
        else {
            nodes.add(node);
            modified = true;
        }
    }

    /**
     * @return The base station of this network
     */
    public BaseStation getBaseStation() {
        for (Node n: nodes)
            if (n.getClass().getName().equals("ar.wsns.core.BaseStation"))
                return (BaseStation) n;

        return null;
    }

    /**
     * @brief Removes a node that is part of this network
     * @param node Node to remove
     * @throws NodeNotFoundException 
     */
    public void removeNode(Node node) throws NodeNotFoundException {
        /* disconnects all the links */
        if (nodes.contains(node)) {
            Station station = (Station) node;
            for (Link link: station.getLinks()) {
                if (link != null)
                    removeLink(station, link.getNodeTo());
            }
            nodes.remove(station);
            modified = true;
        }
        else
            throw new NodeNotFoundException();
    }
    
    /**
     * @return All the nodes that are part of this network
     */
    public ArrayList<Node> getNodes() {
        return nodes;
    }

    /**
     * @brief Gets a node from its ID
     * @param id Node ID
     * @return The node that match the ID specified in the argument
     * @throws NodeNotFoundException 
     */
    public Node getNodeFromID(int id) throws NodeNotFoundException {
        for (Node node: nodes) {
            if (node.getID() == id)
                return node;
        }
        /* if node not found */
        throw new NodeNotFoundException();
    }

    /**
     * @brief Connects two nodes with the specified link
     * @param nodeFrom Source node
     * @param nodeTo Destiny node
     * @param bw Link bandwidth
     * @param dist Link distance
     * @throws DuplicatedLinkException
     * @throws NodeFullLinksException 
     */
    public void addLink(Node nodeFrom, Node nodeTo, int bw, int dist) throws DuplicatedLinkException,
                                                                             NodeFullLinksException {
        if (nodeFrom.isConnectedTo(nodeTo))
            throw new DuplicatedLinkException();
        try {
            nodeFrom.addLink(new Link(nodeTo, bw, dist));
            nodeTo.addLink(new Link(nodeFrom, bw, dist));
            modified = true;
        } catch (NodeFullLinksException ex) {
            /* clean stuff - remove the links created */
            nodeFrom.removeLinkTo(nodeTo);
            nodeTo.removeLinkTo(nodeFrom);
            throw new NodeFullLinksException();
        }
    }

    /**
     * @brief Diconnects two nodes
     * @param nodeFrom Source node
     * @param nodeTo Destiny node
     */
    public void removeLink(Node nodeFrom, Node nodeTo) {
        if (nodeFrom.isConnectedTo(nodeTo)) {
            nodeFrom.removeLinkTo(nodeTo);
            nodeTo.removeLinkTo(nodeFrom);
            modified = true;
        }
    }

    /**
     * @brief Modifies a link that connect two nodes
     * @param nodeFrom Source node
     * @param nodeTo Destiny node
     * @param bw New bandwidth
     * @param dist New distance
     * @throws LinkNotFoundException 
     */
    public void modifyLink(Node nodeFrom, Node nodeTo, int bw, int dist)
                                                throws LinkNotFoundException {
        if (nodeFrom.isConnectedTo(nodeTo)) {
            Link link = nodeFrom.getLinkTo(nodeTo);
            link.setBandWidth(bw);
            link.setDistance(dist);
            link = nodeTo.getLinkTo(nodeFrom);
            link.setBandWidth(bw);
            link.setDistance(dist);
            modified = true;
        }
        else throw new LinkNotFoundException();
    }
}