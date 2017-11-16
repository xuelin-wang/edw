(ns ^:figwheel-no-load edw.app
  (:require [edw.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install!)

(core/init!)
