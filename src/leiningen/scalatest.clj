(ns leiningen.scalatest
  (:require [leiningen.core.main :as lein]
            [leiningen.core.eval :as eval]
            [leiningen.classpath :as classpath]
            [leiningen.scalac :as scalac]))

(defn- scalatest-props
  [project]
  ["-R" "target/classes"
   "-o"])

(defn- scalatest-form
  [project runpath]
  `(do
     (import org.scalatest.tools.Runner)
     (org.scalatest.tools.Runner/run (into-array String ~(scalatest-props project)))
     (System/exit 0)))

(defn scalatest
  "Run scalatest on tests in :scala-test-paths"
  [project & args]
  (if (not (:scala-test-paths project))
    (lein/abort "lein scalatest: You must specify :scala-test-paths [] in your project.clj"    ))
  (scalac/scalac (assoc-in project [:scala-source-paths] (:scala-test-paths project)))
  (let [runpath (:compile-path project)]
    (eval/eval-in-project project (scalatest-form project runpath))))
