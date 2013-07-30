(ns lein-scalatest.plugin)

(defn- version
  [project]
  (or (:scalatest-version project) "2.0.M5b"))

(defn middleware [project]
  (-> project
    (update-in [:dependencies] concat
               [['lein-scalac "0.1.0"]
                ['org.scalatest/scalatest_2.9.1 (version project)]])))

