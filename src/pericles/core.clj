(ns pericles.core
  (require [gpio.core :as gpio])
  (:gen-class))

(def port-number 11)
(def port (gpio/open-port port-number))

(defn -main

  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
