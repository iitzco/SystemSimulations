import os
import sys

N = 300
L= 25
rc = 1
pert = [0.1, 0.5, 1, 2, 4]
speed = 0.03
it = 3000

for p in pert:
    print("Calculating {}".format(p))
    os.system("java -jar target/TP2-CellularAutomata-0.0.1-SNAPSHOT.jar {} {} {} {} {} {} polarization > file{}.txt".format(L, rc, N, it, p, speed, p, p))

