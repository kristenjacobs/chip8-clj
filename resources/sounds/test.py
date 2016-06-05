#!/usr/bin/python

import os
import fileinput

for sound in fileinput.input():
    print(sound),
    os.system('paplay ' + sound)
    os.system('sleep 1')
