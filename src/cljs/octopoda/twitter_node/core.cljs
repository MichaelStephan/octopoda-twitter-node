(ns octopoda.twitter-node.core
  (:require-macros [cljs.core.async.macros :refer (go go-loop)])
  (:require [octopoda.node.core :as node]
            [cljs.nodejs :as nodejs]
            [cljs.core.async :refer [<! >! chan close!]]))

(def twitter-api (nodejs/require "node-twitter-api"))

(defn make-client [config]
  (twitter-api. (clj->js {:consumerKey (:consumer-key config)
                          :consumerSecret (:consumer-secret config)
                          :callback nil})))

(defn direct-message [config recipient msg]
  (let [ret (chan)]
    (->
      config
      (make-client)
      (.direct_messages "new"
                        (clj->js {:user recipient 
                                  :text msg})
                        (:access-token config) (:access-token-secret config)
                        (fn [error data response]
                          (go
                            (when error
                              (>! ret[:error {:domain "New direct message"
                                                :cause error}]))
                            (close! ret)))))
    ret))

(defmulti handle-msg (fn [id config actions msg] (first msg))) 

(defmethod handle-msg :default [id config actions msg]
  (println msg))

(defmethod handle-msg :connected [id config {:keys [subscribe! publish!] :as actions}]
  (subscribe! (node/topic id "in/direct-message") {:qos 1}))

(defmethod handle-msg :message [id {:keys [recipients] :as config} {:keys [publish! error] :as actions} [_ topic msg]]
  (if (empty? recipients)
    (error {:type :no-recipients-configured :msg "No recipients configured"})
    (doseq [r recipients]
      (go
         (let [[status err] (<! (direct-message config r msg))]
           (when (= status :error)
             (error {:type :direct-message-exception :cause error :msg (str "Unable to send msg to recipient " r)})))))))

(defn validated-config [config]
  (map (partial node/throw-if-missing config) [:consumer-key :consumer-secret :access-token :access-token-secret :recipients])
  config)

(defn -main [& _]
  (node/start {:handle handle-msg :validate-config validated-config}))

(set! *main-cli-fn* -main)
