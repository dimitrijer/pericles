(ns pericles.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [gpio.core :as gpio]
            [pericles.gpio :refer [port]]
            [pericles.handlers :as handlers]))

(defroutes api-routes
  (GET "/" [] "Hello world!")
  (GET "/readPort" [] (name (gpio/read-value @port)))
  (GET "/writePort" [value] (gpio/write-value! @port value))
  (route/not-found "Not found!"))

(def app (-> #'api-routes
             (wrap-defaults api-defaults)
             handlers/wrap-catch-exceptions
             handlers/wrap-log-request))
