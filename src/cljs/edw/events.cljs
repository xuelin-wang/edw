(ns edw.events
  (:require [edw.db :as db]
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
