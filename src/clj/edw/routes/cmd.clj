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
   (POST "/cmd" [p :as request]
     (let [p-str (URLDecoder/decode p "UTF-8")
           param (json/parse-string p-str)]
       (println (str param))
       (if true
         (let [cmd-type (get param "cmd-type")
               script (get param "script")
               results (executors/execute cmd-type script)
               results-str (json/generate-string results)]
           (response/ok results-str))
         (response/ok {:data permission-denied})))))