# CellularAutomata

To compile, run

```bash
$ mvn package
```

This will generate the file *TP2-CellularAutomata-0.0.1-SNAPSHOT.jar* in the *target* folder.

To run, execute:

```bash
$ java -jar TP2-CellularAutomata-0.0.1-SNAPSHOT.jar [L] [rc] [N] [it] [pert] [speed] [option]
```

Where:
* [L] represents the square length
* [rc] is the proximity radius
* [N] is the amount of particles
* [pert] is the perturbation
* [speed] represent all particles' speed
* [option] can be 
 * ovito: generate xyz output
 * polarization: calculate polarization index every 5 iterations
 * times: calculate mean polarizations among 10 independant simulations


