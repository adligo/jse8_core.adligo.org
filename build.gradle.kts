import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.adligo.kt.jse.core.build.GwtDeps

import org.adligo.kt.jse.core.build.I_BytesDeps
import org.adligo.kt.jse.core.build.I_CollectionsDeps
import org.adligo.kt.jse.core.build.I_CtxDeps
import org.adligo.kt.jse.core.build.I_Ctx4JseDeps
import org.adligo.kt.jse.core.build.I_GradleCallback
import org.adligo.kt.jse.core.build.I_ThreadsDeps
import org.adligo.kt.jse.core.build.I_Threads4JseDeps

import org.gradle.api.artifacts.Dependency
import org.gradle.api.Project

import org.gradle.plugins.ide.eclipse.model.Container
import org.gradle.plugins.ide.eclipse.model.Classpath
import org.gradle.plugins.ide.eclipse.model.EclipseModel
import org.gradle.plugins.ide.eclipse.model.ProjectDependency

println("this is " + this)
println("entry is \n" + this.toString())
println("${this::class.qualifiedName}") 
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
  `kotlin-dsl`
}

dependencies {
}

class GradleBuildCallback(val dhs: DependencyHandlerScope) : I_GradleCallback {
  override fun implementation(dependencyNotation: String) {
    dhs.implementation(dependencyNotation)
  }
  override fun implementation(dependency: Dependency) {
    dhs.implementation(dependency)
  }
  override fun implementation(project: Project) {
    dhs.implementation(project)
  }
  override fun projectFun(projectName: String): Project {
    return project(projectName)
  }
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(8))
  }
}
fun dependsOnBytes(dhs: DependencyHandlerScope) {
  I_BytesDeps.dependsOnI_Bytes( GradleBuildCallback(dhs))
  dhs.implementation(project("bytes.adligo.org"))
}
 
fun dependsOnCollections(dhs: DependencyHandlerScope) {
   I_CollectionsDeps.dependsOnI_Collections( GradleBuildCallback(dhs))
   dhs.implementation(project("collections.adligo.org"))
}

fun dependsOnCtx(dhs: DependencyHandlerScope) {
   I_Ctx4JseDeps.dependsOnI_Ctx4Jse( GradleBuildCallback(dhs))
   I_Threads4JseDeps.dependsOnI_Threads4Jse( GradleBuildCallback(dhs))
   dhs.implementation(project("ctx.adligo.org"))
}

fun dependsOnI_Pipe(dhs: DependencyHandlerScope) {
  dhs.implementation(project("i_pipe.adligo.org"))
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
  dependencies {
    I_BytesDeps.dependsOnI_Bytes( GradleBuildCallback(this))
  }
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
    GwtDeps.dependsOnGwt( GradleBuildCallback(this))
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

project(":collections.adligo.org") {
  allPlugins(this)
  dependencies {
    I_CollectionsDeps.dependsOnI_Collections( GradleBuildCallback(this))
  }
  eclipse { 
    onEclipse(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":collections_tests.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnCollections(this)
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

project(":ctx.adligo.org") {
  allPlugins(this)
  dependencies {
    I_Ctx4JseDeps.dependsOnI_Ctx4Jse( GradleBuildCallback(this))
    I_Threads4JseDeps.dependsOnI_Threads4Jse( GradleBuildCallback(this))
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
    GwtDeps.dependsOnGwt( GradleBuildCallback(this))
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

project(":i_collections.adligo.org") {
  allPlugins(this)
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

project(":i_bytes.adligo.org") {
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
    I_Ctx4JseDeps.has(GradleBuildCallback(this))
  }
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
    I_Threads4JseDeps.has( GradleBuildCallback(this))
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
    GwtDeps.dependsOnGwt( GradleBuildCallback(this))
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
    I_Threads4JseDeps.dependsOnI_Threads4Jse( GradleBuildCallback(this)) 
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




