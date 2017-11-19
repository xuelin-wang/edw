(ns edw.redis
  (:import (redis.clients.jedis JedisPool JedisPoolConfig)))

(def jedis-pool (JedisPool. (JedisPoolConfig.) "127.0.0.1" 6679))

(defn close-redis []
  (.close jedis-pool))

(defmacro with-redis [redis & body]
         `(with-open [~redis (.getResource jedis-pool)]
           ~@body
           )
         )
