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

package dijkstratest;

import ar.wsns.core.BaseStation;
import ar.wsns.core.Station;
import ar.wsns.core.Network;
import ar.wsns.core.exception.DuplicatedLinkException;
import ar.wsns.core.exception.DuplicatedNodeException;
import ar.wsns.core.exception.NodeFullLinksException;
import ar.wsns.dijkstra.Dijkstra;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Manuel Argüelles
 */
public class DijkstraTest {
    
    public static Network net;

    public static void main(String[] args) {

        Station n1 = new Station("n1", net);
        Station n2 = new Station("n2", net);
        Station n3 = new Station("n3", net);
        Station n4 = new Station("n4", net);
        Station n5 = new Station("n5", net);
        BaseStation base = new BaseStation("base", net);

        net = new Network();

        try {
            //net.addNode(base);
            net.addNode(n1);
            net.addNode(n2);
            net.addNode(n3);
            net.addNode(n4);
            net.addNode(n5);

            //net.addLink(base, n1, 1, 20);
            //net.addLink(base, n2, 1, 10);
            //net.addLink(base, n5, 1, 5);
            net.addLink(n1, n5, 1, 2);
            net.addLink(n1, n3, 1, 8);
            net.addLink(n1, n4, 1, 8);
            net.addLink(n2, n3, 1, 1);
            net.addLink(n3, n4, 1, 5);
            net.addLink(n4, n5, 1, 3);
        }
        catch (NodeFullLinksException ex) {
            Logger.getLogger(DijkstraTest.class.getName()).log(Level.SEVERE, null, ex);
        }        catch (DuplicatedNodeException ex) {
            print("nodo duplicado");
        }
        catch (DuplicatedLinkException ex) {
            print("link duplicado");
        }

        // primero agrego la red
        Dijkstra dijkstra = new Dijkstra(net);

        // modifico la red agregando la estacion base
        try {
            net.addNode(base);
            net.addLink(base, n1, 1, 20);
            net.addLink(base, n2, 1, 10);
            net.addLink(base, n5, 1, 5);
        }
        catch (NodeFullLinksException ex) {
            Logger.getLogger(DijkstraTest.class.getName()).log(Level.SEVERE, null, ex);
        }        catch (DuplicatedNodeException ex) {
            print("nodo duplicado");
        }
        catch (DuplicatedLinkException ex) {
            print("link duplicado");
        }

        dijkstra.run(base);
        dijkstra.printDistances();
    }

    public static void print(Object obj) {
        System.out.println(obj);
    }
}
