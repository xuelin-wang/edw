(ns edw.ui
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    ))

(defn- text-input [event-id event-params input-type init-val save-on-change? props]
  (let [val (r/atom init-val)
        stop #(reset! val init-val)
        save #(rf/dispatch (into [] (concat [event-id] event-params [(if (nil? %) @val %)])))
        save-on-change #(rf/dispatch (into [] (concat [event-id] event-params [@val])))]
    [:input
     (merge
       {
        :type input-type
        :default-value init-val
        :on-blur (partial save nil)
        :on-change #(let [new-val (-> % .-target .-value)]
                      (if save-on-change? (save new-val) (reset! val new-val)))
        :on-key-down #(case (.-which %)
                        13 (save nil)
                        27 (stop)
                        nil)}
       props)]))

(defn- textarea-input [event-id event-params init-val props]
  (let [val (r/atom init-val)
        save #(rf/dispatch (into [] (concat [event-id] event-params [@val])))]
    [:textarea
     (merge
       {
        :default-value init-val
        :on-blur save
        :on-change #(reset! val (-> % .-target .-value))
        :on-key-up #(reset! val (-> % .-target .-value))}
       props)]))
