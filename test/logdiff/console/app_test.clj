(ns logdiff.console.app-test
  (:require [logdiff.console.app :as sut]
            [clojure.test :as t]
            [logdiff.domain :as domain]))

(t/deftest logdiff
  (t/testing "plain output"
    (t/is (= "[-hello-]{+goodbye+} world"
             (with-redefs [domain/logdiff (constantly [[{:lhs "hello" :rhs "goodbye" :ignored false} " world"]])]
               (sut/logdiff :whatever :whatever))))))
