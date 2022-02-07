import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.gradle.plugins.ide.eclipse.model.Container

import org.gradle.plugins.ide.eclipse.model.Classpath
import org.gradle.plugins.ide.eclipse.model.EclipseModel
import org.gradle.plugins.ide.eclipse.model.ProjectDependency

/**
 * This is a new fangled way to build Java using Kotlin :)
 * 
 * @author scott
 *         <pre>
 *         <code>
 * ---------------- Apache ICENSE-2.0 --------------------------
 *
 * Copyright 2022 Adligo Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </code>
 * 
 *         <pre>
 */
fun allPlugins(p: Project) {
  p.apply(plugin="java")
  p.apply(plugin="eclipse")
}

fun allRepos(r: RepositoryHandler) {
  r.mavenLocal()
  r.mavenCentral()
}
plugins {
  println("plugins is ")
  println(this)
  //allPlugins(this);
  eclipse
  java
}

dependencies {
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(8))
  }
}
fun dependsOnBytes(dhs: DependencyHandlerScope) {
   dhs.implementation(project("bytes.adligo.org"))
}
 
fun dependsOnCtx(dhs: DependencyHandlerScope) {
   dependsOnI_Ctx4Jse(dhs)
   dependsOnI_Threads4jse(dhs)
   dhs.implementation(project("ctx.adligo.org"))
}

fun dependsOnGwt(dhs: DependencyHandlerScope) {
  dhs.implementation("com.google.gwt:gwt-user:2.9.0")
  dhs.implementation("com.google.gwt:gwt-dev:2.9.0")
}

fun dependsOnI_Ctx(dhs: DependencyHandlerScope) {
   dhs.implementation(project("i_ctx.adligo.org"))
}

fun dependsOnI_Ctx4Jse(dhs: DependencyHandlerScope) {
   dependsOnI_Ctx(dhs)
   dhs.implementation(project("i_ctx4jse.adligo.org"))
}

fun dependsOnI_Pipe(dhs: DependencyHandlerScope) {
  dhs.implementation(project("i_pipe.adligo.org"))
}

fun dependsOnI_Threads(dhs: DependencyHandlerScope) {
  dhs.implementation(project("i_threads.adligo.org"))
}

fun dependsOnI_Threads4jse(dhs: DependencyHandlerScope) {
  dependsOnI_Threads(dhs)
  dhs.implementation(project("i_threads4jse.adligo.org"))
}

fun dependsOnI_Tests4j(dhs: DependencyHandlerScope) {
  dhs.implementation(project("i_tests4j.adligo.org"))
}

fun dependsOnJaxb(dhs: DependencyHandlerScope) {
  dhs.implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359") 
}

fun dependsOnTen(dhs: DependencyHandlerScope) {
  dependsOnBytes(dhs)
  dhs.implementation(project("ten.adligo.org"))
}

fun dependsOnTests4j4jj(dhs: DependencyHandlerScope) {
  dependsOnI_Tests4j(dhs)
  dependsOnJUnit5(dhs)
  dependsOnMockitoExt(dhs)
  dhs.implementation(project("tests4j4jj.adligo.org"))
}

fun dependsOnJUnit5(dhs: DependencyHandlerScope) {
  dhs.implementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
  dhs.implementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

fun dependsOnMockito(dhs: DependencyHandlerScope) {
   dhs.implementation("org.mockito:mockito-all:1.10.19")
}

fun dependsOnMockitoExt(dhs: DependencyHandlerScope) {
   dependsOnMockito(dhs)
   dhs.implementation(project(":mockito_ext.adligo.org"))
}

fun dependsOnPipe(dhs: DependencyHandlerScope) {
   dependsOnI_Pipe(dhs)
   dhs.implementation(project("pipe.adligo.org"))
}

fun dependsOnTests4j(dhs: DependencyHandlerScope) {
  dependsOnI_Tests4j(dhs)
  dependsOnJaxb(dhs)
  dhs.implementation(project("tests4j.adligo.org"))
}

fun javaSrc(ssc: SourceSetContainer) {
  ssc.main { java { srcDirs("src") } }
}

fun onEclipse(eclipse: EclipseModel) {
  eclipse.classpath.file {
    whenMerged { 
      onEclipseClasspathMerged(this as Classpath)
    }
  }
}

fun onEclipseClasspathMerged(classpath: Classpath) {
  classpath.entries.removeAll { entry -> 
     entry.kind == "con" && entry.toString().contains("JRE_CONTAINER")
  	 //println("entry is " + r + "\n" + entry.toString())
  	 //println("${entry::class.qualifiedName}")  
     //r 
  }
  classpath.entries.add(Container(
     "org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/jdk-8"))
  
  //println("${classpath.entries::class.qualifiedName}")  
}

fun testSrc(ssc: SourceSetContainer) {
  ssc.test { java { srcDirs("src") } }
}

project(":bytes.adligo.org") {
  allPlugins(this)
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":bytes_gwt_examples.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnBytes(this)
    dependsOnGwt(this)
  }
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":bytes_tests.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnBytes(this)
    dependsOnTests4j4jj(this)
  }
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":ctx.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnI_Ctx4Jse(this)
    dependsOnI_Threads4jse(this)
  }
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":ctx_gwt_examples.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnCtx(this)
    dependsOnGwt(this)
  }
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":ctx_tests.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnCtx(this)
    dependsOnTests4j4jj(this)
  }
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}


project(":i_ctx.adligo.org") {
  allPlugins(this)
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":i_ctx4jse.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnI_Ctx(this)
  }
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":i_ctx4jse.adligo.org") {
  allPlugins(this)
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":i_pipe.adligo.org") {
  allPlugins(this)
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":i_tests4j.adligo.org") {
  allPlugins(this)
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":i_threads.adligo.org") {
  allPlugins(this)
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":i_threads4jse.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnI_Threads(this)
  }
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}
project(":mockito_ext.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnMockito(this)
  }
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":pipe.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnI_Pipe(this)
  }
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":pipe_tests.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnPipe(this)
  }
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}


project(":ten.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnBytes(this)
  }
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":ten_gwt_examples.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnTen(this)
    dependsOnGwt(this)
  }
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":ten_tests.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnTen(this)
    dependsOnTests4j4jj(this)
  }
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":tests4j.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnI_Tests4j(this)
    dependsOnJaxb(this)
  }
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":tests4j4jj.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnI_Tests4j(this)
    dependsOnJUnit5(this)
    dependsOnMockitoExt(this)
  }
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":tests4j4jj_tests.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnTests4j4jj(this)
  }
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":tests4j_4mockito.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnTests4j(this) 
    //an old version of Mockito that uses jdk 1.5 byte code for Apache Beam
    implementation("org.mockito:mockito-all:1.10.19")
    implementation(project(":tests4j.adligo.org"))
  }
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":threads.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnI_Threads4jse(this) 
  }
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

repositories {
  mavenLocal()
  mavenCentral()
}

/**
I have found that the JAVA_HOME environment variable that is set when your run this task ;
    gradle cleanEclipse eclipse
is the one that is included in the Eclipse BuildPath
*/
tasks.register<GradleBuild>("ecp") {
    tasks = listOf("cleanEclipseClasspath", "eclipseClasspath")
}




