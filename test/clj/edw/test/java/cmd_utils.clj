(ns edw.test.java.cmd-utils
  (:require [clojure.test :refer :all])
  (:import (edw.util CmdUtils)))

(deftest test-reg
  (testing "cmd-utils"
    (let [results (CmdUtils/execute (into-array ["/bin/bash" "-c" "pwd"]) nil nil 15)]
      (println (str "results: " (aget results 0) (aget results 1) (aget results 2)))
      (is (= "0" (aget results 0)))
      ))
)

