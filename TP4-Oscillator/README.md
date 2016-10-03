# Oscillator

## Compare different time-driven simulation integration methods applied to a dumped oscillator

## How to run

#### Compile

In order to compile, run

```bash
$ mvn package
```

#### To generate xyz output

```bash
$ java -jar (target_file.jar) 2 deltaT deltaT2
```

Where **deltaT** is the simulation time step and **deltaT2** is the animation time step (for example, 0.02 for 50 fps)

#### To get position values

```bash
$ java -jar (target_file.jar) 0 deltaT
```

Where **deltaT** is the simulation time step

#### To get errors

```bash
$ java -jar (target_file.jar) 1 deltaT
```

Where **deltaT** is the simulation time step
