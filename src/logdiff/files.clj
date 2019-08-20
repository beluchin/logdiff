(ns logdiff.files
  (:require [clojure.string :as str]))

(declare slurp-log)

(defn get-line-seq [filename]
  (.split (slurp-log filename) (System/lineSeparator)))

(defn- normalize-line-endings [s]
  (str/replace s #"\r\n|\n" (System/lineSeparator)))

(defn- slurp-log [filename]
  (normalize-line-endings (slurp filename)))
