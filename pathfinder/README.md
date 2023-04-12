# A4 - Urbanism

  * Author: < Jeffery Liu >
  * Email: < liu1121@mcmaster.ca >

## Rationale and Explanations

The pathfinder sub project contains an ADT that represents nodes and edges, an interface to define the methods needed to implement a pathfinding algorithm based on the nodes and edges,
and an implementation of the interface. The nodes contain information about their coordinates and their private ID as reference for the edges and graph. The edges contain information
about the two nodes that it connects and the weight of the edge. Both nodes and edges have attributes that can be added to them according to an interface. The graph ADT combines both
nodes and edges into a graph, and also contains the pathfinder algorithm. 

<br>

By using an interface, a framework for how the pathfinder algorithm can be upheld in the event that the nature
of the graph changes. Additionally, extending the library with a new algorithm encapsulates some of the complexity and prevents the graph ADT from becoming a god class.

## Usage and extension

To use the library, create a graph with x and y bounds. Nodes should contain information about their x and y coordinates, along with a unique ID. Edges should contain the two nodes that
they connect, and a weight if required. The weight of the edge is calculated by the distance between two nodes by default.

<br>

Attributes and different pathfinding algorithms can be added as desired, given that they implement their respective interfaces. 
