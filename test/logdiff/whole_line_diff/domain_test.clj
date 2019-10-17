(ns logdiff.whole-line-diff.domain-test
  (:require  [clojure.test :as t]
             [logdiff.domain :as sut]
             [logdiff.domain.model :as model]))

(t/deftest token-word-mismatch
  (t/is (= (model/->WholeLineDiff "word1 [word2]" "word1 word2")
           (sut/log-line-diff "word1 [word2]" "word1 word2" {}))))

(t/deftest token-mismatch
  #_(t/is (= :structurally-different
             (sut/loglinediff "a[" "a]" {}))))

(t/testing "different length")

