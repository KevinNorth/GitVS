#!/usr/bin/env bash

cd git-sonification-code
java -jar dist/GitVS.jar 96372fa7a3c5a87eef9a97acada44ea17deb9306 21f56e91efd4af4a8b2c54d6ba6b50b717f2aca7 ../../storm/.git data/storm_conflicts.dat
