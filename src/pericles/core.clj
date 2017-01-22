(ns pericles.core
  (:require [clojure.tools.logging :as log]
            [clojure.java.io :as io]
            [ring.adapter.jetty :refer [run-jetty]]
            [pericles.routes :refer [app]]
            [pericles.gpio :as gpio])
  (:gen-class))

;; By using defonce we prevent multiple evaluation during REPL reload. It also
;; allows us to start and stop the server interactively.
(defonce ^:private server (atom nil))

(defn -main
  "Entry point of the app."
  [& args]
  (let [{:keys [gpio-port server-port]} (read-string (slurp
                                                       (io/file
                                                         (io/resource "settings.edn"))))]
    (try
      (gpio/init-port gpio-port)
      (.addShutdownHook (Runtime/getRuntime)
                        (Thread. (fn [] (gpio/destroy-port gpio-port))))
      ;; Passing app as var makes it possible to redefine routes/handlers while
      ;; the server is running.
      (reset! server (run-jetty #'app {:port server-port :join? false}))
      (log/infof "Started server on port %d." server-port)
      (catch Exception ex
        (log/error ex "Unhandled exception during server startup!")
        (gpio/destroy-port gpio-port)
        (System/exit 1)))))
