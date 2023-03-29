# Assignment A2: Mesh Generator

  - Michael Shi [shim34@mcmaster.ca]
  - Johnny Qu [quj22@mcmaster.ca]
  - Jeffery Liu [liu1121@mcmaster.ca]

## How to run the product

Note that the generator.jar and visualizer.jar files are in their own folders. You will have to navigate between the two and use appropriate filepaths for the command line arguments.

To run the generator.jar file (given that you are in the generator subfolder):
````
java -jar generator.jar -t meshType -r relaxationCycles -seed rngSeed -f filepath
````
All fields except ````-f filepath ```` are optional. meshType is either "grid" or "irregular", relaxationCycles is an integer specifying the number of relaxation cycles
to perform, rngSeed is an integer if replicable analysis is desired, and filepath is the destination filepath of the mesh file. For example:

````
java -jar generator.jar -t irregular -r 100 -f sample.mesh
````
would generate an irregular mesh with 100 relaxation cycles, with the mesh file saved to sample.mesh.

To run the visualizer.jar file (given that you are in the visualizer subfolder):

````
java -jar visualizer.jar meshFilepath svgFilepath -X
````
-X is the only optional field, and it is a debug flag that changes the colours of the generated vertices, segments, and neighbour relationships for readability. meshFilepath is
the filepath of the mesh file that should be used for the SVG generation, and svgFilepath is the destination filepath of the SVG file. For example:

````
java -jar visualizer.jar ../generator/sample.mesh sample.svg
````
would generate an SVG file from the sample.mesh file in the generator subfolder, with sample.svg as the output.

### Installation instructions

This product is handled by Maven, as a multi-module project. We assume here that you have cloned the project in a directory named `A2`

To install the different tooling on your computer, simply run:

```
mosser@azrael A2 % mvn install
```

After installation, you'll find an application named `generator.jar` in the `generator` directory, and a file named `visualizer.jar` in the `visualizer` one. 

### Generator

To run the generator, go to the `generator` directory, and use `java -jar` to run the product. The product takes one single argument (so far), the name of the file where the generated mesh will be stored as binary.

```
mosser@azrael A2 % cd generator 
mosser@azrael generator % java -jar generator.jar sample.mesh
mosser@azrael generator % ls -lh sample.mesh
-rw-r--r--  1 mosser  staff    29K 29 Jan 10:52 sample.mesh
mosser@azrael generator % 
```

### Visualizer

To visualize an existing mesh, go the the `visualizer` directory, and use `java -jar` to run the product. The product take two arguments (so far): the file containing the mesh, and the name of the file to store the visualization (as an SVG image).

```
mosser@azrael A2 % cd visualizer 
mosser@azrael visualizer % java -jar visualizer.jar -i ../generator/sample.mesh -o sample.svg -x

... (lots of debug information printed to stdout) ...

mosser@azrael visualizer % ls -lh sample.svg
-rw-r--r--  1 mosser  staff    56K 29 Jan 10:53 sample.svg
mosser@azrael visualizer %
```
To viualize the SVG file:

  - Open it with a web browser
  - Convert it into something else with tool slike `rsvg-convert`

## Island Generator

Required options: 
-i
-o


## How to contribute to the project

When you develop features and enrich the product, remember that you have first to `package` (as in `mvn package`) it so that the `jar` file is re-generated by maven.

## Backlog

### Definition of Done

A feautre is considered completed when it has completed at least three tests. Each test should be unique so that the feature is tested in a holistic way.
For example, each test should input different parameters to the program or handle an edge case.

### Feature Backlog

### A2 Features
| Id  | Feature title                                          | Who?        | Start      | End        | Status |
| :-: | ------------------------------------------------------ | ----------- | ---------- | ---------- | ------ |
| F01 | Draw segments between vertices                         | @johnny-q   | 02/03/2023 | 02/24/2023 | D      |
| F02 | Create Mesh ADT                                        | @johnny-q   | 02/24/2023 | 02/24/2023 | D      |
| F03 | Add debug mode for segments and verticies              | @liujeffery | 02/25/2023 | 02/27/2023 | D      |
| F04 | Generate relaxed irregular meshes via Llyod Relaxation | @icecapped  | 02/26/2023 | 02/27/2023 | D      |
| F05 | Order segments via Convex Hull                         | @icecapped  | 02/26/2023 | 02/27/2023 | D      |
| F06 | Add colour and thickness properties                    | @liujeffery | 02/26/2023 | 02/27/2023 | D      |
| F07 | Produce full meshes (compute neighbour information)    | @johnny-q   | 02/26/2023 | 02/27/2023 | D      |
| F08 | Change parameters via command line arguments           | @icecapped  | 02/27/2023 | 02/27/2023 | D      |
| B01 | Export mesh to wavefront obj file                      | @johnny-q   | 02/27/2023 | 02/27/2023 | D      |

### A3 Features
| Id  | Feature title                                                             | Who?    | Start    | End      | Status |
| :-: | ------------------------------------------------------------------------- | ------- | -------- | -------- | ------ |
| F01 | Take mesh as input via cmdline and generate basic water/land base island. | Michael | 23/03/13 | 23/03/26 | D      |
| F02 | Generate mesh with internal lagoon                                        | Johnny  | 23/03/26 | 23/03/26 | D      |
| F03 | Generate mesh with beaches based on land proximity to water               | Johnny  | 23/03/26 | 23/03/26 | D      |
| F04 | Generate complex shapes for land                                          | Johnny  | 23/03/26 | 23/03/26 | D      |
| F05 | Generate mesh w/ elevations selectable via cmdline                        | Jeffery | 23/03/26 | 23/03/26 | D      |
| F06 | Generate mesh w/ lakes, max number selectable via cmdline                 | Jeffery | 23/03/26 | 23/03/26 | D      |
| F07 | Generate mesh w/ rivers, max number selectable via cmdline                | Johnny  | 23/03/26 |          | S      |
| F08 | Generate mesh w/ river merging behaviour                                  | Johnny  |          |          | B      |
| F09 | Generate mesh w/ aquifers, max number selectable via cmdline              | Jeffery | 23/03/26 | 23/03/26 | D      |
| F10 | Generate mesh w/ biomes based on temperature and moisture                 | Jeffery | 23/03/26 | 23/03/29 | D      |
| F11 | Generate mesh w/ biomes based on whittaker diagrams                       | Jeffery | 23/03/26 | 23/03/29 | D      |
| F12 | Generate reproducible mesh based on an input seed                         | Jeffery | 23/03/28 | 23/03/28 | D      |
| B01 | Generate resource information based on the mesh terrain                   |         |          |          | B      |
| B02 | Generate heatmaps of features                                             | Jeffery | 23/03/29 |          | S      |


## Appendix
### Backlog changes:
f04: transfer from Michael -> Johnny <br>
f02: transfer from Jeffery -> Johnny <br>
f05: transfer from Michael -> Jeffery <br>
f10: transfer from Michael -> Jeffery <br>
f11: transfer from Michael -> Jeffery <br>
f12: transfer from Johnny -> Michael <br>
f12: transfer from Michael -> Jeffery <br>