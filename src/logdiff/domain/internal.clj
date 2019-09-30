(ns logdiff.domain.internal
  (:require [clojure.string :as string]
            [clojure.string :as str]))

(declare trans-pred word-diffs join-strings split-pattern
         structurally-different?)

(defn trans-preds [rules]
  (set (map trans-pred rules)))

(defn loglinediff [lhs rhs trans-preds]
  (let [ltokens (.split split-pattern lhs)
        rtokens (.split split-pattern rhs)]
    (if (structurally-different? ltokens rtokens)
      :structurally-different
      (join-strings (word-diffs ltokens rtokens trans-preds)))))

(def ^:const line-delimiter (System/lineSeparator))
(def ^:const word-delimiters #{" " "[" "]"})

;;; derived from above
(def ^:const token-delimiter (letfn [(escaped [coll] (map #(str "\\" %) coll))]
                               (format "[%s]" (apply str (escaped word-delimiters)))))
(def ^:const split-pattern (re-pattern (format "(?<=\\S)(?=%s)|(?<=%s)(?=\\S)"
                                               token-delimiter
                                               token-delimiter)))

(defn- word-delimiter? [x]
  (contains? word-delimiters x))

(defn- word-delimiter-mismatch? [l r]
  (or (and (word-delimiter? l) (not (word-delimiter? r)))
      (and (not (word-delimiter? l)) (word-delimiter? r))))

(defn- structurally-different? [ltokens rtokens]
  (some (fn [[l r]] (word-delimiter-mismatch? l r))
                     
;;; https://stackoverflow.com/a/2588385/614800
        (map vector ltokens rtokens)))

(defn- join-strings [word-diffs]
  (map #(if (string? (first %)) (string/join %) (first %))
         (partition-by #(string? %) word-diffs)))

(defn- to-num-or-na [xs]
  (try
    (vec (map #(Double/valueOf %) xs))
    (catch NumberFormatException _
      :na)))

(def ^{:doc (clojure.string/join "\n"
             ["transformer: [l r] -> [newl newr] | :na (not-applicable)"
              "  predicate-builder: x -> l r idx -> true | false"])
       :const true
       :private true}
  rule-def
  {:col {:transformer identity
         :predicate-builder (fn [x] (if (set? x)
                                      #(contains? x %3)
                                      #(= x %3)))}
   :num {:transformer to-num-or-na
         :predicate-builder identity}
   :word {:transformer identity
          :predicate-builder (fn [k v] (fn [l r _] (and (= k l) (= v r))))}})

(defn- string-trans-pred [k v]
  (let [rd (rule-def :word)
        transformer (:transformer rd)
        predicate (:predicate-builder rd)]
    [transformer (predicate k v)]))

(defn- ignore-diff? [lhs rhs idx [transformer predicate]]
  (let [transformed (transformer [lhs rhs])]
    (if (= :na transformed)
      false
      (apply #(predicate %1 %2 idx) transformed))))

(defn- diff [lhs rhs idx trans-preds]
  {:lhs lhs
   :rhs rhs
   :ignored (not-every? false? (map (partial ignore-diff? lhs rhs idx) trans-preds))})

(defn- diff-or-token
  "takes in two tokens. applies rules only if the tokens are different"
  [lhs rhs idx trans-preds]
  (if (= lhs rhs) lhs (diff lhs rhs idx trans-preds)))

(defn- word-diffs [ltokens rtokens trans-preds]
  (let [range-from-1 (drop 1 (range))]
    (map #(diff-or-token %1 %2 %3 trans-preds) ltokens rtokens range-from-1)))

(defn- contains-diff? [line]
  (some vector? line))

(defn- conj-if-diff [coll line]
  (if (contains-diff? line)
    (conj coll line)
    coll))

(defn- trans-pred [[k v :as rule]]
  (if (string? k)
    (string-trans-pred k v)
    (let [trans-pred (rule-def k)
          transformer (:transformer trans-pred)
          predicate-builder (:predicate-builder trans-pred)]
      [transformer (predicate-builder v)])))


(comment
  (contains? #{:a} :a)
  (some (constantly true) #{} #{})
  (seq (.split split-pattern "hello     dolly"))
  (clojure.string/join "" #{"a" "b"})
  (str "a" "b")
  (format "[%s]" "hello")
                 
  )
