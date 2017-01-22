(ns pericles.settings
  (:require [clojure.java.io :as io]))

(defonce cfg (read-string (slurp (io/resource "settings.edn"))))
