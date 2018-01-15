(ns edw.routes.cmd
  (:require
    [compojure.core :refer [defroutes GET POST]]
    [cheshire.core :as json]
    [edw.executors :as executors]
    [ring.util.http-response :as response])
  (:import [java.net URLDecoder])
  )


(def permission-denied {:error "Permission denied"})

(defroutes cmd-routes
   (POST "/cmdExecute" [p :as request]
     (let [p-str (URLDecoder/decode p "UTF-8")
           param (json/parse-string p-str)]
       (if true
         (let [cmd-type (get param "cmd-type")
               script-and-params (get param "script")
               script (get script-and-params "script")
               script-params (get script-and-params "params")
               results (executors/execute cmd-type script script-params)
               results-str (json/generate-string results)]
           (response/ok results-str))
         (response/ok {:data permission-denied}))))

   (POST "/cmdSearchScripts" [p :as request]
     (let [p-str (URLDecoder/decode p "UTF-8")
           param (json/parse-string p-str)]
       (if true
         (let [cmd-type (get param "cmd-type")
               cmd-list (get param "list-name")
               pattern (get param "pattern")
               max-return (get param "max-return")
               results (executors/search-scripts cmd-type cmd-list pattern (int (or max-return "50")))
               results-str (json/generate-string results)]
           (response/ok results-str))
         (response/ok {:data permission-denied}))))

   (POST "/cmdSaveScript" [p :as request]
     (let [p-str (URLDecoder/decode p "UTF-8")
           param (json/parse-string p-str)]
       (if true
         (let [cmd-type (get param "cmd-type")
               script-and-params (get param "script")
               script (get script-and-params "script")
               script-params (get script-and-params "params")
               list-name (get param "list-name")
               results (executors/save-script cmd-type script script-params list-name)
               results-str (json/generate-string results)]
           (response/ok results-str))
         (response/ok {:data permission-denied}))))

   (POST "/cmdSearchScriptParams" [p :as request]
     (let [p-str (URLDecoder/decode p "UTF-8")
           param (json/parse-string p-str)]
       (if true
         (let [cmd-type (get param "cmd-type")
               cmd-list (get param "list-name")
               script (get param "script")
               script-params-key (executors/redis-script-params-key cmd-type cmd-list script)
               results (executors/search-script-params cmd-type cmd-list script)
               results-str (json/generate-string results)]
           (response/ok results-str))
         (response/ok {:data permission-denied}))))
           )
