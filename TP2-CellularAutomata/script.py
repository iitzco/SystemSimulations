import os
import sys

N = 40
L= 3.1
rc = 1
pert = range(0, 52, 2)
speed = 0.03
it = 1500

for p in pert:
    os.system("java -jar target/TP2-CellularAutomata-0.0.1-SNAPSHOT.jar {} {} {} {} {} {} times".format(L, rc, N, it, p/10, speed))
