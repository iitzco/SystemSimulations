import os
import sys

N = 10
L= 1.28
rc = 1
pert = range(0, 52, 2)
speed = 0.03
it = 1500

if len(sys.argv) == 2:
    print('Computing for {}'.format(sys.argv[1]))
    os.system("java -jar target/TP2-CellularAutomata-0.0.1-SNAPSHOT.jar {} {} {} {} {} {} times".format(L, rc, N, it, sys.argv[1], speed))
else:
    for p in pert:
        os.system("java -jar target/TP2-CellularAutomata-0.0.1-SNAPSHOT.jar {} {} {} {} {} {} times".format(L, rc, N, it, p/10, speed))
