# Pedestrian Dynamics

To compile, run

```bash
$ mvn package
```

This will generate **.jar** file in the *target* folder.

To run, execute:

```bash
$ java -jar TP5-GranularEnvironment-0.0.1-SNAPSHOT.jar L W D deltaT deltaT2 tf kn kt [ovito|escape|total] maxParticles desiredSpeed
```

Where:
* L is the container's height
* W is the container's width
* D is the container's opening. It also defines particle's radio (between D/7 and D/5)
* deltaT is the time step of the simulation
* deltaT2 is the time step for the animation (with 'escape' or 'total' option it will be ignored)
* tf is the simulation's total time
* kn is normal constant
* kt is tangent constant
* [ovito|escape|total] for the option. 'ovito' for xyz animation output. 'escape' to see the moments where an escape occured. 'total' to compute total escape time.
* maxParticles to specify maximum amount of particles in the container
* desiredSpeed to specify target speed of each person

Examples:

```bash
$ java -jar TP6-PedestrianDynamics-0.0.1-SNAPSHOT.jar 20 20 1.2 0.0001 0.02 5 1.2E5 2.4E5 ovito 200 6 > ovito.xyz
$ java -jar TP6-PedestrianDynamics-0.0.1-SNAPSHOT.jar 20 20 1.2 0.0001 0.02 5 1.2E5 2.4E5 escape 300 2
```

