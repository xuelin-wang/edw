(ns edw.dashboard
  ;  (:require [react :refer [createElement]] ["react-dom/server" :as ReactDOMServer :refer [renderToString]])
  )

(defn dashboard-page []
  ;  (js/console.log (renderToString (createElement "div" nil "Hello World!")))
  [:div.container

   [:div.row
    [:div.col-md-12
     "Welcome to dashboard"
     ]]

   ])
