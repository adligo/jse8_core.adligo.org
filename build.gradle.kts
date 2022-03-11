import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.adligo.kt.jse.core.build.GwtDeps

import org.adligo.kt.jse.core.build.BytesDeps
import org.adligo.kt.jse.core.build.CollectionsDeps
import org.adligo.kt.jse.core.build.CtxDeps
import org.adligo.kt.jse.core.build.I_BytesDeps
import org.adligo.kt.jse.core.build.I_CollectionsDeps
import org.adligo.kt.jse.core.build.I_CtxDeps
import org.adligo.kt.jse.core.build.I_Ctx4JseDeps
import org.adligo.kt.jse.core.build.I_GradleCallback
import org.adligo.kt.jse.core.build.I_PipeDeps
import org.adligo.kt.jse.core.build.I_Tests4jDeps
import org.adligo.kt.jse.core.build.I_ThreadsDeps
import org.adligo.kt.jse.core.build.I_Threads4JseDeps
import org.adligo.kt.jse.core.build.JUnit5Deps
import org.adligo.kt.jse.core.build.MockitoDeps
import org.adligo.kt.jse.core.build.MockitoExtDeps
import org.adligo.kt.jse.core.build.Tests4j4jjDeps

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

fun dependsOnJaxb(dhs: DependencyHandlerScope) {
  dhs.implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359") 
}

fun dependsOnTen(dhs: DependencyHandlerScope) {
  BytesDeps.dependsOnBytes( GradleBuildCallback(dhs))
  dhs.implementation(project("ten.adligo.org"))
}

fun dependsOnPipe(dhs: DependencyHandlerScope) {
   I_PipeDeps.dependsOnI_Pipe( GradleBuildCallback(dhs))
   dhs.implementation(project("pipe.adligo.org"))
}

fun dependsOnTests4j(dhs: DependencyHandlerScope) {
  I_Tests4jDeps.dependsOnI_Tests4j( GradleBuildCallback(dhs))
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
    BytesDeps.has( GradleBuildCallback(this))
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
    BytesDeps.dependsOnBytes( GradleBuildCallback(this))
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
    BytesDeps.testsHave( GradleBuildCallback(this))
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
    CollectionsDeps.has( GradleBuildCallback(this))
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
    CollectionsDeps.testsHave( GradleBuildCallback(this))
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
    CtxDeps.has( GradleBuildCallback(this))
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
    CtxDeps.dependsOnCtx( GradleBuildCallback(this))
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
    CtxDeps.testsHave( GradleBuildCallback(this))
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
    MockitoExtDeps.has( GradleBuildCallback(this))
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
    I_PipeDeps.dependsOnI_Pipe( GradleBuildCallback(this))
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
    BytesDeps.dependsOnBytes( GradleBuildCallback(this))
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
    Tests4j4jjDeps.dependsOnTests4j4jj( GradleBuildCallback(this))
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
    I_Tests4jDeps.dependsOnI_Tests4j( GradleBuildCallback(this))
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
    Tests4j4jjDeps.has( GradleBuildCallback(this))
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
    Tests4j4jjDeps.testsHave( GradleBuildCallback(this))
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
    MockitoDeps.dependsOnMockito( GradleBuildCallback(this))
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