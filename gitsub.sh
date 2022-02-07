#!/bin/bash
#include <unistd.h>
#
# This checksout the sub projects, which are ignored
# These are in alphabetical order


#
#  This clones all of Adligo Inc's adligo.org projects underneath
# the folder where this script is run.
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

# Thanks https://medium.com/@Drew_Stokes/bash-argument-parsing-54f3b81a6a8f
while (( "$#" )); do
  case "$1" in
    -a | --async)   async="y" ; shift 1  ;;
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

function clone() {
  if [[ "$async" == "y" ]]; then
    clone_async $1 &
  else 
    git clone $1 
  fi
}

function clone_async() {
  #echo "cloning $1 async"
  git clone $1
  #echo "finished clone of $1"
}
clone git@github.com:adligo/artifactory_deploy.sh.adligo.org.git

clone git@github.com:adligo/bytes.adligo.org.git
clone git@github.com:adligo/bytes_gwt_examples.adligo.org.git
clone git@github.com:adligo/bytes_tests.adligo.org.git

clone git@github.com:adligo/ctx.adligo.org.git
clone git@github.com:adligo/ctx_gwt_examples.adligo.org.git
clone git@github.com:adligo/ctx_tests.adligo.org.git

clone git@github.com:adligo/eclipse.adligo.org.git

clone git@github.com:adligo/gradle_kt_examples.adligo.org.git

clone git@github.com:adligo/i_bytes.adligo.org.git
clone git@github.com:adligo/i_ctx.adligo.org.git
clone git@github.com:adligo/i_ctx4jse.adligo.org.git
clone git@github.com:adligo/i_pipe.adligo.org.git
clone git@github.com:adligo/i_tests4j.adligo.org.git
clone git@github.com:adligo/i_threads.adligo.org.git
clone git@github.com:adligo/i_threads4jse.adligo.org.git

clone git@github.com:adligo/mockito_ext.adligo.org.git

clone git@github.com:adligo/pipe.adligo.org.git
clone git@github.com:adligo/pipe_tests.adligo.org.git

clone git@github.com:adligo/ten.adligo.org.git
clone git@github.com:adligo/ten_gwt_examples.adligo.org.git
clone git@github.com:adligo/ten_tests.adligo.org.git

clone git@github.com:adligo/tests4j.adligo.org.git
clone git@github.com:adligo/tests4j_4mockito.adligo.org.git
clone git@github.com:adligo/tests4j4jj.adligo.org.git
clone git@github.com:adligo/tests4j4jj_tests.adligo.org.git
clone git@github.com:adligo/threads.adligo.org.git

wait
