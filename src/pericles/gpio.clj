(ns pericles.gpio
  (require [gpio.core :as gpio]))

(def port (atom nil))

(defn init-port
  "Initializes GPIO port."
  [port-number]
  (reset! port (gpio/open-port port-number :direction :out)))

(defn destroy-port
  "Closes GPIO port."
  [port-number]
  (if-let [old-port (reset! port nil)]
    (gpio/close! old-port)))
