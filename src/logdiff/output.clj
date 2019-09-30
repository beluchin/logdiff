(ns logdiff.output
  (:refer-clojure :exclude [flatten])
  (:require [clojure.string :as str]))

(declare flatten-if-diff structurally-different-output)

(defn one-line [lhs rhs line-diffs]
  (if (= :structurally-different line-diffs)
    (structurally-different-output lhs rhs)
    (str/join (map flatten-if-diff line-diffs))))

(defn- structurally-different-output [lline rline]
  (format "[-[-%s-]-]{+{+%s+}+}" lline rline))

(defn- flatten [{:keys [lhs rhs ignored]}]
  (if ignored
    (format "<%s><%s>" lhs rhs)
    (format "[-%s-]{+%s+}" lhs rhs)))

(defn- flatten-if-diff [x]
  (let [diff? map?] (if (diff? x) (flatten x) x)))
