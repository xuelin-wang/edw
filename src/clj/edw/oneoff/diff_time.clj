(ns edw.oneoff.diff-time
  (:import (java.io BufferedReader FileReader)
           (java.text SimpleDateFormat)))

(def ^:private date-format-pattern "yyyy-MM-dd:HH:mm:ss")
(def ^:private date-format-pattern-len (.length date-format-pattern))
(def ^:private date-format (SimpleDateFormat. date-format-pattern))

(defn time-millis [ymdhms]
  (let [dt (.parse date-format ymdhms)]
    (.getTime dt)
    )
  )

(defn diff-time [time1 time2]
  (if (some nil? [time1 time2])
    nil
    (- time1 time2))
  )

(defn process-alerts-line [line curr-time]
  (let [save-time-str (.substring line 13 (+ 13 date-format-pattern-len))
        save-time (time-millis save-time-str)]
    [(diff-time save-time curr-time) save-time]
    )
  )

(defn process-reports-line [line curr-time]
  (let [ss "readTime\":\""
        read-time-index (.indexOf line ss)
        time-index (+ read-time-index (.length ss))
        read-time-str (.substring line time-index (+ time-index date-format-pattern-len))
        read-time (time-millis read-time-str)]
    [(diff-time read-time curr-time) curr-time]
    )
  )

(declare process-line)

(defn process-key-line [line curr-time]
  (let [ss ":list"
        ss-index (.indexOf line ss)
        item-index (+ ss-index (.length ss) 2)
        item (.substring line item-index)
        [diff _] (process-line item curr-time)]
    [diff nil]
    )
  )

(defn- is-alerts-line [line]
  (.startsWith line "{\"saveTime\":\"")
  )

(defn- is-report-line [line]
  (.startsWith line "{\"uuid\":")
  )

(defn- is-key-line [line]
  (not-any? true? ((juxt is-alerts-line is-report-line) line))
  )

(defn process-line [line curr-time]
  (cond
    (is-alerts-line line)
      (process-alerts-line line curr-time)
    (is-report-line line)
      (process-reports-line line curr-time)
    :else
      (process-key-line line curr-time)
    )
  )

(defn process [file-path]
  (let [lines (line-seq (BufferedReader. (FileReader. file-path)))
        tmp-results (reduce
                      (fn [{:keys [accum output]} line]
                        (if (is-key-line line)
                          {:accum [line] :output (conj output accum)}
                          {:accum (conj accum line) :output output}
                          )
                        )
                      {:accum [] :output []}
                      lines)
        lines-by-key (conj (:output tmp-results) (:accum tmp-results))
        ]
    (reduce
      (fn [results lines-for-key]
        (let [
              {:keys [curr-time output]}
              (reduce
                (fn [{:keys [curr-time output]} line]
                  (let [[diff-millis new-curr-time] (process-line line curr-time)]
                    {:curr-time new-curr-time :output (conj output [diff-millis line])}
                    )
                  )
                {:curr-time nil :output []}
                (reverse lines-for-key)
                )
              ]
            (conj results output)
          )
        )
      []
      lines-by-key
      )
    )
  )

(process "/Users/xuelinwang/dev/storm/xstorm/20180112/listkeys11-sa-east-1-kvs.txt")