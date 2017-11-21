(ns edw.events
  (:require [edw.db :as db]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx reg-sub]]))

;;dispatchers

(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(reg-event-db
  :set-active-page
  (fn [db [_ page]]
    (assoc db :page page)))

(reg-event-db
  :update-value
  (fn [db [_ value-path val]]
    (assoc-in db value-path val)))

(reg-event-db
  :set-docs
  (fn [db [_ docs]]
    (assoc db :docs docs)))


(defn process-response [db [_ path response]]
  (-> db
      (assoc-in (conj path :loading?) false)
      (assoc-in (conj path :result) response)))

(reg-event-db
  :process-cmd-response
  []
  (fn [db [_ response]]
    (process-response db [_ [:cmd] response])))

(reg-event-db
  :process-cmd-search-response
  []
  (fn [db [_ response]]
    (assoc-in db [:cmd :scripts] (:data response))
    ))


(defn url-encode [ss] (js/encodeURIComponent ss))
(defn url-encode-map [mm]
  (let [nvs (into [] mm)
        encoded-nvs (map #(into [] (map url-encode %))
                         nvs)]
    (into {} (into [] encoded-nvs))))

(defn clj->json [ds] (.stringify js/JSON (clj->js ds)))
(defn clj->url-encoded-json [ds] (url-encode (clj->json ds)))


(reg-event-fx
  :execute-cmd
  []
  (fn [{:keys [db]} [_]]
    (let
      [cmd (:cmd db)
       cmd-type (or (:cmd-type cmd) "bash")
       params {:cmd-type cmd-type :script (get-in cmd [:script cmd-type])}]
      {:http-xhrio {:method          :post
                    :uri             "/cmdExecute"
                    :timeout         8000
                    :response-format (ajax/json-response-format {:keywords? true})

                    :format           (ajax/url-request-format)
                    :params         {:p (clj->url-encoded-json params)}

                    :on-success      [:process-cmd-response]
                    :on-failure      [:process-cmd-response]}
       :db  (assoc-in db [:cmd :loading?] true)})))


(reg-event-fx
  :search-scripts
  []
  (fn [{:keys [db]} [_ search-string]]
    (println (str "search-string: " search-string))
    (let [cmd (:cmd db)
          cmd-type (or (:cmd-type cmd) "bash")
          params {:cmd-type cmd-type :pattern search-string}]
      {
       :http-xhrio {:method          :post
                    :uri             "/cmdSearchScripts"
                    :timeout         8000
                    :response-format (ajax/json-response-format {:keywords? true})

                    :format           (ajax/url-request-format)
                    :params         {:p (clj->url-encoded-json params)}

                    :on-success      [:process-cmd-search-response]
                    :on-failure      [:process-cmd-search-response]}
       :db (assoc-in db [:cmd :search-string] search-string)}

      )
    )
  )

;;subscriptions

(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
  :docs
  (fn [db _]
    (:docs db)))

(reg-sub
  :cmd
  (fn [db _]
    (:cmd db)))
