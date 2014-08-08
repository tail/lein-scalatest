(ns leiningen.scalatest
  (:require [leiningen.core.main :as lein]
            [leiningen.core.eval :as eval]
            [leiningen.compile :as compile]
            [leiningen.jar :as jar]
            [leiningen.scalac :as scalac]))

(defn- scalatest-xunit-path []
  (or (System/getenv "XUNIT_RESULTS_PATH") "test_results/xunit"))

(defn- scalatest-props [project]
  ["-R" (:compile-path project) "-o" "-u" (scalatest-xunit-path)])

(defn- scalatest-form
  [project]
  `(do
     (import org.scalatest.tools.Runner)
     (let [xunit-path# ~(scalatest-xunit-path)
           ret# (org.scalatest.tools.Runner/run (into-array String ~(scalatest-props project)))]
        ; Jenkins-xUnit does not like the output from ScalaTest, so fix it
        (doseq [junit# (.listFiles (clojure.java.io/file xunit-path#))]
          (if-not (.isDirectory junit#)
            (spit junit# (clojure.string/replace (slurp junit#) #">\s*</property>" "/>"))))
        (if ret#
          (System/exit 0)
          (System/exit 1))
      )))

(defn scalatest
  "Run scalatest on tests in :scala-test-paths"
  [project & args]
  (if (not (:scala-test-paths project))
    (lein/abort "lein scalatestc: You must specify :scala-test-paths [] in your project.clj"))
  (let [project (assoc project
      :dependencies
        (apply concat (for [k [:test :provided]] (get-in project [:profiles k :dependencies])))
      :resource-paths
        [(jar/get-jar-filename project :standalone)]
      :prep-tasks [])]
    (doseq [path (:scala-test-paths project)]
      (scalac/scalac (assoc project :scala-source-path path)))
    (eval/eval-in-project project (scalatest-form project))))