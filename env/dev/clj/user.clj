(ns user
  (:require 
            [mount.core :as mount]
            [edw.figwheel :refer [start-fw stop-fw cljs]]
            edw.core))

(defn start []
  (mount/start-without #'edw.core/repl-server))

(defn stop []
  (mount/stop-except #'edw.core/repl-server))

(defn restart []
  (stop)
  (start))


