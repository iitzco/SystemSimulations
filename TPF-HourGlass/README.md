# Hourglass

Hourglass 3D simulation modeled with two semispheres. 

To compile, run

```bash
$ mvn package
```

This will generate **.jar** file in the *target* folder.

To run, execute:

```bash
$ java -jar TPF-HourGlass-0.0.1-SNAPSHOT.jar R D deltaT deltaT2 tf kn g (maxParticles)
```

Where:
* R is the semisphere radius
* D defines particle's radio (between D/7 and D/5)
* deltaT is the time step of the simulation
* deltaT2 is the time step for the animation or the data collection
* tf is the simulation's total time
* kn is normal constant
* g is gamma value for damping
* (maxParticles) is OPTIONAL to specify maximum amount of particles in the container.

Example:

```bash
$ java -jar TPF-HourGlass-0.0.1-SNAPSHOT.jar 1 0.5 0.00001 0.02 10 1E4 100 1
```

**IMPORTANT** deltaT should be more or less 0.00001. Higher values will introduce errors in the integration method.
