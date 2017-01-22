(ns pericles.gpio
  (require [clojure.tools.logging :as log]
           [gpio.core :as gpio]
           [pericles.settings :refer [cfg]]))

(def port (atom nil))

(defn init-port
  "Initializes GPIO port."
  []
  (if-not (:debug? cfg)
    (let [port-num (:gpio-port cfg)
          new-port {:num port-num
                    :port (gpio/open-port port-num :direction :out)}
          old-port (reset! port new-port)]
      (log/debugf "Opened GPIO port %d." port-num)
      (if old-port (gpio/close! (:port old-port)))
      new-port)))

(defn destroy-port
  "Closes GPIO port."
  []
  (if-not (:debug? cfg)
    (when-let [old-port (reset! port nil)]
      (gpio/close! (:port old-port))
      (log/debugf "Closed GPIO port %d." (:num old-port)))))
