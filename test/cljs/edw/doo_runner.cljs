(ns edw.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [edw.core-test]))

(doo-tests 'edw.core-test)

