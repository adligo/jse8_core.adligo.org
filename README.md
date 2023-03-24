# jse8-core.adligo.org

## To use this project from a Mac, GitBash or Linux;
- git clone git@github.com:adligo/jse8_core.adligo.org.git

Then you MAY upload a ssh key to github
Then download the other sub projects with either ssl the default;
- cd jse8_core.adligo.org
- ./gitsub.sh
Or using https
- ./gitsub.sh -s

Alternatively to check out faster with the async structure.
- ./gitsub.sh -a 

Finally build it with Java (I'm on 11) and Gradle (I'm on 7.6.1)
- gradle build --parallel
- ./moveTestXmlFiles.sh

You can use this docker image if you want a copy of the entire working build server;
- adligo/jenkis_jdk11_gradle7.6.1:2023-03-21
- install it and add the JUnit plugin
- https://github.com/adligo/jse8_core.adligo.org/blob/main/jdk1.8_build.png

For a single threaded build with specific versions
- ./build.sh

Additionally you can setup the Jenkins Build;

## Jenkins on Liunx, Mac OS or Windows
Note Adligo uses the following project names (there MUST NOT be any spaces in the Jenkins project names);
- jse8_core

Both Builds consist of two simple steps;
1) Execute Shell
2) Post Build Action / Publish JUnit test result report 
```
-depot/tests/*.xml
```

## Parallel Build Execute Shell Step
```
if [ -d "depot" ]; then
  rm -fr depot
fi
export PATH=$PATH:/var/opt/gradle-7.6.1/bin
export GRADLE_HOME=/var/opt/gradle-7.6.1
mkdir depot
mkdir depot/tests
mkdir $BUILD_NUMBER
cd  $BUILD_NUMBER
git clone https://github.com/adligo/jse8_core.adligo.org.git
cd jse8_core.adligo.org
./gitsub.sh -a -s
gradle build --parallel
./moveTestXmlFiles.sh -j
```

## Single Threaded Build Execute Shell Stell
```
export PATH=$PATH:/var/opt/gradle-7.6.1/bin
export GRADLE_HOME=/var/opt/gradle-7.6.1
mkdir $BUILD_NUMBER
cd  $BUILD_NUMBER
git clone https://github.com/adligo/jse8_core.adligo.org.git
cd  jse8_core.adligo.org
./gitsub.sh -a -s
./build.sh -j
```



