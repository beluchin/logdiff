(ns logdiff.output
  (:refer-clojure :exclude [flatten])
  (:require [clojure.string :as str])
  (:import [logdiff.domain.model TokenDiffs WholeLineDiff]))

(declare flatten-if-diff structurally-different-output)

(defprotocol LineDiffToString
  (to-string [line-diff]))

(extend-protocol LineDiffToString
  TokenDiffs
  (to-string [this]
    (str/join (map flatten-if-diff (:diffs this))))

  WholeLineDiff
  (to-string [this] (format "[-[-%s-]-]{+{+%s+}+}" (:lhs this) (:rhs this))))

(defn- flatten [{:keys [lhs rhs ignored]}]
  (if ignored
    (format "<%s><%s>" lhs rhs)
    (format "[-%s-]{+%s+}" lhs rhs)))

(defn- flatten-if-diff [x]
  (let [diff? map?] (if (diff? x) (flatten x) x)))
