(ns logdiff.interactive.domain-test
  (:require [clojure.test :as t]
            [logdiff.interactive.domain :as sut]
            [logdiff.domain.num :as num]))

(declare drop-last-arg)

(t/deftest logdiffline
  (t/testing "word difference"
    (t/is (= [{:lhs "a" :rhs "b" :ignored false}]
             (sut/loglinediff "a" "b" {})))
    (t/is (= [{:lhs "hello" :rhs "goodbye" :ignored false} " world"]
             (sut/loglinediff "hello world" "goodbye world" {}))))

  (t/testing "identical lines"
    (t/is (= ["hello world"]
             (sut/loglinediff "hello world" "hello world" {})))))

(t/deftest rules
  (t/testing "word"
    (t/is (= [{:lhs "a" :rhs "b" :ignored true}]
             (sut/loglinediff "a" "b" {"a" "b"})))
    (t/is (= [{:lhs "a" :rhs "b" :ignored false}]
             (sut/loglinediff "a" "b" {"b" "a"})))) ; not symmetrical

  (t/testing "numerical"
    (let [rule-spec {:num (comp #(< % 0.1) #(Math/abs %) (drop-last-arg -))}]
      (t/is (= [{:lhs "1.01" :rhs "1.02" :ignored true}]
               (sut/loglinediff "1.01" "1.02" rule-spec)))
      (t/is (= [{:lhs "1.01" :rhs "1.02" :ignored true}
                " "
                {:lhs "hello" :rhs "world" :ignored false}]
               (sut/loglinediff "1.01 hello" "1.02 world"
                                {:num (num/diff-no-larger-than 0.1)})))))

  (t/testing "one column"
    (t/is (= ["hello " {:lhs "world" :rhs "mundo" :ignored false}]
             (sut/loglinediff "hello world" "hello mundo" {:col 1})))
    (t/is (= ["hello " {:lhs "world" :rhs "mundo" :ignored true}]
             (sut/loglinediff "hello world" "hello mundo" {:col 3}))))

  (t/testing "multiple columns"
      (t/is (= [{:lhs "hello" :rhs "goodbye" :ignored true}
                " dear "
                {:lhs "world" :rhs "mundo" :ignored true}]
               (sut/loglinediff "hello dear world" "goodbye dear mundo"
                            {:col #{1 5}})))))

(defn- drop-last-arg [fn]
  #(apply fn (drop-last %&)))
