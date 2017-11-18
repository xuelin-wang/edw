(ns edw.executors
  (:require
    [clojure.string :as str]
    [edw.common-utils]))

(defn- execute-sh [script]
  {:data "hello from bash"})

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