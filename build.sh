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
    -b | --begin)   begin=$2 ; shift 2  ;;
    -j | --inJenkins)   inJenkins="y" ; shift 1  ;;
    -h | --help) help="y" ; shift 1 ;;
    -p | --pull) pull="y" ; shift 1 ;;
    -s | --skipTests) skipTests="y" ; shift 1 ;;
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
  echo "-b | --begin"
  echo "     Selects the starting project, using the previous build cache"
  echo "-j | --inJenkins"
  echo "     Moves the depot directory up so that it can be identified easier by Jenkins"
  echo "-s | --skipTests"
  echo "     Skips the tests and the compile / build of the test modules."
  echo "-p | --pull"
  echo "     Pulls from Git before running builds, failing if a pull has a conflict."  
fi

RED=RED
NC=
#RED='\033[0;31m'
#NC='\033[0m' # No Color

depotDir=$dir/depot
if [[ "$inJenkins" == "y" ]]; then
  echo "rasign the depot dir for Jenkis which can't find it's own $BUILD_NUMBER variable!"
  cd ../..
  upDir=`pwd`
  depotDir=$upDir/depot
  cd $dir
else 
  echo "Using the depot directory in the current dir."
fi

started="n"
if [[ -z $begin ]]; then
  started="y"
fi

function build_project() {  
  check_start()
  if [[ "$started" == "n" ]]; then
    return
  fi
  
  if [[ -d $dir/$1 ]]; then
    echo "" #the dir exists
  else
    echo -e "${RED} The directory $DIR/$1 does NOT Exist ${NC}"
    echo "Please run gitsub.sh first"
    exit 0
  fi
  cd $dir/$1
  if [[ $pull == "y" ]]; then
      echo pulling $1 from git
      status=$?
      git pull
      status=$?
      if [[ "0" != "$status" ]]; then
       echo -e "${RED} git pull Failed with exit code $status ${NC}"
       exit 0;
      fi
  fi
  
  echo building $1 with version $2  
    
  status=$?
  gradle publishToMavenLocal -Ptag=$2
  status=$?
  if [[ "0" != "$status" ]]; then
   echo -e "${RED} Build.sh Failed with exit code $status ${NC}"
   exit 0;
  fi
}

function check_start() {
  if [[ "$started" == "y" ]]; then
    echo "..."  
    
  elif  [[ "$begin" == "$1" ]]; then
    echo "starting on project $1"
    started="y"
  fi
}

function run_tests() {
  check_start()
  if [[ "$started" == "n" ]]; then
    return
  fi
  
  echo running tests in $1 
  cd $dir/$1
  status=$?
  gradle build
  status=$?
  if [[ "0" != "$status" ]]; then
   echo -e "${RED} Build.sh Failed with exit code $status ${NC}"
   exit 0;
  fi
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

build_project i_ctx.adligo.org v0_1+_SNAPSHOT
build_project i_ctx4jse.adligo.org v0_1+_SNAPSHOT
build_project i_tests4j.adligo.org v0_2+_SNAPSHOT
build_project i_pipe.adligo.org v0_4+_SNAPSHOT
build_project i_threads.adligo.org v0_1+_SNAPSHOT
build_project i_threads4jse.adligo.org v0_1+_SNAPSHOT

build_project ctx.adligo.org v0_1+_SNAPSHOT
run_tests ctx_tests.adligo.org v0_1+_SNAPSHOT

build_project gwt_ctx_example.adligo.org v0_1+_SNAPSHOT

build_project mockito_ext.adligo.org v0_1+_SNAPSHOT
build_project tests4j4jj.adligo.org v0_2+_SNAPSHOT
run_tests tests4j4jj_tests.adligo.org

build_project pipe.adligo.org v0_1+_SNAPSHOT
# run_tests pipe_tests.adligo.org 
