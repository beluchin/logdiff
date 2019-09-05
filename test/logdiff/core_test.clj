(ns logdiff.core-test
  (:require [clojure.test :as t]
            [logdiff.core :as sut]
            [logdiff.domain :as domain]
            [logdiff.tempfile :as tempfile]
            [tempfile.core :as core]))

(declare logdiff)

(t/deftest plain-output
  (t/is (= "[-hello-]{+goodbye+} world"
           (with-redefs [domain/logdiff (constantly [[{:lhs "hello" :rhs "goodbye" :ignored false} " world"]])]
             (logdiff "what" "ever")))))


(defn- logdiff [lhstext rhstext]
  (tempfile/with-filenames [lpath lhstext
                            rpath rhstext]
    (sut/logdiff lpath rpath)))
