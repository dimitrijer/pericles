(ns pericles.core
  (:require [clojure.tools.logging :as log]
            [ring.adapter.jetty :refer [run-jetty]]
            [pericles.routes :refer [app]]
            [pericles.gpio :as gpio]
            [pericles.settings :refer [cfg]])
  (:gen-class))

;; By using defonce we prevent multiple evaluation during REPL reload. It also
;; allows us to start and stop the server interactively.
(defonce ^:private server (atom nil))

(defn -main
  "Entry point of the app."
  [& args]
  (let [port (:server-port cfg)]
    (try
      (.addShutdownHook (Runtime/getRuntime)
                        (Thread. (fn [] (gpio/destroy-port))))
      (gpio/init-port)
      ;; Passing app as var makes it possible to redefine routes/handlers while
      ;; the server is running.
      (reset! server (run-jetty #'app {:port port :join? false}))
      (log/infof "Started server on port %d." port)
      (catch Exception ex
        (log/error ex "Unhandled exception during server startup!")
        (System/exit 1)))))
