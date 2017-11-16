(ns edw.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[edw started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[edw has shut down successfully]=-"))
   :middleware identity})
