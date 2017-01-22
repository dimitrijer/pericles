(ns pericles.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.coercions :refer [as-int]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.http-response :as resp]
            [pericles.gpio :as gpio]
            [pericles.handlers :as handlers]
            [pericles.onewire :as onewire]))

(defroutes api-routes
  (GET "/" []
       (resp/found "/index.html"))
  (GET "/readPort" []
       (gpio/read-port))
  (GET "/writePort" [value :<< as-int]
       (do
         (gpio/write-port value)
         (gpio/read-port)))
  (GET "/readTemp" []
       (onewire/read-all))
  (route/not-found "Not found!"))

(def app (-> #'api-routes
             wrap-json-response
             (wrap-defaults (assoc api-defaults :static {:resources "public"}))
             handlers/wrap-catch-exceptions
             handlers/wrap-log-request))
