(ns edw.db)

(def default-db
  {:page :home
   :auth {:user ""
          :registering? false
          }
   :cmd {:cmd-type "bash"
         :script {
                  "bash"
                  {
                   "script"
                   "
 echo 'Bash version ${BASH_VERSION}...'
 for i in $(seq 0 $execute_times)
 do
     echo \"Welcome $i times\"
 done
 "
                   "params"
                   {
                    "execute_times"
                    "10"
                    }
                   }

                  "clojure"
                  {"script"
                   "
                   (let [pi (Math/PI)
                       r  (Integer/parseInt (script-params \"radius\"))
                       area (* 4 pi r r)]
                     (str \"Earth area is \" (int area) \" square miles\"))
                     "
                    "params"
                     {
                      "radius"
                      "1959"
                      }
                   }
                  }}})
