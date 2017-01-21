(ns pericles.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [pericles.handlers :as handlers]))

(defroutes api-routes
  (GET "/" [] "Hello world!")
  (route/not-found "Not found!"))

(def app (-> #'api-routes
             (wrap-defaults api-defaults)
             handlers/wrap-catch-exceptions
             handlers/wrap-log-request))
