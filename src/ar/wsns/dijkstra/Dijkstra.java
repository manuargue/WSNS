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

package ar.wsns.dijkstra;

import ar.wsns.core.Link;
import ar.wsns.core.Network;
import ar.wsns.core.Node;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * @brief Implements the Dijkstra algorithm with a priority queue
 * @author Manuel Argüelles
 */
public class Dijkstra {

    /**
     * @brief Implements a priority queue containing Node objects
     */
    private static class NodePriorityQueue extends PriorityQueue<Node> {

        public NodePriorityQueue() {
            super();
        }

        /**
         * @brief Updates the position of a particular node
         * @param node Node to update
         */
        public void updateNode(Node node) {
            remove(node);
            add(node);
        }

    }

    public static int INFINITY = 999999;                 // represents infinity
    private Network network;                             // network to analyze
    private NodePriorityQueue queue;

    /**
     * @brief Constructor
     * @param network Network to analyze
     */
    public Dijkstra(Network network) {
        this.network = network;
        queue = new NodePriorityQueue();
    }
    
    /**
     * @brief Implements Dijkstra algorithm with a priority queue
     * @param startNode Initial node to start the algorithm
     * @return A HashMap containing all the nodes of the network as keys, and
     * the first node of the path to each node as values.
     */
    public HashMap<Node, Node> run(Node startNode) {
        initialize(startNode);
        Node node = null;
        Node adjNode = null;
        int possiblePathCost = -1;

        while (!queue.isEmpty()) {
            node = queue.remove();
            for (Link link: node.getLinks()) {
                if (link != null) {
                    adjNode = link.getNodeTo();

                    possiblePathCost = link.getCost() + node.getBaseNodeDistance();

                    if (possiblePathCost < adjNode.getBaseNodeDistance()) {
                        adjNode.setBaseNodeDistance(possiblePathCost);
                        adjNode.setPreviousNode(node);
                        queue.updateNode(adjNode);
                    }
                }
            }
        }
        return getHashMap(startNode);
    }

    /**
     * @brief Sets the initial conditions to execute the algorithm
     * @param startNode Initial node to start the algorithm
     */
    private void initialize(Node startNode) {
        /* distances = infinity, previous node = null */
        for (Node n: network.getNodes()) {
            n.setBaseNodeDistance(INFINITY);
            n.setPreviousNode(null);
        }
        startNode.setBaseNodeDistance(0);
        /* add nodes to the priority queue */
        queue.addAll(network.getNodes());
    }

    /**
     * @brief Generates the route table
     * @param startNode Initial node to start the algorithm
     * @return A HashMap containing all the nodes of the network as keys, and
     *         the first node of the path to each node as values.
     */
    private HashMap<Node, Node> getHashMap(Node startNode) {
        HashMap<Node, Node> map = new HashMap<Node, Node>();
        Node prev;

        for (Node n: network.getNodes()) {
            if (n != startNode) {
                prev = n.getPreviousNode();
                if (prev == startNode)
                    map.put(n, n);
                else {
                    while (prev.getPreviousNode() != startNode)
                        prev = prev.getPreviousNode();
                    map.put(n, prev);
                }
            }
        }
        return map;
    }

    public void printDistances() {
        for (Node n: network.getNodes())
            System.out.println(n.getName() + " : " + n.getBaseNodeDistance());
    }

}
