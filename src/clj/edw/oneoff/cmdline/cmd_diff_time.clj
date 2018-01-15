(ns edw.oneoff.cmdline.cmd-diff-time
  (:require [clojure.string :as cstr]
            [edw.oneoff.diff-time :as diff-time]
            [clojure.stacktrace]
            [clojure.tools.cli :refer [parse-opts]])
  (:gen-class)
  (:import (java.nio.file Files Paths)))

(defn- validate-file [file]
  (Files/isRegularFile (Paths/get file) (into-array []))
  )

(def cli-options
  ;; An option with a required argument
  [
   ["-f" "--file FILE" "File path"
    ;    :validate [#(validate-file %) "File must be a regular file"]
    ]
   ["-o" "--output OUTPUT" "Output file path"
    ;    :validate [#(validate-file %) "File must be a regular file"]
    ]
   ["-h" "--help"]])

(defn usage [summary]
  (println summary)
  )

(defn -main [& args]
  (try
    (let [{:keys [options errors summary]} (parse-opts args cli-options)]
      (cond
        (:help options)
        (usage summary)

        errors
        (do
          (.println *err* errors)
          (System/exit 1)
          )

        :else
        (let [diff-and-lines
              (diff-time/process (:file options))
              results (cstr/join "\n"
                                 (map #(cstr/join "\n" (map (fn [[diff line]] (str diff "|" line)) %)) diff-and-lines)
                                 )
              ]
            (if (:output options)
              (spit (:output options) results)
              (println results)
              )
          )
        )
      )

    (catch Throwable exc
      (clojure.stacktrace/print-stack-trace exc)
      (System/exit 2)
           )
    )
  )
