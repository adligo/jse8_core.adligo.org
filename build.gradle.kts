import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import java.util.function.Consumer

import org.adligo.kt.jse.core.build.GwtDeps

import org.adligo.kt.jse.core.build.BytesDeps
import org.adligo.kt.jse.core.build.CollectionsDeps
import org.adligo.kt.jse.core.build.CtxDeps
import org.adligo.kt.jse.core.build.JaxbDeps
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
import org.adligo.kt.jse.core.build.TenDeps
import org.adligo.kt.jse.core.build.Tests4jDeps
import org.adligo.kt.jse.core.build.Tests4j_4MockitoDeps
import org.adligo.kt.jse.core.build.Tests4j4jjDeps
import org.adligo.kt.jse.core.build.ThreadsDeps

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

fun projectTemplate(p: Project, cgc : Consumer<I_GradleCallback>) {
  allPlugins(p)
  //println("in project template with p " + p)
  p.dependencies {
    //println("in project template dependencies with this " + this)
    cgc.accept( GradleBuildCallback(this) )
    //println("called cgc ? " + cgc)
  }
  p.eclipse { 
    onEclipse(this)
  }
  p.repositories {
    allRepos(this)
  }

}

fun testSrc(ssc: SourceSetContainer) {
  ssc.test { java { srcDirs("src") } }
}

project(":bytes.adligo.org") {
  projectTemplate(this, { gc ->
      BytesDeps.has(gc) 
  })
}

project(":bytes_gwt_examples.adligo.org") {
  projectTemplate(this, { gc ->
      BytesDeps.gwtExamplesHave(gc) 
  })
}

project(":bytes_tests.adligo.org") {
  projectTemplate(this, { gc ->
      BytesDeps.testsHave(gc) 
  })
}

project(":collections.adligo.org") {
  projectTemplate(this, { gc ->
      CollectionsDeps.has(gc) 
  })
}

project(":collections_tests.adligo.org") {
  projectTemplate(this, { gc ->
      CollectionsDeps.testsHave(gc) 
  })
}

project(":ctx.adligo.org") {
  projectTemplate(this, { gc ->
      CtxDeps.has(gc) 
  })
}

project(":ctx_gwt_examples.adligo.org") {
  projectTemplate(this, { gc ->
      CtxDeps.gwtExamplesHave(gc) 
  })
}

project(":ctx_tests.adligo.org") {
  projectTemplate(this, { gc ->
      CtxDeps.testsHave(gc) 
  })
}

project(":i_collections.adligo.org") {
  projectTemplate(this, { gc -> 
     //do nothing
  })
}

project(":i_ctx.adligo.org") {
  projectTemplate(this, { gc -> 
     //do nothing
  })
}

project(":i_bytes.adligo.org") {
  projectTemplate(this, { gc -> 
     //do nothing
  })
}

project(":i_ctx4jse.adligo.org") {
  projectTemplate(this, { gc -> 
     I_Ctx4JseDeps.has(gc)
  })
}

project(":i_pipe.adligo.org") {
    projectTemplate(this, { gc -> 
     //do nothing
  })
}

project(":i_tests4j.adligo.org") {
  projectTemplate(this, { gc -> 
     //do nothing
  })
}

project(":i_threads.adligo.org") {
  projectTemplate(this, { gc -> 
     //do nothing
  })
}

project(":i_threads4jse.adligo.org") {
  projectTemplate(this, { gc -> 
     I_Threads4JseDeps.has( gc)
  })
}
project(":mockito_ext.adligo.org") {
  projectTemplate(this, { gc -> 
     MockitoExtDeps.has( gc)
  })
}

project(":pipe.adligo.org") {
  projectTemplate(this, { gc -> 
     I_PipeDeps.dependsOnI_Pipe( gc)
  })
}

project(":pipe_tests.adligo.org") {
  projectTemplate(this, { gc -> 
     //do nothing
  })
}


project(":ten.adligo.org") {
  projectTemplate(this, { gc -> 
     TenDeps.has(gc)
  })
}

project(":ten_gwt_examples.adligo.org") {
  projectTemplate(this, { gc -> 
     TenDeps.gwtExamplesHave(gc)
  })
}

project(":ten_tests.adligo.org") {
  projectTemplate(this, { gc -> 
     TenDeps.testsHave(gc)
  })
}

project(":tests4j.adligo.org") {
  projectTemplate(this, { gc -> 
     Tests4jDeps.has(gc)
  })
}

project(":tests4j4jj.adligo.org") {
  projectTemplate(this, { gc -> 
     Tests4j4jjDeps.has(gc)
  })
}

project(":tests4j4jj_tests.adligo.org") {
  projectTemplate(this, { gc -> 
     Tests4j4jjDeps.testsHave(gc)
  })
}

project(":tests4j_4mockito.adligo.org") {
  projectTemplate(this, { gc -> 
     Tests4j_4MockitoDeps.has(gc)
  })
}

project(":threads.adligo.org") {
  projectTemplate(this, { gc -> 
     ThreadsDeps.has(gc)
  })
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