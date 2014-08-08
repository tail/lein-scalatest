(ns lein-scalatest.plugin
	(:require [leiningen.scalatest :as scalatest]
		      [leiningen.test :as test]
		      [robert.hooke]))

(defn middleware [project]
  (-> project
    (update-in [:dependencies] concat
               [['lein-scalac "0.1.0"]])))

(defn- replace-test-scalatest [f & args]
  (apply scalatest/scalatest args))

(defn hooks []
	(robert.hooke/add-hook #'test/test
                           #'replace-test-scalatest))