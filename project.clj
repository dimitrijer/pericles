(defproject pericles "0.1.0-SNAPSHOT"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 ;; Logging dependencies.
                 [org.clojure/tools.logging "0.3.1"]
                 [org.slf4j/slf4j-api "1.7.21"]
                 [org.apache.logging.log4j/log4j-slf4j-impl "2.6.2"]
                 [org.apache.logging.log4j/log4j-api "2.6.2"]
                 [org.apache.logging.log4j/log4j-core "2.6.2"]
                 ;; Ring + Compojure powered by Jetty.
                 [compojure "1.5.1"]
                 [ring/ring-jetty-adapter "1.5.0"]
                 [clj-gpio "0.2.0"]]
  :main ^:skip-aot pericles.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
