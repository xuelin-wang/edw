(ns user
  (:require 
            [mount.core :as mount]
            [edw.figwheel :refer [start-fw stop-fw cljs]]
            ))

(defn start []
  (mount/start-without
    (eval "(do (require '[edw.core]) #'edw.core/repl-server)")
    ))

(defn stop []
  (mount/stop-except
    (eval "(do (require '[edw.core]) #'edw.core/repl-server)")
    ))

(defn restart []
  (stop)
  (start))


