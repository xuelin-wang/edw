(ns edw.executors
  (:require
    [clojure.string :as str]
    [edw.redis :as r]
    [edw.common-utils])
  (:import [edw.util CmdUtils]
           (redis.clients.jedis ScanParams)))

(defn- execute-program [commands script-type script]
  (let [exitOutputError (CmdUtils/execute commands nil nil (int 100000))
        exit-code (aget exitOutputError 0)]
    (when (= exit-code "0")
      (r/with-redis redis (.zincrby redis (str "scripts/" script-type) 1.0 script) )
      )
    {:exit exit-code
     :output (aget exitOutputError 1)
     :error (aget exitOutputError 2)}
    ))

(defn- execute-clj [script]
  (print (str "script is: " script))
  (let
    [results (eval (read-string script))]
    (r/with-redis redis (.zincrby redis "scripts/clojure" 1.0 script) )
    {:data (str results)}))

(defn execute [script-type script]
  (let []
    (case script-type
      ("bash" "python3") (execute-program (into-array (into (vec (edw.common-utils/cmds script-type)) [script])) script-type script)
      "clojure" (execute-clj script)
      {:error (str "type " script-type " is not one of "
                   (str/join ", " edw.common-utils/cmd-types))})))

(defn search-scripts [script-type pattern max-return]
  (let [scan-params (-> (ScanParams.) (.count max-return) (.match pattern))
        scan-result
        (r/with-redis redis (.zscan redis (str "scripts/" script-type) "0" scan-params) )
        tuples (.getResult scan-result)
        scripts (vec (map (fn [tuple] (.getElement tuple)) tuples))
        ]
{:data scripts}
    ))