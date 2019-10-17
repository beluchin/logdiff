(ns logdiff.token-diffs.domain-test
  (:require  [clojure.test :as t]
             [logdiff.domain :as sut]
             [logdiff.domain.model :as model]
             [logdiff.domain.num :as num]))

(declare drop-last-arg log-diff log-line-diff)

(t/deftest basic
  (t/is (= [[{:lhs "hello" :rhs "goodbye" :ignored false}  " world"]]
             (log-diff ["hello world"] ["goodbye world"]))))

(t/deftest identical-lines
  (t/is (let [s "hello world"] (= [[s]] (log-diff [s] [s])))))

(t/deftest many-lines
  (t/is (let [first-line "first line is identical"]
          (= [[first-line]
              ["second has " {:lhs "one" :rhs "una" :ignored false} " difference"]]
             (log-diff [first-line "second has one difference"]
                       [first-line "second has una difference"])))))

(t/deftest logdiffline
  (t/testing "word difference"
    (t/is (= [{:lhs "a" :rhs "b" :ignored false}]
             (log-line-diff "a" "b")))
    (t/is (= [{:lhs "hello" :rhs "goodbye" :ignored false} " world"]
             (log-line-diff "hello world" "goodbye world"))))

  (t/testing "identical lines"
    (t/is (= ["hello world"]
             (log-line-diff "hello world" "hello world")))))

(t/deftest rules
  (t/testing "word"
    (t/is (= [{:lhs "a" :rhs "b" :ignored true}]
             (log-line-diff "a" "b" {"a" "b"})))
    (t/is (= [{:lhs "a" :rhs "b" :ignored false}]
             (log-line-diff "a" "b" {"b" "a"})))) ; not symmetrical

  (t/testing "numerical"
    (let [rule-spec {:num (comp #(< % 0.1) #(Math/abs %) (drop-last-arg -))}]
      (t/is (= [{:lhs "1.01" :rhs "1.02" :ignored true}]
               (log-line-diff "1.01" "1.02" rule-spec)))
      (t/is (= [{:lhs "1.01" :rhs "1.02" :ignored true}
                " "
                {:lhs "hello" :rhs "world" :ignored false}]
               (log-line-diff "1.01 hello" "1.02 world"
                                {:num (num/diff-no-larger-than 0.1)})))))

  (t/testing "one column"
    (t/is (= ["hello " {:lhs "world" :rhs "mundo" :ignored false}]
             (log-line-diff "hello world" "hello mundo" {:col 1})))
    (t/is (= ["hello " {:lhs "world" :rhs "mundo" :ignored true}]
             (log-line-diff "hello world" "hello mundo" {:col 3}))))

  (t/testing "multiple columns"
      (t/is (= [{:lhs "hello" :rhs "goodbye" :ignored true}
                " dear "
                {:lhs "world" :rhs "mundo" :ignored true}]
               (log-line-diff "hello dear world" "goodbye dear mundo"
                            {:col #{1 5}})))))

(defn- drop-last-arg [fn]
  #(apply fn (drop-last %&)))

(defn- log-line-diff
  ([lhs rhs] (log-line-diff lhs rhs {}))
  ([lhs rhs rules] (:diffs (sut/log-line-diff lhs rhs rules))))

(defn- log-diff [lhs rhs]
  (map :diffs (sut/logdiff lhs rhs {})))

