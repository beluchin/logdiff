(ns logdiff.console.app
  (:refer-clojure :exclude [flatten])
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [logdiff.console.files :as files]
            [logdiff.domain :as domain]))

(declare output flatten-if-diff all-diff-ignored?)

(defn logdiff
  ([lhs rhs] (logdiff lhs rhs "{}"))
  ([lhs rhs rules]
   (output (domain/logdiff (files/get-line-seq lhs)
                           (files/get-line-seq rhs)
                           (clojure.edn/read-string rules)))))

(defn one-line-output [line-diffs]
  (str/join (map flatten-if-diff line-diffs)))

(defn- output [diffs]
  (let [ss (map one-line-output (remove all-diff-ignored? diffs))]
    (when (not= ss []) (str/join (System/lineSeparator) ss))))

(defn- all-diff-ignored? [line-diffs]
  (every? #(:ignored %) (remove string? line-diffs)))

(defn- flatten [{:keys [lhs rhs ignored]}]
  (if ignored
    (format "<%s><%s>" lhs rhs)
    (format "[-%s-]{+%s+}" lhs rhs)))

(defn- flatten-if-diff [x]
  (let [diff? map?] (if (diff? x) (flatten x) x)))


(comment
  (str/replace "a\r\nb\n" #"(\r\n|\n)" (System/lineSeparator))
  
  (sequential? [])
  (sequential? '())
  (sequential? "")
  )
