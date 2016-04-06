#!/usr/bin/env bash

cd git-sonification-code
java -jar dist/GitVS.jar 5760e50293fca30ccfb99ca16b859a94da6b3cc0 2aa6c62dc58d47d0f94e600db962f8af8bd5db37 ../../training_repo/.git data/training_conflicts.dat
