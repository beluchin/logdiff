(ns logdiff.core
  (:gen-class)
  (:require [clojure.string :as str]
            [logdiff.domain :as domain]
            [logdiff.files :as files]
            [logdiff.interactive :as interactive]
            [logdiff.output :as output]))

(declare print-if-not-empty output)

(defn logdiff
  ([lhs rhs] (logdiff lhs rhs "{}"))
  ([lhs rhs rules]
   (output (domain/logdiff (files/get-line-seq lhs)
                           (files/get-line-seq rhs)
                           (clojure.edn/read-string rules)))))

(defn -main [& args]
  (if (= (first args) "-i")
    (apply interactive/run (rest args))
    (print-if-not-empty (apply logdiff args))))

(defn- print-if-not-empty [s]
  (when s (println s)))

(defn- output [diffs]
  (let [ss (map output/to-string (remove domain/all-diff-ignored? diffs))]
    (when (not= ss []) (str/join (System/lineSeparator) ss))))


(comment
  (-main "resources/lhs" "resources/rhs")
  (logdiff "resources/lhs" "resources/rhs")
  (domain/logdiff ["hello world"] ["goodbye world"] {}) ;; (({:lhs "hello", :rhs "goodbye", :ignored false} " world"))
  (output [[{:lhs "hello", :rhs "goodbye", :ignored false} " world"]])
  (all-diff-ignored? [{:lhs "hello", :rhs "goodbye", :ignored false} " world"])
  (output/one-line [{:lhs "hello", :rhs "goodbye", :ignored false} " world"])

  (apply app/logdiff ["resources/lhs" "resources/rhs"])
  
  (apply (fn [x y]) [1 2 3])

  (clojure.edn/read-string "{\"a\" \"b\"}")

  ;; Hello World - https://github.com/google/diff-match-patch/wiki/Language:-Java
  (import 'name.fraser.neil.plaintext.diff_match_patch)
  (def tool (diff_match_patch.))
  (def diff (.diff_main tool "Hello World" "Goodbye World"))
  (.diff_cleanupSemantic tool diff)
  (.println System/out diff)

  (def lhs (slurp "resources/lhs"))
  (def rhs (slurp "resources/rhs"))
  (def diff (.diff_main tool lhs rhs))
  (.diff_cleanupSemantic tool diff)
  (.println System/out diff)
  )
