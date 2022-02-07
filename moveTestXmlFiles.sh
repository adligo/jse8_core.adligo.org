#!/bin/bash

#
#  This script runs Gradle builds in individual sub projects calling
# gradle in each module / sub project.
#
# --------------------- Apache License LICENSE-2.0 -------------------
#
#  Copyright 2022 Adligo Inc

#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.
#

dir=`pwd`

# Thanks https://medium.com/@Drew_Stokes/bash-argument-parsing-54f3b81a6a8f
while (( "$#" )); do
  case "$1" in
    -j | --inJenkins)   inJenkins="y" ; shift 1  ;;
    -h | --help) help="y" ; shift 1 ;;
    -*|--*=) # unsupported flags
      echo "Error: Unsupported flag $1" >&2
      exit 1
      ;;
    *) # preserve positional arguments
      PARAMS="$PARAMS $1"
      shift
      ;;
  esac
done

if [[ "$help" == "y" ]]; then
  echo "The following summarizes the command line flags that can be used with this script; "
  echo "This script builds the Adligo Inc's adligo.org code base using Gradle and bash. "
  echo "Also note this script is an alterantaive to the Fabricate Build System"
  echo "-j | --inJenkins"
  echo "     Moves the depot directory up so that it can be identified easier by Jenkins"
fi

RED=RED
NC=
#RED='\033[0;31m'
#NC='\033[0m' # No Color

depotDir=$dir/depot
if [[ "$inJenkins" == "y" ]]; then
  echo "creating the depot dir for Jenkis which can't find it's own $BUILD_NUMBER variable!"
  cd ..
  upDir=`pwd`
  depotDir=$upDir/depot
  cd $dir
else 
  echo "Using the depot directory in the current dir."
fi

function move_test_xml_files() {
  cd $dir/$1
  echo "in; "
  pwd
  echo "copying tests to $depotDir/tests "
  cp build/test-results/test/*.xml $depotDir/tests
}

# A Fabricate Style Depot :)
# Without the Fabricate Style Concurrency :(
if [[ -d $depotDir ]]; then
  rm -fr $depotDir
fi
mkdir $depotDir
mkdir $depotDir/tests

move_test_xml_files ctx_tests.adligo.org 
move_test_xml_files tests4j4jj_tests.adligo.org

# run_tests pipe_tests.adligo.org 
