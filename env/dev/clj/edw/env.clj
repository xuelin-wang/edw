(ns edw.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [edw.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[edw started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[edw has shut down successfully]=-"))
   :middleware wrap-dev})
