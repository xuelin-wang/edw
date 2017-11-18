(ns edw.executors
  (:require
    [clojure.string :as str]
    [edw.common-utils])
  (:import [edw.util CmdUtils]))

(defn- execute-sh [script]
  (let [exitOutputError (CmdUtils/executeBash (into-array ["/bin/bash" "-c" script]) nil nil (int 100000))]
    {:exit (aget exitOutputError 0)
     :output (aget exitOutputError 1)
     :error (aget exitOutputError 2)}
    ))

(defn- execute-clj [script]
  (let
    [results (eval (read-string script))]
    {:data results}))

(defn execute [script-type script]
  (let []
    (case script-type
      "bash" (execute-sh script)
      "clojure" (execute-clj script)
      {:error (str "type " script-type " is not one of "
                   (str/join ", " edw.common-utils/cmd-types))})))