(ns logdiff.domain-test
  (:require [clojure.string :as string]
            [clojure.test :as t]
            [logdiff.domain :as sut]
            [logdiff.domain.num :as num]))

(declare drop-last-arg)

(t/deftest logdiff
  (t/testing "word difference"
    (t/is (= [[{:lhs "hello" :rhs "goodbye" :ignored false}  " world"]]
             (sut/logdiff ["hello world"] ["goodbye world"] {}))))

  (t/testing "identical lines"
    (t/is (let [s "hello world"] (= [[s]] (sut/logdiff [s] [s] {})))))

  (t/testing "multilines"
    (t/is (let [first-line "first line is identical"]
            (= [[first-line]
                ["second has " {:lhs "one" :rhs "una" :ignored false} " difference"]]
               (sut/logdiff [first-line "second has one difference"]
                            [first-line "second has una difference"]
                            {}))))))

(t/deftest logdiffline
  (t/testing "word difference"
    (t/is (= [{:lhs "a" :rhs "b" :ignored false}]
             (sut/loglinediff "a" "b" {})))
    (t/is (= [{:lhs "hello" :rhs "goodbye" :ignored false} " world"]
             (sut/loglinediff "hello world" "goodbye world" {}))))

  (t/testing "structurally different lines"
    (t/is (= :structurally-different
             (sut/loglinediff "6:45 [main] count: 42"
                              "10:32 [main] Foo[bar:42, baz:58]"
                              {}))))

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


(comment
  ((comp #(< % 0.1) #(Math/abs %) (drop-last-arg -)) 1.01 1.02 :whatever)
  
  (sut/logdiff "hello" "world" {:num (comp #(< % 0.1) #(Math/abs %) -)})
  (sut/logdiff "world goodbye" "world hello")
  (sut/logdiff "1.01 hello" "1.02 world" {:num (comp #(< % 0.1) #(Math/abs %) -)})
  (sut/logdiff "1.01" "1.02" {:num (comp #(< % 0.1) #(Math/abs %) -)})
  
  ((comp #(< % 0.1) #(Math/abs %) -) 1.01 1.02) ; true
  ((comp #(< % 0.1) #(Math/abs %) -) 1 2) ; false

  (new BigDecimal "3.012345678901234567890") ;3.012345678901234567890M
  (Double/valueOf "3.012345678901234567890") ;3.0123456789012346
  (number? )
  (Math/abs -3)
  ((comp #(< % 0.1) #(Math/abs %) -) 1.01 1.02)
  )
