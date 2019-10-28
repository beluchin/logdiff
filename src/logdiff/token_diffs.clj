(ns logdiff.token-diffs
  (:refer-clojure :exclude [flatten])
  (:require [logdiff.protocols :as protocols]
            [clojure.string :as str])
  (:import logdiff.domain.model.TokenDiffs))

(declare flatten-if-diff)

(extend-type TokenDiffs
  protocols/AllDiffIgnored?
  (all-diff-ignored? [this]
    (every? #(:ignored %) (remove string? (:diffs this))))

  protocols/LineDiffToString
  (to-string [this]
    (str/join (map flatten-if-diff (:diffs this)))))

(defn- flatten [{:keys [lhs rhs ignored]}]
  (if ignored
    (format "<%s><%s>" lhs rhs)
    (format "[-%s-]{+%s+}" lhs rhs)))

(defn- flatten-if-diff [x]
  (let [diff? map?] (if (diff? x) (flatten x) x)))
