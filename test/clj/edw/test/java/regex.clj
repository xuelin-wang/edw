(ns edw.test.java.regex
  (:require [clojure.test :refer :all]))

(deftest test-reg
  (testing "main route"
    (let [response ((app) (request :get "/"))]
      (is (= 200 (:status response)))))
)