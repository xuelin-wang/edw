(ns edw.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [edw.ajax :refer [load-interceptors!]]
            [edw.events]
            [edw.cmd]
            [edw.dashboard])
  (:import goog.History))

(defn nav-link [uri title page collapsed?]
  (let [selected-page (rf/subscribe [:page])]
    [:a.navbar-item
     {:class (when (= page @selected-page) "is-active")
      :href uri
      :on-click #(reset! collapsed? true)}
     title
     ]
    ))


(defn navbar []
  (r/with-let [collapsed? (r/atom true)]
    [:nav.navbar {:role "navigation" :aria-label "main navigation"}

     [:button.button.navbar-burger
      {:on-click #(swap! collapsed? not)}
      [:span]
      [:span]
      [:span]
      ]

    [:div.navbar-brand
     [nav-link "#/" "Home" :home collapsed?]
     [nav-link "#/cmd" "Command" :cmd collapsed?]
     [nav-link "#/dashboard" "Dashboard" :dashboard collapsed?]
     [nav-link "#/about" "About" :about collapsed?]

     ]
     ]
  ))




(defn img-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:img {:src (str js/context "/img/warning_clojure.png")}]]]])

(defn home-page []
  [:div.container
   [:a {:href "#/cmd"} "command"]
   [:br]
   [:a {:href "#/dashboard"} "dashboard"]
   [:br]
   [:a {:href "#/about"} "about"]
   ])

(defn about-page []
  [:div.container
   (when-let [docs @(rf/subscribe [:docs])]
     [:div.row>div.col-sm-12
      [:div {:dangerouslySetInnerHTML
             {:__html (md->html docs)}}]])])

(def pages
  {:home #'home-page
   :cmd #'edw.cmd/cmd-page
   :dashboard #'edw.dashboard/dashboard-page
   :about #'about-page})

(defn page []
  [:div
   [navbar]
   [(pages @(rf/subscribe [:page]))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :home]))

(secretary/defroute "/cmd" []
                    (rf/dispatch [:set-active-page :cmd]))

(secretary/defroute "/dashboard" []
                    (rf/dispatch [:set-active-page :dashboard]))

(secretary/defroute "/about" []
  (rf/dispatch [:set-active-page :about]))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
  (GET "/docs" {:handler #(rf/dispatch [:set-docs %])}))

(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (load-interceptors!)
  (fetch-docs!)
  (hook-browser-navigation!)
  (mount-components))
