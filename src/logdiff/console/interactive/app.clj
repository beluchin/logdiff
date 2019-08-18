(ns logdiff.console.interactive.app
  (:refer-clojure :exclude [next])
  (:require [logdiff.console.app :as app]
            [logdiff.console.files :as files]
            [logdiff.console.interactive.domain :as domain]))

(declare session logdiffline)

(defn interactive [lhs rhs]
  (reset! session {:lhs (files/get-line-seq lhs)
                   :rhs (files/get-line-seq rhs)
                   :pos 0}))

(defn next []
  (let [pos (:pos @session)
        lhs (:lhs @session)
        rhs (:rhs @session)]
    (if (and (< pos (count lhs)) (< pos (count rhs)))
      (let [result (logdiffline (nth lhs pos) (nth rhs pos))]
        (swap! session update-in [:pos] inc)
        result)
      :no-more-diffs)))

(def ^:private session (atom nil))

(defn- logdiffline [lhs rhs]
  (app/one-line-output (domain/logdiffline (.trim lhs) (.trim rhs) {})))
