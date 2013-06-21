(ns lein-scalatest.plugin)

(defn middleware [project]
  (-> project
    (update-in [:dependencies] concat
               [['com.duramec/lein-scalac "0.1.1"]
                ['org.scalatest/scalatest_2.10 "2.0.M5b"]])))
