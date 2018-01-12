(ns edw.redis
  (:import (redis.clients.jedis JedisPool JedisPoolConfig)))


(defn create-jedis-pool [host port]
  (JedisPool. (JedisPoolConfig.) host port)
  )

(def jedis-pool (create-jedis-pool "127.0.0.1" 6679))

(defn close-redis []
  (.close jedis-pool))

(defmacro with-redis-pool [pool redis & body]
  `(with-open [~redis (.getResource ~pool)]
     ~@body
     )
  )

(defmacro with-web-redis [redis & body]
  (concat
    (list 'edw.redis/with-redis-pool 'edw.redis/jedis-pool redis)
    body
    )
  )

(defn get-rows [pool key-pattern]
  (with-redis-pool pool redis
                   (let [rkeys (.keys redis key-pattern)
                         ]
                     (map (fn [rkey]
                            (let [rval (.get redis rkey)]
                              [rkey rval]
                              )
                            )
                          rkeys
                          )
                     )
    )
  )


(defn get-list-rows [pool key-pattern]
  (with-redis-pool pool redis
                   (let [rkeys (.keys redis key-pattern)
                         ]
                     (map (fn [rkey]
                            (let [rval (.lrange redis rkey 0 -1)]
                              [rkey rval]
                              )
                            )
                          rkeys
                          )
                     )
                   )
  )
