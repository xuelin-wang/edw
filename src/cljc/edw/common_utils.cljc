(ns edw.common-utils)

(def cmds
  {"bash" ["/bin/bash" "-c"]
   "clojure" nil
   "python3" ["/Users/xuelinwang/anaconda3/bin/python3" "-c"]
   })

(def cmd-types (keys cmds))

(def cmd-lists ["builtin" "executed"])

(defn redis-script-key [script-type script-list]
  (str "scripts/" script-type "/" script-list)
  )
