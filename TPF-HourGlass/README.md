# Hourglass

Hourglass 3D simulation modeled with two semispheres. 

To compile, run

```bash
$ mvn package
```

This will generate **.jar** file in the *target* folder.

To run, execute:

```bash
$ java -jar TPF-HourGlass-0.0.1-SNAPSHOT.jar R D S deltaT deltaT2 kn g end [time|flips] (maxParticles)
```

Where:
* R is the semisphere radius
* D defines particle's radio (between D/7 and D/5)
* S defines the hole's radius
* deltaT is the time step of the simulation
* deltaT2 is the time step for the animation or the data collection
* kn is normal constant
* g is gamma value for damping
* end will establish when the simulation finished. It can be seconds (if next parameter is 'time') or amount of flips (if next parameter is 'flips')
* (maxParticles) is OPTIONAL to specify maximum amount of particles in the container. If not specified, it will try to fill the maximun amount possible in the upper semisphere.

### Output

In stdout, the simulation will be printed in **xyz** format so that it can be visualized in OVITO or similar tool.

In stderr, measured time for each flip will be shown and also the average.

### Examples:

```bash
$ java -jar TPF-HourGlass-0.0.1-SNAPSHOT.jar 1 1 0.5 0.00001 0.02 1E4 10 10 time 5
```

```bash
$ java -jar TPF-HourGlass-0.0.1-SNAPSHOT.jar 1 1 0.5 0.00001 0.02 1E4 10 5 flips
```

**IMPORTANT** deltaT should be more or less 0.00001. Higher values will introduce errors in the integration method.
