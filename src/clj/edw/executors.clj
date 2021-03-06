(ns edw.executors
  (:require
    [clojure.string :as str]
    [edw.redis :as r]
    [cheshire.core :as json]
    [edw.common-utils])
  (:import [edw.util CmdUtils]
           [edw.enc CryptUtil]
           (redis.clients.jedis ScanParams)))

(defn redis-script-params-key [script-type script-list script]
  (let [digest (CryptUtil/digestStr script)]
    (str "scripts/" script-type "/" script-list "/" digest)
    )
  )

(defn save-script-and-params [script-type script script-params script-list]
  (r/with-web-redis redis (.zincrby redis (edw.common-utils/redis-script-key script-type script-list) 1.0 script) )
  (r/with-web-redis redis (.zincrby redis (redis-script-params-key script-type script-list script) 1.0
                                    (json/generate-string script-params)))
  )

(defn- execute-bash [script-type script script-params]
  (let [execute-script (apply str
                         (into (vec (map (fn [[name val]] (str name "=" val ";")) script-params))
                               script
                               )
                         )
        commands (into-array (into (vec (edw.common-utils/cmds script-type)) [execute-script]))
        _ (println (str execute-script))
        exitOutputError (CmdUtils/execute commands nil nil (int 100000))
        exit-code (aget exitOutputError 0)]
    (when (= exit-code "0")
      (save-script-and-params script-type script script-params "executed")
      )
    {:exit exit-code
     :output (aget exitOutputError 1)
     :error (aget exitOutputError 2)}
    ))

(defn- execute-python3 [script-type script script-params]
  (let [commands (into-array (into (vec (edw.common-utils/cmds script-type)) [script]))
        exitOutputError (CmdUtils/execute commands nil nil (int 100000))
        exit-code (aget exitOutputError 0)]
    (when (= exit-code "0")
      (save-script-and-params script-type script script-params "executed")
      )
    {:exit exit-code
     :output (aget exitOutputError 1)
     :error (aget exitOutputError 2)}
    ))

(defn to-pretty-json [obj]
  (let [ss (str obj)
        json-obj (json/parse-string ss)]
    (json/generate-string json-obj {:pretty true})
    )
  )

(defn local-eval [x]
  (binding [*ns* (find-ns 'edw.executors)]
    (eval x)))

(declare ^:dynamic script-params)
(defn- execute-clj [script params]
  (let
    [execute-script script
     results (binding [script-params params]
               (let [results (local-eval (read-string script))]
                 (save-script-and-params "clojure" script script-params "executed")
                 results
                 )
               )]
    {:data (str results)}))

(defn execute [script-type script script-params]
  (case script-type
    "bash" (execute-bash script-type script script-params)
    "python3" (execute-python3 script-type script script-params)
    "clojure" (execute-clj script script-params)
    {:error (str "type " script-type " is not one of "
                 (str/join ", " edw.common-utils/cmd-types))})
  )

(defn search-script-params [script-type script-list script]
  (let [scan-params (-> (ScanParams.) (.count (int 1)) (.match "*"))
        scan-result
        (r/with-web-redis redis (.zscan redis (redis-script-params-key script-type script-list script) "0" scan-params) )
        tuples (.getResult scan-result)
        script-params (.getElement (first tuples))
        ]
    {:data script-params}
    )
  )

(defn search-scripts [script-type script-list pattern max-return]
  (let [scan-params (-> (ScanParams.) (.count max-return) (.match pattern))
        scan-result
        (r/with-web-redis redis (.zscan redis (edw.common-utils/redis-script-key script-type script-list) "0" scan-params) )
        tuples (.getResult scan-result)
        scripts (vec (map (fn [tuple] (.getElement tuple)) tuples))
        ]
{:data scripts}
    )
  )

(defn save-script [script-type script script-params script-list]
  (save-script-and-params script-type script script-params script-list)
  {:data true}
  )