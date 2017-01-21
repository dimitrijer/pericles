(ns pericles.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.coercions :refer [as-int]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.util.http-response :as resp]
            [gpio.core :as gpio]
            [pericles.gpio :refer [port]]
            [pericles.handlers :as handlers]))

(defroutes api-routes
  (GET "/" [] "Hello world!")
  (GET "/readPort" [] (name (gpio/read-value @port)))
  (GET "/writePort" [value :<< as-int]
       (do
         (gpio/write-value! @port value)
         (name (gpio/read-value @port))))
  (GET "/readTemp" [] "27C")
  (route/not-found "Not found!"))

(def app (-> #'api-routes
             (wrap-defaults api-defaults)
             handlers/wrap-catch-exceptions
             handlers/wrap-log-request))
