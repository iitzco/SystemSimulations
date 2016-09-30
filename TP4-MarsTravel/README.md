# Travel to Mars!

## Calculate when to launch spaceship to arrive safe at Mars

## How to run

#### Compile

In order to compile, run

```bash
$ mvn package
```

#### To generate xyz output

```bash
$ java -jar (target_file.jar) 0 [dT] [totalSimulation] [[launchSpeed (km/s)]] [dT2] [launchTime] [[launchAngle (degrees)]] [[BackOrForth]]
```

#### To calculate best launch time

With this option, you are going to be able to perform several simulations of launches and for each see the minimun distance the ship gets to Mars.

```bash
$ java -jar (target_file.jar) 1 [dT] [totalSimulation] [[launchSpeed (km/s)]] [from] [to] [step] [[arriveDistance (m)]] [[launchAngle (degrees)]] [[BackOrForth]]
```

For example, if you run:

```bash
$ java -jar target/TP4-MarsTravel-0.0.1-SNAPSHOT.jar 1 360-S 3-Y 10 0-M 2-Y 1-M 1000000000 90 b 
```

Mean that it will simulate one launch from Mars (90° degrees) each month (step equals to 1-M) for 2 years (see from and to). It will inform per each launch the minimum distance, except if it arrives (that is if the ship gets closer to Mars than arriveDistance). Launch speed will be 10 km/s.


##### IMPORTANT

* Parameters sorrounded with '[]' must recieve an input with format x-X, where x is a number and X can be S, H, D, W, M or Y (which correspond to Seconds, Hours, Days, Weeks, Months, Years)
* BackOrForth means if launch is from Earth or from Mars. Parameter should be 'f' (from Earth) or 'b' (from Mars)
* launchAngle must be a number bewteen 0 and 360. It represents the angle the ship will launch. 0° is perpendicular to the alignment with the sun (in the direction of speed). 90° is facing the sun. 270° is facing outer space.
* spaceship is always 1500 km from the surface in the position where the 1500 km vector is perpendicular to lauching vector.
* in perspective, if looking at launch from surface in a 2D world, launch should be always to the left. 

