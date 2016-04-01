#!/usr/bin/env bash

cd git-sonification-code
java -jar dist/GitVS.jar 2a42e63e7ea8ed06facf9b7e68671bcd2345c8a6 d7153c957785c8236b43030385e479b6f788e897 ../../voldemort/.git data/voldemort_conflicts.dat
