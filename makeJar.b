#!/bin/bash

cd class
jar cfm quadrat.jar ../src/manifest.txt *.class res
mv quadrat.jar ../
cd ..
