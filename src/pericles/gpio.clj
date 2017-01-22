(ns pericles.gpio
  (require [clojure.tools.logging :as log]
           [gpio.core :as gpio]
           [pericles.settings :refer [cfg]]))

(def ^:private port (atom nil))

(defn- port-available?
  []
  (and (not (:debug? cfg)) (not (nil? @port))))

(defn- open-port
  [old-port new-port-num]
  (let [open-new-port? (or
                         (not old-port)
                         (not= (:num old-port) new-port-num))]
    (if (open-new-port?)
      (do
        (if old-port (gpio/close! (:port old-port)))
        {:num new-port-num :port (gpio/open-port new-port-num :direction :out)})
      old-port)))

(defn init-port
  "Initializes GPIO port."
  []
  (if-not (:debug? cfg)
    (let [port-num (:gpio-port cfg)
          new-port (swap! port open-port port-num)]
      (log/debugf "Opened GPIO port %d." (:num new-port)))))

(defn destroy-port
  "Closes GPIO port."
  []
  (when-not (:debug? cfg)
    (swap! port (fn [old-port]
                  (when old-port
                    (gpio/close! (:port old-port))
                    (log/debugf "Closed GPIO port %d." (:num old-port)))))))

(defn read-port
  "Reads port value."
  []
  (if (port-available?)
    {:status (name (gpio/read-value (:port @port)))}))

(defn write-port
  "Writes 1 or 0 to port."
  [value]
  {:pre [(or (= 1 value) (= 0 value))]}
  (if (port-available?)
    (gpio/write-value! (:port @port) value)))
