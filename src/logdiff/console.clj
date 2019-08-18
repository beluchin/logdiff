(ns logdiff.console
  (:require [logdiff.console.app :as app]
            [logdiff.console.interactive :as interactive]))

(defn logdiff [& args]
  (if (= (first args) "-i")
    (apply interactive/interactive (rest args))
    (when-let [s (apply app/logdiff args)] (println s))))
