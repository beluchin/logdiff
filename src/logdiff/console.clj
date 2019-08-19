(ns logdiff.console
  (:require [logdiff.console.app :as app]
            [logdiff.console.interactive :as interactive]))

(def interactive interactive/run)

(defn logdiff [& args]
  (when-let [s (apply app/logdiff args)] (println s)))
