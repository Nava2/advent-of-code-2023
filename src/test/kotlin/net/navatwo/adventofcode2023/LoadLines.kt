package net.navatwo.adventofcode2023

import net.navatwo.adventofcode2023.framework.Solution

private val loadedLines: MutableMap<String, String> = mutableMapOf()

fun loadText(resourceName: String): String {
  return loadedLines.computeIfAbsent(resourceName) {
    ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName)!!.use { resourceStream ->
      resourceStream.bufferedReader().use { reader ->
        reader.readText().trim()
      }
    }
  }
}

fun <I> Solution<I>.parseResource(resourceName: String): I {
  return parse(loadText(resourceName).lineSequence())
}
