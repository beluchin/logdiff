(ns logdiff.domain
  (:require [logdiff.domain.internal :as internal]))

(declare batch-output)

(defn logdiff 
  "lhs, rhs: sequences of lines (line endings should be trimmed prior)
   rules:    a map of rules"
  [lhs rhs rules]
  (let [t-p (internal/trans-preds rules)]
    (map
     #(batch-output (internal/loglinediff %1 %2 t-p) %1 %2)
     lhs rhs)
    #_(map #(internal/logdiffline %1 %2 t-p) lhs rhs)))

(defn all-diff-ignored? [line-diffs]
  (if (= :structurally-different line-diffs)
    false
    (every? #(:ignored %) (remove string? line-diffs))))

(defn loglinediff [lhs rhs rules]
  (internal/loglinediff lhs rhs (internal/trans-preds rules)))

(defn- batch-output [internal-diff lline rline]
  (if (= :structurally-different internal-diff)
    {:lhs lline :rhs rline :type :structurally-different}
    internal-diff))

(comment
  (seq (.split split-pattern "hello        world"))
  (line-word-diffs "hello world" "goodbye world" {})
  (partition-by #(string? %) [["a" "b"] " " "c"])
  (map #(if (string? (first %)) (string/join %) (first %))
       (partition-by #(string? %)
                     [["a" "b"] "           " "c"]))
  
  (str "a" "b")
  (#{:a} :a)
  (#{:a} :b)
  (contains? #{:a} :a)
  (contains? #{:a} :b)
  
  (into {} [[:a 1] [:b 2]])
  (seq? [])
  (seq? {})
  (sequential? #{:a})
  (sequential? [])
  (sequential? '())
  (set? #{:a})
  ({:a 1} :b 0)
  
  (to-num-or-error "hello" "world")

  (Double/valueOf "123hello")
  
  (#(identity % %&) 1 2)
  (map identity {:a 1}) ; ([:a 1])
  (some true? [false])
  
  (seq {:a 1, :b 2})
  (seq (.split "        hello  \t  world              " "\\"))
  (seq (.split " hello " "\\s+"))
  (seq (.split "hello=123 mundo[456]" "[\\s=\\[\\]]+"))
   
  (map + [1 2] [100 200])

  (logdiff "hello world" "goodbye world")
  (conj [1 2] 3)
  (vector? [])
  (empty? (seq))
)
