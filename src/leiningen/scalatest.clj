(ns leiningen.scalatest
  (:require [leiningen.core.main :as lein]
            [leiningen.core.eval :as eval]
            [leiningen.scalac :as scalac]))

(defn- scalatest-props
  [project]
  (let [runpath (:compile-path project)]
    ["-R" runpath 
     "-o"]))

(defn- scalatest-form
  [project]
  `(do
     (import org.scalatest.tools.Runner)
     (org.scalatest.tools.Runner/run (into-array String ~(scalatest-props project)))
     (System/exit 0)))

(defn- scalac-props
  [project]
  (update-in project [:scala-source-paths] concat (:scala-test-paths project)))

(defn scalatest
  "Run scalatest on tests in :scala-test-paths"
  [project & args]
  (if (not (:scala-test-paths project))
    (lein/abort "lein scalatest: You must specify :scala-test-paths [] in your project.clj"))
  (scalac/scalac (scalac-props project)) 
  (eval/eval-in-project project (scalatest-form project)))
