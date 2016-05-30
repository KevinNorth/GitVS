#!/usr/bin/env bash

cd git-sonification-code
java -jar dist/GitVS.jar b930e100a9f74e832fd9142d0c825d9f24d0af1d d448e34812e5cbffe848ce88189a05df1be3226e ../../Comparison-Repository/.git data/storm_conflicts.dat --no-sound
