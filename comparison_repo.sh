#!/usr/bin/env bash

cd git-sonification-code
java -jar dist/GitVS.jar dbcb138603f55fc9f234d2d3b2404a2bfc92e4d9 4c097329187412f75e80a657effa997e2656923f ../../storm/.git data/storm_conflicts_new.dat
