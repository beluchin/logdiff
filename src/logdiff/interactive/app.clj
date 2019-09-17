(ns logdiff.interactive.app
  (:refer-clojure :exclude [next])
  (:require [logdiff.output :as output]
            [logdiff.files :as files]
            [logdiff.domain :as domain]))

;;;
;;; sits between the jline-related code and the domain 
;;;

(declare session loglinediff)

(defn init [lhs rhs]
  (reset! session {:lhs (files/get-line-seq lhs)
                   :rhs (files/get-line-seq rhs)
                   :pos 0}))

(defn next []
  (let [lhs (:lhs @session)
        rhs (:rhs @session)]
    (loop [pos (:pos @session)]
      (if (< pos (count lhs))
        (let [diff (domain/loglinediff (nth lhs pos) (nth rhs pos) {})]
          (if (domain/all-diff-ignored? diff)
            (recur (inc pos))
            (do
              (swap! session assoc-in [:pos] (inc pos))
              (output/one-line diff))))
        :no-more-diffs))))

(defn previous []
  (let [pos (:pos @session)
        lhs (:lhs @session)
        rhs (:rhs @session)]
    (if (< 0 pos)
      (do
        (swap! session update-in [:pos] dec)
        (loglinediff (nth lhs (dec pos)) (nth rhs (dec pos))))
      :no-more-diffs)))

(defn toggle-showing-identical [] "toggled showing identical")

(def ^:private session (atom nil))

(defn- loglinediff [lhs rhs]
  (output/one-line (domain/loglinediff (.trim lhs) (.trim rhs) {})))
