(ns edw.ui
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    [clojure.string :as str]
    ))

(defn text-input [event-id event-params input-type init-val save-on-change? props]
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

(defn textarea-input [event-id event-params val props]
  (let [save #(rf/dispatch (into [] (concat [event-id] event-params [%])))]
    [:textarea
     (merge
       {
        :value val
        :on-change #(save (-> % .-target .-value))
        }
       props)]))

(defn raw-textbox [props text]
  (when text
    [:pre (merge props
                 {:dangerouslySetInnerHTML
                  {:__html (str/replace (str text) "\\n" "\n")}}
                 ) ]
    )
  )

(defn items-list [options key-prefix click-callback]
  (let [items
        (vec
          (map-indexed
            (fn [index item]
              (let [comp-key (str key-prefix "-" index)]
                [:div
                 {
                  :on-click (fn [] (when (some? click-callback) (click-callback item)))
                  }
                 [raw-textbox {:class (if (even? index) "alternate-a" "alternate-b") :key comp-key} item]
                 ]
                )
              )
            options)
          )
        ]
    (into [:div.container] (interpose [:div [:hr]] items))
    )
  )

(defn login [auth]
  [:div.container
   [:div.field
    [:label.label "Email address: "]
    [:div.control
     [text-input :update-value [[:auth :user]] "text" (:user auth) false nil]
     ]
    ]

   [:div.field
    [:label.label "Password: "]
    [:div.control
     [text-input :update-value [[:auth :password]] "password" (:password auth) false nil]
     ]
    ]

   [:div.field
    [:div.control
     [:button.button.is-link {:on-click #(rf/dispatch [:login])} "Login"]
     ]
    ]

   [:p.is-danger (get-in auth [:login :error])
    ]

   [:div.field
    "Don't have an account? please "
    [:button.button.is-link
     {:on-click #(rf/dispatch [:update-value [:auth :registering?] true])} "register"]
    ]
   ])

(defn register [auth]
  [:div.container
   [:div.field
    [:label.label "Email address: "]
    [:div.control
     [text-input :update-value [[:auth :user]] "text" (:user auth) false nil]
     ]
    ]

   [:div.field
    [:label.label "Password: "]
    [:div.control
     [text-input :update-value [[:auth :password]] "password" (:password auth) false nil]
     ]
    ]

   [:div.field
    [:label.label "Confirm Password: "]
    [:div.control
     [text-input :update-value [[:auth :confirm-password]] "password" (:confirm-password auth) false {:on-paste #(do)}]
     ]
    ]

   [:div.field
    [:div.control
     [:button.button.is-link {:on-click #(rf/dispatch [:register])} "Register"]
     ]
    ]

   [:p.is-danger (get-in auth [:register :error])
    ]
   [:p.is-success (get-in auth [:register :message])
    ]

   [:div.field
    "Already have an account? please "
    [:button.button.is-link
     {:on-click #(rf/dispatch [:update-value [:auth :registering?] false])} "login"]
    ]
   ])
