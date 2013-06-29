(ns lein-scalatest.plugin)

(defn- version
  [project]
  (or (:scalatest-version project) "2.0.M5b"))

(defn middleware [project]
  (-> project
    (update-in [:dependencies] concat
               [['com.duramec/lein-scalac "0.1.1"]
                ['org.scalatest/scalatest_2.10 (version project)]])))

