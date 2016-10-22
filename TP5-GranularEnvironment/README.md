# Granular System

To compile, run

```bash
$ mvn package
```

This will generate **.jar** file in the *target* folder.

To run, execute:

```bash
$ java -jar TP5-GranularEnvironment-0.0.1-SNAPSHOT.jar L W D deltaT deltaT2 tf kn kt [open|closed] [ovito|energy|flow] (maxParticles)
```

Where:
* L is the container's height
* W is the container's width
* D is the container's opening. It also defines particle's radio (between D/7 and D/5)
* deltaT is the time step of the simulation
* deltaT2 is the time step for the animation or the data collection
* tf is the simulation's total time
* kn is normal constant
* kt is tangent constant
* [open|closed] if the container has an opening or not
* [ovito|energy|flow] for the option. 'ovito' for xyz animation output. 'energy' to see every deltaT2 total energy of system. 'flow' to have every deltaT2 particle's flow 1meter below opening.
* (maxParticles) is OPTIONAL to specify maximum amount of particles in the container

Example:

```bash
$ java -jar TP5-GranularEnvironment-0.0.1-SNAPSHOT.jar 5 2 1 0.00001 0.02 5 1E5 2E5 open energy 
```

