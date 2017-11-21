(ns edw.executors
  (:require
    [clojure.string :as str]
    [edw.redis :as r]
    [edw.common-utils])
  (:import [edw.util CmdUtils]
           (redis.clients.jedis ScanParams)))

(defn- execute-sh [script]
  (let [exitOutputError (CmdUtils/executeBash (into-array ["/bin/bash" "-c" script]) nil nil (int 100000))
        exit-code (aget exitOutputError 0)]
    (when (= exit-code "0")
      (r/with-redis redis (.zincrby redis "scripts/bash" 1.0 script) )
      )
    {:exit exit-code
     :output (aget exitOutputError 1)
     :error (aget exitOutputError 2)}
    ))

(defn- execute-clj [script]
  (let
    [results (eval (read-string script))]
    (r/with-redis redis (.zincrby redis "scripts/clojure" 1.0 script) )
    {:data results}))

(defn execute [script-type script]
  (let []
    (case script-type
      "bash" (execute-sh script)
      "clojure" (execute-clj script)
      {:error (str "type " script-type " is not one of "
                   (str/join ", " edw.common-utils/cmd-types))})))

(defn search-scripts [script-type pattern]
  (let [scan-params (-> (ScanParams.) (.count (int 10)) (.match pattern))
        scan-result
        (r/with-redis redis (.zscan redis (str "scripts/" script-type) "0" scan-params) )
        tuples (.getResult scan-result)
        scripts (vec (map (fn [tuple] (.getElement tuple)) tuples))
        ]
{:data scripts}
    ))