# Brownian Motion

To compile, run

```bash
$ mvn clean compile assembly:single
```

This will generate the file *tp3-1.0-SNAPSHOT-jar-with-dependencies.jar* in the *target* folder.

To run, execute:

```bash
$ java -jar tp3-1.0-SNAPSHOT-jar-with-dependencies.jar [N] [Sp] [Sc] [M] [option]
```

Where:
* [N] is the amount of small particles
* [Sp] represent all particles' speed
* [Sc] represent the amount of seconds of the simulation
* [M] represent the big particle's mass in grams (note that small particles have 0.1 g mass)
* [option] can be
 * 0: generate xyz output for visualization tool (ovito, for example). FPS=25
 * 1: show all intervals between crashes
 * 2: show average speed of each particle of the last third of the simulation
 * 3: calculate the big particle distance to center in 10 equidistant moments (10% of simulation interval)

IMPORTANT: make sure to redirect Standard Output to a file. In Standard Error (console or redirect with 2>) you will see the total system temperature.
