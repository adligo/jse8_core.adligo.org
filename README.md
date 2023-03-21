# jse8-core.adligo.org

## To use this project from a Mac, GitBash or Linux;
- git clone git@github.com:adligo/jse8_core.adligo.org.git

Then you will need to upload a ssh key to github
Then download the other sub projects;
- cd jse8_core.adligo.org
- ./gitsub.sh

Alternatively to check out faster async structure.
- ./gitsub.sh -a -s

Finally build it with Java (I'm on 11) and Gradle (I'm on 7.3.3)
- gradle build --parallel
- ./moveTestXmlFiles.sh

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
mkdir $BUILD_NUMBER
cd  $BUILD_NUMBER
git clone https://github.com/adligo/jse8_core.adligo.org.git
cd  jse8_core.adligo.org
./gitsub.sh -a -s
./build.sh -j
```



