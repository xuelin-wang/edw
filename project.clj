(defproject edw "0.1.0-SNAPSHOT"

  :description ""
  :url "http://what.com"

  :dependencies [[ch.qos.logback/logback-classic "1.2.3"]
                 [cheshire/cheshire "5.8.0"]
                 [clj-time "0.14.2"]
                 [cljs-ajax "0.7.3" :exclusions [ceshire]]
                 [cljsjs/d3 "4.2.2-0"]
                 [org.clojure/core.async "0.3.465"]
                 [com.walmartlabs/lacinia "0.23.0-rc-1"]
                 [compojure "1.6.0"]
                 [cprop "0.1.12-SNAPSHOT"]
                 [funcool/struct "1.1.0"]
                 ["javax.xml.bind/jaxb-api" "2.3.0"]
                 [luminus-jetty "0.1.5" :exclusions [com.fasterxml.jackson.core/jackson-core]]
                 [luminus-nrepl "0.1.4"]
                 [luminus/ring-ttl-session "0.3.2"]
                 [markdown-clj "1.0.1"]
                 [metosin/compojure-api "2.0.0-alpha12"
                  :exclusions [cheshire]]
                 [metosin/muuntaja "0.3.2" :exclusions [com.fasterxml.jackson.core/jackson-core]]
                 [metosin/ring-http-response "0.9.0"]
                 [mount "0.1.11"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946" :scope "provided"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.clojure/tools.logging "0.4.0"]
                 [org.clojure/tools.reader "1.1.0"]
                 [org.webjars.bower/tether "1.4.0"]
                 [org.webjars/bootstrap "4.0.0-alpha.5"]
                 [org.webjars/font-awesome "4.7.0"]
                 [re-com "2.1.0"]
                 [re-frame "0.10.2"]
                 [day8.re-frame/http-fx "0.1.4" :exclusions [com.fasterxml.jackson.core/jackson-core]]
                 [reagent "0.7.0"]
                 [reagent-utils "0.2.1"]
                 [rid3 "0.2.0"]
                 [ring-webjars "0.2.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-defaults "0.3.1"]
                 [secretary "1.2.3"]
                 [selmer "1.11.2" :exclusions [com.fasterxml.jackson.core/jackson-core]]
                 [redis.clients/jedis "2.9.0"]
                 [com.amazonaws/aws-java-sdk-bundle "1.11.248"]
                 ]

  :min-lein-version "2.8.1"

  :jvm-opts ["-server" "-Dconf=.lein-env"]
  :java-source-paths ["src/java"]
  :javac-options ["-target" "1.8" "-source" "1.8"]
  :source-paths ["src/clj" "src/cljc"]
  :test-paths ["test/clj"]
  :resource-paths ["resources" "target/cljsbuild"]
  :target-path "target/%s/"
  :main ^:skip-aot edw.core

  :plugins [[lein-cprop "1.0.3"]
            [lein-cljsbuild "1.1.7"]]
  :clean-targets ^{:protect false}
  [:target-path [:cljsbuild :builds :app :compiler :output-dir] [:cljsbuild :builds :app :compiler :output-to]]
  :figwheel
  {:http-server-root "public"
   :nrepl-port 7002
   :css-dirs ["resources/public/css"]
   :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
  

  :profiles
  {:uberjar {:omit-source true
             :prep-tasks ["javac" "compile" ["cljsbuild" "once" "min"]]
             :cljsbuild
             {:builds
              {:min
               {:source-paths ["src/cljc" "src/cljs" "env/prod/cljs"]
                :compiler
                {:output-to "target/cljsbuild/public/js/app.js"
                 :optimizations :advanced
                 :install-deps true
                 :npm-deps {
                            ;:react-grid-layout "0.16.0"
                            ;                                 :react "15.6.1"
                            ;                                 :react-dom "15.6.1"
                            }
                 :pretty-print false
                 :closure-warnings
                 {:externs-validation :off :non-standard-jsdoc :off}
                 :externs ["react/externs/react.js"]}}}}

             :aot :all
             :uberjar-name "edw.jar"
             :source-paths ["env/prod/clj"]
             :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev  {:dependencies [[prone "1.1.4"]
                                 [ring/ring-mock "0.3.1"]
                                 [ring/ring-devel "1.6.3"]
                                 [pjstadig/humane-test-output "0.8.3"]
                                 [binaryage/devtools "0.9.7"]
                                 [com.cemerick/piggieback "0.2.2"]
                                 [doo "0.1.8"]
                                 [figwheel-sidecar "0.5.14"  :exclusions [org.clojure/tools.nrepl org.clojure/core.async]]]
                  :plugins      [[com.jakemccrary/lein-test-refresh "0.19.0"]
                                 [lein-doo "0.1.8"]
                                 [lein-figwheel "0.5.14"]
                                 [org.clojure/clojurescript "1.9.946"]]
                  :cljsbuild
                  {:builds
                   {:app
                    {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
                     :figwheel {:on-jsload "edw.core/mount-components"}
                     :compiler
                     {:main "edw.app"
                      :asset-path "/js/out"
                      :output-to "target/cljsbuild/public/js/app.js"
                      :output-dir "target/cljsbuild/public/js/out"
                      :source-map true
                      :optimizations :none
                      :install-deps true
                      :npm-deps {
                                 ;:react-grid-layout "0.16.0"
                                 ;                                 :react "15.6.1"
                                 ;                                 :react-dom "15.6.1"
                                 }
                      :pretty-print true}}}}
                  
                  
                  
                  :doo {:build "test"}
                  :source-paths ["env/dev/clj"]
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns user}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]}
   :project/test {:resource-paths ["env/test/resources"]
                  :cljsbuild
                  {:builds
                   {:test
                    {:source-paths ["src/cljc" "src/cljs" "test/cljs"]
                     :compiler
                     {:output-to "target/test.js"
                      :main "edw.doo-runner"
                      :optimizations :whitespace
                      :install-deps true
                      :npm-deps {
                                 ; :react-grid-layout "0.16.0"
                                 ;                                 :react "15.6.1"
                                 ;                                 :react-dom "15.6.1"
                                 }
                      :pretty-print true}}}}
                  
                  }
   :profiles/dev {}
   :profiles/test {}})
