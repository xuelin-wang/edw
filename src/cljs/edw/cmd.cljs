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
        cmd-type0 (:cmd-type cmd)
        cmd-type (if cmd-type0 cmd-type0 (first common-utils/cmd-types))
        output (if-let [loading? (:loading? cmd)] "loading..."
                                                  (str (:result cmd)))
        scripts (:scripts cmd)
        set-script (fn [text] (rf/dispatch [:update-value [:cmd :script cmd-type] text]))
        ]
    [:div.section
    [:div.container
     [:nav.level
      [:div.level-left
       [:div.level-item
        [:select
         {:value cmd-type
          :on-change #(rf/dispatch [:update-value [:cmd :cmd-type] (-> % .-target .-value)])
          }
         (map-indexed (fn [idx val] [:option {:key (str "_cmd_t_" idx)} val]) common-utils/cmd-types)]
        ]
       [:div.level-item
        [:button.button
         {:on-click #(rf/dispatch [:execute-cmd])} "Run"]
        ]

       ]

      [:div.level-right
       [:div.level-item "Searh script: "]
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


      [:div
       [ui/textarea-input :update-value [[:cmd :script cmd-type]] (get-in cmd [:script cmd-type]) {:rows 10 :cols 50}]
       ]

      [:div
       [:span.prewrap (-> (str output) (str/replace "\\n" "\n") (str/replace "\\\"" "\""))]
       ;       [ui/raw-textbox nil output]
       ]
      ]

     (when (pos? (count scripts))
       [:div.column
       [ui/items-list scripts "scripts" set-script]
        ]
       )
     ]
     ]
    )
)
