(ns octopoda.twitter-node.test-suite
  (:require [cljs.nodejs :as nodejs]
            [cljs.test :refer-macros [deftest is testing run-tests]]
            [taoensso.timbre :refer [set-level!] :refer-macros [infof]]
            [octopoda.twitter-node.test-core]))

(nodejs/enable-util-print!)
(set-level! :info)

(defn -main []
  (try
    (infof "Running tests")
    (run-tests
      'octopoda.twitter-node.test-core)
    (catch :default _ nil)))

(defn fig-code-reload [& args]
  (-main))

(defn fig-msg [& args])

(set! *main-cli-fn* -main)
