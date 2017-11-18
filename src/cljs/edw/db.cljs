(ns edw.db)

(def default-db
  {:page :home
   :cmd {:cmd-type "bash"
         :script {
                  "bash"
                  "
echo 'Bash version ${BASH_VERSION}...'
for i in {0..10}
do
    echo \"Welcome $i times\"
done
"
                  "clojure"
"
(let [pi (Math/PI)
    r  1959
    area (* 4 pi r r)]
  (str \"Earth area is \" (int area) \" square miles\"))"
                  }}})
