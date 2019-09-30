(ns logdiff.interactive.app
  (:refer-clojure :exclude [next])
  (:require [logdiff.output :as output]
            [logdiff.files :as files]
            [logdiff.domain :as domain]))

;;;
;;; sits between the jline-related code and the domain 
;;;

(declare session loglinediff advance)

(defn init [lhs rhs]
  (reset! session {:lhs (files/get-line-seq lhs)
                   :rhs (files/get-line-seq rhs)
                   :pos -1}))

(defn next []
  (advance inc))

(defn previous []
  (advance dec))

(defn- advance [dir-fn]
  (let [lhs (:lhs @session)
        rhs (:rhs @session)
        valid? #(and (<= 0 %) (< % (count lhs)))]
    (loop [pos (:pos @session)]
      (let [newpos (dir-fn pos)]
        (if (valid? newpos)
          (let [lline (nth lhs newpos)
                rline (nth rhs newpos)
                diff (domain/loglinediff lline rline {})]
            (if (domain/all-diff-ignored? diff)
              (recur newpos)
              (do
                (swap! session assoc-in [:pos] newpos)
                (output/one-line lline rline diff))))
          :no-more-diffs)))))

(defn toggle-showing-identical [] "toggled showing identical")

(def ^:private session (atom nil))
