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

import ar.wsns.dijkstra.Dijkstra;
import java.util.HashMap;

/**
 * @brief Represents a router that perform the Dijkstra algorithm over the network
 * @author Manuel Argüelles
 */
public class Router {

    private HashMap<Node, Node> routeTable;
    private static Dijkstra dijkstra;
    private Node node;

    /**
     * 
     * @param node
     * @param net 
     */
    public Router(Node node, Network net) {
        routeTable = new HashMap<Node, Node>();
        dijkstra = new Dijkstra(net);
        this.node = node;
    }

    /**
     * 
     * @return 
     */
    public HashMap<Node, Node> getRouteTable() {
        return routeTable;
    }

    /**
     * 
     * @param nodeTo
     * @return 
     */
    public Node getPath(Node nodeTo) {
        return routeTable.get(nodeTo);
    }

    /**
     * 
     */
    public void updateRouteTable() {
        routeTable = dijkstra.run(node);
    }
}
