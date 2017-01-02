(defproject octopoda.node/twitter "1.0.0"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :dependencies [[octopoda.node/base "1.0.0"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.34"]
                 [org.clojure/core.async "0.2.374"]
                 [com.taoensso/timbre "4.1.4"]]
  :profiles {:dev {:plugins [[lein-cljsbuild "1.1.2"]
                             [lein-figwheel "0.5.0-2"]
                             [lein-cljfmt "0.3.0"]
                             [lein-npm "0.6.1"]]}}
  :npm {:dependencies
        [[source-map-support "*"]
         [node-twitter-api "*"]
         [mqtt "1.10.0"]]}
  :clean-targets ["out.dev"
                  "out.prod"
                  "target"
                  "figwheel_server.log"
                  "npm_modules"]
  :lein-release {:scm :git
                 :deploy-via :lein-install}
  :source-paths ["src/cljs"]
  :cljsbuild {:builds [{:id "test"
                        :source-paths ["src/cljs" "test"]
                        :figwheel {:on-jsload "octopoda.twitter-node.test-suite/fig-code-reload"
                                   :on-message "octopoda.twitter-node.test-suite/fig-msg"}
                        :compiler {:main octopoda.twitter-node.test-suite
                                   :output-to "out.dev/octopoda-test-suite.js"
                                   :output-dir "out.dev"
                                   :target :nodejs
                                   :optimizations :none
                                   :recompile-dependents true
                                   :source-map true}}
                       {:id "prod"
                        :source-paths ["src/cljs"]
                        :compiler {:main octopoda.twitter_node.core
                                   :output-to "out.prod/node.js"
                                   :output-dir "out.prod"
                                   :target :nodejs
                                   :optimizations :none}}]}
  :figwheel {})
