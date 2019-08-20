(ns logdiff.output
  (:refer-clojure :exclude [flatten])
  (:require [clojure.string :as str]))

(declare flatten-if-diff)

(defn one-line [line-diffs]
  (str/join (map flatten-if-diff line-diffs)))

(defn- flatten [{:keys [lhs rhs ignored]}]
  (if ignored
    (format "<%s><%s>" lhs rhs)
    (format "[-%s-]{+%s+}" lhs rhs)))

(defn- flatten-if-diff [x]
  (let [diff? map?] (if (diff? x) (flatten x) x)))



