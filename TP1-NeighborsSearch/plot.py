import matplotlib.pyplot as plt
import re
import sys

fig = plt.figure(figsize=(12, 12))
ax = plt.axes()

points_with_annotation = []

if len(sys.argv)<3:
    print("Missing files. Must run: python plot.py points_filename neighbors_filename")
    exit()

points_filename = sys.argv[1]
neighbors_filename = sys.argv[2]

f = open(points_filename, 'r')
particles_count = int(f.readline())
limit = float(f.readline())

arr = [ line[:-1].split() for line in f.readlines()]
points = {int(l[0]): plt.Circle((float(l[1]), float(l[2])), float(l[3]), color = 'b') for l in arr}

f = open(neighbors_filename, 'r')
arr = [ line[:-1].split() for line in f.readlines()]
neighbors = {int(l[0]): list(map(int,l[1:])) for l in arr}


plt.axis([0, limit, 0, limit])

for id, c in points.items():
    ax.add_artist(c)

cleaned_up = True
previous = None

def on_move(event):
    over_circle = False
    for id, circle in points.items():
        if circle.contains(event)[0]:
            circle.set_color('r')
            if neighbors.get(id):
                for n in neighbors.get(id):
                    points.get(n).set_color('r')
            over_circle = True
            previous = id
            cleaned_up = False
            plt.draw()

    if not over_circle:
        for _, n in points.items():
            n.set_color('b')
        plt.draw()


on_move_id = fig.canvas.mpl_connect('motion_notify_event', on_move)

plt.show()
