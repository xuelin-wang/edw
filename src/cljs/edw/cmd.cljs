(ns edw.cmd
  (:require
    [re-frame.core :as rf]
    [edw.common-utils :as common-utils]
    [edw.ui :as ui]
    [clojure.string :as str]
    )
)

(defn cmd-page []
  (let [cmd @(rf/subscribe [:cmd])
        cmd-type0 (:cmd-type cmd)
        cmd-type (if cmd-type0 cmd-type0 (first common-utils/cmd-types))
        output (if-let [loading? (:loading? cmd)] "loading..." (str (:result cmd)))
        ]
    [:div.container
     [:div.row
      [:div.col-sm-12
       [:select
        {:value cmd-type
         :on-change #(rf/dispatch [:update-value [:cmd :cmd-type] (-> % .-target .-value)])
         ;        :on-change #(rf/dispatch [:update-value :dashboard])
         }
        (map-indexed (fn [idx val] [:option {:key (str "_cmd_t_" idx)} val]) common-utils/cmd-types)]
       [:button.btn.btn-default.btn-sm
        {:type "button" :on-click #(rf/dispatch [:execute-cmd])} "Run"]
       [:br]
       [ui/textarea-input :update-value [[:cmd :script cmd-type]] (get-in cmd [:script cmd-type]) {:rows 10 :cols 50}]
       ]]
     [:div.row
      [:div.col-sm-12
       [:pre {:dangerouslySetInnerHTML
              {:__html (str/replace output "\\n" "\n")}}]
       ]
      ]
     ]
    )
)
