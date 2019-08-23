(ns logdiff.interactive.app
  (:refer-clojure :exclude [next])
  (:require [logdiff.output :as output]
            [logdiff.files :as files]
            [logdiff.interactive.domain :as domain]))

;;;
;;; sits between the jline-related code and the domain 
;;;

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
  (output/one-line (domain/logdiffline (.trim lhs) (.trim rhs) {})))
