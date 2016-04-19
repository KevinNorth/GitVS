#!/usr/bin/env bash

cd git-sonification-code
java -jar dist/GitVS.jar 946029f95ee7edd550f4175b354fa42a8033f76c 02420ab2e76d0d1aeda60a762ee3256ebf51bcfd ../../Your-Repository/.git data/voldemort_conflicts.dat --no-sound
