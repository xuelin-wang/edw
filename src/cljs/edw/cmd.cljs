(ns edw.cmd
  (:require
    [re-frame.core :as rf]
    [edw.common-utils :as common-utils]
    [edw.ui :as ui]
    [clojure.string :as str]
    [cljs.pprint :as pprint]
    )
)

(defn cmd-page []
  (let [cmd @(rf/subscribe [:cmd])
        cmd-type (or (:cmd-type cmd) (first common-utils/cmd-types))

        cmd-list (or (:list cmd) (first common-utils/cmd-lists))

        output (if-let [loading? (:loading? cmd)] "loading..."
                                                  (str (:result cmd)))
        scripts (:scripts cmd)
        set-script (fn [text]
                     (let [js-obj (.parse js/JSON text)]
                       (rf/dispatch [:update-value [:cmd :script cmd-type] (js->clj js-obj)])
                       )
                     )
        set-param (fn [name val]
                    (rf/dispatch [:update-value [:cmd :script cmd-type "params" name] val])
                     )
        new-param (get-in cmd [:script cmd-type "new-param"])
        new-param-error (or (get-in cmd [:script cmd-type "new-param-error"]) "")
        set-new-param-name (fn [name]
                             (rf/dispatch [:update-value [:cmd :script cmd-type "new-param"] name])
                             )
        add-param (fn [raw-name]
                    (let [name (str/trim (or raw-name ""))
                          params (get-in cmd [:script cmd-type "params"])]
                      (cond
                        (str/blank? name)
                        (rf/dispatch [:update-value [:cmd :script cmd-type "new-param-error"]
                                      (str "Parameter name is empty.")])

                        (nil? (re-matches #"[a-zA-Z_][0-9a-zA-Z_]*" name))
                        (rf/dispatch [:update-value [:cmd :script cmd-type "new-param-error"]
                                      (str "Parameter name must start with letter and contains only letter, digit or underscore.")])

                        (contains? params name)
                        (rf/dispatch [:update-value [:cmd :script cmd-type "new-param-error"]
                                      (str "Parameter " name " already exists.")])

                        :else
                        (do
                          (rf/dispatch [:update-value [:cmd :script cmd-type "params"] (assoc params name "")])
                          (rf/dispatch [:update-value [:cmd :script cmd-type "new-param-error"] ""])
                          )
                        )
                      )
                    )
        delete-param (fn [name]

                       (let [params (get-in cmd [:script cmd-type "params"])]
                         (when (contains? params name)
                           (rf/dispatch [:update-value [:cmd :script cmd-type "params"] (dissoc params name)])
                           )
                         )
                       )
        script-params (get-in cmd [:script cmd-type])
        script (script-params "script")
        params (script-params "params")
        ]
    [:div.section
    [:div.container
     [:nav.level
      [:div.level-left
       [:div.level-item
        [:div.select
         [:select
          {:value cmd-type
           :on-change #(rf/dispatch [:update-value [:cmd :cmd-type] (-> % .-target .-value)])
           }
          (map-indexed (fn [idx val] [:option {:key (str "_cmd_t_" idx)} val]) common-utils/cmd-types)]
         ]
        ]
       [:div.level-item
        [:button.button
         {:on-click #(rf/dispatch [:execute-cmd])} "Run"]
        ]
       [:div.level-item
        [:button.button
         {:on-click #(rf/dispatch [:save-script "builtin"])} "Add script to builtin"]
        ]
       ]

      [:div.level-right
       [:div.level-item "Searh script: "]
       [:div.select
        [:select
         {:value cmd-list
          :on-change #(rf/dispatch [:update-value [:cmd :list] (-> % .-target .-value)])
          }
         (map-indexed (fn [idx val] [:option {:key (str "_cmd_list_t_" idx)} val]) common-utils/cmd-lists)]
        ]

       [:div.level-item
        [ui/text-input :search-scripts nil "text" (:search-string cmd) true nil]
        ]
       [:div.level-item
        [:button.button
         {:on-click #(rf/dispatch [:search-scripts ""])}
         "Clear" ]
        ]
       ]
      ]
     ]


    [:div.columns
     [:div.column
      (into
        [:div
         [ui/textarea-input :update-value [[:cmd :script cmd-type "script"]] script {:rows 10 :style {:width "100%"} }]

         [:div
          [:input.input {:type "text" :value new-param :on-change #(set-new-param-name (-> % .-target .-value))}]
          [:button.button
           {:on-click #(add-param new-param)} "Add Parameter"]
          [:p.help.is-danger new-param-error]
          ]

         ]

        (map (fn [[name val]]
               [:div.field
                [:div.level
                 [:div.level-left
                  [:label.level-item name]
                  [:button.delete.level-item {:on-click #(delete-param name)}]
                  ]
                 ]
                [:input.input {:type "text" :value val :on-change #(set-param name (-> % .-target .-value))} ]
                ]
               )
             (seq params)
             )
        )

      [:div
       [:span.prewrap (-> (str output) (str/replace "\\n" "\n") (str/replace "\\\"" "\""))]
       ;       [ui/raw-textbox nil output]
       ]
      ]

     [:div.column
      (when (pos? (count scripts))
        [ui/items-list scripts "scripts" set-script]
        )
      ]
     ]
     ]
    )
)
