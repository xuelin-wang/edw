(ns edw.events
  (:require [edw.db :as db]
            [edw.crypt]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [clojure.string :as str]
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
      ;      _ (println (str params))
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
  (fn [{:keys [db]} [_ input-search-string]]

    (let [cmd (:cmd db)
          search-string (str/trim (or input-search-string ""))
          cmd-type (or (:cmd-type cmd) "bash")
          cmd-list (or (:list cmd) "builtin")
          params {:cmd-list cmd-list :cmd-type cmd-type :pattern search-string :max-return 50}]
      (if (str/blank? search-string)
        {:db
         (-> db
             (assoc-in [:cmd :scripts] [])
             (assoc-in [:cmd :search-string] search-string)
             )
         }
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
  )


(reg-event-db
  :process-dummy
  []
  (fn [db [_ response]]
    db))

(reg-event-fx
  :save-script
  []
  (fn [{:keys [db]} [_ list-name]]
    (let
      [cmd (:cmd db)
       cmd-type (or (:cmd-type cmd) "bash")
       params {:cmd-type cmd-type :script (get-in cmd [:script cmd-type]) :list-name list-name}]
      ;      _ (println (str params))
      {:http-xhrio {:method          :post
                    :uri             "/cmdSaveScript"
                    :timeout         8000
                    :response-format (ajax/json-response-format {:keywords? true})

                    :format           (ajax/url-request-format)
                    :params         {:p (clj->url-encoded-json params)}

                    :on-success      [:process-dummy]
                    :on-failure      [:process-dummy]}
       :db  db})))


(reg-event-fx
  :process-register-response
  []
  (fn [{:keys [db]} [_ response]]
    (let [registered? (:data response)
          new-db (-> db
                     (assoc-in [:auth :registering?] false)
                     (assoc-in [:auth :register :error]
                               (if registered? "" "There exists an account for the email address already"))
                     (assoc-in [:auth :register :msg]
                               (if registered? "Please check your email and click the confirm link"
                                               "")))]
      {:db new-db})))

(defn validate-auth [auth]
  "true"
  )

(defn hash-auth [auth uuid]
  ""
  )

(defn clear-init [db] (assoc db :init {}))

(reg-event-fx
  :register
  []
  (fn [{:keys [db]} [_]]
    (let [auth (get-in db [:auth])
          msg (validate-auth auth)]
      (if (nil? msg)
        {:http-xhrio {:method          :get
                      :uri             "/auth_register"
                      :params          (hash-auth auth  (edw.crypt/new-uuid))
                      :response-format (ajax/json-response-format {:keywords? true})
                      :on-success      [:process-register-response]
                      :on-failure      [:process-register-response]}
         :db  (-> (clear-init db)
                  (assoc-in [:auth :register :msg] "Registering, please wait...")
                  (assoc-in [:auth :register :error] nil))}
        {:db (assoc-in (clear-init db)  [:auth :register :error] msg)}))))

(reg-event-fx
  :process-login-response
  []
  (fn [{:keys [db]} [_ response]]
    (let [data (:data response)
          login? (:login? data)
          db1 (-> (clear-init db)
                  (assoc-in [:auth :login :error] (if login? "" "Email address or password doesn't match")))
          db2 (assoc-in db1 [:auth] (merge (get-in db1 [:auth]) data))]

      (if login?
        {:db db2
         :dispatch [:pm-get-list nil]}
        {:db db2}))))

(reg-event-fx
  :process-nonce-response
  []
  (fn [{:keys [db]} [_ response]]
    (let
      [nonce (:nonce response)
       auth (get-in db [:auth])]
      {:http-xhrio {:method          :get
                    :uri             "/auth_login"
                    :params          (hash-auth auth nonce)
                    :response-format (ajax/json-response-format {:keywords? true})
                    :on-success      [:process-login-response]
                    :on-failure      [:process-login-response]}
       }
      )))

(reg-event-fx
  :login
  []
  (fn [{:keys [db]} [_]]
    (let
      [auth (get-in db [:auth])]
      {:http-xhrio {:method          :get
                    :uri             "/auth_nonce"
                    :params          {:auth-name (:auth-name auth)}
                    :response-format (ajax/json-response-format {:keywords? true})
                    :on-success      [:process-nonce-response]
                    :on-failure      [:process-nonce-response]}
       })))


(reg-event-db
  :dummy
  []
  (fn [db [_]] db))

(reg-event-fx
  :logout
  []
  (fn [{:keys [db]} [_]]
    (let
      [auth (get-in db [:pm :auth])]
      {:http-xhrio {:method          :get
                    :uri             "/auth_logout"
                    :params          (select-keys auth [:auth-name])
                    :response-format (ajax/json-response-format {:keywords? true})
                    :on-success      [:dummy]
                    :on-failure      [:dummy]}
       :db (assoc db :page :pm :pm {})})))


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
