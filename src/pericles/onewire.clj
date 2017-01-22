(ns pericles.onewire
  (:require [clojure.string :as s]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]))

(def ^:private path "/sys/bus/w1/devices")

(defn- read-slaves
  "Returns a sequence of all registered 1-wire slave devices."
  []
  (s/split-lines (slurp (str path "/w1_bus_master1/w1_master_slaves"))))

(defn- read-temp
  "Reads temperature from a provided 1-wire slave device."
  [slave]
  (try 
    (let [file (io/as-file (str path "/" slave "/w1_slave"))]
      (when (.exists file)
        (let [[crc temp] (s/split-lines (slurp file))]
          (when (re-matches #".*YES$" crc)
            ;; Successful read.
            (let [result (Integer/parseInt (second (first (re-seq #"t=([0-9]*)" temp))))]
              {:name slave
               :celsius (/ (double result) 1000)})))))
    (catch java.io.IOException ex
      (log/errorf ex "Failed to read sensor %s!"))))

(defn read-all
  "Reads all 1wire sensors, returning only valid readings."
  []
  (filter #(not (nil? %)) (map read-temp (read-slaves))))
